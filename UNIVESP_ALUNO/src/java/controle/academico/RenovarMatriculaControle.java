package controle.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.LoginControle;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import jakarta.annotation.PostConstruct;
import jakarta.faces. component.UICommand;
import jakarta.faces. context.FacesContext;
import jakarta.faces. model.SelectItem;
import jakarta.servlet.http.HttpServletRequest;
import negocio.comuns.academico.AproveitamentoDisciplinaVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoComHistoricoAlunoVO;
import negocio.comuns.academico.PeriodoLetivoVO;

import negocio.comuns.academico.PortadorDiplomaVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TipoDocumentoEquivalenteVO;
import negocio.comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.TurmaContratoVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoContabilizacaoDisciplinaDependenciaEnum;
import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.SimularAcessoAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.DadosComerciaisVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.LayoutComprovanteMatriculaEnum;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.financeiro.enumerador.AmbienteCartaoCreditoEnum;
import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;

import negocio.comuns.utilitarias.faturamento.nfe.*;

import negocio.comuns.secretaria.TransferenciaMatrizCurricularMatriculaVO;
import negocio.comuns.utilitarias.AcessoException;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.FinanciamentoEstudantil;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoCoberturaConvenio;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
//import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.DocumentoAssinado;
//import negocio.facade.jdbc.academico.HorarioAluno;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.Usuario;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;

import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

import relatorio.negocio.jdbc.academico.ComprovanteRenovacaoMatriculaRel;
import relatorio.negocio.jdbc.academico.DadosMatriculaRel;


/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas matriculaForm.jsp matriculaCons.jsp) com as funcionalidades da classe
 * <code>Matricula</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Matricula
 * @see MatriculaVO
 */
@Controller("RenovarMatriculaControle")
@Scope("viewScope")
@Lazy
public class RenovarMatriculaControle extends SuperControleRelatorio implements Serializable {

	private String paginaRetornoAdicionarDisciplinaCursandoCorrespodencia;
	private TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatriculaVO;
	private String paginaDestinoInclusaoDisciplina;
	private Boolean apresentarBotaoLiberarControleInclusaoDisciplinaPeriodoFuturo;
	private Boolean apresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular;
	private Boolean apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular;
	private Boolean apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular;
	private Boolean apresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia;
	private String tipoOperacaoFuncionalidadeLiberar;
	private String usernameLiberarOperacaoFuncionalidade;
	private String senhaLiberarOperacaoFuncionalidade;
	private boolean debitoFinanceiroAoIncluirConvenioLiberado = false;
	private String paginaDestinoConfirmacao;
	private static final long serialVersionUID = 1L;
	private Boolean possuiContaPagarRestituicaoConvenioAlunoBaixada;
	private String direcionamentoListaContaPagarRestituicaoConvenioAluno;

	private String usernameLiberarMatriculaAcimaNrVagas;
	private String senhaLiberarMatriculaAcimaNrVagas;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas;
	private Boolean liberarTodasDisciplinasComIndisponibilidadeVagas;
	private Boolean apresentarBotaoLiberarMatriculaTodosPeriodos;
	private String usernameLiberarMatriculaTodosPeriodos;
	private String senhaLiberarMatriculaTodosPeriodos;
	private String usernameLiberarRenovacaoDocumentoPendente;
	private String senhaLiberarRenovacaoDocumentoPendente;
	private Integer totalCHCrPendenteAlunoAtePeriodoAnterior;
	private Integer totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior;
	private String onCompleteIncluirDisciplinaTurma;
	private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVisualizar;
	private Integer codigoGradeCurricular;
	private Integer codigoPeriodoLetivoIncluirDisciplinasGrupoOptativas;
	private PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoVOIncluirGrupoOptativas;
	private List listaSelectItemPeriodoLetivoComGrupoOptativas;
	private Integer codigoMapaEquivalenciaDisciplinaVOIncluir;
	private Integer codigoPeriodoLetivo;
	private Integer codigoMapaEquivalenciaDisciplinaCursar;
	private String filtroSituacaoMatriculaPeriodo;
	private Integer filtroTurno;
	private String anoGerarParcelaAvulsa;
	private String mesGerarParcelaAvulsa;
	private MatriculaVO matriculaVO;
	private String aluno_Erro;
	private List listaSelectItemUnidadeEnsino;
	private String curso_Erro;
	private String matricula_Erro;
	private String usuario_Erro;
	private String responsavelLiberacaoMatricula_Erro;
	private List listaSelectItemTurno;
	private List listaSelectItemTipoMidiaCaptacao;
	private List<SelectItem> listaSelectItemTurma;

	protected List listaSelectItemParceiro;
	protected List listaSelectItemConvenio;
	protected List listaSelectItemPlanoDesconto;
	private List listaSelectItemDescontoProgresivo;
	private ProcessoMatriculaCalendarioVO processoCalendarioMatriculaVO;
	protected boolean editarParcela;
	protected boolean permitirFecharRichModalMatriculaPeriodoVencimento;
	protected boolean novaMatriculaPeriodoVencimento;

	private boolean matriculaBloqueiaDescontoParcelaMatricula = Boolean.FALSE;
	private boolean permiteSuspenderMatricula = Boolean.FALSE;
	private boolean permiteLiberarPgtoMatricula = Boolean.FALSE;
	private boolean permiteConfirmarCancelarPreMatricula = Boolean.FALSE;
	private Boolean permitirIncluirExcluirDisciplinaNaVisaoAlunoAposPagamentoMatricula;	
	/**
	 * Interface <code>MatriculaInterfaceFacade</code> responsável pela
	 * interconexão da camada de controle com a camada de negócio. Criando uma
	 * independência da camada de controle com relação a tenologia de
	 * persistência dos dados (DesignPatter: Façade).
	 */
	private DocumetacaoMatriculaVO documetacaoMatriculaVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private List listaSelectItemGradeCurricular;
	private String responsavelRenovacaoMatricula_Erro;
	private List listaSelectItemPeriodoLetivoMatricula;
	private List listaSelectItemPeriodoLetivoInclusaoDisciplina;
	private Integer periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina;
	private String turma_Erro;
	protected String userName;
	protected String senha;
	private Boolean matriculaForaPrazo;
	private Boolean imprimir;
	
	private List listaSelectItemMatriculaVisaoAluno;
	private String matriculaVisaoAluno;
	private Boolean exibirRenovacaoMatricula;
	protected String inscricao_Erro;
	protected String guiaAba;
	protected List listaGradeDisciplinas;
	protected List listaSelectItemPeriodoLetivo;
	protected List listaSelectItemCurso;
	protected List listaConsultaCurso;
	protected List listaConsultaConvenio;
	protected List listaConsultaPlanoDesconto;
	protected String campoConsultaCurso;
	protected String valorConsultaCurso;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List listaConsultaTurma;
	protected List listaConsultaAlunoNovaMatricula;
	private String campoConsultaAlunoNovaMatricula;
	private String valorConsultaAlunoNovaMatricula;
	protected List listaConsultaAluno;
	protected String campoConsultaAluno;
	protected String valorConsultaAluno;
	protected List listaConsultaCandidato;
	protected String campoConsultaCandidato;
	protected String valorConsultaCandidato;
	protected List listaDisciplinasGradeCurricular;
	protected Integer codigoUnidadeEnsinoCurso;
	protected boolean validarCadastrarAluno;
	protected boolean liberarAvancar;
	protected boolean pedirLiberacaoMatricula;
	protected Boolean turmaComVagasPreenchidas;
	protected Boolean turmaComLotacaoPreenchida;
	protected Boolean exibirMatricula;
	protected List listaSelectItemProcessoMatricula;
	protected Boolean apresentarPlanoFinanceiroCurso = false;
	private List listaSelectItemPlanoFinanceiroCurso;
	private List listaSelectItemCondicoesDePagamentoDoPlanoFinanceiroCurso;
	private List listaSelectItemCategoriaPlanoFinanceiroCurso;
	private Integer gradeDisciplinaAdicionar;
	private Integer gradeDisciplinaEquivalenteAdicionar;
	protected List listaSelectItemDisciplinaEquivalenteAdicionar;
	protected List listaSelectItemTurmaAdicionar;
	private List listaSelectItemMapaEquivalenciaDisciplinaIncluir;
	protected List listaSelectItemPeriodoLetivoAdicionar;
	protected Integer periodoLetivoAdicionar;
	protected List listaSelectItemDisciplinaAdicionar;
	protected List listaSelectItemDescontoAlunoMatricula;
	protected List listaSelectItemDescontoAlunoParcela;
	protected List ordemDesconto;

	private Boolean apresentarBotaoRenegociacao;
	private Boolean imprimirContrato;
	private Boolean montarDadosNovoPeriodoLetivo;
	private List listaDisciplinasPreRequisitos;
	private Boolean liberarAlteracaoDescontoProgressivoPrimeiraParcela;
	private Boolean liberarRenovacaoComDebitosFinanceiros;
	private String usernameLiberarRenovacaoComDebitosFinanceiros;
	private String senhaLiberarRenovacaoComDebitosFinanceiros;
	private String usernameLiberarInclusaoDisciplinaForaPrazo;
	private String senhaLiberarInclusaoDisciplinaForaPrazo;
	private List<HistoricoVO> listaDisciplinasPeriodoLetivoAlunoJaAprovado;
	private List<HistoricoVO> listaDisciplinasPeriodoLetivoAlunoEstaCursando;
	private List<GradeDisciplinaVO> listaDisciplinasPeriodoLetivoAlunoPendente;
	/*********************************************************
	 * TODOS OS CONTROLES ABAIXO SAO PARA INCLUSAO DE DISCIPLINAS, SEJA, DE
	 * OPTATIVAS, GRUPO DE OPTATIVAS OU POR EQUIVALENCIA - MAPA DE EQUIVALENCIA
	 * - VERSÃO 5.0
	 *********************************************************/
	protected MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVOAdicionar;
	// private DisciplinaVO disciplinaIncluirMatriculaPeriodo;
	// private TurmaVO turmaIncluirMatriculaPeriodo;
	// Atributo utilizado somente quando a inclusão se tratar de uma disciplina
	// de um grupo de optativa
	// private GradeCurricularGrupoOptativaDisciplinaVO
	// gradeGrupoOptativaDisciplinaVOIncluir;
	// private GradeDisciplinaVO gradeDisciplinaVOIncluir;
	/**
	 * Atributo que define o mapa de equivalencia, caso a inclusao seja por
	 * equivalencia Tanto uma disciplina da matriz, quanto do grupo de optativas
	 * por ser incluída por equivalencia.
	 */
	// private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVOIncluir;
	/*********************************************************
     *********************************************************/
	private boolean portadorDiploma;
	private Integer codigoPortadorDiploma;
	private List<SelectItem> listaSelectItemFinanciamentoEstudantil;
	private Boolean isMostrarFinanciamentoEstudantil;
	private Boolean naoApresentarBotoesMatriculaRenovacao;
	/**
	 * Atributo para controlar se a o ManagedBean está sendo utilizado para
	 * cadastrar uma nova matrícula ou para registrar/editar uma renovação de
	 * matrícula.
	 */
	private Boolean realizandoNovaMatriculaAluno;
	private Boolean apresentarDisciplinasEquivalentesIncluir;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasExcluidas;
	private List<DisciplinaVO> listaDisciplinasTurma;
	private String alertaGradeTurmaDiferenteGradeAluno;
	public Boolean mostrarAbaDescontos;
	public Boolean mostrarAbaDescontoPlanoFinanceiro;
	public Boolean mostrarCheckBoxFinanceiroManual;
	public Boolean permissaoAlterarDadosAcademicos;
	private Date valorConsultaData;
	private Integer primeiroPeriodoLetivoSelecionado;
	private Integer primeiraTurmaSelecionada;
	private String apresentarRichAvisoContaReceber;
	private boolean avisoRenovandoMatriculaExistente = true;
	private Boolean renovandoMatricula;
	private List listaSelectItemFormacaoAcademicaAluno;
	private List listaSelectItemReconhecimentoCurso;
	private String situacaoFiltroTurma;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private List listaConsultaFuncionario;
	private Boolean autorizacaoApresentarConsultorMatricula;
	private Boolean permiteInformarTipoMatricula;
	private Boolean permiteAnexarImagemDocumentosEntregues;
	private Boolean editandoMatricula;
	private Boolean controlarSuspensaoMatriculaPendenciaDocumentos;
	private Boolean apresentarModalAvisoAlunoReprovado = Boolean.FALSE;
	private String mensagemModalAvisoAlunoReprovado;
	private Integer tipoLayoutComprovanteMatricula;
	private Boolean renovacaoRecusada;
	private Boolean apresentarModalTermoAceite;
	private String nomeArquivo;
//	private List<PlanoFinanceiroAlunoLogVO> planoFinanceiroAlunoLogVOs;
//	private List<HorarioTurmaDisciplinaSemanalVO> listaHorarioTurmaDisciplinaSemanalVOs;
	private Date dataInicioHorario;
	private Date dataTerminoHorario;
	private Boolean permitirRealizarMatriculaDisciplinaPreRequisito;
	private String usernamePermitirRealizarMatriculaDisciplinaPreRequisito;
	private String senhaPermitirRealizarMatriculaDisciplinaPreRequisito;
	private Boolean apresentarFormacaoAcademica;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
//	private List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaFazParteComposicao;
	private List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs;
	// Ano e semestre serão usados apenas para editar matricula automaticamente
	private String ano;
	private String semestre;
	private Boolean autorizacaoMatriculaAcimaRealizada;
	private Boolean apresentarUsuarioSenhaAutorizarMatriculaAcimaRealizada;
	private String apresentarModalUsuarioSenhaAutorizarMatriculaAcimaRealizada;
	private String mensagemUsuarioSenhaAutorizarMatriculaAcimaRealizada;
	private String usernameLiberarMatriculaAcimaNrAulas;
	private String senhaLiberarMatriculaAcimaNrAulas;
	private Boolean permitirMatriculaOnline;
	private Integer banner;
	private Boolean termoAceiteContrato;
	private String apresentarModalInscricao;
	private Boolean realizouTransferenciaMatrizCurricular;

	private Boolean abrirPainelParcelaExtra;	
	private Double valorTotalParcelasInclusao;
	private boolean permitirUsuarioDesconsiderarDiferencaValorRateio = false;
	
	private String usernameLiberarDesativacaoFinanceiroManual;
	private String senhaLiberarDesativacaoFinanceiroManual;
	private Boolean apresentarPanelDesativarFinanceiroManual;
	private String userNameLiberarDescontoAluno;
	private String senhaLiberarDescontoAluno;
	private Boolean apresentarPanelDigitarSenhaLiberacaoDescontoAluno;
	private Boolean usuarioPossuiPermissaoParaDigitarDescontoAluno;
	private Double percDescontoMatricula;
	private Double valorDescontoMatricula;
	private Double percDescontoParcela;
	private Double valorDescontoParcela;
	private String tipoDescontoMatricula;
	private String tipoDescontoParcela;
	private Boolean liberadoInclusaoPorEquivalencia;
	private Boolean liberadoInclusaoOptativa;
	private boolean liberadoInclusaoTurmaOutroUnidadeEnsino = false;
	private Boolean liberadoInclusaoTurmaOutroCurso;
	private Boolean liberadoInclusaoTurmaOutroMatrizCurricular;
	private Boolean permitirAlterarTurnoRenovacao;
	private String oncompleteOperacaoFuncionalidade;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO;
	private List<OperacaoFuncionalidadeVO> operacaoFuncionalidadePersistirVOs;
	private List<SelectItem> listaSelectItemSubturmaPratica;
	private List<SelectItem> listaSelectItemSubturmaTeorica;
	private TurmaVO subturmaPraticaDestino;
	private TurmaVO subturmaTeoricaDestino;
	private TurmaVO turmaTelaConsulta;
	private boolean abrirPanelAlterarSubturmaPraticaTeorica = false;
	private Boolean liberarAtivacaoUltimaMatriculaPeriodo;
	private String usernameAtivarUltimaMatriculaPeriodo;
	private boolean abrirPanelAlterarSubturmaPraticaTeoricaFilhaComposicao = false;
	private String senhaAtivarUltimaMatriculaPeriodo;
	private boolean alunoSimulacaoValido = false;
	private List listaSelectItemDescontoProgressivoParceiro;
	private List listaSelectItemDescontoProgressivoAluno;
	private List listaSelectItemCentroReceita;
	private List listaSelectItemFormaPagamento;
	private List listaConsultaCentroDespesa;
	private String campoConsultaCentroDespesa;
	private String valorConsultaCentroDespesa;
	private String manterModalConvenioAberto;
	private Boolean liberarMatriculaTurmaSemVagaSemAula;
	private Boolean realizarAdiamentoSuspensaoMatricula;
	private String situacaoCancelamentoBloqueioMatricula;
	private Date dataAdiamentoSuspensaoMatricula;
	private Boolean liberarAlteracaoCategoriaPlanoFinanceiro;
	private Boolean permiteAlterarPlanoCondicaoFinanceiraCurso;
	private String userNamePermitirRegerarFinanceiro;
	private String senhaPermitirRegerarFinanceiro;
	private Boolean permitirRegerarFinanceiro;
	private Boolean permiteAlterarDataBaseGeracaoParcelas;
	private Map<String, Integer> dadosRecuperadosAlteracaoProcessoMatricula;
	private boolean obrigatorioContratoPorTurma =false;
	
	private String userNameLiberarValidacaoDadosEnadeCenso;
	private String senhaLiberarValidacaoDadosEnadeCenso;
	private Boolean permiteLiberarValidacaoDadosEnadeCenso;
	
	private TipoCartaoOperadoraCartaoEnum tipoCartao;
	private List<String> listCamposPreencherEnadeCenso;
	private Boolean permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso;
	private Boolean permiteAlunoAlterarTurmaBaseSugerida;	
	
	private ConfiguracaoBibliotecaVO configuracaoBibliotecaVO;
	private Boolean apresentarBotaoLiberarValidacaoAlunoBloqueioBiblioteca;
	private Boolean permiteLiberarValidacaoAlunoBloqueioBiblioteca;
	
	private Boolean solicitarLiberacaoFinanceira;
	private Boolean solicitarLiberacaoMatriculaAposInicioXModulos;
	
	private Boolean permitirSolicitarAprovacaoLiberacaoFinanceira;
	private Boolean permitirSolicitarLiberacaoMatriculaAposInicioXModulos;
	private Boolean permitirReconsideracaoSolicitacao;
	private String caminhoPreviewCertificado;
	private Boolean permitirEmitirBoletoMatriculaVisaoAluno;
	private String abrirModalInclusaoArquivoVerso;
	private DocumetacaoMatriculaVO documetacaoMatriculaVOAux;
	private Boolean permissaoAlterarDadosFinanceiros;
	private String onCompleteIncluirDisciplinaRegimeEspecial;
	private Boolean apresentarPanelIncluirDisciplinaRegimeEspecial;
	private MatriculaPeriodoVO matriculaPeriodoAlteracaoSituacaoManualVO;
	private SituacaoVinculoMatricula situacaoVinculoMatricula;
	private SituacaoMatriculaPeriodoEnum situacaoMatriculaPeriodo;
	private Boolean alterarSituacaoManualMatricula;
	private Boolean alterarSituacaoManualMatriculaPeriodo;
	private Boolean permitirAlterarSituacaoMatriculaManual;
	private Boolean habilitadoAssinarEletronicamente;
	private DocumentoAssinadoVO documentoAssinadoExcluir;
	private Integer quantidadeDocumentoAssinadoMatriculaPeriodo;
	private Boolean rejeitarContratosPendentesEmitidos;
	private List<DocumentoAssinadoVO> documentoAssinadoMatriculaPeriodo;
	private String caminhoPreviewContrato;
	private DocumentoAssinadoVO documentoAssinadoVO;
	private String motivoRejeicao;
	private Boolean documentoRejeitado;
	private Boolean permitirGerarContratoMatrila;
	private Boolean realizandoNovaMatriculaAlunoPartindoTransferenciaInterna;
	private Boolean existeRegraRenovacaoPorProcessoMatriculaTurma;
	private static final ConcurrentHashMap<Integer, Object> bloqueioMatricula = new ConcurrentHashMap<>();


	
	public RenovarMatriculaControle() throws Exception {
		super();
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("nomePessoa");
		verificarPermissaoSolicitarAprovacaoLiberacaoFinanceira();
		verificarPermissaoSolicitarLiberacaoMatriculaAposInicioXModulos();
	}

	@PostConstruct
	public void init() {
		try {
			LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(Matricula.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", false, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(layoutPadraoVO)) {
				setVersaoNova(Boolean.valueOf(layoutPadraoVO.getValor()));
				setVersaoAntiga(!Boolean.valueOf(layoutPadraoVO.getValor()));
			} else {
				setVersaoAntiga(false);
				setVersaoNova(true);
			}
			if (context().getExternalContext().getSessionMap().get("transferenciaMatrizCurricularMatricula") != null) {
				executarEdicaoMatriculaParaEliminarDisciplinasCursandoPorCorrespodencia();
				return;
			} else if (getPossuiCodigoTransferenciaEntrada()) {
				matricularApartirTransferenciaEntrada();
				return;
			} else if (context().getExternalContext().getSessionMap().get("TransferenciaInternaVO") != null && context().getExternalContext().getSessionMap().get("TransferenciaInternaVO") instanceof TransferenciaEntradaVO) {
				realizarMatriculaVindoDaTelaDaTransferenciaInterna();
				return;
			} else if (context().getExternalContext().getSessionMap().get("pessoaItem") != null) {
				realizarMatriculaVindoDaTelaDoAluno();
			} else {
				// obterUsuarioLogado();
				isVerificarUsuarioLogadoOtimize();
				setControleConsulta(new ControleConsulta());
				getControleConsulta().setCampoConsulta("nomePessoa");
				inicializarManagedBeanIniciarProcessamento();
				// montarListaSelectItemMatiulaVisaoAluno();
				montarListaSelectItemTurno();
				try {
					ConfiguracoesVO configuracoes = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getUnidadeEnsinoLogado().getCodigo());
					setControlarSuspensaoMatriculaPendenciaDocumentos(configuracoes.getControlarSuspensaoMatriculaPendenciaDocumentos());
				} catch (Exception e) {
					setControlarSuspensaoMatriculaPendenciaDocumentos(Boolean.FALSE);
				}
				if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) && (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle") != null) {
					VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
					if (visaoAlunoControle.getMenuRenovacaoMatricula()) {
						setMatriculaVisaoAluno(visaoAlunoControle.getMatricula().getMatricula());
						renovarMatriculaVisaoAluno();
					} else {
						visaoAlunoControle.inicializarMenuNovaMatricula();
					}
				} else {
					if (getMatriculaVO().getCurso().getCodigo() != 0 && getMatriculaVO().getUnidadeEnsino().getCodigo() != 0 && getMatriculaVO().getTurno().getCodigo() != 0) {
						if (getMatriculaVO().getTurno().getNome().equals("")) {
							getMatriculaVO().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(getMatriculaVO().getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
						}
						selecionarCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(getMatriculaVO().getCurso().getCodigo(), getMatriculaVO().getUnidadeEnsino().getCodigo(), getMatriculaVO().getTurno().getCodigo(), getUsuarioLogado()));
					}
					setPortadorDiploma(false);
					setMensagemID("msg_entre_prmconsulta");
					setGuiaAba("Inicio");
				}
//				getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "init1"+ getMatriculaVO().getMatricula());
				if (((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("banner") != null) {
					setBanner(Integer.valueOf(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("banner")));
					realizarInicializacaoDadosNovaMatricula(banner);
				}
				verificarApresentarBotoesMatricularRenovar();
				verificarApresentarBotaoAtivarUltimaMatriculaPeriodo();
				validarPermissaoInformarNumeroMatriculaManualmente();
			}
			verificarPermissaoUsuarioVisualizarConsultorMatricula();
			renovarMatriculaReativada();			
			realizarMatriculaVindoDaTelaDoPortadorDiplima();			
			realizarMatriculaVindoDaTelaResultadoProcessoSeletivo();
			verificarPermissaoAlterarSituacaoMatriculaManual();
			inicializarMensagemVazia();
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "init2"+ getMatriculaVO().getMatricula());
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public Boolean getPossuiCodigoTransferenciaEntrada() {
		Integer codigoTransferenciaEntrada = (Integer) context().getExternalContext().getSessionMap().get("codigoTransferenciaEntrada");
		return codigoTransferenciaEntrada != null;
	}
	
	public void matricularApartirTransferenciaEntrada() {
		try {
			if (context().getExternalContext().getSessionMap().get("transferenciaMatrizCurricularMatricula") != null) {
				return;
			}
			Integer codigoTransferenciaEntrada = (Integer) context().getExternalContext().getSessionMap().get("codigoTransferenciaEntrada");
			if (codigoTransferenciaEntrada == null || codigoTransferenciaEntrada == 0) {
				return;
			}
			TransferenciaEntradaVO transferenciaEntradaVO = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(codigoTransferenciaEntrada, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if ((transferenciaEntradaVO != null) && (!transferenciaEntradaVO.getCodigo().equals(0))) {
				matriculaApartirTranferenciaEntrada(transferenciaEntradaVO);
			}
			context().getExternalContext().getSessionMap().put("codigoTransferenciaEntrada", null);
			validarPermissaoInformarNumeroMatriculaManualmente();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	/**
	 * Método responsavel
	 * 
	 * @author Otimize - 30 de set de 2016
	 */
	public void executarEdicaoMatriculaParaEliminarDisciplinasCursandoPorCorrespodencia() {
		try {
			setTransferenciaMatrizCurricularMatriculaVO(null);
			if (context().getExternalContext().getSessionMap().get("transferenciaMatrizCurricularMatricula") != null) {
				TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatriculaVO = (TransferenciaMatrizCurricularMatriculaVO) context().getExternalContext().getSessionMap().get("transferenciaMatrizCurricularMatricula");
				setTransferenciaMatrizCurricularMatriculaVO(transferenciaMatrizCurricularMatriculaVO);
				int nrPeriodoLetivoMatrizAnterior = transferenciaMatrizCurricularMatriculaVO.getMatriculaPeriodoUltimoPeriodoVO().getPeriodoLetivo().getPeriodoLetivo();

				try {
					ConfiguracoesVO configuracoes = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getUnidadeEnsinoLogado().getCodigo());
					setControlarSuspensaoMatriculaPendenciaDocumentos(configuracoes.getControlarSuspensaoMatriculaPendenciaDocumentos());
				} catch (Exception e) {
					setControlarSuspensaoMatriculaPendenciaDocumentos(Boolean.FALSE);
				}
				inicializarManagedBeanIniciarProcessamento();
				matriculaPeriodoVO = transferenciaMatrizCurricularMatriculaVO.getMatriculaPeriodoUltimoPeriodoVO();
				matriculaPeriodoVO.setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				matriculaVO.setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				prepararMatriculaPeriodoParaEdicao();

				// mantendo a lista matriculaPeriodoTurmaDisciplina, pois
				// algumas delas vamos ser mantidas, mesmo com a alteracao da
				// grade.
				// Como por exemplo, matriculaPeriodoTurmaDisciplina que foram
				// alteradas para foraDaGrade para pagar uma disciplina da
				// matriz via
				// mapa de equivalencia. É preciso manter este dados em memoria,
				// pois vamos precisar deles, quando formos para a tela de
				// edicacao
				// das disciplinas em si.
				if (transferenciaMatrizCurricularMatriculaVO.getEliminandoDisciplinasCursandoPorCorrespodencia()) {
					List<MatriculaPeriodoTurmaDisciplinaVO> listaCorrespondenciaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
					listaCorrespondenciaVOs.addAll(transferenciaMatrizCurricularMatriculaVO.getListaDisciplinasPorCorrespondencia());
					transferenciaMatrizCurricularMatriculaVO.getListaDisciplinasPorCorrespondencia().clear();
					inicializarListaDisciplinasCursandoPorCorrespondeciaAposTransferenciaMatrizCurricular(listaCorrespondenciaVOs);
//					transferenciaMatrizCurricularMatriculaVO.getListaDisciplinasPorCorrespondencia().addAll(getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs());
				}
				getMatriculaPeriodoVO().setTransferenciaMatrizCurricularMatriculaVO(transferenciaMatrizCurricularMatriculaVO);

				if (transferenciaMatrizCurricularMatriculaVO.getTurnoMigrarMatricula() != null) {
					// se o turno vei informado da transferencia é por que o
					// usuário deseja, realizar alem da elimincacao
					// das disciplinas cursando por correspoencia, também uma
					// mudança de turno. Assim, iremos alterar o turno
					// da matriculaPeriodo, e limpar todos os dados que dependem
					// do turno, como por exemplo, Turma, calendario,
					// e planoFinanceiro.
					if (!getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getTurno().getCodigo().equals(transferenciaMatrizCurricularMatriculaVO.getTurnoMigrarMatricula().getCodigo())) {
						// se o turno na transferencia é diferente do existente
						// na MatriculaPeriodo, entao vamos definir o novo turno
						// para a mesma,
						// e vamos inicializar dados que dependiam do turno
						// anterio.
						UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(getMatriculaVO().getCurso().getCodigo(), getMatriculaVO().getUnidadeEnsino().getCodigo(), transferenciaMatrizCurricularMatriculaVO.getTurnoMigrarMatricula().getCodigo(), getUsuarioLogado());
						getMatriculaVO().setTurno(unidadeEnsinoCursoVO.getTurno());
						getMatriculaPeriodoVO().setUnidadeEnsinoCursoVO(unidadeEnsinoCursoVO);
						getMatriculaPeriodoVO().setUnidadeEnsinoCurso(unidadeEnsinoCursoVO.getCodigo());
					}
				}
				getMatriculaPeriodoVO().setTurma(null);
//				getMatriculaPeriodoVO().setPlanoFinanceiroCurso(null);
//				getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(null);

				montarListaProcessoMatricula(false);

				// temos que levar o aluno, para a nova matriz, contudo,
				// mantendo o mesmo período letivo.
				// pois a intençao aqui, não é uma evolução / refinição de
				// período letivo. Mas somente,
				// o realinhamento de disciplinas que estão sendo cursadas por
				// correspodencia.
				PeriodoLetivoVO periodoLetivo = matriculaVO.getGradeCurricularAtual().getPeriodoLetivoPorNumero(nrPeriodoLetivoMatrizAnterior);
				if (periodoLetivo == null) {
					// se nao achamos um periodo letivo correspondente, vamos
					// adotar o ultimo periodo letivo da nova matriz.
					// isto por ocorrer quando o aluno migrar de uma matriz de
					// 10 periodos pode exemplo, para uma matriz com 8 periodos.
					periodoLetivo = matriculaVO.getGradeCurricularAtual().getUltimoPeriodoLetivoGrade();
				}
				getMatriculaPeriodoVO().setPeridoLetivo(periodoLetivo);
				this.setCodigoPeriodoLetivo(periodoLetivo.getCodigo());

				// como trata-se de uma edicao, vamos adicionar forcar para o
				// combobox de periodo letivo, somentte o periodoletivo
				// da matriculaperiodo atual. Assim, para poder alterar o mesmo,
				// caso tenha permissao para isto.
				getMatriculaPeriodoVO().getListaPeriodosLetivosValidosParaMatriculaPeriodo().clear();
				getMatriculaPeriodoVO().getListaPeriodosLetivosValidosParaMatriculaPeriodo().add(periodoLetivo);

				montarListaSelectItemPeriodoLetivo();

				selecionarPeriodoLetivo();

				verificarPermissaoUsuarioInformarTipoMatriculaMatricula();
				validarPermissaoInformarNumeroMatriculaManualmente();
				context().getExternalContext().getSessionMap().remove("transferenciaMatrizCurricularMatricula");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			matriculaPeriodoVO.setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
			matriculaVO.setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
			context().getExternalContext().getSessionMap().remove("transferenciaMatrizCurricularMatricula");
		} finally {
			context().getExternalContext().getSessionMap().remove("transferenciaMatrizCurricularMatricula");
		}
	}

	public void inicializarListaDisciplinasCursandoPorCorrespondeciaAposTransferenciaMatrizCurricular(List<MatriculaPeriodoTurmaDisciplinaVO> listaCorrespondenciaVOs) {
		for (MatriculaPeriodoTurmaDisciplinaVO objAdicionar : getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs()) {
			for (MatriculaPeriodoTurmaDisciplinaVO objCorrespondente : listaCorrespondenciaVOs) {
				if (objCorrespondente.getDisciplina().getCodigo().equals(objAdicionar.getDisciplina().getCodigo())) {
					getMatriculaPeriodoVO().getTransferenciaMatrizCurricularMatriculaVO().getListaDisciplinasPorCorrespondencia().add(objAdicionar);
					break;
				}
			}
		}
	}

	public void verificarApresentarBotoesMatricularRenovar() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_NaoApresentarBotoesMatriculaRenovacao", getUsuarioLogado());
			setNaoApresentarBotoesMatriculaRenovacao(Boolean.TRUE);
		} catch (Exception e) {
			setNaoApresentarBotoesMatriculaRenovacao(Boolean.FALSE);
		}
	}

	public void verificarPermissaoUsuarioRealizarMatriculaCursarDisciplinaPreRequisito() {
		try {
			UsuarioVO usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.usernamePermitirRealizarMatriculaDisciplinaPreRequisito, this.senhaPermitirRealizarMatriculaDisciplinaPreRequisito, true, Uteis.NIVELMONTARDADOS_TODOS);
			verificarPermissaoUsuarioLiberarMatriculaComPendenciasFinanceiras(usuarioVerif, "Matricula_PermitirRealizarMatriculaDisciplinaPreRequisito");
			getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARMATRICULA_INCLUSAODISCIPLINAPREREQUISITO, usuarioVerif, ""));
			setPermitirRealizarMatriculaDisciplinaPreRequisito(Boolean.TRUE);
			setMensagemID("msg_PermissaoUsuarioRealizarMatriculaDisciplinaPreRequisito");
		} catch (Exception e) {
			setPermitirRealizarMatriculaDisciplinaPreRequisito(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void verificarUsuarioPossuiPermissaoIncluirNegociacaoRecebimentoContaReceber() throws Exception {
		getFacadeFactory().getControleAcessoFacade().incluir("NegociacaoRecebimento", true, getUsuarioLogado());
	}

	


	

	

	public void matriculaApartirTranferenciaEntrada(TransferenciaEntradaVO transferenciaEntradaVO) {
		try {
			if (!getMatriculaVO().getTransferenciaEntrada().getCodigo().equals(0)) {
				// se for diferente de zero é por que este método já foi chamado
				// e os
				// dados da transferencia / renovação já foram iniciados.
				return;
			}
			novo();
			navegarAbaDadosBasicos();
			// LISTAGEM DE DISCIPLINA APROVEITADAS, REFERENTE A APROVEITAMENTO
			// PREVISTO.

			if (transferenciaEntradaVO.getMatriculado().equals(false) || transferenciaEntradaVO.getMatricula().getMatricula().equals("")) {
				getFacadeFactory().getMatriculaFacade().incializarDadosAPartirTransferenciaEntrada(getMatriculaVO(), transferenciaEntradaVO, getMatriculaPeriodoVO(), getUsuarioLogado());
				AproveitamentoDisciplinaVO aprov = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultarPorCodigoRequerimento(transferenciaEntradaVO.getCodigoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				setRealizandoNovaMatriculaAlunoPartindoTransferenciaInterna(true);
				inicializarMatriculaComHistoricoAluno(true);
				// é necessário chamar o método abaixo, pois vincula, cada
				// aproveitamento a sua respectiva GradeDisciplinaVO ou
				// GradeCurricularGrupoOptativaVO
				aprov.setMatricula(this.getMatriculaVO());
				aprov.setGradeCurricular(this.getMatriculaVO().getGradeCurricularAtual());
				getMatriculaVO().setGradeCurricularAtual(transferenciaEntradaVO.getGradeCurricular());
				setCodigoGradeCurricular(getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
				getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarMontagemPainelMatrizCurricularComDisciplinasAproveitadas(aprov, getUsuarioLogado());
				getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarMontagemListaDisciplinasAproveitadas(aprov, aprov.getInstituicao(), aprov.getCidadeVO(), getUsuarioLogado());
				List<HistoricoVO> historicosPrevitosAproveitamento = getFacadeFactory().getAproveitamentoDisciplinaFacade().gerarHistoricosPrevistosDisciplinasAproveitadas(aprov, getUsuarioLogado());
				getMatriculaVO().setHistoricosAproveitamentoDisciplinaPrevisto(historicosPrevitosAproveitamento);
				getMatriculaVO().setAproveitamentoDisciplinaVO(aprov);
				// é necessário ser chamado após o
				// inicializarMatriculaComHistoricoAluno para garantir que os
				// históricos do aproveitamento, informados acima
				// sejam reprocessados, gerando assim uma visão da evolução do
				// aluno, quando se trata de uma transferencia de entrada com
				// aproveitamento.
				getFacadeFactory().getHistoricoFacade().atualizarDadosMatriculaComHistoricoAlunoVO(getMatriculaVO(), getMatriculaVO().getGradeCurricularAtual().getCodigo(), false, getMatriculaVO().getCurso().getConfiguracaoAcademico(), getUsuarioLogado());
				// atualizar a lista de periodos permitidos para matricular o
				// aluno, considerando agora os aproveitamentos supracitados.
				//getFacadeFactory().getMatriculaPeriodoFacade().obterListaPeriodosLetivosValidosParaRenovacaoMatriculaInicializandoPeriodoLetivoPadrao(getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaVO().getCurso().getConfiguracaoAcademico(), getMatriculaVO().getMatriculaComHistoricoAlunoVO(), getRealizandoNovaMatriculaAlunoPartindoTransferenciaInterna(), getUsuarioLogado());
			} else {
				editarComMatricula(transferenciaEntradaVO.getMatricula());
				setRealizandoNovaMatriculaAlunoPartindoTransferenciaInterna(true);
				getMatriculaVO().setGradeCurricularAtual(transferenciaEntradaVO.getGradeCurricular());
				setCodigoGradeCurricular(getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
				inicializarMatriculaComHistoricoAluno(true);
			}
			getMatriculaVO().setTransferenciaEntrada(transferenciaEntradaVO);
			getMatriculaPeriodoVO().setTranferenciaEntrada(transferenciaEntradaVO.getCodigo());

			gerarDadosGraficoEvolucaoAcademicaAluno();
			montarListaSelectItemUnidadeEnsino();
			montarListaProcessoMatricula(false);
			montarListaSelectItemGradeCurricular();
			montarListaSelectItemPeriodoLetivo();
			montarListaSelectItemTurma();
			apresentarPrimeiro = new UICommand();
			apresentarAnterior = new UICommand();
			apresentarPosterior = new UICommand();
			apresentarUltimo = new UICommand();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public  void verificarPermissaoUsuarioLiberarMatriculaComPendenciasFinanceiras(UsuarioVO usuario, String nomeEntidade) throws Exception {
		getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public void verificarUsuarioPodeAlterarDescontoProgressivoPrimeiraParcela() {
		Boolean liberar = false;
		try {
			verificarPermissaoUsuarioLiberarMatriculaComPendenciasFinanceiras(getUsuarioLogado(), "Matricula_LiberarDescontoProgressivoPrimeiraParcela");
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setLiberarAlteracaoDescontoProgressivoPrimeiraParcela(liberar);
	}

	public void executarVerificacaoUsuarioPodeLiberarMatriculaAcimaNrVagas() {
		boolean usuarioValido = false;
		UsuarioVO usuarioVerif = null;
		try {
			usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.getUsernameLiberarMatriculaAcimaNrVagas(), this.getSenhaLiberarMatriculaAcimaNrVagas(), true, Uteis.NIVELMONTARDADOS_TODOS);
			usuarioValido = true;
		} catch (Exception e) {
		}
		boolean usuarioTemPermissaoLiberarDentroLimiteMaximoTurma = false;
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MatriculaNrMaximoVagas", usuarioVerif);
			usuarioTemPermissaoLiberarDentroLimiteMaximoTurma = true;
		} catch (Exception e) {
		}
		boolean usuarioTemPermissaoLiberarAcimaLimiteMaximoTurma = false;
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MatriculaAcimaNrMaximoVagas", usuarioVerif);
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
				// está dentro do limite
				// maximo da turma. Basta permitir a matricula normalmente
				Integer vagasDisponiveisDentroLimite = 0;
				if (!Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaPratica()) && !Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaTeorica())) {
					vagasDisponiveisDentroLimite = this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurma().getNrMaximoMatricula() - this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getNrAlunosMatriculados();
				}
				if (Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaPratica())) {
					vagasDisponiveisDentroLimite = this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaPratica().getNrMaximoMatricula() - this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getNrAlunosMatriculadosTurmaPratica();
				}
				if (Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaTeorica()) && vagasDisponiveisDentroLimite >= 0) {
					vagasDisponiveisDentroLimite = this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaTeorica().getNrMaximoMatricula() - this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getNrAlunosMatriculadosTurmaTeorica();
				}
				if ((vagasDisponiveisDentroLimite > 0)) {
					if (!usuarioTemPermissaoLiberarDentroLimiteMaximoTurma) {
						throw new Exception("Você não tem permissão para liberar a matrícula/renovação deste aluno acima do número de vagas disponíveis na turma.");
					}
				} else {
					throw new Exception("Você não tem permissão para liberar a matrícula/renovação deste aluno acima do número máximo de vagas disponíveis na turma.");
				}
			}
			for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina : this.getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs()) {
				if ((this.getLiberarTodasDisciplinasComIndisponibilidadeVagas()) || (matriculaPeriodoTurmaDisciplina.getDisciplina().getCodigo().equals(this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplina().getCodigo())) || (getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplinaComposta() && matriculaPeriodoTurmaDisciplina.getGradeDisciplinaCompostaVO().getGradeDisciplina().getCodigo().equals(this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getGradeDisciplinaVO().getCodigo())) || (getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplinaFazParteComposicao() && matriculaPeriodoTurmaDisciplina.getGradeDisciplinaVO().getCodigo().equals(this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getGradeDisciplinaCompostaVO().getGradeDisciplina().getCodigo()))) {
					if (usuarioTemPermissaoLiberarAcimaLimiteMaximoTurma) {
						matriculaPeriodoTurmaDisciplina.setLiberadaSemDisponibilidadeVagas(Boolean.TRUE);
						matriculaPeriodoTurmaDisciplina.setUsuarioLiberadaSemDisponibilidadeVagas(usuarioVerif);
						matriculaPeriodoTurmaDisciplina.setDataLiberadaSemDisponibilidadeVagas(new Date());
					} else {
						if ((matriculaPeriodoTurmaDisciplina.getApresentarLiberacaoVaga()) && (usuarioTemPermissaoLiberarDentroLimiteMaximoTurma)) {
							matriculaPeriodoTurmaDisciplina.setLiberadaSemDisponibilidadeVagas(Boolean.TRUE);
							matriculaPeriodoTurmaDisciplina.setUsuarioLiberadaSemDisponibilidadeVagas(usuarioVerif);
							matriculaPeriodoTurmaDisciplina.setDataLiberadaSemDisponibilidadeVagas(new Date());
						}
					}
				}
			}
			if (getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplinaFazParteComposicao()) {
				for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : listaMatriculaPeriodoTurmaDisciplinaFazParteComposicao) {
					if (usuarioTemPermissaoLiberarAcimaLimiteMaximoTurma || ((matriculaPeriodoTurmaDisciplinaVO.getApresentarLiberacaoVaga()) && (usuarioTemPermissaoLiberarDentroLimiteMaximoTurma))) {
						matriculaPeriodoTurmaDisciplinaVO.setLiberadaSemDisponibilidadeVagas(Boolean.TRUE);
						matriculaPeriodoTurmaDisciplinaVO.setUsuarioLiberadaSemDisponibilidadeVagas(usuarioVerif);
						matriculaPeriodoTurmaDisciplinaVO.setDataLiberadaSemDisponibilidadeVagas(new Date());
					}
				}
			}
			getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARMATRICULADISICPLINASEMVAGA, usuarioVerif, ""));
			this.setUsernameLiberarMatriculaAcimaNrVagas("");
			this.setSenhaLiberarMatriculaAcimaNrVagas("");
			setMensagemID("msg_ConfirmacaoLiberacaoMatriculaAcimaNrVadas");
		} catch (Exception e) {
			this.setUsernameLiberarMatriculaAcimaNrVagas("");
			this.setSenhaLiberarMatriculaAcimaNrVagas("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarVerificacaoUsuarioPodeLiberarMatriculaAcimaNrAulas() {
		boolean usuarioValido = false;
		UsuarioVO usuarioVerif = null;
		try {
			usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.getUsernameLiberarMatriculaAcimaNrAulas(), this.getSenhaLiberarMatriculaAcimaNrAulas(), true, Uteis.NIVELMONTARDADOS_TODOS);
			usuarioValido = true;
		} catch (Exception e) {
		}
		boolean usuarioTemPermissaoLiberar = false;
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MatriculaNrMaximoAulas", usuarioVerif);
			usuarioTemPermissaoLiberar = true;
		} catch (Exception e) {
		}
		try {
			if (!usuarioValido) {
				throw new Exception("Usuário/Senha Inválidos");
			}
			if (!usuarioTemPermissaoLiberar) {
				throw new Exception("Você não tem permissão para liberar a matrícula/renovação deste aluno acima do número de aulas já realizadas na turma.");
			} else {
				setAutorizacaoMatriculaAcimaRealizada(Boolean.TRUE);
			}
			getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARMATRICULADISICPLINASEMVAGA, usuarioVerif, ""));
			this.setUsernameLiberarMatriculaAcimaNrAulas("");
			this.setSenhaLiberarMatriculaAcimaNrAulas("");
			setApresentarModalUsuarioSenhaAutorizarMatriculaAcimaRealizada("");
			setMensagemID("msg_ConfirmacaoLiberacaoMatriculaAcimaNrAulas");
		} catch (Exception e) {
			this.setUsernameLiberarMatriculaAcimaNrAulas("");
			this.setSenhaLiberarMatriculaAcimaNrAulas("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarVerificacaoUsuarioPodeLiberarRenovacaoDocumentoPendente() {
		try {
			UsuarioVO usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.getUsernameLiberarRenovacaoDocumentoPendente(), this.getSenhaLiberarRenovacaoDocumentoPendente(), true, Uteis.NIVELMONTARDADOS_TODOS);
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_RenovarComPendenciaDocumentoObrigatorio", usuarioVerif);
			getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARMATRICULA_PENDENCIADOCUMENTO, usuarioVerif, ""));
			this.getMatriculaVO().setLiberarRenovacaoDocumentaoImpediRenovacaoPendente(Boolean.TRUE);
			this.getMatriculaVO().setExisteDocumentoPendenteImpediRenovacao(Boolean.FALSE); // para
																							// q
																							// botao
																							// nao
																							// seja
																							// apresentado
																							// novamente
			this.setUsernameLiberarRenovacaoDocumentoPendente("");
			this.setSenhaLiberarRenovacaoDocumentoPendente("");
			setMensagemID("msg_ConfirmacaoLiberacaoRenovarMatriculaComDocumentosPendentes");
		} catch (Exception e) {
			this.setUsernameLiberarRenovacaoDocumentoPendente("");
			this.setSenhaLiberarRenovacaoDocumentoPendente("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarVerificacaoUsuarioPodeLiberarMatriculaTodosPeriodosLetivos() {
		try {
			UsuarioVO usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.getUsernameLiberarMatriculaTodosPeriodos(), this.getSenhaLiberarMatriculaTodosPeriodos(), true, Uteis.NIVELMONTARDADOS_TODOS);
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_PermitirAlterarPeriodoLetivoLimiteRenovacao", usuarioVerif);
			getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARMATRICULA_OUTROPERIODOLETIVO, usuarioVerif, ""));
			this.getMatriculaVO().setLiberarMatriculaTodosPeriodos(Boolean.TRUE);
			this.setApresentarBotaoLiberarMatriculaTodosPeriodos(Boolean.FALSE);
			this.setUsernameLiberarMatriculaTodosPeriodos("");
			this.setSenhaLiberarMatriculaTodosPeriodos("");
			//getFacadeFactory().getMatriculaPeriodoFacade().obterListaPeriodosLetivosValidosParaRenovacaoMatriculaInicializandoPeriodoLetivoPadrao(getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaVO().getCurso().getConfiguracaoAcademico(), getMatriculaVO().getMatriculaComHistoricoAlunoVO(), getUsuarioLogado());
			montarListaSelectItemPeriodoLetivo();
			setMensagemID("msg_ConfirmacaoLiberacaoMatriculaTodosOsPeriodos");
		} catch (Exception e) {
			this.setUsernameLiberarMatriculaTodosPeriodos("");
			this.setSenhaLiberarMatriculaTodosPeriodos("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarVerificacaoUusuarioPodeLiberarMatriculaComPendenciasFinanceiras() {
		try {
			UsuarioVO usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.usernameLiberarRenovacaoComDebitosFinanceiros, this.senhaLiberarRenovacaoComDebitosFinanceiros, true, Uteis.NIVELMONTARDADOS_TODOS);
			verificarPermissaoUsuarioLiberarMatriculaComPendenciasFinanceiras(usuarioVerif, "Matricula_AutorizarRenovacaoComDebito");
			getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARMATRICULA_ALUNOPENDENTEFINANCEIRO, usuarioVerif, ""));
			this.liberarRenovacaoComDebitosFinanceiros = true;
			setMensagemID("msg_ConfirmacaoLiberacaoMatriculaComPendencias");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarVerificacaoUusuarioPodeAnexarImagemDocumentosEntregues() {
		Boolean liberar = false;
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MatriculaAnexarImagemDocumentosEntregues", getUsuarioLogado());
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setPermiteAnexarImagemDocumentosEntregues(liberar);
	}

	public void selecionarObjetoDocumentacaoMatricula() {
		setOncompleteModal("");
		try {
			setTam(100);
			setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			setDocumetacaoMatriculaVO(obj);
			getFacadeFactory().getDocumetacaoMatriculaFacade().validarPermissaoPermiteUploadDocumentoIndeferidoForaPrazoParaMatriculaProcessoSeletivo(getMatriculaVO() , getDocumetacaoMatriculaVO(),getUsuarioLogado());
			if (obj != null && obj.getCodigo().equals(0) && getDocumetacaoMatriculaVOAux() == null) {
				try {
					setDocumetacaoMatriculaVOAux((DocumetacaoMatriculaVO) obj.clone());
				} catch (Exception e) {
				}
			}
			if (obj.getArquivoVO().getNome().equals("")) {
				getDocumetacaoMatriculaVO().setArquivoVO(new ArquivoVO());
				getDocumetacaoMatriculaVO().getArquivoVO().setDescricao(obj.getTipoDeDocumentoVO().getNome());
			}
			setOncompleteModal("PF('panelUpload').show()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarObjetoDocumentacaoMatriculaVerso() throws CloneNotSupportedException {
		setOncompleteModal("");
		try {
			setTam(100);
			setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			setDocumetacaoMatriculaVO((DocumetacaoMatriculaVO) obj.clone());
			getFacadeFactory().getDocumetacaoMatriculaFacade().validarPermissaoPermiteUploadDocumentoIndeferidoForaPrazoParaMatriculaProcessoSeletivo(getMatriculaVO() , getDocumetacaoMatriculaVO(),getUsuarioLogado());
			if (obj.getArquivoVOVerso().getNome().equals("")) {
				getDocumetacaoMatriculaVO().setArquivoVOVerso(new ArquivoVO());
				getDocumetacaoMatriculaVO().getArquivoVOVerso().setDescricao(obj.getTipoDeDocumentoVO().getNome()+"_VERSO");
			}
		    setOncompleteModal("PF('panelUploadVerso').show()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getDocumetacaoMatriculaFacade().realizarUploadArquivo(uploadEvent, null, getDocumetacaoMatriculaVO(), getMatriculaVO().getAluno(), true, false, null, getUsuarioLogado(), getUsuarioLogado().getVisaoLogar());
			setMensagemID("msg_sucesso_upload");
			setOncompleteModal("PF('panelUpload').hide(); PF('panelImagemDocumentosEntregues').show();");
		} catch (Exception e) {
			setOncompleteModal("PF('panelUpload').hide(); PF('panelImagemDocumentosEntregues').hide();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			uploadEvent = null;
		}
	}

	public void upLoadArquivoVerso(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getDocumetacaoMatriculaFacade().realizarUploadArquivo(uploadEvent, null, getDocumetacaoMatriculaVO(), getMatriculaVO().getAluno(), false, false, null, getUsuarioLogado(), getUsuarioLogado().getVisaoLogar());
			setMensagemID("msg_sucesso_upload");
			setOncompleteModal("PF('panelUploadVerso').hide(); PF('panelImagemDocumentosEntreguesVerso').show();");
		} catch (Exception e) {
			setOncompleteModal("PF('panelUploadVerso').hide(); PF('panelImagemDocumentosEntreguesVerso').hide();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			uploadEvent = null;
		}
	}
	
	public String getCaminhoPDF() {
		return getDocumetacaoMatriculaVO().getArquivoVO().getPastaBaseArquivoWeb() + "?embedded=true";
	}
	
	public String getCaminhoPDFVerso() {
		return getDocumetacaoMatriculaVO().getArquivoVOVerso().getPastaBaseArquivoWeb() + "?embedded=true";
	}

	public void criarNomeArquivo() {
		setTam(100);
		setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
	}

	public void upLoadArquivoScanner() {
		try {

			getDocumetacaoMatriculaVO().getArquivoVO().setCpfAlunoDocumentacao(getMatriculaVO().getAluno().getCPF());
			getDocumetacaoMatriculaVO().setUsuario(getUsuarioLogadoClone());
			if (!getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getControlaAprovacaoDocEntregue()) {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.TRUE);
			} else {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.FALSE);
			}
			// if
			// (context().getExternalContext().getSessionMap().get("nomeArquivo")
			// != null) {
			getDocumetacaoMatriculaVO().getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline()));
			getFacadeFactory().getArquivoHelper().upLoadDocumentacaoScanner(getNomeArquivo(), getDocumetacaoMatriculaVO().getArquivoVO(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());

			// } else {
			// throw new
			// Exception("Não foi possível obter o arquivo do scanner.");
			// }
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
		}
	}

	public void upLoadArquivoScannerVerso() {
		try {

			getDocumetacaoMatriculaVO().getArquivoVO().setCpfAlunoDocumentacao(getMatriculaVO().getAluno().getCPF());
			getDocumetacaoMatriculaVO().setUsuario(getUsuarioLogadoClone());
			if (!getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getControlaAprovacaoDocEntregue()) {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.TRUE);
			} else {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.FALSE);
			}
			// if
			// (context().getExternalContext().getSessionMap().get("nomeArquivo")
			// != null) {
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline()));
			getFacadeFactory().getArquivoHelper().upLoadDocumentacaoScanner(getNomeArquivo(), getDocumetacaoMatriculaVO().getArquivoVOVerso(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());

			// } else {
			// throw new
			// Exception("Não foi possível obter o arquivo do scanner.");
			// }
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
		}
	}

	public String getCaminhoServidorDownloadDocumentacao() {
		try {
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getDocumetacaoMatriculaVO().getArquivoVO(), getDocumetacaoMatriculaVO().getArquivoVO().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema()) + "?UID=" + new Date().getTime();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadDocumentacaoVerso() {
		try {
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getDocumetacaoMatriculaVO().getArquivoVOVerso().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema()) + "?UID=" + new Date().getTime();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public void rotacionar90GrausParaEsquerda() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireita() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180Graus() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void recortarImagem() {
		try {

			if (getLargura() == 0f && getAltura() == 0f && getX() == 0f && getY() == 0f) {
				throw new Exception("Clique e arraste sobre a imagem para selecionar a área que deve ser recortada.");
			}
			getFacadeFactory().getArquivoHelper().recortarImagem(getDocumetacaoMatriculaVO().getArquivoVO(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY(), getUsuarioLogado());
			limparMensagem();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void rotacionar90GrausParaEsquerdaVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireitaVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180GrausVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void recortarImagemVerso() {
		try {
			if (getLarguraVerso() == 0f && getAlturaVerso() == 0f && getXcropVerso() == 0f && getYcropVerso() == 0f) {
				throw new Exception("Clique e arraste sobre a imagem para selecionar a área que deve ser recortada.");
			}
			getFacadeFactory().getArquivoHelper().recortarImagem(getDocumetacaoMatriculaVO().getArquivoVOVerso(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getLarguraVerso(), getAlturaVerso(), getXcropVerso(), getYcropVerso(), getUsuarioLogado());
			limparMensagem();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void executarZoomIn() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOut() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomInVerso() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOutVerso() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
	}

	public void adicionarArquivoDocumentacaoMatricula() {
		try {
			setAbrirModalInclusaoArquivoVerso("");
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Adicionar Arquivo Documentação Matrícula", "Uploading");
			getDocumetacaoMatriculaVO().getArquivoVO().getResponsavelUpload().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoMatriculaVO().getArquivoVO().getResponsavelUpload().setNome(getUsuarioLogado().getNome());
			getDocumetacaoMatriculaVO().getUsuario().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoMatriculaVO().getUsuario().setNome(getUsuarioLogado().getNome());
			getDocumetacaoMatriculaVO().getArquivoVO().setDataUpload(new Date());
			getDocumetacaoMatriculaVO().getArquivoVO().setManterDisponibilizacao(true);
			getDocumetacaoMatriculaVO().getArquivoVO().setDataDisponibilizacao(getDocumetacaoMatriculaVO().getArquivoVO().getDataUpload());
			getDocumetacaoMatriculaVO().getArquivoVO().setDataIndisponibilizacao(null);
			getDocumetacaoMatriculaVO().getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getDocumetacaoMatriculaVO().getArquivoVO().setOrigem(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor());
			getDocumetacaoMatriculaVO().getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
			getDocumetacaoMatriculaVO().setExcluirArquivo(false);
			if (!getDocumetacaoMatriculaVO().getEntregue() && getDocumetacaoMatriculaVO().getTipoDeDocumentoVO().getDocumentoFrenteVerso()) {
				setAbrirModalInclusaoArquivoVerso("PF('panelIncluirArquivoVerso').show()");
			} else {
				int x = 0;
				for(DocumetacaoMatriculaVO doc: getMatriculaVO().getDocumetacaoMatriculaVOs()) {
					if(doc.getTipoDeDocumentoVO().getCodigo().equals(getDocumetacaoMatriculaVO().getTipoDeDocumentoVO().getCodigo()) 
							&& doc.getAno().equals(getDocumetacaoMatriculaVO().getAno())
							&& doc.getSemestre().equals(getDocumetacaoMatriculaVO().getSemestre())) {
					getMatriculaVO().getDocumetacaoMatriculaVOs().set(x, getDocumetacaoMatriculaVO());
					break;
					}
					x++;
				}
				setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Adicionar Arquivo Documentação Matrícula", "Uploading");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getCaminhoServidorDownload() {
		try {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			context().getExternalContext().getSessionMap().put("arquivoVO", obj.getArquivoVO());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public void adicionarArquivoDocumentacaoMatriculaVerso() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Adicionar Arquivo Documentação Matrícula", "Uploading");
			getDocumetacaoMatriculaVO().getArquivoVO().getResponsavelUpload().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoMatriculaVO().getArquivoVO().getResponsavelUpload().setNome(getUsuarioLogado().getNome());
			getDocumetacaoMatriculaVO().getUsuario().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoMatriculaVO().getUsuario().setNome(getUsuarioLogado().getNome());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDataUpload(new Date());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setManterDisponibilizacao(true);
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDataDisponibilizacao(getDocumetacaoMatriculaVO().getArquivoVOVerso().getDataUpload());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDataIndisponibilizacao(null);
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setOrigem(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
			int index = 0;
			Iterator<DocumetacaoMatriculaVO> i = getMatriculaVO().getDocumetacaoMatriculaVOs().iterator();
			while (i.hasNext()) {
				DocumetacaoMatriculaVO documetacaoMatriculaVO = i.next();
				if (documetacaoMatriculaVO.getTipoDeDocumentoVO().getCodigo().equals(getDocumetacaoMatriculaVO().getTipoDeDocumentoVO().getCodigo())
						&& documetacaoMatriculaVO.getAno().equals(getDocumetacaoMatriculaVO().getAno())
						&& documetacaoMatriculaVO.getSemestre().equals(getDocumetacaoMatriculaVO().getSemestre())) {
					getMatriculaVO().getDocumetacaoMatriculaVOs().set(index,getDocumetacaoMatriculaVO());
					break;
				}
				index++;
			}
			getDocumetacaoMatriculaVO().setExcluirArquivo(false);
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Adicionar Arquivo Documentação Matrícula", "Uploading");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getCaminhoServidorDownloadVerso() {
		try {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			context().getExternalContext().getSessionMap().put("arquivoVO", obj.getArquivoVOVerso());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public void removerArquivoDocumentacao() throws Exception {
		try {
			getDocumetacaoMatriculaVO().setExcluirArquivo(true);
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Remover Arquivo Documentação Matrícula ", "Downloading - Removendo");
			getFacadeFactory().getDocumetacaoMatriculaFacade().excluirDocumentacaoMatricula(getDocumetacaoMatriculaVO(),getConfiguracaoGeralPadraoSistema(), !getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() , getUsuarioLogado());
			getDocumetacaoMatriculaVO().setEntregue(false);
			getDocumetacaoMatriculaVO().setDataEntrega(null);
			getDocumetacaoMatriculaVO().setUsuario(null);			
			getDocumetacaoMatriculaVO().setArquivoVO(new ArquivoVO());
			getDocumetacaoMatriculaVO().getArquivoVO().setDescricao("");			
			getDocumetacaoMatriculaVO().setArquivoVOVerso(new ArquivoVO());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDescricao("");
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Remover Arquivo Documentação Matrícula ", "Downloading - Removendo");
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getVerificarPossuiArquivoDocumentacaoMatricula() {
		try {
			if (getDocumetacaoMatriculaVO() != null && !getDocumetacaoMatriculaVO().getArquivoVO().getNome().equals("")) {
				return "PF('panelExcluir').show()";
			}
			return "";
		} catch (Exception e) {
			return "";
		}
	}

	public void cancelarExclusaoArquivoDocumentoEntregue() {
		if (getDocumetacaoMatriculaVO() != null && !getDocumetacaoMatriculaVO().getArquivoVO().getNome().equals("") && !getDocumetacaoMatriculaVO().getEntregue()) {
			getDocumetacaoMatriculaVO().setEntregue(true);
		}
		setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
	}

	public void selecionarArquivoDocumentoEntregueExclusao() {
		setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
		obj.setUsuario(getUsuarioLogadoClone());
		if (!obj.getTipoDeDocumentoVO().getTipoDocumentoEquivalenteVOs().isEmpty()) {
			Iterator j = obj.getTipoDeDocumentoVO().getTipoDocumentoEquivalenteVOs().iterator();
			while (j.hasNext()) {
				TipoDocumentoEquivalenteVO tipo = (TipoDocumentoEquivalenteVO) j.next();
				Iterator i = getMatriculaVO().getDocumetacaoMatriculaVOs().iterator();
				while (i.hasNext()) {
					DocumetacaoMatriculaVO doc = (DocumetacaoMatriculaVO) i.next();
					if (tipo.getTipoDocumentoEquivalente().getCodigo().intValue() == doc.getTipoDeDocumentoVO().getCodigo().intValue()) {
						if (obj.getEntregue()) {

							if (!doc.getEntregue()) {
								doc.setEntregue(Boolean.TRUE);
								doc.setDataEntrega(new Date());
								doc.setEntreguePorEquivalencia(Boolean.TRUE);
							} else {
								if (doc.getEntreguePorEquivalencia()) {
									doc.setEntregue(Boolean.FALSE);
									doc.setDataEntrega(null);
									doc.setEntreguePorEquivalencia(Boolean.FALSE);
								}
							}
						} else {
							if (doc.getEntreguePorEquivalencia()) {
								doc.setEntregue(Boolean.FALSE);
								doc.setDataEntrega(null);
								doc.setEntreguePorEquivalencia(Boolean.FALSE);
							}
						}
					}
				}
			}
		}
		setDocumetacaoMatriculaVO(obj);
	}

	public void prepararParaLiberarMatriculaComPendenciasFinanceiras() {
		usernameLiberarRenovacaoComDebitosFinanceiros = "";
		senhaLiberarRenovacaoComDebitosFinanceiros = "";
	}

	public void prepararParaLiberarRelizarMatriculaDisciplinaPreRequisito() {
		usernamePermitirRealizarMatriculaDisciplinaPreRequisito = "";
		senhaPermitirRealizarMatriculaDisciplinaPreRequisito = "";
	}

	public  void verificarPermissaoUsuarioLiberarInclusaoDisciplinaForaPrazo(UsuarioVO usuario, String nomeEntidade) throws Exception {
		getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public void realizarVerificacaoUusuarioLiberacaoInclusaoDisciplinaForaPrazo() {
		try {
			UsuarioVO usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.getUsernameLiberarInclusaoDisciplinaForaPrazo(), this.getSenhaLiberarInclusaoDisciplinaForaPrazo(), true, Uteis.NIVELMONTARDADOS_TODOS);
			verificarPermissaoUsuarioLiberarInclusaoDisciplinaForaPrazo(usuarioVerif, "Matricula_AutorizarInclusaoDisciplinaForaPrazo");
			getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARMATRICULA_INCLUSAODISCIPLINAFORAPRAZO, usuarioVerif, ""));
			getMatriculaPeriodoVO().setPossuiPermissaoInclusaoExclusaoDisciplina(Boolean.TRUE);
			getMatriculaVO().setPermitirInclusaoExclusaoDisciplinasRenovacao(Boolean.TRUE);
			setMensagemID("msg_ConfirmacaoLiberacaoInclusaoExclusaoDisciplina");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public  void verificarPermissaoUsuarioVisualizarConsultorMatricula(UsuarioVO usuario, String nomeEntidade) throws Exception {
		getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public void verificarPermissaoUsuarioVisualizarConsultorMatricula() {
		Boolean liberar = false;
		try {
			verificarPermissaoUsuarioVisualizarConsultorMatricula(getUsuarioLogado(), "ApresentarConsultorMatricula");
			liberar = true;
			if (liberar) {
				if (getMatriculaVO().getConsultor().getCodigo().intValue() == 0) {
					try {
						FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), false, null);
						getMatriculaVO().setConsultor(func);
					} catch (Exception e) {
						getMatriculaVO().setConsultor(new FuncionarioVO());
					}
				}
			}
		} catch (Exception e) {
			liberar = false;
		}
		this.setAutorizacaoApresentarConsultorMatricula(liberar);
	}

	public  void verificarPermissaoUsuarioInformarTipoMatricula(UsuarioVO usuario, String nomeEntidade) throws Exception {
		getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public  void verificarPermissaoUsuarioSuspenderMatricula(UsuarioVO usuario, String nomeEntidade) throws Exception {
		getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public  void verificarPermissaoUsuarioLiberarPgtoMatricula(UsuarioVO usuario, String nomeEntidade) throws Exception {
		getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public void verificarPermissaoUsuarioInformarTipoMatriculaMatricula() {
		Boolean liberar = false;
		try {
			verificarPermissaoUsuarioInformarTipoMatricula(getUsuarioLogado(), "MatriculaTipoMatricula");
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setPermiteInformarTipoMatricula(liberar);
	}

	public void verificarPermissaoUsuarioSuspenderMatricula() {
		Boolean liberar = false;
		try {
			verificarPermissaoUsuarioSuspenderMatricula(getUsuarioLogado(), "MatriculaSuspenderAtivaPendenciaDocumento");
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setPermiteSuspenderMatricula(liberar);
	}

	public void verificarPermissaoUsuarioLiberarPgtoMatricula() {
//		Boolean liberar = false;
//		try {
//			verificarPermissaoUsuarioLiberarPgtoMatricula(getUsuarioLogado(), "Matricula_LiberarMatricula");
//			liberar = true;
//		} catch (Exception e) {
//			liberar = false;
//		}
		this.setPermiteLiberarPgtoMatricula(false);
	}

	public void verificarPermissaoUsuarioConfirmarCancelarPreMatricula() {
		Boolean liberar = false;
		try {
			verificarPermissaoUsuarioSuspenderMatricula(getUsuarioLogado(), "MatriculaConfirmarCancelarPreMatricula");
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setPermiteConfirmarCancelarPreMatricula(liberar);
	}

	public void inicializarDadosUserNameSenhaLiberacaoDisciplinaForaPrazo() {
		setUsernameLiberarInclusaoDisciplinaForaPrazo("");
		setSenhaLiberarInclusaoDisciplinaForaPrazo("");
	}

	public String selecionarAlunoNovaMatricula() {
		inicializarManagedBeanIniciarProcessamento();
		getMatriculaVO().getAluno().setNome("Selecione o aluno que deseja matricular...");
		setGuiaAba("InicioNovaMatricula");
		setMensagemID("msg_entre_dados");
		setInscricao_Erro("");
		setApresentarFormacaoAcademica(verificarFormacaoAcademica());
		if (getConfiguracaoGeralPadraoSistema().getControlaQtdDisciplinaExtensao().booleanValue()) {
			getMatriculaVO().setQtdDisciplinasExtensao(getConfiguracaoGeralPadraoSistema().getQtdDisciplinaExtensao());
		}
		realizandoNovaMatriculaAluno = true;
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Matricula</code> para edição pelo usuário da aplicação.
	 */
	public String novaMatriculaAluno(Boolean transfereciaInterna) {
		setOncompleteModal("");
		inicializarListasSelectItemTodosComboBoxDadosBasicos(transfereciaInterna);
		inicializarUsuarioResponsavelMatriculaUsuarioLogado();
		validarPermissaoInformarNumeroMatriculaManualmente();
		if (getCodigoPortadorDiploma() == null || getCodigoPortadorDiploma() == 0) {
			setPortadorDiploma(false);
		}
		setDebitoFinanceiroAoIncluirConvenioLiberado(false);
		setMensagemID("msg_entre_dados");
		setGuiaAba("DadosBasicos");
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Matricula</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		setOncompleteModal("");
		registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Novo Renovar Matrícula", "Novo");

		removerObjetoMemoria(this);
		inicializarManagedBeanIniciarProcessamento();
		inicializarListasSelectItemTodosComboBoxDadosBasicos(false);
		inicializarUsuarioResponsavelMatriculaUsuarioLogado();
		verificarPermissaoUsuarioVisualizarConsultorMatricula();
		verificarPermissaoUsuarioInformarTipoMatriculaMatricula();
		validarPermissaoInformarNumeroMatriculaManualmente();
		setNovoNumeroMatriculaManualmente("");
		setMensagemID("msg_entre_dados");
		setGuiaAba("Inicio");
		setPortadorDiploma(false);
		setDebitoFinanceiroAoIncluirConvenioLiberado(false);
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
	}

	public void inicializarUsuarioResponsavelMatriculaUsuarioLogado() {
		try {
			matriculaVO.getUsuario().setCodigo(getUsuarioLogado().getCodigo());
			matriculaVO.getUsuario().setNome(getUsuarioLogado().getNome());
//			matriculaVO.getPlanoFinanceiroAluno().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
//			matriculaVO.getPlanoFinanceiroAluno().getResponsavel().setNome(getUsuarioLogado().getNome());
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	public void carregarDadosGerarRenovacaoMatriculaPeriodoFaseDadosBasicos() throws Exception {
		getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, NivelMontarDados.TODOS, getUsuarioLogado());

		// TODO (SEI CA37.1) Adicionado para carregar neste momento o plano
		// financeiro referente a Último Matricula Periodo.
		getDadosRecuperadosAlteracaoProcessoMatricula().clear();
//		matriculaVO.setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(matriculaVO.getUltimoMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//		MatriculaPeriodoVO novoMatriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().obterNovoMatriculaPeriodoBaseadoUltimoPeriodoLetivo(this.getMatriculaVO(), getUsuarioLogado());
		String digitoTurma = matriculaVO.getUltimoMatriculaPeriodoVO().getTurma().getDigitoTurma();
//		novoMatriculaPeriodo.setDigitoTurma(digitoTurma);
//		setMatriculaPeriodoVO(novoMatriculaPeriodo);
		
		getDadosRecuperadosAlteracaoProcessoMatricula().put("Curso", matriculaVO.getCurso().getCodigo());
		getDadosRecuperadosAlteracaoProcessoMatricula().put("GradeCurricular", matriculaVO.getGradeCurricularAtual().getCodigo());
//		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "carregarDadosGerarRenovacaoMatriculaPeriodoFaseDadosBasicos" + getMatriculaVO().getMatricula());
		if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais())) {
			inicializarMatriculaComHistoricoAluno(false);
		} else {
			inicializarMatriculaComHistoricoAluno(true);
		}
//		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "carregarDadosGerarRenovacaoMatriculaPeriodoFaseDadosBasicos1" + getMatriculaVO().getMatricula());
		setCodigoGradeCurricular(matriculaVO.getGradeCurricularAtual().getCodigo());		
		if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getPeriodoLetivo())) {
			setCodigoPeriodoLetivo(getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo());
			if(getDadosRecuperadosAlteracaoProcessoMatricula().isEmpty() || !getDadosRecuperadosAlteracaoProcessoMatricula().containsKey("PeriodoLetivo")) {
				getDadosRecuperadosAlteracaoProcessoMatricula().put("PeriodoLetivo",	getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo());
			}
		}
//		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "carregarDadosGerarRenovacaoMatriculaPeriodoFaseDadosBasicos2" + getMatriculaVO().getMatricula());
		gerarDadosGraficoEvolucaoAcademicaAluno();
//		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "carregarDadosGerarRenovacaoMatriculaPeriodoFaseDadosBasicos3" + getMatriculaVO().getMatricula());
//		setOrdemDesconto(matriculaVO.getPlanoFinanceiroAluno().obterOrdemAplicacaoDescontosPadraoAtual());
		apresentarPlanoFinanceiroCurso = true;
		verificarUsuarioPodeAlterarTurnoRenovacao();
	}

	public void carregarDadosEditarMatriculaPeriodoFaseDadosBasicos() throws Exception {
		matriculaVO.setMatricula(this.getMatriculaPeriodoVO().getMatricula());
		getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, NivelMontarDados.TODOS, getUsuarioLogado());
//		getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(matriculaPeriodoVO, NivelMontarDados.TODOS,  getUsuarioLogado());
//		matriculaPeriodoVO.setContaPagarRestituicaoVO(getFacadeFactory().getContaPagarFacade().consultaRapidaContaPagarRestituicaoValorPorMatriculaPeriodo(matriculaVO.getMatricula(), matriculaPeriodoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		if (getTransferenciaMatrizCurricularMatriculaVO() != null) {
			// se entramos aqui é por que estamos editando a matricula, a partir
			// da tela de transferencia,
			// com o objetivo de já alterar a matriculaPeriodo para a nova
			// matriz do aluno e remover
			// disciplinas que estejam sendo cursadas por correspodencia. Desta
			// maneira, vamos já alterar
			// na matriculaPeriodo da matriz, definindo a nova matriz e partindo
			// para um reorganizar posterior
			// de todos os objetos da MatriculaPeriodoTurmaDisciplina
			getMatriculaPeriodoVO().setTransferenciaMatrizCurricularMatriculaVO(getTransferenciaMatrizCurricularMatriculaVO());
			matriculaPeriodoVO.setGradeCurricular(matriculaVO.getGradeCurricularAtual());
		}

		if (!matriculaVO.getGradeCurricularAtual().getCodigo().equals(matriculaPeriodoVO.getGradeCurricular().getCodigo()) && getEditandoMatricula()) {
			throw new Exception(UteisJSF.internacionalizar("prt_Matricula_realizadoTransferenciaMatrizCurricular"));
		}
		setRealizouTransferenciaMatrizCurricular(!getMatriculaPeriodoVO().getGradeCurricular().getCodigo().equals(getMatriculaVO().getGradeCurricularAtual().getCodigo()));
		// Calcular e atualizar em MatriculaPeriodoVO todas as variáveis de
		// controle do total de carga horaria/creditos ao aluno.
		getFacadeFactory().getMatriculaPeriodoFacade().executarCalculoAtualizarTotaisCargaHorariaCreditoMatriculaPeriodoAluno(matriculaPeriodoVO, matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs());
		// Mantendo o plano financeiro e condicacao de pagamento já gravados na
		// matriculaPeriodo, que podem ser distintos
		// das possibilidades disponibilidadas na turma/unidadeensinocurso.
		// Assim, os dados antigos devem ser listados no
		// combobox. Permitindo ao usuario ver o antigo plano/condicao e
		// possibilitando ao mesmo alterar para uma nova situação.
//		matriculaPeriodoVO.setPlanoFinanceiroCursoPersistido(matriculaPeriodoVO.getPlanoFinanceiroCurso());
//		atualizarDescricaoCondicaoPagamentoPlanoFinanceiroCurso(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso());
//		matriculaPeriodoVO.setCondicaoPagamentoPlanoFinanceiroCursoPersistido(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso());
//		getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(this.getMatriculaVO(), this.getMatriculaPeriodoVO(), getUsuarioLogado());
		getFacadeFactory().getProcessoMatriculaFacade().carregarDados(matriculaPeriodoVO.getProcessoMatriculaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
		matriculaPeriodoVO.setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
		getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(matriculaVO, matriculaPeriodoVO, NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado());
		// this.setPlanoFinanceiroAlunoVO(matriculaVO.getPlanoFinanceiroAluno());
		// setOrdemDesconto(this.getPlanoFinanceiroAlunoVO().obterOrdemAplicacaoDescontosPadraoAtual());
//		matriculaVO.setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		inicializarMatriculaComHistoricoAluno(true);
		gerarDadosGraficoEvolucaoAcademicaAluno();
		apresentarPlanoFinanceiroCurso = !getMatriculaPeriodoVO().getFinanceiroManual();
		this.setCodigoGradeCurricular(matriculaPeriodoVO.getGradeCurricular().getCodigo());
		this.setCodigoPeriodoLetivo(matriculaPeriodoVO.getPeriodoLetivo().getCodigo());
		if (matriculaVO.getMatriculaPeriodoVOs().size() == 1) {
			this.setRenovandoMatricula(false);
		} else {
			this.setRenovandoMatricula(true);
		}
		getMatriculaPeriodoVO().getProcessoMatriculaOriginal();
		getMatriculaPeriodoVO().getAnoSemestreOriginal();
		matriculaPeriodoVO.setMatriculaVO(matriculaVO);
		if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getContratoMatricula())) {
			getMatriculaPeriodoVO().setContratoMatricula(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getContratoMatricula().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
	}
		if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getContratoFiador())) {
			getMatriculaPeriodoVO().setContratoFiador(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getContratoFiador().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		}
		if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getContratoExtensao())) {
			getMatriculaPeriodoVO().setContratoExtensao(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getContratoExtensao().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		}
		for(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs()) {
			getFacadeFactory().getMatriculaPeriodoFacade().verificarDisciplinaAlunoAdaptacao(matriculaPeriodoTurmaDisciplinaVO, matriculaVO, matriculaPeriodoVO);
			getFacadeFactory().getMatriculaPeriodoFacade().verificarAlunoDependenciaDisciplinaComBaseEmHistoricosAnteriores(matriculaPeriodoTurmaDisciplinaVO, matriculaVO, matriculaPeriodoVO);
			getFacadeFactory().getMatriculaPeriodoFacade().realizarVerificacaoPermissaoExclusaoDisciplina(matriculaVO, matriculaPeriodoVO, matriculaPeriodoTurmaDisciplinaVO, getUsuarioLogado());
			if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO())
					&& !Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getPeriodoLetivoDisciplinaReferenciada())) {
				for(PeriodoLetivoVO periodoLetivoVO : getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs()) {
					if(periodoLetivoVO.getControleOptativaGrupo() && periodoLetivoVO.getGradeCurricularGrupoOptativa().getCodigo().equals(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeCurricularGrupoOptativa().getCodigo())) {
						matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getPeriodoLetivoDisciplinaReferenciada().setCodigo(periodoLetivoVO.getCodigo());
						matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getPeriodoLetivoDisciplinaReferenciada().setPeriodoLetivo(periodoLetivoVO.getPeriodoLetivo());
						matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getPeriodoLetivoDisciplinaReferenciada().setDescricao(periodoLetivoVO.getDescricao());
						break;
					}
				}
			}
		}
	}



	

	
	public Boolean getApresentarPlanoCondicaoFinanceiroPersistidoAluno() {
		if (matriculaPeriodoVO.getCodigo().equals(0)) {
			return Boolean.FALSE;
		} else {
			if (matriculaPeriodoVO.getAlterarPlanoCondicacaoPagamentoPersistido()) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		}
	}

	public Boolean getPermiteAlterarPlanoCondicaoFinanceiraCurso() {
		/*
		if (matriculaPeriodoVO.getAlterarPlanoCondicacaoPagamentoPersistido() && !matriculaPeriodoVO.getCategoriaCondicaoPagamento().equals("")) {
			permiteAlterarPlanoCondicaoFinanceiraCurso = true;
		} else {
			permiteAlterarPlanoCondicaoFinanceiraCurso = false;
		}*/
		if(permiteAlterarPlanoCondicaoFinanceiraCurso == null) {
			permiteAlterarPlanoCondicaoFinanceiraCurso = false;
		}
		return permiteAlterarPlanoCondicaoFinanceiraCurso;
	}

	/**
	 * Método responsável por inicializar listas utilizadas na aba de registro
	 * dos dados básicos. Outros dados são inicializados e carregados na medida
	 * que são requisitados.
	 * 
	 * Daqui para baixo...
	 * 
	 * @author Victor Hugo de Paula Costa Criado uma condição para matrícula
	 *         on-line onde se o banner for diferente de 0, o mesmo montara a
	 *         combox de periodo letivo.
	 * 
	 * 
	 * @throws Exception
	 */
	public void inicializarListasSelectItemTodosComboBoxDadosBasicos(Boolean transferencia) {
		try {
			montarListaSelectItemUnidadeEnsino();
			if (!getBanner().equals(0)) {
				montarListaSelectItemTurno();
			}
			montarListaSelectItemTipoMidiaCaptacao();
			montarListaProcessoMatricula(transferencia);
			montarListaSelectItemGradeCurricular();
			montarListaSelectItemPeriodoLetivo();
			montarListaSelectItemTurma();
//			montarListaSelectItemCategoriaCondicaoPagamentoPlanoFinanceiroCurso();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por inicializar todas as variáveis, listas, e recursos
	 * necessários para uma nova transação sobre a entidade MatriculaPeriodo.
	 * Tanto para o registro de uma nova MatriculaPeriodo, quanto para a
	 * alteração de uma existente. Este cadastro trabalha com principio de
	 * wizard, portanto todos os dados devem ser inicializados, e na medida que
	 * o usuário for avançando pelo cadastro os dados pertinentes devem ser
	 * carregados. Tornando o cadastro rápido e ágil. Este método deve ser
	 * acionado no início, antes de qualquer operação ou transação neste
	 * ManagedBean.
	 */
	public void inicializarManagedBeanIniciarProcessamento() {
		setMatriculaVO(new MatriculaVO());
		setMatriculaPeriodoVO(new MatriculaPeriodoVO());
		// setPlanoFinanceiroAlunoVO(new PlanoFinanceiroAlunoVO());
//		setPlanoFinanceiroCursoVO(new PlanoFinanceiroCursoVO());
//		setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
		setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		//setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
		setTurmaComLotacaoPreenchida(false);
		setTurmaComVagasPreenchidas(false);
		this.setMatricula_Erro("");
//		setOrdemDescontoVO(new OrdemDescontoVO());
		realizandoNovaMatriculaAluno = false;
		ordemDesconto = new ArrayList(0);
		apresentarPlanoFinanceiroCurso = false;
		imprimirContrato = false;
		montarDadosNovoPeriodoLetivo = true;
		imprimirContrato = false;
		montarDadosNovoPeriodoLetivo = false;
		liberarRenovacaoComDebitosFinanceiros = false;
		verificarUsuarioPodeAlterarDescontoProgressivoPrimeiraParcela();
		executarVerificacaoUusuarioPodeAnexarImagemDocumentosEntregues();
		usernameLiberarRenovacaoComDebitosFinanceiros = "";
		senhaLiberarRenovacaoComDebitosFinanceiros = "";
		gradeDisciplinaAdicionar = 0;
		this.filtroTurno = 0;
		this.filtroSituacaoMatriculaPeriodo = "";
		periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina = 0;
		apresentarDisciplinasEquivalentesIncluir = false;
		anoGerarParcelaAvulsa = Uteis.getAnoDataAtual();
		mesGerarParcelaAvulsa = String.valueOf(Uteis.getMesDataAtual());
		apresentarRichAvisoContaReceber = "";
		setGradeDisciplinaEquivalenteAdicionar((Integer) 0);
		setValorConsultaAlunoNovaMatricula("");
		setCampoConsultaAlunoNovaMatricula("");
		setValidarCadastrarAluno(false);
		setLiberarAvancar(false);
		setPedirLiberacaoMatricula(false);
		setMatriculaForaPrazo(false);
		setValidarCadastrarAluno(false);
		setExibirMatricula(false);
		setExibirRenovacaoMatricula(false);
		setImprimir(false);
		setMatriculaForaPrazo(false);
		setMatriculaVisaoAluno("0");
		setListaConsultaAlunoNovaMatricula(new ArrayList(0));
		setListaConsultaCandidato(new ArrayList(0));
		setListaSelectItemUnidadeEnsino(new ArrayList(0));
		setListaSelectItemConvenio(new ArrayList(0));
		setListaSelectItemCurso(new ArrayList(0));
		setListaSelectItemDescontoAlunoMatricula(new ArrayList(0));
		setListaSelectItemDescontoAlunoParcela(new ArrayList(0));
		setListaSelectItemDescontoProgresivo(new ArrayList(0));
		setListaSelectItemDisciplinaAdicionar(new ArrayList(0));
		setListaSelectItemDisciplinaEquivalenteAdicionar(new ArrayList(0));
		setListaSelectItemMapaEquivalenciaDisciplinaIncluir(new ArrayList(0));
		setListaSelectItemGradeCurricular(new ArrayList(0));
		setListaSelectItemMatriculaVisaoAluno(new ArrayList(0));
		setListaSelectItemPeriodoLetivo(new ArrayList(0));
		setListaSelectItemPeriodoLetivoAdicionar(new ArrayList(0));
		setListaSelectItemPlanoDesconto(new ArrayList(0));
		setListaSelectItemPlanoFinanceiroCurso(new ArrayList(0));
		setListaSelectItemProcessoMatricula(new ArrayList(0));
		setListaSelectItemTipoMidiaCaptacao(new ArrayList(0));
		setListaSelectItemTurma(new ArrayList(0));
		setListaSelectItemTurmaAdicionar(new ArrayList(0));
		setListaSelectItemMapaEquivalenciaDisciplinaIncluir(new ArrayList(0));
		setListaSelectItemTurno(new ArrayList(0));
		setListaDisciplinasPeriodoLetivoAlunoJaAprovado(new ArrayList(0));
		setListaDisciplinasPeriodoLetivoAlunoPendente(new ArrayList(0));
		setApresentarBotaoLiberarControleInclusaoDisciplinaPeriodoFuturo(Boolean.FALSE);
		setApresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular(Boolean.FALSE);
		setApresentarBotaoLiberarRenovacaoSemAceiteTermo(Boolean.FALSE);
		setPermitirRenovacaoSemAceiteTemo(Boolean.FALSE);
		setAlunoJaAceitouTermoAceite(null);
		setApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular(Boolean.FALSE);
		setApresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia(Boolean.FALSE);
		setLiberadoInclusaoPorEquivalencia(getLoginControle().getPermissaoAcessoMenuVO().getPermitirIncluirDisciplinaPorEquivalencia());
		setLiberadoInclusaoOptativa(getLoginControle().getPermissaoAcessoMenuVO().getPermitirIncluirDisciplinaOptativa());
		setLiberadoInclusaoTurmaOutroUnidadeEnsino(!getLoginControle().getPermissaoAcessoMenuVO().getIncluirDisciplinaApenasTurmaProprioUnidadeEnsino());
		setLiberadoInclusaoTurmaOutroCurso(!getLoginControle().getPermissaoAcessoMenuVO().getIncluirDisciplinaApenasTurmaProprioCurso());
		setLiberadoInclusaoTurmaOutroMatrizCurricular(!getLoginControle().getPermissaoAcessoMenuVO().getIncluirDisciplinaApenasTurmaProprioMatrizCurricular());
		setLiberarMatriculaTurmaSemVagaSemAula(false);
		setPermiteAlterarDataBaseGeracaoParcelas(null);
	}
	
	
	public void processarEdicaoAutomaticaMatricula(MatriculaPeriodoVO matPeriodoVO) {
		processarEdicaoAutomaticaMatricula(matPeriodoVO, true);
	}

	public void processarEdicaoAutomaticaMatricula(MatriculaPeriodoVO matPeriodoVO, Boolean validarProcessoMatriculaCalendario) {
		try {
			if (!matPeriodoVO.getPermiteAlteracaoPeloUsuario()) {
				throw new Exception("Matrícula não pode mais ser editada pelo usuário");
			}
			inicializarManagedBeanIniciarProcessamento();
			matriculaPeriodoVO = matPeriodoVO;
			carregarDadosEditarMatriculaPeriodoFaseDadosBasicos();
			// inicializarListasSelectItemTodosComboBoxDadosBasicos();
			// inicializarResultadoProcSeletivoInscricao();
			// ===========================
			// NAVEGAR ABA DOCUMENTACAO
			getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(this.getUsuarioLogadoClone());
//			getFacadeFactory().getMatriculaFacade().inicializarTextoContratoPlanoFinanceiroAluno(this.getMatriculaVO(), this.getMatriculaPeriodoVO(), true, getUsuarioLogado());
			if (!getMatriculaVO().getSituacao().equals("PL")) {
				getMatriculaVO().setUsuario(getUsuarioLogadoClone());
			}
			if (validarProcessoMatriculaCalendario) {
				getFacadeFactory().getMatriculaPeriodoFacade().validarMatriculaPeriodoPodeSerRealizada(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
			}
			getFacadeFactory().getMatriculaFacade().gerenciarEntregaDocumentoMatricula(getMatriculaVO(), getUsuarioLogado());
			setProcessoCalendarioMatriculaVO(getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO());
			// =================================
			// NAVEGAR ABA DISCIPLINAS ALUNOS
			getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosDefinirDisciplinasMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado(), null, false, getPermitirRealizarMatriculaDisciplinaPreRequisito());
			PeriodoLetivoVO periodoAnterior = getFacadeFactory().getMatriculaPeriodoFacade().executarObterPeriodoLetivoAnteriorAoPeriodoLetivoMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO());
			periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina = periodoAnterior.getCodigo();
			atualizarListaDisciplinasHistoricoPeriodoLetivoSelecionado();
			// =================================
			// NAVEGAR ABA PLANO FINANCEIRO ALUNO			
//			getFacadeFactory().getMatriculaPeriodoFacade().inicializarPlanoFinanceiroAlunoMatriculaPeriodo(matriculaVO, matriculaPeriodoVO,  true, getUsuarioLogado());
//			this.setPlanoFinanceiroCursoVO(this.matriculaPeriodoVO.getPlanoFinanceiroCurso());
//			this.setCondicaoPagamentoPlanoFinanceiroCursoVO(this.matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso());
//			setOrdemDesconto(matriculaVO.getPlanoFinanceiroAluno().obterOrdemAplicacaoDescontosPadraoAtual());
//			this.setListaConsultaPlanoDesconto(matriculaVO.getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs("PD"));
//			this.setListaConsultaConvenio(matriculaVO.getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs("CO"));
			// inicializarListasSelectItemTodosComboBoxDadosFinanceiros();
			getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(matriculaVO, matriculaPeriodoVO, getUsuarioLogado());
//			getFacadeFactory().getMatriculaFacade().calcularTotalDesconto(getMatriculaVO(), matriculaPeriodoVO, getOrdemDesconto(), getUsuarioLogado());
			// =================================
			// GRAVAR DADOS E FINANLIZAR MATRICULA
			if (!matriculaVO.getIsAtiva()) {
				throw new Exception("Esta renovação não pode ser gravada pois refere-se a uma matrícula: " + matriculaVO.getSituacao_Apresentar().toUpperCase());
			}
			getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
			matriculaPeriodoVO.setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
			adicionarMatriculaPeriodo();
			inicializarUsuarioResponsavelMatriculaUsuarioLogado();			
//			getFacadeFactory().getMatriculaFacade().alterar(matriculaVO, matriculaPeriodoVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(),  getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
//			getFacadeFactory().getMatriculaFacade().regerarBoletos(matriculaPeriodoVO,  getUsuarioLogado());
			matPeriodoVO.setMensagemErro("Dados Atualizados com Sucesso!");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			matPeriodoVO.setMensagemErro(e.getMessage());
		}
	}

	public void editarAutomaticamenteMatricula() throws Exception {
		int totalMatricula = this.listaConsulta.size();
		int contador = 0;
		Iterator i = this.listaConsulta.iterator();
		while (i.hasNext()) {
			contador++;
			MatriculaPeriodoVO matPeriodoVO = (MatriculaPeriodoVO) i.next();
			// System.out.println("=====================================");
			// System.out.println("Progresso: " + contador + "/" +
			// totalMatricula + " Hora Início: " + new Date());
			// System.out.println("Matrícula: " + matPeriodoVO.getMatricula() +
			// " Aluno: " + matPeriodoVO.getMatriculaVO().getAluno().getNome());
			if (matPeriodoVO.getAno().equals(getAno()) && matPeriodoVO.getSemestre().equals(getSemestre())) {
				processarEdicaoAutomaticaMatricula(matPeriodoVO);
			}
			// System.out.println("Resultado: " +
			// matPeriodoVO.getMensagemErro());
		}
	}

	public void prepararMatriculaPeriodoParaEdicao() throws Exception {
		// TODO Alberto 15/12/2010 Não permite editar matrículas canceladas,
		// concluídas ou trancadas
		setRenovandoMatricula(false);
		setEditandoMatricula(true);
		carregarDadosEditarMatriculaPeriodoFaseDadosBasicos();
		inicializarListasSelectItemTodosComboBoxDadosBasicos(false);
		this.setMatricula_Erro("");
		setMensagemID("msg_dados_editar");
//		inicializarResultadoProcSeletivoInscricao();
		// Usado para trocar as disciplinas de acordo com o periodoLetivo
		// selecionado
		setPrimeiroPeriodoLetivoSelecionado(getMatriculaPeriodoVO().getPeridoLetivo().getCodigo());
		setPrimeiraTurmaSelecionada(getMatriculaPeriodoVO().getTurma().getCodigo());
		verificarPermissaoUsuarioVisualizarConsultorMatricula();
		verificarPermissaoUsuarioInformarTipoMatriculaMatricula();
		verificarPermissaoUsuarioSuspenderMatricula();
		verificarPermissaoUsuarioLiberarPgtoMatricula();
		verificarPermissaoObrigatorioContratoPorTurma();
		validarPermissaoInformarNumeroMatriculaManualmente();
		setNovoNumeroMatriculaManualmente("");
		consultarUsuarioResponsavelMatriculaPorChavePrimaria();
		if (getMatriculaVO().getTipoMatricula().equals("EX")) {
			if (getConfiguracaoGeralPadraoSistema().getControlaQtdDisciplinaExtensao().booleanValue()) {
				if (getMatriculaVO().getQtdDisciplinasExtensao().intValue() == 0) {
					getMatriculaVO().setQtdDisciplinasExtensao(getConfiguracaoGeralPadraoSistema().getQtdDisciplinaExtensao());
				} else {
					if (getMatriculaVO().getQtdDisciplinasExtensao() > getConfiguracaoGeralPadraoSistema().getQtdDisciplinaExtensao()) {
						getMatriculaVO().setQtdDisciplinasExtensao(getConfiguracaoGeralPadraoSistema().getQtdDisciplinaExtensao());
					}
				}
			}
		}
		apresentarPlanoFinanceiroCurso = !getMatriculaPeriodoVO().getFinanceiroManual();
		setApresentarFormacaoAcademica(verificarFormacaoAcademica());
		setGuiaAba("DadosBasicos");
		registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Editar Matricula", "Editando");
		setAbrirPanelAlterarSubturmaPraticaTeorica(false);
		setDebitoFinanceiroAoIncluirConvenioLiberado(false);
		
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Matricula</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		try {
			setPermiteAlterarDataBaseGeracaoParcelas(null);
			setTelaConsulta(false);
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Editar Matricula", "Editando");
			try {
				ConfiguracoesVO configuracoes = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getUnidadeEnsinoLogado().getCodigo());
				setControlarSuspensaoMatriculaPendenciaDocumentos(configuracoes.getControlarSuspensaoMatriculaPendenciaDocumentos());
			} catch (Exception e) {
				setControlarSuspensaoMatriculaPendenciaDocumentos(Boolean.FALSE);
			}
			
			inicializarManagedBeanIniciarProcessamento();
			matriculaPeriodoVO = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
//			verificarMatriculaComParcelaAgenteCobrancaNegativacao(matriculaPeriodoVO);
			if (matriculaPeriodoVO.getMatriculaVO().getCanceladoFinanceiro()) {
				throw new Exception(UteisJSF.internacionalizar("msg_RenovacaoMatricula_cancelamentoFinanceiro"));
			}

			if (!matriculaPeriodoVO.getPermiteAlteracaoPeloUsuario()) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml");
			}
			if(!veriricarUsuarioTemPermissaoAlterarRenovacaoMatricula()){
				registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Usuário Sem Permissão para Editar Matricula", "Editando");
				throw new Exception(UteisJSF.internacionalizar("msg_RenovacaoMatricula_editarMatriculaPeriodo"));
			}			
			prepararMatriculaPeriodoParaEdicao();			
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml");
		}
	}

	
	public void realizarLiberacaoModuloEstagioAluno ()  {
		try {
			editar();
			getFacadeFactory().getMatriculaPeriodoFacade().realizarLiberacaoModuloEstagioAluno(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogadoClone());
			inicializarMensagemVazia();
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void realizarLiberacaoModuloTCCAluno ()  {
		try {
			editar();
			getFacadeFactory().getMatriculaPeriodoFacade().realizarLiberacaoModuloTCCAluno(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogadoClone());
			inicializarMensagemVazia();
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	/**
	 * Método chamado somente quando se deseja realizar uma renovação de
	 * matrícula, baseando-se nos dados da última matrícula período, do aluno em
	 * questão.
	 */
	public void iniciarRenovacaoMatricula() {
		try {
			setOncompleteModal("");
			if (matriculaVO.getMatricula().equals("")) {
				throw new Exception("A renovação não pode ser iniciada, realize a busca do aluno!");
			}
			setRenovandoMatricula(true);
			carregarDadosGerarRenovacaoMatriculaPeriodoFaseDadosBasicos();
			getFacadeFactory().getMatriculaFacade().validarMatriculaPodeSerRenovada(matriculaVO, getMatriculaPeriodoVO());
			inicializarListasSelectItemTodosComboBoxDadosBasicos(false);
			setListaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs(null);
			this.setMatricula_Erro("");
			// setMatriculaVO(getFacadeFactory().getMatriculaFacade().verificarControleDisciplinaReprovacao(getMatriculaVO()));
			getFacadeFactory().getMatriculaFacade().atualizarListaDocumentosEntregarMatricula(matriculaVO, getUsuarioLogado());
			montarListaSelectItemPeriodoLetivo(0);
			montarListaSelectItemTurma();
			//getMatriculaPeriodoVO().setProcessoMatricula(0);
			gerarDadosGraficoEvolucaoAcademicaAluno();
			montarDadosNovoPeriodoLetivo = false;
			setMensagemID("msg_dados_editar");
			setGuiaAba("DadosBasicos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setGuiaAba("Inicio");
		}
	}

	public void verificarPermissaoPerfilAlunoRealizarMatriculaCursarDisciplinaPreRequisito() {
		try {
			verificarPermissaoUsuarioLiberarMatriculaComPendenciasFinanceiras(getUsuarioLogado(), "Matricula_PermitirRealizarMatriculaDisciplinaPreRequisito");
			setPermitirRealizarMatriculaDisciplinaPreRequisito(Boolean.TRUE);
		} catch (Exception e) {
			setPermitirRealizarMatriculaDisciplinaPreRequisito(Boolean.FALSE);
		}
	}

	public void renovarMatriculaVisaoAluno() throws Exception {
		try {
			// getFacadeFactory().getMatriculaFacade().validarConsultarRenovacaoMatriculaVisaoAluno(getUsuarioLogadoClone());
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "renovarMatriculaVisaoAluno"+ getMatriculaVO().getMatricula());
			if (getMatriculaVisaoAluno().equals("")) {
				throw new Exception("A renovação não pode ser iniciada, nenhuma matrícula selecionada!");
			} else {
				this.setMatricula_Erro("");
				getMatriculaVO().setMatricula(getMatriculaVisaoAluno());
				getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, NivelMontarDados.TODOS, getUsuarioLogado());
				MatriculaPeriodoVO obj = getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodoRenovadaVisaoAlunoPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(getMatriculaVO().getTurno().getCodigo(), getMatriculaVO().getCurso().getCodigo(), getMatriculaVO().getUnidadeEnsino().getCodigo(), "AT", "PR_AT", true, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getMatriculaVO().getMatricula(), getUsuarioLogado());
				verificarPermissaoPerfilAlunoRealizarMatriculaCursarDisciplinaPreRequisito();
				verificarPermissaoAlunoAlterarTrumaBaseSugerida();
				if (obj != null && !obj.getCodigo().equals(0)) {
					editarMatriculaPeriodoOnline(obj);
				} else {
					setAvisoRenovandoMatriculaExistente(false);
					setRenovandoMatricula(true);
					setRenovacaoRecusada(Boolean.FALSE);
					getMatriculaVO().setMatricula(getMatriculaVisaoAluno());
					carregarDadosGerarRenovacaoMatriculaPeriodoFaseDadosBasicos();
					getFacadeFactory().getMatriculaFacade().validarMatriculaPodeSerRenovada(getMatriculaVO(), getMatriculaPeriodoVO());
					this.setMatricula_Erro("");
					getFacadeFactory().getMatriculaFacade().atualizarListaDocumentosEntregarMatricula(matriculaVO, getUsuarioLogado());
					montarListaProcessoMatricula(false);
					getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosAnoSemestreMatricula(getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), false);
					montarListaSelectItemPeriodoLetivo();
					montarListaSelectItemTurma();
//					validarRestricoesRenovacaoMatriculaVisaoAluno();
					if (Uteis.removeHTML(getMatriculaPeriodoVO().getProcessoMatriculaVO().getMensagemApresentarVisaoAluno()).trim().isEmpty()) {
						getMatriculaPeriodoVO().getProcessoMatriculaVO().setMensagemApresentarVisaoAluno("");
					}
					//getFacadeFactory().getMatriculaPeriodoFacade().carregarDadosConsultorUsuarioResp(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
					//preencherNomeParcelaMatriculaApresentarAluno(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo());
					if (getMatricula_Erro().isEmpty()) {
//						getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "renovarMatriculaVisaoAluno1"+ getMatriculaVO().getMatricula());
						setExibirRenovacaoMatricula(true);
						if (getMatriculaPeriodoVO().getProcessoMatriculaVO().getApresentarTermoAceite()
								&& Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline())) {
							TextoPadraoVO texto = getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline().getCodigo(), 
									Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//							getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "renovarMatriculaVisaoAluno2"+ getMatriculaVO().getMatricula()+" texto"+ texto.getCodigo());
							getMatriculaPeriodoVO().getProcessoMatriculaVO().setTextoPadraoContratoRenovacaoOnline(texto);
//							if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso())) {
//								getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade()
//										.consultarPorChavePrimaria(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//							}
//							validarApresentacaoTermoAceitePorDesignTextoPadrao();
//							getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "renovarMatriculaVisaoAluno3"+ getMatriculaVO().getMatricula());
						}
//						getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "renovarMatriculaVisaoAluno4"+ getMatriculaVO().getMatricula());
						setMensagemID("msg_dados_editar", Uteis.ALERTA);
						setGuiaAba("DadosBasicos");
					} else {
						setExibirRenovacaoMatricula(false);
						setMensagemDetalhada("", "", "");
						setMensagemID("", "");
					}
				}

			}
		} catch (Exception e) {
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "renovarMatriculaVisaoAluno5"+ getMatriculaVO().getMatricula()+" erro"+ e.getMessage());
			setExibirRenovacaoMatricula(false);
			novo();
			setTurma_Erro("");			
			throw e;
			
		}
	}

	public boolean veriricarUsuarioTemPermissaoAlterarRenovacaoMatricula() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "PermitirRealizarEdicaoMatriculaRenovada");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String editarMatriculaPeriodoOnline(MatriculaPeriodoVO obj) throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Editar Matricula", "Editando");
			try {
				ConfiguracoesVO configuracoes = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getUnidadeEnsinoLogado().getCodigo());
				setControlarSuspensaoMatriculaPendenciaDocumentos(configuracoes.getControlarSuspensaoMatriculaPendenciaDocumentos());
			} catch (Exception e) {
				setControlarSuspensaoMatriculaPendenciaDocumentos(Boolean.FALSE);
			}
			inicializarManagedBeanIniciarProcessamento();
			setMatriculaPeriodoVO(obj);

			// TODO Alberto 15/12/2010 Não permite editar matrículas canceladas,
			// concluídas 
			if (!getMatriculaPeriodoVO().getPermiteAlteracaoPeloUsuario()) {
				if(getUsuarioLogado().getIsApresentarVisaoAluno()) {
					throw new Exception("Esta matrícula não pode ser renovada pois a mesma encontra-se: " + matriculaVO.getSituacao_Apresentar().toUpperCase());
				}
				return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml");
			}
			setAvisoRenovandoMatriculaExistente(true);
			setRenovandoMatricula(false);
			setEditandoMatricula(true);
			setExibirRenovacaoMatricula(true);
			if (!veriricarUsuarioTemPermissaoAlterarRenovacaoMatricula()) {
				setMatricula_Erro(UteisJSF.internacionalizar("msg_RenovacaoMatricula_editarMatriculaPeriodo"));
				setExibirRenovacaoMatricula(false);
				setMensagemDetalhada("", "", "");
				setMensagemID("", "");
				registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Usuário Sem Permissão para Editar Matricula", "Editando");
				return "";
			}
			carregarDadosEditarMatriculaPeriodoFaseDadosBasicos();
			getFacadeFactory().getMatriculaFacade().validarMatriculaPodeSerRenovada(getMatriculaVO(), getMatriculaPeriodoVO());
			getMatriculaPeriodoVO().setAlunoConcordaComTermoRenovacaoOnline(getMatriculaPeriodoVO().getAceitouTermoContratoRenovacaoOnline());
			if(!getMatriculaPeriodoVO().getAceitouTermoContratoRenovacaoOnline()) {
				getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(matriculaVO, matriculaPeriodoVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//				getFacadeFactory().getMatriculaPeriodoFacade().definirSituacaoMatriculaPeriodoComBaseProcesso(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());			
				if (getMatriculaPeriodoVO().getProcessoMatriculaVO().getApresentarTermoAceite() && Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline())) {
					TextoPadraoVO texto = getMatriculaPeriodoVO().getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline();
//					String contratoPronto = texto.substituirTagsTextoPadraoContratoMatricula(null, getMatriculaVO(),  new ArrayList<ContaReceberVO>(), matriculaPeriodoVO, new PlanoFinanceiroAlunoVO(), new ArrayList(0), new DadosComerciaisVO(), getUsuarioLogado());
//					String contrato = texto.removerHtmlBotaoImprimirTextoPadraoContratoMatricula(contratoPronto);
//					contrato = getFacadeFactory().getTextoPadraoDeclaracaoFacade().removerBordaDaPagina(contrato);
//					contrato = getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarStyleFormatoPaginaTextoPadrao(contrato, texto.getOrientacaoDaPagina());
//					getMatriculaPeriodoVO().getProcessoMatriculaVO().setTermoAceite(contrato);
					// 	getMatriculaPeriodoVO().getProcessoMatriculaVO().setMensagemApresentarVisaoAluno(contrato);
				}
			}
			getDadosRecuperadosAlteracaoProcessoMatricula().put("Curso", getMatriculaVO().getCurso().getCodigo());
			getDadosRecuperadosAlteracaoProcessoMatricula().put("GradeCurricular", matriculaVO.getGradeCurricularAtual().getCodigo());
			getDadosRecuperadosAlteracaoProcessoMatricula().put("PeriodoLetivo", getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo());
			getDadosRecuperadosAlteracaoProcessoMatricula().put("Turma", getMatriculaPeriodoVO().getTurma().getCodigo());
			inicializarListasSelectItemTodosComboBoxDadosBasicos(false);
			this.setMatricula_Erro("");
			setMensagemID("msg_dados_editar");
//			inicializarResultadoProcSeletivoInscricao();
			// Usado para trocar as disciplinas de acordo com o periodoLetivo
			// selecionado
			setPrimeiroPeriodoLetivoSelecionado(getMatriculaPeriodoVO().getPeridoLetivo().getCodigo());
			setPrimeiraTurmaSelecionada(getMatriculaPeriodoVO().getTurma().getCodigo());
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			verificarPermissaoUsuarioVisualizarConsultorMatricula();
			verificarPermissaoUsuarioInformarTipoMatriculaMatricula();
			verificarPermissaoUsuarioSuspenderMatricula();
			verificarPermissaoUsuarioLiberarPgtoMatricula();
			}
			consultarUsuarioResponsavelMatriculaPorChavePrimaria();
			if (getMatriculaVO().getTipoMatricula().equals("EX")) {
				if (getConfiguracaoGeralPadraoSistema().getControlaQtdDisciplinaExtensao().booleanValue()) {
					if (getMatriculaVO().getQtdDisciplinasExtensao().intValue() == 0) {
						getMatriculaVO().setQtdDisciplinasExtensao(getConfiguracaoGeralPadraoSistema().getQtdDisciplinaExtensao());
					} else {
						if (getMatriculaVO().getQtdDisciplinasExtensao() > getConfiguracaoGeralPadraoSistema().getQtdDisciplinaExtensao()) {
							getMatriculaVO().setQtdDisciplinasExtensao(getConfiguracaoGeralPadraoSistema().getQtdDisciplinaExtensao());
						}
					}
				}
			}
			setApresentarFormacaoAcademica(verificarFormacaoAcademica());
//			preencherNomeParcelaMatriculaApresentarAluno(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo());
			setGuiaAba("DadosBasicos");
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Editar Matricula", "Editando");
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
		}
	}

	public void recusarTermoAceiteRenovacaoOnline() {
		setMatricula_Erro(UteisJSF.internacionalizar("msg_RenovacaoMatricula_recusaTermoRenovacao"));
		getMatriculaPeriodoVO().setAceitouTermoContratoRenovacaoOnline(Boolean.FALSE);
		setExibirRenovacaoMatricula(false);
		setRenovacaoRecusada(Boolean.TRUE);
		setMensagemDetalhada("", "", "");
		setMensagemID("", "");
	}

	public void aceitarTermoAceiteRenovacaoOnline() {
		getMatriculaPeriodoVO().setAceitouTermoContratoRenovacaoOnline(Boolean.TRUE);
		setExibirRenovacaoMatricula(true);
		setRenovacaoRecusada(Boolean.FALSE);
		setGuiaAba("PlanoFinanceiroAluno");
		setMensagemID("msg_dados_editar", Uteis.ALERTA);
		this.setMatricula_Erro("");
	}

	public void lerTermoContrato() {
		setExibirRenovacaoMatricula(true);
		setRenovacaoRecusada(Boolean.FALSE);
		realizarDefinicaoApresentacaoModalTermoAceite();
		setMensagemID("msg_dados_editar", Uteis.ALERTA);
		setGuiaAba("PlanoFinanceiroAluno");
		this.setMatricula_Erro("");
	}

	public void realizarDefinicaoApresentacaoModalTermoAceite() {
		if (getMatriculaPeriodoVO().getProcessoMatriculaVO().getApresentarTermoAceite()) {
			setApresentarModalTermoAceite(Boolean.TRUE);
		}
	}

//	public void validarRestricoesRenovacaoMatriculaVisaoAluno() throws Exception {
//
//		for (MatriculaPeriodoVO mt : getMatriculaVO().getMatriculaPeriodoVOs()) {
//			if (mt.getSituacaoPreMatricula()) {
//				setMatricula_Erro(UteisJSF.internacionalizar("msg_RenovacaoMatriculaVisaoAluno_existePreMatricula").replace("NOME_ALUNO", getMatriculaVO().getAluno().getNome()).replace("NUMERO_MATRICULA", getMatriculaVO().getMatricula()));
//			}
//		}
//		setApresentarBotaoRenegociacao(false);
//		if (getMatricula_Erro().isEmpty()) {
//			
//			if (getListaSelectItemProcessoMatricula() == null || getListaSelectItemProcessoMatricula().isEmpty()) {
//				setMatricula_Erro(UteisJSF.internacionalizar("msg_RenovacaoMatriculaVisaoAluno_processoMatriculaFechado").replace("NOME_ALUNO", getMatriculaVO().getAluno().getNome()));
//			} else if (getListaSelectItemTurma() == null || getListaSelectItemTurma().isEmpty() || (getListaSelectItemTurma().size() == 1 && ((Integer)getListaSelectItemTurma().get(0).getValue()).equals(0))) {
//				setMatricula_Erro(UteisJSF.internacionalizar("msg_RenovacaoMatriculaVisaoAluno_turmaInexistente").replace("NOME_ALUNO", getMatriculaVO().getAluno().getNome()));
//			} else if ((getListaSelectItemPlanoFinanceiroCurso() == null || getListaSelectItemPlanoFinanceiroCurso().isEmpty()) && (!getMatriculaPeriodoVO().getFinanceiroManual())) {
//				setMatricula_Erro(UteisJSF.internacionalizar("msg_RenovacaoMatriculaVisaoAluno_planoFinanceiroInexistente").replace("NOME_ALUNO", getMatriculaVO().getAluno().getNome()));
//			} else if (!getConfiguracaoGeralPadraoSistema().getPermiteRenovarComParcelaVencida() 
//					&& getFacadeFactory().getContaReceberFacade().consultarSeExistePendenciaFinanceiraPorRestricoesRenovacaoMatriculaVisaoAluno(matriculaVO.getMatricula(), getUsuarioLogado()) 				
//					&& !getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()).getUtilizarIntegracaoFinanceira()) {
//				List listaContas = getFacadeFactory().getContaReceberFacade().consultarPorMatriculaEUnidadeEnsino("VE", getMatriculaVO().getMatricula(), getMatriculaVO().getUnidadeEnsino().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//				if(Uteis.isAtributoPreenchido(listaContas)){
//					setApresentarBotaoRenegociacao(true);	
//					setMatricula_Erro(getConfiguracaoGeralPadraoSistema().getMensagemPadraoRenovacaoMatriculaComParcelaVencida());
//				}
//				listaContas = null;
//			} else if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()).getUtilizarIntegracaoFinanceira()) {
//				// Neste caso o financeiro é gerenciado por outro sistema. Assim
//				// o aluno só poderá renovar se parcela de matrícula
//				// estiver paga (ou a mesma não existir mais na base de dados).
//				// Alguns clientes assim que a parcela é paga a mesma
//				// é removida da base do dados do SEI. Adicionalmente, o aluno
//				// também não poderá renovar caso tenha alguma parcela
//				// vencida do periodo letivo anterior. Ou seja, competencia
//				// anterior a competencia base da renovação.
//				getMatriculaPeriodoVO().setProcessoMatriculaCalendarioVO(getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getCursoVO().getCodigo(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getTurnoVO().getCodigo(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getProcessoMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//				Date dataCompetenciaMatricula = getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getDataCompetenciaMatricula();
//				if (getFacadeFactory().getIntegracaoFinanceiroFacade().realizarVerificacaoProcessamentoIntegracaoFinanceira()) {
//					setMatricula_Erro("Prezado, este recurso está temporariamente indisponível, tente mais tarde.");
//				}
//				Boolean podeRenovar = getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaIntegracaoFinanceiraPodeRenovarVisaoAluno(getMatriculaVO().getMatricula(), dataCompetenciaMatricula);
//				if (!podeRenovar) {
//					setMatricula_Erro(UteisJSF.internacionalizar("msg_RenovacaoMatriculaVisaoAluno_existePendenciaFinanceira").replace("NOME_ALUNO", getMatriculaVO().getAluno().getNome()));
//				}
//			}
//			if (getConfiguracaoGeralSistemaVO().getBloquearRealizarRenovacaoComEmprestimoBiblioteca()) {
//				String matricula = getFacadeFactory().getItemEmprestimoFacade().consultarPorSituacaoExecucaoAtrasadoRenovadoEPorPessoa(getMatriculaVO().getAluno().getCodigo(), getUsuarioLogado());
//		    	Boolean possuiPendenciaEmprestimo = Uteis.isAtributoPreenchido(matricula);
//				if (possuiPendenciaEmprestimo) {
//					setMatricula_Erro(UteisJSF.internacionalizar("msg_MatriculaPeriodo_AlunoPossuiEmprestimos").replace("{0}", matricula));
//				}
//			}
//			
//			
//			
//			String msg = getFacadeFactory().getContaReceberFacade().validarDadosExisteContaReceberRenegociadadaQueNaoCumpriuAcordo(getMatriculaVO().getMatricula(), getMatriculaVO().getAluno().getNome());
//			if (msg != null && !msg.trim().isEmpty()) {
//				setMatricula_Erro(msg);
//			}
//		}
//
//	}

	public String editarComMatricula(MatriculaVO obj) throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Editar Matricula - Com Matricula", "Editando");
		setMatriculaForaPrazo(false);
		setTurmaComLotacaoPreenchida(Boolean.FALSE);
		setTurmaComVagasPreenchidas(Boolean.FALSE);
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
		obj.setNovoObj(Boolean.FALSE);
		setMatriculaVO(obj);
		// setPlanoFinanceiroAlunoVO(obj.getPlanoFinanceiroAluno());
		setMatriculaPeriodoVO((MatriculaPeriodoVO) obj.getMatriculaPeriodoVOs().get(0));
		inicializarListasSelectItemTodosComboBox();
//		inicializarResultadoProcSeletivoInscricao();
		getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
		setMensagemID("msg_dados_editar");
		registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Editar Matricula - Com Matricula", "Editando");
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
	}

	public void alterarTagsTermoAceiteContratoRenovacaoOnline(String tipoContrato) throws Exception {
		getFacadeFactory().getMatriculaFacade().montarDadosUnidadeEnsino(getMatriculaVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		getMatriculaVO().setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getMatriculaVO().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
 		getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getMatriculaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
//		PlanoFinanceiroAlunoVO plano = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//		plano.setCondicaoPagamentoPlanoFinanceiroCursoVO(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso());
//		if (getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getNaoControlarMatricula()) {
//			getMatriculaPeriodoVO().setQtdeParcelaContrato(getFacadeFactory().getMatriculaPeriodoFacade().consultarQuantidadeParcelaContratoMatriculaNaoControlada(getMatriculaPeriodoVO().getCodigo(), getMatriculaPeriodoVO().getMatricula(), false, getUsuarioLogado()));
//		}
		String ano = Constantes.EMPTY;
		String semestre =  Constantes.EMPTY;
		if (!getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			ano = getMatriculaPeriodoVO().getAno();
			semestre = getMatriculaPeriodoVO().getSemestre();
		}
//		getMatriculaPeriodoVO().setDataInicioAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaAgrupada(getMatriculaPeriodoVO().getTurma().getCodigo(), ano, semestre));
//		getMatriculaPeriodoVO().setDataFinalAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(getMatriculaPeriodoVO().getTurma().getCodigo(), ano, semestre));
//		DadosComerciaisVO dc = getFacadeFactory().getDadosComerciaisFacade().consultarEmpregoAtualPorCodigoPessoa(getMatriculaVO().getAluno().getCodigo(), getUsuarioLogado());
		if (getMatriculaVO().getFormacaoAcademica().getCodigo().intValue() != 0) {
			getMatriculaVO().getAluno().setFormacaoAcademicaVOs(new ArrayList<FormacaoAcademicaVO>(0));
			getMatriculaVO().getAluno().getFormacaoAcademicaVOs().add(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorChavePrimaria(getMatriculaVO().getFormacaoAcademica().getCodigo(), getUsuarioLogado()));
		}
		TextoPadraoVO texto = getMatriculaPeriodoVO().getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline();
//		String contratoPronto = texto.substituirTagsTextoPadraoContratoMatricula(null, getMatriculaVO(), getMatriculaPeriodoVO(), plano,  dc, getUsuarioLogado());
// 		getMatriculaPeriodoVO().getProcessoMatriculaVO().setTermoAceite(texto.removerHtmlBotaoImprimirTextoPadraoContratoMatricula(contratoPronto));

	}

	public String getImprimirTermoAceiteContratoRenovacaoOnline() {
		boolean imprimirContrato = false;
		String textoContrato = getMatriculaPeriodoVO().getProcessoMatriculaVO().getTermoAceite();
		try {
			String texto = textoContrato.replace("<body>", "<body onload=\"window.print();\">");
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("textoRelatorio", texto);
			imprimirContrato = true;
		} catch (Exception e) {
			imprimirContrato = false;
		}
		if (imprimirContrato) {
			return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
		}
		return "";
	}

	public void getPreencherDadosMatricula(MatriculaVO obj) throws Exception {
		setMatriculaForaPrazo(false);
		setTurmaComLotacaoPreenchida(Boolean.FALSE);
		setTurmaComVagasPreenchidas(Boolean.FALSE);
		obj.setNovoObj(Boolean.FALSE);
		setMatriculaVO(obj);
		// setPlanoFinanceiroAlunoVO(obj.getPlanoFinanceiroAluno());
//		setOrdemDesconto(obj.getPlanoFinanceiroAluno().obterOrdemAplicacaoDescontosPadraoAtual());
		setMatriculaPeriodoVO(new MatriculaPeriodoVO());
		// if (obj.getMatriculaPeriodoVOs() != null &&
		// !obj.getMatriculaPeriodoVOs().isEmpty()) {
		// setMatriculaPeriodoVO((MatriculaPeriodoVO)
		// obj.getMatriculaPeriodoVOs().get(0));
		// matriculaPeriodoVO =
		// getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getCodigo(),
		// Uteis.NIVELMONTARDADOS_TODOS);
		// }
		// MatriculaPeriodo.montarDadosProcessoMatriculaCalendarioVO(matriculaPeriodoVO,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS):
		inicializarListasSelectItemTodosComboBox();
//		inicializarResultadoProcSeletivoInscricao();
		getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
		
	}

	

	



	public void verificaPermisaoMatriculaForaPrazo() throws Exception {
		UsuarioVO usuario = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(getUserName(), getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS);
		getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(usuario, "MatriculaForaPrazo");
		getMatriculaPeriodoVO().setResponsavelMatriculaForaPrazo(usuario);
	}

	public Boolean usuarioTemPermissaoParaLiberarDesconto() {
		try {
			UsuarioVO usuario = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.getUsuarioLogado().getUsername(), this.getUsuarioLogado().getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS);
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(usuario, "Matricula_LiberarDescontoMatricula");
		} catch (Exception e) {
			return false;
		} finally {
			return true;
		}
	}

	public void fecharForaPrazo() {
		setMatriculaForaPrazo(Boolean.FALSE);
		setUserName("");
		setSenha("");
	}

	

	

	public Boolean getExisteRegraRenovacaoPorProcessoMatriculaTurma() {
		if(existeRegraRenovacaoPorProcessoMatriculaTurma == null) {
			existeRegraRenovacaoPorProcessoMatriculaTurma = Boolean.FALSE;
		}
		return existeRegraRenovacaoPorProcessoMatriculaTurma;
	}
	
	public void setExisteRegraRenovacaoPorProcessoMatriculaTurma(Boolean existeRegraRenovacaoPorProcessoMatriculaTurma) {
		this.existeRegraRenovacaoPorProcessoMatriculaTurma = existeRegraRenovacaoPorProcessoMatriculaTurma;
	}
	
	public void montarListaProcessoMatricula(Boolean transferenciaInterna) {
		try {
			List objs = new ArrayList(0);
			if (this.getMatriculaVO().getUnidadeEnsino().getCodigo() == null || this.getMatriculaVO().getUnidadeEnsino().getCodigo() == 0) {
				setListaSelectItemProcessoMatricula(objs);
				return;
			}
			if(getExisteRegraRenovacaoPorProcessoMatriculaTurma() && Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getProcessoMatriculaVO())){
				objs.add(new SelectItem(getMatriculaPeriodoVO().getProcessoMatriculaVO().getCodigo(), getMatriculaPeriodoVO().getProcessoMatriculaVO().getDescricao()));
			} else { 
			List<ProcessoMatriculaVO> resultadoConsulta = new ArrayList<ProcessoMatriculaVO>();
			if (getBanner().equals(0)) {
				resultadoConsulta = getFacadeFactory().getProcessoMatriculaFacade().consultarProcessoMatriculaPorUnidadeEnsino(this.getMatriculaVO(), this.getMatriculaPeriodoVO(), getRenovandoMatricula(), getExisteRegraRenovacaoPorProcessoMatriculaTurma(), getUsuarioLogadoClone(), transferenciaInterna);
			} else {
				resultadoConsulta = getFacadeFactory().getProcessoMatriculaFacade().consultarProcessosMatriculasPorCodigoBanner(getBanner(), getMatriculaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
			}
			Iterator i = resultadoConsulta.iterator();
			if (!getUsuarioLogado().getVisaoLogar().equals("aluno") && !getUsuarioLogado().getVisaoLogar().equals("pais") && !getUsuarioLogado().getIsApresentarVisaoProfessor() && !getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				objs.add(new SelectItem(0, ""));
			}
			boolean primeiro = true;
			while (i.hasNext()) {
				ProcessoMatriculaVO obj = (ProcessoMatriculaVO) i.next();
				if (primeiro && ((getUsuarioLogado().getIsApresentarVisaoAdministrativa() && !Uteis.isAtributoPreenchido(getMatriculaPeriodoVO())) || (getUsuarioLogado().getVisaoLogar().equals("aluno") || getUsuarioLogado().getVisaoLogar().equals("pais") || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()))) {
					getMatriculaPeriodoVO().setProcessoMatriculaVO(obj);
					getMatriculaPeriodoVO().setProcessoMatricula(obj.getCodigo());
					objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
					if(getDadosRecuperadosAlteracaoProcessoMatricula().isEmpty() || !getDadosRecuperadosAlteracaoProcessoMatricula().containsKey("ProcessoMatricula")) {
						getDadosRecuperadosAlteracaoProcessoMatricula().put("ProcessoMatricula",	obj.getCodigo());
					}
					atualizarSituacaoMatriculaPeriodoPartindoProceMatricula();
					primeiro = false;
				} else {
					// if (obj.ativoParaMatriculaEPreMatricula(new Date())) {
					if (getMatriculaPeriodoVO().isNovoObj() && !obj.getSituacao().equals("FI")) {// se
						// obj é novo, so add processoMatricula q ñ estaja
						// FInalizado
						// removida inicialização abaixo na versao 5.0 para
						// forçar selecionar um
						// processo e aí atualizar o gráfico de evolução
						// academica do aluno.
						// getMatriculaPeriodoVO().setProcessoMatriculaVO(obj);
						// getMatriculaPeriodoVO().setProcessoMatricula(obj.getCodigo());
						objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
					} else if (!getMatriculaPeriodoVO().isNovoObj()) { // se
						// nao
						// for
						// novo,
						// add
						// todos
						// processoMatricula
						objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
					}
					// }
				}
			}
			if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais() || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) && (objs.isEmpty())) {
				setPermitirMatriculaOnline(false);
			} else {
				setPermitirMatriculaOnline(true);
			}
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemProcessoMatricula(objs);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}


	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>DescontoAlunoMatricula</code>.
	 */
	public void montarListaSelectItemDescontoAlunoMatricula() {
		List obj = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoDescontoAluno.class);
		//obj.remove(0);
		setListaSelectItemDescontoAlunoMatricula(obj);
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>DescontoAlunoParcela</code>.
	 */
	public void montarListaSelectItemDescontoAlunoParcela() {
		List obj = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoDescontoAluno.class);
//		obj.remove(0);
		setListaSelectItemDescontoAlunoParcela(obj);
	}

//	public List consultarProcessoMatriculaPorUnidadeEnsino(MatriculaVO matriculaVO) throws Exception {
//		boolean existeOutraMatriculaPeriodoAtiva = false; 
//		//existeOutraMatriculaPeriodoAtiva = getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMatriculaPeriodoAtivaPorSituacao(matriculaVO.getMatricula(), this.getMatriculaPeriodoVO().getCodigo(), "AT");
//		// boolean existeOutraMatriculaPeriodoAtiva =
//		// getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMatriculaPeriodoPorSituacao(matriculaVO.getMatricula(),
//		// "AT");
//		List<ProcessoMatriculaVO> listaProcessoMatriculaVOs = new ArrayList<ProcessoMatriculaVO>(0);
//		
//		if (renovandoMatricula == null) {
//			renovandoMatricula = Boolean.FALSE;
//		}
//		if ((existeOutraMatriculaPeriodoAtiva && renovandoMatricula)) {
//			// //||
//			// (matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals("AT"))
//			// ||
//			// (matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals("PR")))
//			// {
//			listaProcessoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(matriculaVO.getTurno().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), "AT", "PR", getUsuarioLogado().getVisaoLogar().equals("aluno"), false, Uteis.NIVELMONTARDADOS_TODOS, matriculaVO.getMatricula(), getUsuarioLogado());
//		} else if ((!existeOutraMatriculaPeriodoAtiva && renovandoMatricula)) {
//			listaProcessoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(matriculaVO.getTurno().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), "AT", "PR_AT", getUsuarioLogado().getVisaoLogar().equals("aluno"), false, Uteis.NIVELMONTARDADOS_TODOS, matriculaVO.getMatricula(), getUsuarioLogado());
//		}
//		if (listaProcessoMatriculaVOs.isEmpty() && !renovandoMatricula) {
//			listaProcessoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(matriculaVO.getTurno().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), "AT", "PR_AT", getUsuarioLogado().getVisaoLogar().equals("aluno"), false, Uteis.NIVELMONTARDADOS_TODOS, matriculaVO.getMatricula(), getUsuarioLogado());
//		}
//		realizarInclusaoProcessoMatriculaDeAcordoComMatriculaPeriodo(getMatriculaPeriodoVO(), listaProcessoMatriculaVOs);
//		return listaProcessoMatriculaVOs;
//	}

	/**
	 * Método responsável por verificar a existência e adicionar o processo de
	 * matrícula na lista da combobox caso esteja editando uma matricula
	 * periodo.
	 * 
	 * @param obj
	 * @param listaProcessoMatriculaVOs
	 * @throws Exception
	 */
	public void realizarInclusaoProcessoMatriculaDeAcordoComMatriculaPeriodo(MatriculaPeriodoVO obj, List<ProcessoMatriculaVO> listaProcessoMatriculaVOs) throws Exception {
		if (obj.getCodigo().equals(0)) {
			return;
		}
		if (!listaProcessoMatriculaVOs.isEmpty()) {
			boolean existeProcessoMatriculaLista = false;
			for (ProcessoMatriculaVO processoMatriculaVO : listaProcessoMatriculaVOs) {
				if (processoMatriculaVO.getCodigo().equals(obj.getProcessoMatricula())) {
					existeProcessoMatriculaLista = true;
				}
			}
			if (!existeProcessoMatriculaLista) {
				listaProcessoMatriculaVOs.add(adicionarProcessoMatriculaVinculadoMatriculaPeriodo(obj));
			}
		} else {
			listaProcessoMatriculaVOs.add(adicionarProcessoMatriculaVinculadoMatriculaPeriodo(obj));
		}
	}

	public ProcessoMatriculaVO adicionarProcessoMatriculaVinculadoMatriculaPeriodo(MatriculaPeriodoVO obj) throws Exception {
		ProcessoMatriculaVO processoMatriculaVO = new ProcessoMatriculaVO();
		if (!obj.getProcessoMatricula().equals(0)) {
			processoMatriculaVO.setCodigo(obj.getProcessoMatricula());
			getFacadeFactory().getProcessoMatriculaFacade().carregarDados(processoMatriculaVO, NivelMontarDados.BASICO, getUsuarioLogado());
			return processoMatriculaVO;
		}
		processoMatriculaVO = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorMatriculaPeriodo(obj.getCodigo(), getUsuarioLogado());
		return processoMatriculaVO;
	}

	public void montarListaSelectItemMatiulaVisaoAluno(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarMatriculasPorAluno(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				MatriculaVO obj = (MatriculaVO) i.next();
				objs.add(new SelectItem(obj.getMatricula(), obj.getMatricula()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemMatriculaVisaoAluno(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemMatiulaVisaoAluno() {
		try {
			montarListaSelectItemMatiulaVisaoAluno("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	public List consultarMatriculasPorAluno(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getMatriculaFacade().consultaRapidaCompletaPorCodigoPessoa(super.getUsuarioLogado().getPessoa().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		return lista;
	}

	public void consultarAlunoNovaMatricula() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaAlunoNovaMatricula().equals("Aluno")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorNome(getValorConsultaAlunoNovaMatricula(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaAlunoNovaMatricula().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorCPF(getValorConsultaAlunoNovaMatricula(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaAlunoNovaMatricula().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorRG(getValorConsultaAlunoNovaMatricula(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaAlunoNovaMatricula().equals("registroAcademico")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPessoaPorRegistroAcademico(getValorConsultaAlunoNovaMatricula(),0, "",false, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaAlunoNovaMatricula(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAlunoNovaMatricula(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void liberarSelecionarCurso() {
		getMatriculaVO().setCurso(new CursoVO());
		getMatriculaVO().setTurno(new TurnoVO());
		getMatriculaPeriodoVO().setProcessoMatricula(0);
		getMatriculaPeriodoVO().setProcessoMatriculaVO(new ProcessoMatriculaVO());
		getMatriculaPeriodoVO().setGradeCurricular(new GradeCurricularVO());
		getMatriculaPeriodoVO().setPeridoLetivo(new PeriodoLetivoVO());
		getMatriculaPeriodoVO().setTurma(new TurmaVO());
//		getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(new CondicaoPagamentoPlanoFinanceiroCursoVO());
		setListaSelectItemPlanoFinanceiroCurso(new ArrayList(0));
		setListaSelectItemTurma(new ArrayList(0));
		setListaSelectItemPeriodoLetivo(new ArrayList(0));
		setListaSelectItemGradeCurricular(new ArrayList(0));
		setListaSelectItemProcessoMatricula(new ArrayList(0));
		realizarValidacaoPermiteMatriculaOnlineAposAlteracaoDados();
	}

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		getMatriculaVO().setConsultor(obj);
	}

	public void limparConsultaFuncionario() {
		getListaConsultaFuncionario().clear();
	}

	public void limparDadosFuncionario() {
		getMatriculaVO().setConsultor(null);
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
//
//	public void consultarCandidato() {
//		try {
//			List objs = new ArrayList(0);
//			if (getValorConsultaCandidato().equals("")) {
//				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
//			}
//			if (getCampoConsultaCandidato().equals("nomeCandidato")) {
//				objs = getFacadeFactory().getInscricaoFacade().consultarPorNomePessoa(getValorConsultaCandidato(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//			}
//			if (getCampoConsultaCandidato().equals("cpfCandidato")) {
//				objs = getFacadeFactory().getInscricaoFacade().consultarPorCPFPessoa(getValorConsultaCandidato(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//			}
//			if (getCampoConsultaCandidato().equals("nrInscricao")) {
//				Integer valor = 0;
//				if (getValorConsultaCandidato().isEmpty()) {
//					setValorConsultaCandidato("0");
//					valor = new Integer(getValorConsultaCandidato());
//				} else {
//					valor = new Integer(getValorConsultaCandidato());
//				}
//				objs = getFacadeFactory().getInscricaoFacade().consultarPorCodigoInscricao(valor, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//			}
//			setListaConsultaCandidato(objs);
//			setMensagemID("msg_dados_consultados");
//		} catch (Exception e) {
//			setListaConsultaCandidato(new ArrayList(0));
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
//	}
//
//	public void selecionarCandidato() throws Exception {
//		try {
//			InscricaoVO obj = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
//			obj = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//			Integer campoConsulta = obj.getCodigo();
//			InscricaoVO inscricao = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//			matriculaVO.setInscricao(inscricao);
//			inicializarResultadoProcSeletivoInscricao();
//			if (getResultadoProcessoSeletivoVO().getCodigo().intValue() == 0) {
//				// Não encontrou nenhum resultado para a inscrição
//				inicializarInscricaoEDadosRelacionados();
//				this.setInscricao_Erro(getMensagemInternalizacao("msg_matricula_resultadoProcSeletivoNaoEncontrado"));
//			} else {
//				if (!getResultadoProcessoSeletivoVO().isAprovadoProcessoSeletivo()) {
//					inicializarInscricaoEDadosRelacionados();
//					this.setInscricao_Erro(getMensagemInternalizacao("msg_matricula_naoAprovadoResultadoProcSeletivo"));
//				} else {
//					inicializarMatriculaComDadosInscricao();
//					this.setInscricao_Erro("");
//					setMensagemID("msg_dados_consultados");
//					setGuiaAba("DadosBasicos");
//				}
//			}
//			setValorConsultaCandidato("");
//			setCampoConsultaCandidato("");
//			getListaConsultaCandidato().clear();
//		} catch (Exception e) {
//			novo();
//			setTurma_Erro("");
//			setMensagemID("");
//			setMensagemDetalhada("msg_erro", "");
//			inicializarInscricaoEDadosRelacionados();
//			this.setInscricao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
//		}
//	}

	public void iniciarNovaMatriculaAluno() throws Exception {
		setOncompleteModal("");
		inicializarListasSelectItemTodosComboBoxDadosBasicos(false);
		setListaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs(null);
		inicializarUsuarioResponsavelMatriculaUsuarioLogado();
		verificarPermissaoUsuarioVisualizarConsultorMatricula();
		verificarPermissaoUsuarioInformarTipoMatriculaMatricula();
		verificarPermissaoSolicitarAprovacaoLiberacaoFinanceira();
		verificarPermissaoSolicitarLiberacaoMatriculaAposInicioXModulos();
		setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		setGuiaAba("DadosBasicos");

	}

	public void selecionarAlunoBuscaParaNovaMatricula() throws Exception {
		try {
			setOncompleteModal("");
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("alunoNovaMatriculaItens");
			// necessario pois precisamos da data de nascimento do usuario para
			// gerar um possivel usuario para o aluno.
			obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getCodigo(), false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			matriculaVO.setAluno(obj);
//			consultarInscricaoAluno();
			if (getListaConsultaCandidato().isEmpty()) {
				setValorConsultaAlunoNovaMatricula("");
				setCampoConsultaAlunoNovaMatricula("");
				getListaConsultaAlunoNovaMatricula().clear();
				iniciarNovaMatriculaAluno();
			} else {
				setOncompleteModal("PF('panelAlunoNovaMatricula_Inscricao_AtualizarDado2').show()");
			}
			/*PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("alunoNovaMatriculaItens");
			// necessario pois precisamos da data de nascimento do usuario para
			// gerar um possivel usuario para o aluno.
			obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			matriculaVO.setAluno(obj);
			setValorConsultaAlunoNovaMatricula("");
			setCampoConsultaAlunoNovaMatricula("");
			getListaConsultaAlunoNovaMatricula().clear();
			consultarInscricaoAluno();*/
		} catch (Exception e) {

		}
	}

//	public void consultarInscricaoAluno() {
//		try {
//			setMensagemID("msg_dados_consultados");
//			List<InscricaoVO> objs = new ArrayList(0);
//			objs = getFacadeFactory().getInscricaoFacade().consultarCanditadoPorCodigo(matriculaVO.getAluno(), 0, 0, false, getUsuarioLogado());
//			if (objs.size() == 1 && objs.stream().allMatch(p-> !Uteis.isAtributoPreenchido(p.getMatriculaVO().getMatricula()))) {
//				((InscricaoVO) objs.get(0)).setSelecionar(Boolean.TRUE);
//			}
//			setListaConsultaCandidato(objs);
//		} catch (Exception e) {
//
//		}
//	}

//	public void iniciarAlunoBuscaParaNovaMatricula() throws Exception {
//		try {
//			InscricaoVO insc = validarInscricaoCandidado();
//			if (insc.getCodigo().intValue() > 0) {
//				if (insc.getResultadoProcessoSeletivoVO().getCodigo().intValue() == 0) {
//					// Não encontrou nenhum resultado para a inscrição
//					matriculaVO.setUnidadeEnsino(new UnidadeEnsinoVO());
//					matriculaVO.setCurso(new CursoVO());
//					matriculaVO.setTurno(new TurnoVO());
//					setApresentarModalInscricao("");
//					throw new Exception(getMensagemInternalizacao("msg_matricula_resultadoProcSeletivoNaoEncontrado"));
//				} else {
//					if (!insc.getResultadoProcessoSeletivoVO().isAprovadoProcessoSeletivo()) {
//						matriculaVO.setUnidadeEnsino(new UnidadeEnsinoVO());
//						matriculaVO.setCurso(new CursoVO());
//						matriculaVO.setTurno(new TurnoVO());
//						throw new Exception(getMensagemInternalizacao("msg_matricula_naoAprovadoResultadoProcSeletivo"));
//					} else {
//						iniciarNovaMatriculaAluno();
//						setValorConsultaAlunoNovaMatricula("");
//						setCampoConsultaAlunoNovaMatricula("");
//						getListaConsultaAlunoNovaMatricula().clear();
//						setApresentarModalInscricao("PF('panelAlunoNovaMatricula_Inscricao_AtualizarDado').hide()");
//						getFacadeFactory().getInscricaoFacade().carregarDados(insc, getUsuarioLogado());
//						getMatriculaVO().setFormaIngresso(FormaIngresso.PROCESSO_SELETIVO.getValor());
//						getMatriculaVO().setInscricao(insc);
//						inicializarMatriculaComDadosInscricao();
//						setMensagemID("msg_dados_consultados");
//						setGuiaAba("DadosBasicos");
//					}
//				}
//			} else {
//				iniciarNovaMatriculaAluno();
//				setValorConsultaAlunoNovaMatricula("");
//				setCampoConsultaAlunoNovaMatricula("");
//				getListaConsultaAlunoNovaMatricula().clear();
//				setApresentarModalInscricao("PF('panelAlunoNovaMatricula_Inscricao_AtualizarDado').hide()");
//				getMatriculaVO().setFormaIngresso("");
//				setMensagemID("msg_dados_consultados");
//				setGuiaAba("DadosBasicos");
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
//	}

//	public void selecionarInscricao() throws Exception {
//		InscricaoVO obj = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
//		if (!getListaConsultaCandidato().isEmpty()) {
//			if (getListaConsultaCandidato().size() > 1) {
//				Iterator i = getListaConsultaCandidato().iterator();
//				while (i.hasNext()) {
//					InscricaoVO insc = (InscricaoVO) i.next();
//					if (obj.getCodigo().intValue() != insc.getCodigo().intValue() && obj.getSelecionar() && insc.getSelecionar()) {
//						insc.setSelecionar(Boolean.FALSE);
//					}
//				}
//			}
//		}
//	}

//	public void selecionarInscricaoMatricula() throws Exception {
//		try {
//			if (!getListaConsultaCandidato().isEmpty()) {
//				InscricaoVO insc = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItem");								
//					setApresentarModalInscricao("");
//					if (insc.getResultadoProcessoSeletivoVO().getCodigo().intValue() == 0) {
//						getMatriculaVO().setInscricao(new InscricaoVO());
//						throw new Exception(getMensagemInternalizacao("msg_matricula_resultadoProcSeletivoNaoEncontrado"));
//					}else if(Uteis.isAtributoPreenchido(insc.getMatriculaVO().getMatricula())) {
//						getMatriculaVO().setInscricao(new InscricaoVO());
//						throw new Exception("Inscrição utilizada para a matrícula de número "+ insc.getMatriculaVO().getMatricula() );
//					} else {
//						if (!insc.getResultadoProcessoSeletivoVO().isAprovadoProcessoSeletivo()) {
//							getMatriculaVO().setInscricao(new InscricaoVO());
//							throw new Exception(getMensagemInternalizacao("msg_matricula_naoAprovadoResultadoProcSeletivo"));
//						} else {
//							getFacadeFactory().getInscricaoFacade().carregarDados(insc, getUsuarioLogado());
//							setApresentarModalInscricao("PF('panelAlunoNovaMatricula_Inscricao_AtualizarDado2').hide()");
//							getMatriculaVO().setFormaIngresso("PS");
//							getMatriculaVO().setInscricao(insc);
//							getMatriculaVO().setMatriculaOnlineProcSeletivo(Boolean.TRUE);							
//							inicializarMatriculaComDadosInscricao();
//							montarListaSelectItemGradeCurricular();
//							montarListaProcessoMatricula(false);
//							if (getMatriculaPeriodoVO().getGradeCurricular().getCodigo() > 0) {
//								inicializarMatriculaComHistoricoAluno(true);
//							}
//							montarListaSelectItemPeriodoLetivo();
//							prepararDadosTurma();
//							getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().clear();
//							verificarPermissaoUsuarioInformarTipoMatriculaMatricula();									
//						}
//					}
//			
//			}
//			if (Uteis.isAtributoPreenchido(realizandoNovaMatriculaAluno) && realizandoNovaMatriculaAluno && Uteis.isAtributoPreenchido(matriculaPeriodoVO.getTurma())) {
//				processarDadosPertinentesTurmaSelecionada(false);
//			}
//			setMensagemID("msg_dados_consultados");
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
//	}

//	public String atualizarDadosAlunoBuscaParaNovaMatricula() throws Exception {
//		try {
//			InscricaoVO insc = validarInscricaoCandidado();
//			if (insc.getCodigo().intValue() > 0) {
//				getMatriculaVO().setFormaIngresso(FormaIngresso.PROCESSO_SELETIVO.getValor());
//				getMatriculaVO().setInscricao(insc);
//				if (insc.getResultadoProcessoSeletivoVO().getCodigo().intValue() == 0) {
//					// Não encontrou nenhum resultado para a inscrição
//					// inicializarInscricaoEDadosRelacionados();
//					setApresentarModalInscricao("");
//					throw new Exception(getMensagemInternalizacao("msg_matricula_resultadoProcSeletivoNaoEncontrado"));
//				} else {
//					if (!insc.getResultadoProcessoSeletivoVO().isAprovadoProcessoSeletivo()) {
//						// inicializarInscricaoEDadosRelacionados();
//						throw new Exception(getMensagemInternalizacao("msg_matricula_naoAprovadoResultadoProcSeletivo"));
//					} else {
//						setApresentarModalInscricao("PF('panelAlunoNovaMatricula_Inscricao_AtualizarDado').hide()");
//						getFacadeFactory().getInscricaoFacade().carregarDados(insc, getUsuarioLogado());
//						getMatriculaVO().setFormaIngresso(FormaIngresso.PROCESSO_SELETIVO.getValor());
//						getMatriculaVO().setInscricao(insc);
//						inicializarMatriculaComDadosInscricao();
//						setMensagemID("msg_dados_consultados");
//						setGuiaAba("DadosBasicos");
//					}
//				}
//			} else {
//				getMatriculaVO().setFormaIngresso("");
//			}
//			removerControleMemoriaFlashTela("AlunoControle");
//			context().getExternalContext().getSessionMap().put("aluno", getMatriculaVO().getAluno());
//			setValorConsultaAlunoNovaMatricula("");
//			setCampoConsultaAlunoNovaMatricula("");
//			getListaConsultaAlunoNovaMatricula().clear();
//			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm.xhtml");
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//			return "";
//		}
//	}

//	public InscricaoVO validarInscricaoCandidado() throws Exception {
//		InscricaoVO inscricao = new InscricaoVO();
//		if (!getListaConsultaCandidato().isEmpty()) {
//			if (getListaConsultaCandidato().size() > 0) {
//				int qtdSelecionado = 0;
//				Iterator i = getListaConsultaCandidato().iterator();
//				while (i.hasNext()) {
//					InscricaoVO insc = (InscricaoVO) i.next();
//					if (insc.getSelecionar()) {
//						qtdSelecionado++;
//						inscricao = insc;
//					}
//				}
//				if (qtdSelecionado > 1) {
//					inscricao = new InscricaoVO();
//					throw new Exception("Selecione somente uma inscrição para avançar a próxima etapa!");
//				}
//			}
//		}
//		Uteis.checkState(Uteis.isAtributoPreenchido(inscricao) && Uteis.isAtributoPreenchido(inscricao.getMatriculaVO().getMatricula()), "Inscrição utilizada para a matrícula de número "+ inscricao.getMatriculaVO().getMatricula() );
//		return inscricao;
//	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
//			getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().validarDadosPendenciaEmprestimoBiblioteca(objAluno, false, false, false, false, false, true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			this.setMatriculaVO(objAluno);
			this.setMatriculaPeriodoVO(new MatriculaPeriodoVO());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
			iniciarRenovacaoMatricula();
			setValorConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
//			getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().validarDadosPendenciaEmprestimoBiblioteca(objAluno, false, false, false, false, false, true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			this.setMatriculaVO(objAluno);
			this.setMatriculaPeriodoVO(new MatriculaPeriodoVO());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			this.setMatriculaVO(new MatriculaVO());
		}
	}

	public String navegarCadastrarAluno() {
		try {
			AlunoControle alunoControle = null;
			alunoControle = (AlunoControle) context().getExternalContext().getSessionMap().get(AlunoControle.class.getSimpleName());

			if (alunoControle == null) {
				alunoControle = new AlunoControle();
				context().getExternalContext().getSessionMap().put(AlunoControle.class.getSimpleName(), alunoControle);
			}
			alunoControle.novo();
			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm.xhtml");
		} catch (Exception e) {
			return "";
		}
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(new MatriculaVO());
		setMensagemID("msg_entre_dados");
	}

	public String getForaPrazo() {
		if (getMatriculaForaPrazo()) {
			return "PF('panelForaPrazo').show()";
		}
		return "PF('panelForaPrazo').hide()";
	}

	public String getPagamentoMatriculaForaPrazo() {
		if (getMatriculaForaPrazo()) {
			return "PF('panelPagamentoMatriculaForaPrazo').show()";
		}
		return "PF('panelPagamentoMatriculaForaPrazo').hide()";
	}
	

//	public void inicializarControleTransferenciaEntrada() {
//		TransferenciaEntradaControle transferenciaEntradaControle = (TransferenciaEntradaControle) context().getExternalContext().getSessionMap().get("TransferenciaEntradaControle");
//		transferenciaEntradaControle.getTransferenciaEntradaVO().setMatricula(this.getMatriculaVO());
//	}

//	public void inicializarResultadoProcSeletivoInscricao() {
//		try {
//			ResultadoProcessoSeletivoVO resultadoProcessoSeletivo = getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(matriculaVO.getInscricao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
//			this.setResultadoProcessoSeletivoVO(resultadoProcessoSeletivo);
//			getResultadoProcessoSeletivoVO().getSituacaoAprovacaoGeralProcessoSeletivo();
//		} catch (Exception e) {
//			this.setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
//		}
//	}

//	public boolean validarContaReceberAbertaEntregue(String matricula, Integer codMatriculaPeriodo) throws Exception {
//		if (codMatriculaPeriodo != 0) {
//			List listaContaReceberVO = getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaContaReceberMatriculaMensalidadeImpressaoBoletoTrue(matricula, codMatriculaPeriodo);
//			if (!listaContaReceberVO.isEmpty()) {
//				setApresentarRichAvisoContaReceber("PF('panelAvisoContaReceber').show()");
//				return false;
//			} else {
//				setApresentarRichAvisoContaReceber("");
//				return true;
//			}
//		}
//		return true;
//	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Matricula</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	
	
//	public void montarListaSelectItemPlanoFinanceiroCursoEspecificoIntegracaoFinanceira() throws Exception {
//		List objs = new ArrayList(0);
//		List resultadoConsulta = new ArrayList(0);
//		
//		String planoFinanceiroDesc = this.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getDescricao();
//		if (!planoFinanceiroDesc.equals("")) {
//			if (planoFinanceiroDesc.length() > 20) {
//				planoFinanceiroDesc = planoFinanceiroDesc.substring(0, 20);
//			}
//			planoFinanceiroDesc = planoFinanceiroDesc + " - ";
//		}
//		resultadoConsulta = getFacadeFactory().getMatriculaPeriodoFacade().executarMontagemComboCondicaoPagamentoPlanoFinanceiroCurso(getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo(), getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), matriculaVO, matriculaPeriodoVO, getUsuarioLogado());
//		Iterator i = resultadoConsulta.iterator();
//		boolean primeiro = true;
//		while (i.hasNext()) {
//			CondicaoPagamentoPlanoFinanceiroCursoVO obj = (CondicaoPagamentoPlanoFinanceiroCursoVO) i.next();
//			objs.add(new SelectItem(obj.getCodigo(), planoFinanceiroDesc + obj.getDescricao()));
//
//			if (primeiro && (getMatriculaPeriodoVO().getCodigo().equals(0)) && (getUsuarioLogado().getVisaoLogar().equals("aluno") || getUsuarioLogado().getVisaoLogar().equals("pais") || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador())) {
//				getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(obj.getCodigo());
//				getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setTextoPadraoContratoMatricula(obj.getTextoPadraoContratoMatricula());
//				getFacadeFactory().getMatriculaPeriodoFacade().realizarDefinicaoContratoPadraoSerUsadoMatriculaPeriodo(TipoContratoMatriculaEnum.NORMAL, getMatriculaPeriodoVO(), true, false, getUsuarioLogado());				
//				primeiro = false;
//			}
//		}
//		setListaSelectItemPlanoFinanceiroCurso(objs);		
//	}
//	
	

	

	public void cancelarSuspensao() {
		try {
			getFacadeFactory().getMatriculaFacade().realizarCancelamentoBloqueioMatricula(getMatriculaVO(), getUsuarioLogado());
			setSituacaoCancelamentoBloqueioMatricula("CANCELAMENTO_REALIZADO_COM_SUCESSO");
			setMensagemDetalhada("Cancelamento de suspensão de matrícula realizada com sucesso!");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setSituacaoCancelamentoBloqueioMatricula("");
		}
	}

	public void realizarSuspensaoMatricula() {
		try {
			getMatriculaVO().setMatriculaSuspensa(Boolean.TRUE);
			getMatriculaVO().setDataBaseSuspensao(getMatriculaVO().getData());
			getFacadeFactory().getMatriculaFacade().alterarMatriculaSuspensao(getMatriculaVO());
			setMensagemDetalhada("Suspensão de matrícula realizada com sucesso!");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String gravar() {
		try {
			if (Uteis.isAtributoPreenchido(getBanner()) && getPermitirMatriculaOnline()) {
				limparMensagem();
//				navegarAbaPlanoFinanceiroAluno();
				if (getMensagemID().equals("msg_erro") && getMensagemDetalhada() != null && !getMensagemDetalhada().trim().isEmpty() && !getMatriculaPeriodoVO().getFinanceiroManual()) {
					throw new Exception(getMensagemDetalhada());
				}
			}
			executarValidacaoSimulacaoVisaoAluno();
//			setApresentarMensagemPlanoAlteradoConvenioParcelaRecebida(false);
//			verificarSeAlunoBolsista();
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema = getAplicacaoControle().getConfiguracaoGeralSistemaVO(getMatriculaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());			
//			getFacadeFactory().getMatriculaPeriodoFacade().verificarPreRequisitoDisciplina(matriculaPeriodoVO, matriculaVO, getPermitirRealizarMatriculaDisciplinaPreRequisito(), getUsuarioLogado());
//			getFacadeFactory().getMatriculaPeriodoFacade().validarDisponibilidadeVagasMatriculaPeriodoTurmaDisciplina(matriculaVO, matriculaPeriodoVO, false, getUsuarioLogado());
			verificarExistemDisciplinasCursandoPorCorrespodenciaQuePrecisamSerMigradasNovaTurma();
			if (this.getRenovandoMatricula()) {
				getFacadeFactory().getMatriculaFacade().verificarDocumentaoImpediRenovacaoEstaPendente(getMatriculaVO(), getUsuarioLogado());
			}
			setApresentarRichAvisoContaReceber("");
			setAbrirPainelParcelaExtra(false);
			if (matriculaVO.getIsAtiva()) {
				getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
				if(!Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula())) {
				getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
				}
				adicionarMatriculaPeriodo();				
				if (!matriculaVO.getMatriculaJaRegistrada()) {
					if (!getBanner().equals(0) && getMatriculaPeriodoVO().getProcessoMatriculaVO().getMensagemApresentarVisaoAluno().equals("") && (!getTermoAceiteContrato() || getTermoAceiteContrato())) {
						setTermoAceiteContrato(true);
					} else if (!getBanner().equals(0) && !getMatriculaPeriodoVO().getProcessoMatriculaVO().getMensagemApresentarVisaoAluno().equals("") && !getTermoAceiteContrato()) {
						throw new Exception("Para prosseguir, você deve Aceitar Termos do Contrato");
					}
					registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Incluir Matricula", "Incluindo");
					if (getMatriculaVO().getAluno().getCodProspect() != null && getMatriculaVO().getAluno().getCodProspect() != 0) {
						getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, matriculaPeriodoVO, getProcessoCalendarioMatriculaVO(),  configuracaoGeralSistema, false, getUsuarioLogado());
						registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Incluir Matricula", "Incluindo");
					} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador() || getUsuarioLogado().getIsApresentarVisaoProfessor()) {
						getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, matriculaPeriodoVO, getProcessoCalendarioMatriculaVO(),  configuracaoGeralSistema, false, getUsuarioLogado());
						registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Incluir Matricula", "Incluindo");
					} else {
						//if(getSolicitarLiberacaoFinanceira() || getSolicitarLiberacaoMatriculaAposInicioXModulos()) {
						if(matriculaVO.getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira() || matriculaVO.getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica()) {
							matriculaVO.setBloqueioPorSolicitacaoLiberacaoMatricula(Boolean.TRUE);
						}
						boolean validarAlunoCurso = Uteis.isAtributoPreenchido(getMatriculaVO().getTransferenciaEntrada()) && getMatriculaVO().getTransferenciaEntrada().getTipoTransferenciaEntrada().equals("IN");
						getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, matriculaPeriodoVO, getProcessoCalendarioMatriculaVO(),  configuracaoGeralSistema, validarAlunoCurso, getUsuarioLogado());
						registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Incluir Matricula", "Incluindo");
					}
//					if (!getBanner().equals(0)) {
//						setMatriculaConfirmada(true);
////						setPermitirRecebimentoCartaoCreditoOnline(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().verificarExistenciaConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()).getCodigo(), getNegociacaoRecebimentoVO().getValorTotal(), "permitirecebimentocartaoonline"));
//						if (getPermitirRecebimentoCartaoCreditoOnline()) {
//							consultarContaReceberAlunoMatriculaOnline();
//						}
//					}
							
				} else {
					registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Alterar Matricula", "Alterando");
					inicializarUsuarioResponsavelMatriculaUsuarioLogado();
//					if (!getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaDocumentacao()) {
//						navegarAbaDocumentacao();
//					}
					if (!getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaDisciplinas()) {
						navegarAbaDisciplinas();
					}
					verificarPermissaoUsuarioLiberarAlterarCategoriaCondicaoPagamentoPlanoFinanceiroCurso();
					getMatriculaPeriodoVO().setPermitirVisualizarAbaDescontos(getMostrarAbaDescontos());
//					getFacadeFactory().getMatriculaFacade().alterar(matriculaVO, matriculaPeriodoVO, getProcessoCalendarioMatriculaVO(), configuracaoGeralSistema, getUsuarioLogado());
//					getFacadeFactory().getMatriculaPeriodoVencimentoFacade().realizarGeracaoDescricaoDescontoMatriculaPeriodoVencimento(getMatriculaVO(), getMatriculaPeriodoVO(), getOrdemDesconto(), getUsuarioLogado());
					registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Alterar Matricula", "Alterando");
				}
//				if (getPortadorDiploma()) {
//					registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Alterar Matricula - Portador Diploma", "Alterando");
//					getFacadeFactory().getPortadorDiplomaFacade().alterarMatricula(getCodigoPortadorDiploma(), getMatriculaVO().getMatricula());
//					setPortadorDiploma(false);
//					registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Alterar Matricula - Portador Diploma", "Alterando");
//				}
				for (OperacaoFuncionalidadeVO operacaoFuncionalidadeVO : getOperacaoFuncionalidadePersistirVOs()) {
					if (OrigemOperacaoFuncionalidadeEnum.MATRICULA.equals(operacaoFuncionalidadeVO.getOrigem())) {
						operacaoFuncionalidadeVO.setCodigoOrigem(getMatriculaVO().getMatricula());
//						if(operacaoFuncionalidadeVO.getOperacao().equals(OperacaoFuncionalidadeEnum.MATRICULA_PERMITIR_USUARIO_DESCONSIDERAR_DIFERENCA_VALOR_RATEIO)) {
//							operacaoFuncionalidadeVO.setObservacao(getMatriculaPeriodoVO().getObservacaoParaLiberacaoDiferencaValorRateio());
//						}
					} else if (OrigemOperacaoFuncionalidadeEnum.MATRICULA_PERIODO.equals(operacaoFuncionalidadeVO.getOrigem())) {
						operacaoFuncionalidadeVO.setCodigoOrigem(getMatriculaPeriodoVO().getCodigo().toString());
					}
					getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(operacaoFuncionalidadeVO);
				}
				if (!getBanner().equals(0) && validarAssinaturaDigitalHabilitada(getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getMatriculaPeriodoVO().getContratoMatricula().getCodigo())) {
					getMatriculaPeriodoVO().setRegerarDocumentoAssinado(true);
					imprimirContrato("MA");
					setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodoContratoAssinado(getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + getCaminhoRelatorio());
					setModalAssinarDocumento("PF('panelDocumentoContratoAssinado').show()");
				}
				navegarAbaDisciplinaMatriculado();
//				getMatriculaPeriodoVO().reiniciarControleBloqueioCompetencia();
				limparMensagem();
				inicializarMensagemVazia();
				if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
					montarDadosMatriculaPeriodoTurmaDisciplinaPeriodoLetivoGradeCurricularGrupoOptativaDisciplina();
					montarDadosMatriculaPeriodoTurmaDisciplinaPeriodoLetivoGradeDisciplinaComposta();
				}
				setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
				if (!getBanner().equals(0)) {
					getLoginControle().inicializarBanner();					
				}				
				
			} else {
				setMensagemDetalhada("Esta renovação não pode ser gravada pois refere-se a uma matrícula: " + matriculaVO.getSituacao_Apresentar().toUpperCase());
			}
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
//				if (this.getRenovandoMatricula()) {
//					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemRenovacaoMatricula(getMatriculaVO(), getUsuarioLogado());
//				}
				return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaConfirmadoForm.xhtml");
//				return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
			}else {
				return getModalPagamentoOnline();
			}
		} catch (Exception e) {
			
			if (e.getMessage().contains("check_matriculaperiodo_ano_semestre_matricula")) {
				setMensagemDetalhada("msg_erro", "Já existe uma matrícula período para essa matrícula nesse ano e semestre", Uteis.ERRO);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
			e.printStackTrace();
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
			}else {
				return "";
			}
		}
	}

	public void gravarVisaoAluno() throws Exception {
		Integer usuario = getUsuarioLogado().getCodigo();
	    Object lock = bloqueioMatricula.computeIfAbsent(usuario, k -> new Object());
	    synchronized (lock) {
	        MatriculaVO mat = getFacadeFactory().getMatriculaFacade().consultarBloqueioMatriculaPorMatricula(matriculaVO);
	        if (Uteis.isAtributoPreenchido(mat)) {
				throw new ConsistirException("Não é Possível Renovar a Matrícula Enquanto um Requerimento Estiver em Processo de Abertura.");
	        }
		try {
			executarValidacaoSimulacaoVisaoAluno();
			aceitarTermoAceiteRenovacaoOnline();		

			setApresentarRichAvisoContaReceber("");
			String situacaoMatricula = getFacadeFactory().getMatriculaFacade().consultarSituacaoMatriculaPorMatricula(getMatriculaVO());
			if(Uteis.isAtributoPreenchido(situacaoMatricula) && situacaoMatricula.equals("TI")){
				throw new ConsistirException("A MATRÍCULA está Transferida Internamente, por esse Motivo não é Possível Realizar uma Renovação.");
			}
			if (getMatriculaVO().getIsAtiva()) {
				getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
				getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
				adicionarMatriculaPeriodo();
				registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Alterar Matricula", "Alterando");
				inicializarUsuarioResponsavelMatriculaUsuarioLogado();
				verificarExisteBloqueioDisciplinasDeDependenciaMaximaOfertadaDevemSerCursadasPeloAluno();
				getFacadeFactory().getMatriculaFacade().alterar(getMatriculaVO(), getMatriculaPeriodoVO(), getProcessoCalendarioMatriculaVO(),  getAplicacaoControle().getConfiguracaoGeralSistemaVO(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()), getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Alterar Matricula", "Alterando");
//				if (getPortadorDiploma()) {
//					registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Alterar Matricula - Portador Diploma", "Alterando");
//					getFacadeFactory().getPortadorDiplomaFacade().alterarMatricula(getCodigoPortadorDiploma(), getMatriculaVO().getMatricula());
//					setPortadorDiploma(false);
//					registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Alterar Matricula - Portador Diploma", "Alterando");
//				}
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemRenovacaoMatricula(getMatriculaVO(), getUsuarioLogado());
				setMensagemID("msg_dados_gravados");
				navegarAbaDisciplinaMatriculado();
				if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) && !veriricarUsuarioTemPermissaoAlterarRenovacaoMatricula()) {
					getVisaoAlunoControle().setExisteProcessoMatriculaAberto(false);
				}
				verificarPermissaoEmitirBoletoMatriculaVisaoAluno();
				setPermitirGerarContratoMatrila(false);
				if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getContratoMatricula())) {
					setPermitirGerarContratoMatrila(validarAssinaturaDigitalHabilitada(getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getMatriculaPeriodoVO().getContratoMatricula().getCodigo()) && (!matriculaVO.getCurso().getPermitirAssinarContratoPendenciaDocumentacao() && !getFacadeFactory().getDocumetacaoMatriculaFacade().verificaAlunoDevendoDocumentoQueSuspendeMatricula(matriculaVO.getMatricula())) || (matriculaVO.getCurso().getPermitirAssinarContratoPendenciaDocumentacao()));
				}else {
					setPermitirGerarContratoMatrila(false);
				}
				if (getPermitirGerarContratoMatrila()) {
					getMatriculaPeriodoVO().setRegerarDocumentoAssinado(true);
					setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodoContratoAssinado(getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					if(Uteis.isAtributoPreenchido(getDocumentoAssinadoVO()) && 
							getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(
									documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))) {
						if(getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorSei()){
							getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream()
							.filter(documentoPessoaPendendePessoa -> documentoPessoaPendendePessoa.getSituacaoDocumentoAssinadoPessoaEnum().isPendente())
							.forEach(documentoPessoaPendenteRejeitar -> getFacadeFactory().getDocumentoAssinadoPessoaFacade().rejeitarContratoAssinadoPendenteAutomaticamente(documentoPessoaPendenteRejeitar, getUsuarioLogado()));
							imprimirContrato("MA");
							setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodoContratoAssinado(getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
						}
					}else {
						imprimirContrato("MA");
						setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodoContratoAssinado(getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					}

					if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {						
						if(getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorCertisign() && 
								!getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(
										documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))) {
							getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(getDocumentoAssinadoVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
							setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"+ getDocumentoAssinadoVO().getArquivo().getPastaBaseArquivo().replace("\\", "/") +"/"+ getDocumentoAssinadoVO().getArquivo().getNome());
							setModalAssinarDocumento("PF('panelDocumentoContratoAssinado').show()");
						}else if(getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorCertisign() && 
								getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(
										documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))) {
							setModalAssinarDocumento("SignSingleDocument('"+getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().filter(
									documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)).findFirst().get().getUrlAssinatura()+"');");							
						}
						if(getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorTechCert() &&
								!getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(
										documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))) {
							getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(getDocumentoAssinadoVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
							setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"+ getDocumentoAssinadoVO().getArquivo().getPastaBaseArquivo().replace("\\", "/") +"/"+ getDocumentoAssinadoVO().getArquivo().getNome());
							setModalAssinarDocumento("PF('panelDocumentoContratoAssinado').show()");
						}else if(getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorTechCert() &&
								getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(
										documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))) {
							setModalAssinarDocumento("SignSingleDocument('"+getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().filter(
									documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)).findFirst().get().getUrlAssinatura()+"');");
						}else if (getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorSei()) {
							setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + getDocumentoAssinadoVO().getArquivo().getNome());
							setModalAssinarDocumento("PF('panelDocumentoContratoAssinado').show()");
						}
												
					}
				}
			} else {
				setMensagemDetalhada("Esta renovação não pode ser gravada pois refere-se a uma matrícula: " + matriculaVO.getSituacao_Apresentar().toUpperCase());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}finally {
			bloqueioMatricula.remove(usuario);
		}
    }
}


	public String navegarParaCadastroAluno() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm.xhtml");
	}

	public String navegarAbaDadosBasicos() {
		try {
			setApresentarModalAvisoAlunoReprovado(Boolean.FALSE);
			setMensagemModalAvisoAlunoReprovado("");
			gerarDadosGraficoEvolucaoAcademicaAluno();
			setGuiaAba("DadosBasicos");
			setMensagemID("msg_dados_selecionados");
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "navegarAbaDadosBasicos"+ getMatriculaVO().getMatricula()+ "-hora-"+UteisData.getDataComHoraAtual());
		} catch (Exception e) {
			setGuiaAba("DadosBasicos");
		}
		return realizarNavegacaoPagina();
	}

	public String navegarAbaLiberadaMatricula() {
		setApresentarModalAvisoAlunoReprovado(Boolean.FALSE);
		setMensagemModalAvisoAlunoReprovado("");
		setGuiaAba("LiberadaMatricula");
		return realizarNavegacaoPagina();
	}
	
	public void inicializarDadosHistoricoASerExcluidoAposRemoverMatriculaPeriodoTurmaDisciplinaPorEquivalenciaRemovida(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		matriculaPeriodoVO.getTransferenciaMatrizCurricularMatriculaVO().getListaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaDisciplinaEquivalenteUsuarioVOs().add(matriculaPeriodoTurmaDisciplinaVO);
	}

	public void removerMatriculaPeriodoTurmaDisciplina() {
		try {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaItens");			
			// Caso a disciplina esteja cursando por equivalência e tenha sido carregado devido a transferência de matriz curricular
			// Será carregada em uma lista para realizar a eliminição da equivalencia e caso seja incluida novamente salvar as notas e dados academicos do histórico
			if (obj.getDisciplinaIncluidaAposTransferenciaMatrizCurricularControlarEquivalencia()) {
				inicializarDadosHistoricoASerExcluidoAposRemoverMatriculaPeriodoTurmaDisciplinaPorEquivalenciaRemovida(obj);
			}
			if (getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && getMatriculaVO().getCurso().getConfiguracaoAcademico().getBloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular() && (!getMatriculaPeriodoVO().getLiberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular())) {
				// se entrar aqui é por que o aluno é regular (nao deve nenhuma
				// materia do passado) e o
				// bloqueio para exclusao de disciplina está ativo. Logo, nao
				// pode ser excluida disciplina para
				// este aluno até que o usuário com senha tenha permissão para
				// fazer isto.
				if (Uteis.isAtributoPreenchido(obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo()) && getPermiteIncluirDisciplinaOptativa()) {
					this.setApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular(false);
				} else {
					this.setApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular(true);
					if (!getRenovandoMatriculaDaVisaoDoAluno()) {
						throw new Exception("Não é permitido a Exclusão de Disciplina para um Aluno Regular (Aluno que não está devendo nenhuma disciplina dos períodos anteriores). Clique no botão Liberar Exclusão de Disciplinas para realizar esta operação.");
					} else {
						throw new Exception("Não é permitido a Exclusão de Disciplina para um Aluno Regular (Aluno que não está devendo nenhuma disciplina dos períodos anteriores). ");
					}
				}
			}
			
			if (!getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && !getMatriculaVO().getCurso().getConfiguracaoAcademico().getPermitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual() && Uteis.isAtributoPreenchido(obj.getGradeDisciplinaVO()) && obj.getGradeDisciplinaVO().getPeriodoLetivoVO().getCodigo().equals(getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo()) && (!getMatriculaPeriodoVO().getLiberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular())) {
				this.setApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular(true);
				if (!getRenovandoMatriculaDaVisaoDoAluno()) {
					throw new Exception("Não é permitido a Exclusão de Disciplina para um Aluno Irregular (Aluno que está devendo alguma disciplina dos períodos anteriores). Clique no botão Liberar Exclusão de Disciplinas para realizar esta operação.");
				} else {
					throw new Exception("Não é permitido a Exclusão de Disciplina para um Aluno Irregular (Aluno que está devendo alguma disciplina dos períodos anteriores). ");
				}
			}
			
			//getFacadeFactory().getEstagioFacade().validarDadosDisciplinaIncluidaEmEstagio(obj.getMatricula(), obj.getDisciplina().getCodigo(), getMatriculaPeriodoVO().getAno(), getMatriculaPeriodoVO().getSemestre(), getUsuarioLogado());
			obj.getGradeDisciplinaVO().setGradeDisciplinaOfertada(true);
			getFacadeFactory().getMatriculaPeriodoFacade().removerMatriculaPeriodoTurmaDisciplinaComposta(getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs(), obj, getUsuarioLogado());
			getFacadeFactory().getMatriculaPeriodoFacade().removerMatriculaPeriodoTurmaDisciplinaObjEspecifico(matriculaPeriodoVO, matriculaVO, obj, getUsuarioLogado());
			// lista que mantem itens que o usuario removeu durante a edicao da
			// matriculaPeriodo
			// esta lista é importante para permitir que facilmente o usuario
			// possa adicionar
			// novamente o objeto excluido, pois o mesmo na edicao em especial
			// ja possui
			// um historico determinando que o mesmo esta sendo cursada. O que
			// poderia impossibilitar
			// da disciplina ser listada para ser incluida novamente, pelo fluxo
			// normal deste cadastro
			if (!obj.getDisciplinaEquivale()) {
				// no caso so adicionamos para a lista para ser adicionada, caso
				// nao seja uma disciplina
				// de equivalencia. Pois se for por equivalencia a inclusao da
				// mesma segue fluxo
				// proprio para o mapa de equivalencia.
				getListaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs().add(obj);
			}			
			atualizarListaDisciplinasHistoricoPeriodoLetivoSelecionado();
			if(Uteis.isAtributoPreenchido(obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo())) {
				if(getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().stream().anyMatch(d -> d.getCodigo().equals(obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo()))){
					getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().stream().filter(d -> d.getCodigo().equals(obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo())).findFirst().get().setDisciplinaJaAdicionada(false);
				}
			}else {
				atualizarListaDisciplinasHistoricoPeriodoLetivoSelecionado();
			}
			prepararHorarioAulaAluno();
			
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerListaMatriculaPeriodoTurmaDisciplina(List listaRemover) throws Exception {
		Iterator i = listaRemover.iterator();
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			getFacadeFactory().getMatriculaPeriodoFacade().removerMatriculaPeriodoTurmaDisciplinaComposta(getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs(), obj, getUsuarioLogado());
			getFacadeFactory().getMatriculaPeriodoFacade().removerMatriculaPeriodoTurmaDisciplinaObjEspecifico(matriculaPeriodoVO, matriculaVO, obj, getUsuarioLogado());
			atualizarListaDisciplinasHistoricoPeriodoLetivoSelecionado();
		}
	}

	public void prepararMapaEquivalenciaParaVisualizacao(MapaEquivalenciaDisciplinaVO mapaVisualizar, Integer numeroAgrupamentoEquivalenciaDisciplina) {
		// mapaVisualizar.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()
		this.getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().atualizarSituacaoMapaEquivalenciaDisciplinaAluno(mapaVisualizar, numeroAgrupamentoEquivalenciaDisciplina, this.getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs());
		setMapaEquivalenciaDisciplinaVisualizar(mapaVisualizar);
	}

	public void selecionarMapaEquivalenciaParaVisualizacao() {
		MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaItens");
		Integer numeroAgrupamentoEquivalenciaDisciplina = 0;
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			try {
				numeroAgrupamentoEquivalenciaDisciplina = getFacadeFactory().getHistoricoFacade().consultarNumeroAgrupamentoEquivalenciaDisciplinaPorMatriculaPeriodoTurmaDisciplina(obj.getCodigo(), obj.getMapaEquivalenciaDisciplinaVOIncluir().getCodigo());
			} catch (Exception e) {

			}
		}
		MapaEquivalenciaDisciplinaVO mapaVisualizar = obj.getMapaEquivalenciaDisciplinaVOIncluir();
		prepararMapaEquivalenciaParaVisualizacao(mapaVisualizar, numeroAgrupamentoEquivalenciaDisciplina);
	}

	public void prepararDisciplinaMapaEquivalenciaParaInclusao() {
		try {
			MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO = (MapaEquivalenciaDisciplinaCursadaVO) context().getExternalContext().getRequestMap().get("disciplinaCursarMapaItens");
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

			// inicializando lista de Mapas somanete com a opção válida do mapa
			// que
			// está sendo referenciado.
			List listaMapasEquivalenciaSomenteComOpcaoDoMapaSelecionado = new ArrayList();
			listaMapasEquivalenciaSomenteComOpcaoDoMapaSelecionado.add(getMapaEquivalenciaDisciplinaVisualizar());
			setListaSelectItemMapaEquivalenciaDisciplinaIncluir(listaMapasEquivalenciaSomenteComOpcaoDoMapaSelecionado);

			// definindo disciplina será cursada
			montarListaSelectItemDisciplinaEquivalenteAdicionar();
			setCodigoMapaEquivalenciaDisciplinaCursar(mapaEquivalenciaDisciplinaCursadaVO.getCodigo());
			// getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCreditoAlunoPodeCursar();

			setMensagemID("msg_informe_dados");

			// montar depois de setar a mensagem, pois ao montar a turma,
			// pode-se
			// gerar mensagens ao usuário
			montarListaSelectItemTurmaAdicionar(novoObj);
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(novoObj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarMatriculaPeriodoTurmaDisciplinaComposta() {
		MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaItens");
		try {
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(obj);
			setListaMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(obj, getMatriculaPeriodoVO()));
			setMensagemID("msg_informe_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getPermitirIncluirDisciplinaNaVisaoAluno() {
		if ((matriculaPeriodoVO.getProcessoMatriculaVO().getPermiteIncluirExcluirDisciplinaVisaoAluno()) && (matriculaPeriodoVO.getPermitirIncluirExcluirDisciplinas()) && (getPermissaoAlterarDadosAcademicos())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public void adicionarMatriculaPeriodoTurmaDisciplinaPorInclusao() {
		try {
			adicionarMatriculaPeriodoTurmaDisciplinaPorInclusao(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarMatriculaPeriodoTurmaDisciplinaPorInclusao(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaAdicionarVO) throws Exception {
		try {
			getMatriculaPeriodoVO().setMensagemAvisoUsuario(null);
			if (matriculaPeriodoTurmaDisciplinaAdicionarVO.getTurma().getCodigo().equals(0)) {
				throw new Exception("Selecionar uma Turma para incluir a disciplina.");
			}

			getFacadeFactory().getTurmaFacade().carregarDados(matriculaPeriodoTurmaDisciplinaAdicionarVO.getTurma(), getUsuarioLogado());
			matriculaPeriodoTurmaDisciplinaAdicionarVO.setModalidadeDisciplina(ModalidadeDisciplinaEnum.ON_LINE);
//			for (TurmaDisciplinaVO td : matriculaPeriodoTurmaDisciplinaAdicionarVO.getTurma().getTurmaDisciplinaVOs()) {
//				if(matriculaPeriodoTurmaDisciplinaAdicionarVO.getDisciplinaPorEquivalencia() && matriculaPeriodoTurmaDisciplinaAdicionarVO.getMapaEquivalenciaDisciplinaCursada().getDisciplinaVO().getCodigo().equals(td.getDisciplina().getCodigo())){
//					matriculaPeriodoTurmaDisciplinaAdicionarVO.setModalidadeDisciplina(td.getModalidadeDisciplina());
//				}else if (!matriculaPeriodoTurmaDisciplinaAdicionarVO.getDisciplinaPorEquivalencia() && td.getDisciplina().getCodigo().intValue() == matriculaPeriodoTurmaDisciplinaAdicionarVO.getDisciplina().getCodigo().intValue()) {
//					matriculaPeriodoTurmaDisciplinaAdicionarVO.setModalidadeDisciplina(td.getModalidadeDisciplina());
//					break;
//				}
//			}
			getFacadeFactory().getMatriculaPeriodoFacade().adicionarEValidarMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoVO, matriculaVO, getPermitirRealizarMatriculaDisciplinaPreRequisito(), matriculaPeriodoTurmaDisciplinaAdicionarVO,  false, getUsuarioLogado());
			if (!getMatriculaPeriodoVO().getMensagemAvisoUsuario().getListaMensagemErro().isEmpty()) {
				throw getMatriculaPeriodoVO().getMensagemAvisoUsuario();
			}
			atualizarListaDisciplinasHistoricoPeriodoLetivoSelecionado();
			prepararHorarioAulaAluno();
			
			if (TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA.equals(matriculaPeriodoTurmaDisciplinaAdicionarVO.getGradeDisciplinaVO().getTipoControleComposicao())) {
				matriculaPeriodoTurmaDisciplinaAdicionarVO.setNrAlunosMatriculados(0);
				matriculaPeriodoTurmaDisciplinaAdicionarVO.setNrVagasDisponiveis(0);
				setListaMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getMatriculaPeriodoTurmaDisciplinaVOAdicionar(), getMatriculaPeriodoVO()));
				setOnCompleteIncluirDisciplinaTurma("PF('panelIncluirDisciplinas').hide(); PF('panelIncluirDisciplinasFazParteComposicao').hide(); PF('panelDisciplinasComposta').show();");
			} else {
				setOnCompleteIncluirDisciplinaTurma("PF('panelIncluirDisciplinas').hide(); PF('panelIncluirDisciplinasFazParteComposicao').hide();");
			}
			setMensagemID("msg_dados_incluidos");
		} catch (ConsistirException e) {
			matriculaPeriodoTurmaDisciplinaAdicionarVO.setTurma(null);
			if (e.getReferenteChoqueHorario()) {
				matriculaPeriodoTurmaDisciplinaAdicionarVO.setSugestaoTurmaPraticaTeoricaRealizada(false);
				matriculaPeriodoTurmaDisciplinaAdicionarVO.setDescricaoChoqueHorarioDisciplina(e.getToStringMensagemErro());
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
			setOnCompleteIncluirDisciplinaTurma("");
		} catch (Exception e) {
			matriculaPeriodoTurmaDisciplinaAdicionarVO.setTurma(null);
			setOnCompleteIncluirDisciplinaTurma("");
			throw e;
		}
	}

	public void adicionarMatriculaPeriodoTurmaDisciplina() {
		try {
			getFacadeFactory().getTurmaFacade().carregarDados(this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma(), getUsuarioLogado());
			getFacadeFactory().getMatriculaPeriodoFacade().adicionarEValidarMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoVO, matriculaVO, false, this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma(), null,  false, getUsuarioLogado());
			atualizarListaDisciplinasHistoricoPeriodoLetivoSelecionado();
		} catch (ConsistirException e) {
			if (e.getReferenteChoqueHorario()) {
				setConsistirExceptionMensagemDetalhada("msg_aviso", e, Uteis.ALERTA);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarMatriculaPeriodoTurmaDisciplinaRenovacaoOnline() {
		try {
			TurmaVO turma = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			getFacadeFactory().getMatriculaPeriodoFacade().adicionarMatriculaPeriodoTurmaDisciplinaVO(getMatriculaPeriodoVO(), getMatriculaVO(), turma, this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina(), null, getPermitirRealizarMatriculaDisciplinaPreRequisito(), null, getMatriculaVO().getCurso().getConfiguracaoAcademico(), getUsuarioLogado());
			atualizarListaDisciplinasHistoricoPeriodoLetivoSelecionado();
		} catch (ConsistirException e) {
			if (e.getReferenteChoqueHorario()) {
				setConsistirExceptionMensagemDetalhada("msg_aviso", e, Uteis.ALERTA);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

//	public void realizarMontagemQuadroHorarioTurmaDisciplina() {
//		getListaHorarioTurmaDisciplinaSemanalVOs().clear();
//		TurmaVO turmaIncluir = this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma();
//		if (!turmaIncluir.getCodigo().equals(0)) {
//			setListaHorarioTurmaDisciplinaSemanalVOs(getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaSemanalPorTurmaDisciplina(turmaIncluir.getCodigo(), this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo(), "", "", getUsuarioLogado()));
//			for (HorarioTurmaDisciplinaSemanalVO obj : getListaHorarioTurmaDisciplinaSemanalVOs()) {
//				setDataInicioHorario(obj.getDataInicio());
//				setDataTerminoHorario(obj.getDataTermino());
//				break;
//			}
//		}
//	}

	public void verificarDisciplinaNaGradeAluno(MatriculaPeriodoVO mat, GradeDisciplinaVO obj) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> lista = mat.getMatriculaPeriodoTumaDisciplinaVOs();
		for (MatriculaPeriodoTurmaDisciplinaVO objeto : lista) {
			if (objeto.getDisciplina().getCodigo().intValue() == obj.getDisciplina().getCodigo().intValue()) {
				throw new Exception("Disciplina já incluída na matrícula deste período.");
			}
		}
	}

	public List consultarMapaEquivalenciaDisciplina() throws Exception {
		List listaResultado = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatriz(this.getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo(), NivelMontarDados.TODOS, false);
		return listaResultado;
	}

	public void montarListaSelectItemMapaEquivalenciaDisciplina() {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (!this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia()) {
				setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
				setCodigoMapaEquivalenciaDisciplinaCursar(0);
				setListaSelectItemMapaEquivalenciaDisciplinaIncluir(new ArrayList(0));
				montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
				return;
			}			
			resultadoConsulta = consultarMapaEquivalenciaDisciplina();
			List objs = new ArrayList(0);
			i = resultadoConsulta.iterator();
			while (i.hasNext()) {
				MapaEquivalenciaDisciplinaVO obj = (MapaEquivalenciaDisciplinaVO) i.next();
				objs.add(obj);
			}
			setListaSelectItemMapaEquivalenciaDisciplinaIncluir(objs);
			montarListaSelectItemDisciplinaEquivalenteAdicionar();
			montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
			if (resultadoConsulta.isEmpty()) {
				throw new Exception("Não Existem Equivalências Disponíveis para Esta Disciplina (Mapas de Equivalências)");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaSelectItemMapaEquivalenciaDisciplinaIncluir(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			// setMensagem("msg_dados_consultados");
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void selecionarMapaDisciplinaEquivalenteAdicionar() {
		try {
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
			montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemDisciplinaEquivalenteAdicionar() {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if ((!this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia()) || (this.getCodigoMapaEquivalenciaDisciplinaVOIncluir().equals(0))) {
				setListaSelectItemDisciplinaEquivalenteAdicionar(new ArrayList(0));
				return;
			}

			MapaEquivalenciaDisciplinaVO mapa = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(this.getCodigoMapaEquivalenciaDisciplinaVOIncluir(), NivelMontarDados.TODOS);
			getFacadeFactory().getMatriculaPeriodoFacade().validarAlunoPodeCursarDisciplinaPorMapaEquivalencia(this.getMatriculaVO(), this.getMatriculaPeriodoVO(), mapa, true);

			this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setMapaEquivalenciaDisciplinaVOIncluir(mapa);
			resultadoConsulta = mapa.getMapaEquivalenciaDisciplinaCursadaVOs();
			List objs = new ArrayList(0);
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
			setListaSelectItemDisciplinaEquivalenteAdicionar(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			// Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void selecionarTurmaIncluirDisciplina() {
		try {
			selecionarTurmaIncluirDisciplina(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
		} catch (Exception e) {
			if (e instanceof ConsistirException) {
				ConsistirException consistirException = (ConsistirException) e;
				setMensagemDetalhada("msg_erro", consistirException.getToStringMensagemErro(), Uteis.ERRO);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void selecionarTurmaIncluirDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		try {
			matriculaPeriodoTurmaDisciplinaVO.setTurmaPratica(null);
			matriculaPeriodoTurmaDisciplinaVO.setTurmaTeorica(null);
			matriculaPeriodoTurmaDisciplinaVO.setSugestaoTurmaPraticaTeoricaRealizada(false);
			matriculaPeriodoTurmaDisciplinaVO.setDescricaoChoqueHorarioDisciplina("");
			TurmaVO turmaSelecionada = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaIncluirDisciplinaItens");			
			getFacadeFactory().getTurmaFacade().carregarDados(turmaSelecionada, NivelMontarDados.BASICO, getUsuarioLogado());
			matriculaPeriodoTurmaDisciplinaVO.setTurma(turmaSelecionada);
			MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO = null;
			if (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaPorEquivalencia()) {
				mapaEquivalenciaDisciplinaCursadaVO = getFacadeFactory().getMapaEquivalenciaDisciplinaCursadaFacade().consultarPorChavePrimaria(getCodigoMapaEquivalenciaDisciplinaCursar());
			}
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarSugestaoTurmaPraticaTeorica(getMatriculaPeriodoVO(), matriculaPeriodoTurmaDisciplinaVO, matriculaPeriodoTurmaDisciplinaVO.getDisciplinaPorEquivalencia() ? mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO() : matriculaPeriodoTurmaDisciplinaVO.getDisciplina(), getMatriculaVO().getCurso().getConfiguracaoAcademico(),  false, getUsuarioLogado());
			if (!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo())) {								
//				if (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaPorEquivalencia() && Uteis.isAtributoPreenchido(mapaEquivalenciaDisciplinaCursadaVO)) {					
//					getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(getMatriculaPeriodoVO(),  getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs(), turmaSelecionada.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo(), mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo(), getUsuarioLogado(), true, getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno());
//				} else {
//					getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(getMatriculaPeriodoVO(),  getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs(), turmaSelecionada.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), getUsuarioLogado(), true, getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno());
//				}
			}
			limparMensagem();
			setApresentarPanelIncluirDisciplinaRegimeEspecial(false);
		} catch (ConsistirException e) {
			if (e.getReferenteChoqueHorario()) {
				matriculaPeriodoTurmaDisciplinaVO.setSugestaoTurmaPraticaTeoricaRealizada(false);
				matriculaPeriodoTurmaDisciplinaVO.setDescricaoChoqueHorarioDisciplina(e.getToStringMensagemErro());
				if (getMatriculaVO().getCurso().getConfiguracaoAcademico().getPermitirInclusaoDiscipDependenciaComChoqueHorario()) {
					setApresentarPanelIncluirDisciplinaRegimeEspecial(true);
					throw e;	
				}
			}
			matriculaPeriodoTurmaDisciplinaVO.setTurmaPratica(null);
			matriculaPeriodoTurmaDisciplinaVO.setTurmaTeorica(null);
			matriculaPeriodoTurmaDisciplinaVO.setTurma(null);
			throw e;
		} catch (Exception e) {
			matriculaPeriodoTurmaDisciplinaVO.setTurmaPratica(null);
			matriculaPeriodoTurmaDisciplinaVO.setTurmaTeorica(null);
			matriculaPeriodoTurmaDisciplinaVO.setTurma(null);
			throw e;
		}
		// realizarMontagemQuadroHorarioTurmaDisciplina();
	}
	
	public void selecionarTurmaIncluirRenovacaoOnline(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		try {
			matriculaPeriodoTurmaDisciplinaVO.setTurmaPratica(null);
			matriculaPeriodoTurmaDisciplinaVO.setTurmaTeorica(null);
			matriculaPeriodoTurmaDisciplinaVO.setSugestaoTurmaPraticaTeoricaRealizada(false);
			matriculaPeriodoTurmaDisciplinaVO.setDescricaoChoqueHorarioDisciplina("");
			if (getListaSelectItemTurmaAdicionar().isEmpty()) {
				throw new Exception("Nenhuma turma habilitada para a inclusão da disciplina");
			}
			TurmaVO turmaSelecionada = (TurmaVO) getListaSelectItemTurmaAdicionar().get(0);			
			getFacadeFactory().getTurmaFacade().carregarDados(turmaSelecionada, NivelMontarDados.BASICO, getUsuarioLogado());
			matriculaPeriodoTurmaDisciplinaVO.setTurma(turmaSelecionada);
			MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO = null;
			if (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaPorEquivalencia()) {
				mapaEquivalenciaDisciplinaCursadaVO = getFacadeFactory().getMapaEquivalenciaDisciplinaCursadaFacade().consultarPorChavePrimaria(getCodigoMapaEquivalenciaDisciplinaCursar());
			}
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarSugestaoTurmaPraticaTeorica(getMatriculaPeriodoVO(), matriculaPeriodoTurmaDisciplinaVO, matriculaPeriodoTurmaDisciplinaVO.getDisciplinaPorEquivalencia() ? mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO() : matriculaPeriodoTurmaDisciplinaVO.getDisciplina(), getMatriculaVO().getCurso().getConfiguracaoAcademico(),  false, getUsuarioLogado());
//			if (!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo())) {								
////				if (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaPorEquivalencia() && Uteis.isAtributoPreenchido(mapaEquivalenciaDisciplinaCursadaVO)) {					
////					getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs(), turmaSelecionada.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo(), mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo(), getUsuarioLogado(), true, getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno());
////				} else {
////					getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs(), turmaSelecionada.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), getUsuarioLogado(), true, getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno());
////				}
//			}
			adicionarMatriculaPeriodoTurmaDisciplinaPorInclusao(matriculaPeriodoTurmaDisciplinaVO);
			limparMensagem();
			setApresentarPanelIncluirDisciplinaRegimeEspecial(false);
		} catch (ConsistirException e) {
			if (e.getReferenteChoqueHorario()) {
				matriculaPeriodoTurmaDisciplinaVO.setSugestaoTurmaPraticaTeoricaRealizada(false);
				matriculaPeriodoTurmaDisciplinaVO.setDescricaoChoqueHorarioDisciplina(e.getToStringMensagemErro());
				if (getMatriculaVO().getCurso().getConfiguracaoAcademico().getPermitirInclusaoDiscipDependenciaComChoqueHorario()) {
					setApresentarPanelIncluirDisciplinaRegimeEspecial(true);
					throw e;	
				}
			}
			matriculaPeriodoTurmaDisciplinaVO.setTurmaPratica(null);
			matriculaPeriodoTurmaDisciplinaVO.setTurmaTeorica(null);
			matriculaPeriodoTurmaDisciplinaVO.setTurma(null);
			throw e;
		} catch (Exception e) {
			matriculaPeriodoTurmaDisciplinaVO.setTurmaPratica(null);
			matriculaPeriodoTurmaDisciplinaVO.setTurmaTeorica(null);
			matriculaPeriodoTurmaDisciplinaVO.setTurma(null);
			throw e;
		}
		// realizarMontagemQuadroHorarioTurmaDisciplina();
	}

	public void montarListaSelectItemTurmaAdicionar(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		if (matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo().equals(0)) {
			setListaSelectItemTurmaAdicionar(new ArrayList(0));
			return;
		}
		List<TurmaVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			// Por default vamos buscar pela disciplina indica pelo usuário para
			// inclusão
			Integer disciplinaConsultarTurma = matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo();
			Integer cargaHorariaConsultarTurma = matriculaPeriodoTurmaDisciplinaVO.getCargaHorariaDisciplina();
			if (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaPorEquivalencia()) {
				if (this.getCodigoMapaEquivalenciaDisciplinaCursar().equals(0)) {
					setListaSelectItemTurmaAdicionar(new ArrayList(0));
					return;
				}
				// Caso seja, por equivalencia, entao termos que buscar turmas
				// para a disciplina equivalente selecionada.
				MapaEquivalenciaDisciplinaCursadaVO mapa = matriculaPeriodoTurmaDisciplinaVO.getMapaEquivalenciaDisciplinaVOIncluir().consultarObjMapaEquivalenciaDisciplinaCursadaVOPorCodigo(this.getCodigoMapaEquivalenciaDisciplinaCursar());
				matriculaPeriodoTurmaDisciplinaVO.setMapaEquivalenciaDisciplinaCursada(mapa);
				disciplinaConsultarTurma = mapa.getDisciplinaVO().getCodigo();
				cargaHorariaConsultarTurma = mapa.getCargaHoraria();
				if (disciplinaConsultarTurma.intValue() == 0) {
					setListaSelectItemTurmaAdicionar(new ArrayList(0));
					return;
				}
			}
			
			Integer unidadeEnsinoFiltro = isLiberadoInclusaoTurmaOutroUnidadeEnsino() ? 0 : getMatriculaVO().getUnidadeEnsino().getCodigo();
			Integer cursoFiltro = getLiberadoInclusaoTurmaOutroCurso() ? 0 : getMatriculaVO().getCurso().getCodigo();
			Integer matrizCurricularFiltro = getLiberadoInclusaoTurmaOutroMatrizCurricular() ? 0 : getMatriculaVO().getGradeCurricularAtual().getCodigo();
			Boolean apresentarRenovacaoOnline = false ; 			
			if((getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoDisciplinaPeriodoFuturo())) {
				apresentarRenovacaoOnline = !matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getPeriodoLetivoVO().getPeriodoLetivo().equals(getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo());
			}
			resultadoConsulta = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(disciplinaConsultarTurma, cargaHorariaConsultarTurma, unidadeEnsinoFiltro, "AB", false, cursoFiltro, matrizCurricularFiltro, getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), getMatriculaPeriodoVO().getAno(), getMatriculaPeriodoVO().getSemestre(), true, false, false, false, false, getUsuarioLogado(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(),apresentarRenovacaoOnline, getMatriculaVO());
			
			List objs = new ArrayList(0);
			i = resultadoConsulta.iterator();
			boolean turmaLocalizada = false;
			while (i.hasNext()) {
				TurmaVO obj = (TurmaVO) i.next();
				if (obj.getSituacao().equals("AB")) {
					turmaLocalizada = true;
					objs.add(obj);
				}
			}
			if (!turmaLocalizada) {
				throw new Exception("Não existe nenhuma turma (Aberta/Programação de Aula) disponível para esta inclusão desta disciplina.");
			}
			setListaSelectItemTurmaAdicionar(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaSelectItemTurmaAdicionar(new ArrayList(0));
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarPorDisciplinaSituacaoNrVagas() throws Exception {
		DisciplinaVO obj = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoGradeDisciplina(gradeDisciplinaAdicionar, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina(obj);
		List listaResultado = getFacadeFactory().getTurmaFacade().consultarPorDisciplinaSituacaoNrVagasIncluindoTurmasLotadas(obj.getCodigo(), "AB", getMatriculaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return listaResultado;
	}

	public Boolean getApresentarDisciplinasIncluir() {
		if (!getListaSelectItemDisciplinaAdicionar().isEmpty()) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarDisciplinasEquivalentesIncluir() {
		if (apresentarDisciplinasEquivalentesIncluir == null) {
			apresentarDisciplinasEquivalentesIncluir = false;
		}
		return apresentarDisciplinasEquivalentesIncluir;
	}

	public void montarListaSelectItemDisciplina() {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			setListaSelectItemTurmaAdicionar(new ArrayList(0));
			if (getPeriodoLetivoAdicionar().intValue() == 0) {
				setListaSelectItemDisciplinaAdicionar(new ArrayList(0));
				return;
			}
			resultadoConsulta = consultarDisciplinaPorPeriodoLetivo();
			List objs = new ArrayList(0);
			i = resultadoConsulta.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				GradeDisciplinaVO obj = (GradeDisciplinaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDisciplina().getNome()));
			}
			setListaSelectItemDisciplinaAdicionar(objs);
		} catch (Exception e) {
			setListaSelectItemDisciplinaAdicionar(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarDisciplinaPorPeriodoLetivo() throws Exception {
		List listaResultado = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getPeriodoLetivoAdicionar(), false, getUsuarioLogado(), null);
		return listaResultado;
	}

	public String getDescricaoListaDisciplinasPeriodoLetivoAlunoEstaCursando() {
		if (getListaDisciplinasPeriodoLetivoAlunoEstaCursando().isEmpty()) {
			return "Não há disciplina que o aluno esteja cursando neste período";
		}
		return "";
	}

	public String getDescricaoListaDisciplinasPeriodoLetivoAlunoJaAprovado() {
		if (getListaDisciplinasPeriodoLetivoAlunoJaAprovado().isEmpty()) {
			return "Não há disciplina realizada neste período";
		}
		return "";
	}

	public String getDescricaoListaDisciplinasPeriodoLetivoAlunoPendente() {
		if (getListaDisciplinasPeriodoLetivoAlunoPendente().isEmpty()) {
			return "Não há disciplina pendente neste período";
		} else {
			String cargaHorariaPendenteStr = "";
			String cargaHorariaPendenteGrupoOptativaStr = "";

			cargaHorariaPendenteStr = "CH Ofertada: " + matriculaPeriodoVO.getSaldoCHPendenteAlunoAtePeriodoAnterior() + "h";

			Integer saldoCHGrupoOptativa = matriculaPeriodoVO.getSaldoCHPendenteAlunoGrupoOptativaAtePeriodoAnterior();
			Integer saldoCHRegulares = matriculaPeriodoVO.getSaldoCHPendenteAlunoAtePeriodoAnterior() - saldoCHGrupoOptativa;
			if (saldoCHGrupoOptativa > 0) {
				cargaHorariaPendenteGrupoOptativaStr = " (Regulares: " + saldoCHRegulares + "h - Eletivas:" + saldoCHGrupoOptativa + "h)";
			}
			// } else {
			// PeriodoLetivoComHistoricoAlunoVO periodoComHistorico =
			// matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina);
			// if (periodoComHistorico != null) {
			// cargaHorariaPendenteStr = "CH Pendente: " +
			// periodoComHistorico.getCargaHorariaPendenteAlunoPeriodo() + "h ";
			// Integer cargaHorariaPendenteGrupoOptativa =
			// periodoComHistorico.getCargaHorariaPendentePeriodoComRelacaoGrupoOptativa();
			// cargaHorariaPendenteGrupoOptativa =
			// cargaHorariaPendenteGrupoOptativa -
			// matriculaPeriodoVO.calcularCargaHorariaDisciplinasGrupoOptativaPeriodoLetivo(periodoComHistorico.getPeriodoLetivoVO().getPeriodoLetivo());
			// if (cargaHorariaPendenteGrupoOptativa > 0) {
			// cargaHorariaPendenteGrupoOptativaStr =
			// "  -  CH Pendente Grupo Optativa: " +
			// periodoComHistorico.getCargaHorariaPendenteAlunoPeriodo() + "h ";
			// }
			// } else {
			// cargaHorariaPendenteStr = "";
			// cargaHorariaPendenteGrupoOptativaStr = "";
			// }
			// }
			return cargaHorariaPendenteStr + cargaHorariaPendenteGrupoOptativaStr;
		}
	}

	public void gerarDadosGraficoEvolucaoAcademicaAluno() throws Exception {
		getMatriculaVO().getMatriculaComHistoricoAlunoVO().gerarDadosGraficoEvolucaoAcademicaAluno();
	}

	public void sinalizarDisciplinasPendentesJaIncluidasParaNaoPermitirNovaInclusao() {
		for (GradeDisciplinaVO gradeDisciplina : getListaDisciplinasPeriodoLetivoAlunoPendente()) {
			gradeDisciplina.setJaIncluidaRenovacao(false);
			q: for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTumaDisciplinaVO : matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs()) {
				if (matriculaPeriodoTumaDisciplinaVO.getDisciplinaEquivale()) {
					for (MapaEquivalenciaDisciplinaMatrizCurricularVO medmcVO : matriculaPeriodoTumaDisciplinaVO.getMapaEquivalenciaDisciplinaVOIncluir().getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
						if (gradeDisciplina.getDisciplina().getCodigo().equals(medmcVO.getDisciplinaVO().getCodigo())) {
							gradeDisciplina.setJaIncluidaRenovacao(true);
							gradeDisciplina.setGradeDisciplinaOfertada(true);
							break q;
						}
					}
				} else {
					if (gradeDisciplina.getDisciplina().getCodigo().equals(matriculaPeriodoTumaDisciplinaVO.getDisciplina().getCodigo())) {
						gradeDisciplina.setJaIncluidaRenovacao(true);
						gradeDisciplina.setGradeDisciplinaOfertada(true);
						break;
					}
				}
			}
		}
	}

	/**
	 * Remove da lista de cursando disciplinas que estao incluidas na
	 * MatriculaPeriodo que está sendo editada. Para nao gerar a sensação no
	 * usuário de duplicidade, pois estas disciplinas já são listadas como
	 * disciplinas do período da renovação. Este problema ocorre somente durante
	 * a edição de uma matrícula, pois neste momento, já existem historicos
	 * gerados para estas disciplinas que sao percebidos pela rotina que monta
	 * MatriculaComHistoricoAlunoVO
	 */
	public void removerDisciplinasPeriodoLetivoAlunoEstaCursandoDoProprioPeriodoRenovacao() throws Exception {
		if (matriculaPeriodoVO.getIsNovaMatriculaPeriodo()) {
			// para uma nova matricula nao temos que fazer nada, somente para
			// uma edicao
			// é necessário esta preocupação.
			return;
		}
		int i = getListaDisciplinasPeriodoLetivoAlunoEstaCursando().size() - 1;
		while (i >= 0) {
			HistoricoVO historicoCursando = getListaDisciplinasPeriodoLetivoAlunoEstaCursando().get(i);
			for (MatriculaPeriodoTurmaDisciplinaVO disciplinaRenovacao : matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs()) {
				if (historicoCursando.getDisciplina().getCodigo().equals(disciplinaRenovacao.getDisciplina().getCodigo())) {
					getListaDisciplinasPeriodoLetivoAlunoEstaCursando().remove(i);
					break;
				}
			}
			i--;
		}
	}

	/**
	 * Método responsável por controlar se durante a edição de uma
	 * matriculaperiodo o usuário exclui alguma matriculaPeriodoTurmaDisciplina.
	 * Se fizer isto, temos que adicionar esta disciplina para lista de
	 * pendências. De forma que o usuário possa adicioná-la novamente para o
	 * aluno, caso o mesmo deseje fazer isto.
	 */
	public void adicionarDisciplinasRemovidasPeloUsuarioDuranteEdicaoMatricula(Integer periodoLetivoAdicionar) {
		if ((!matriculaPeriodoVO.getIsNovaMatriculaPeriodo()) && (!getListaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs().isEmpty())) {
			for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : getListaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs()) {
				boolean periodoValidado = false;
				Integer periodoLetivoMatricula = matriculaPeriodoVO.getPeridoLetivo().getPeriodoLetivo() - 1;
				if (periodoLetivoMatricula <= 0) {
					periodoLetivoMatricula = 1;
				}
				if ((periodoLetivoAdicionar.equals(0) || periodoLetivoAdicionar < 0) && (periodoLetivoAdicionar < 0 || matriculaPeriodoTurmaDisciplinaVO.getTurma().getPeridoLetivo().getPeriodoLetivo().compareTo(periodoLetivoMatricula) <= 0)) {
					// periodoLetivoAdicionar é por que o usuário está vendo
					// todas pendentes até o periodo anterior.
					// Logo temos que adicionar somente se a disciplina excluida
					// for de uma periodo menor que o atual da
					// matriculaPeriodo
					periodoValidado = true;
				} else {
					if ((periodoLetivoAdicionar != 0) && (matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getPeriodoLetivoVO().getCodigo().equals(periodoLetivoAdicionar))) {
						periodoValidado = true;
					}
				}
				if ((periodoValidado) && (!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa())) {
					adicionarGradeDisciplinaListaDisciplinasPendentes(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO());
				}
			}
		}
	}

	public void adicionarGradeDisciplinaListaDisciplinasPendentes(GradeDisciplinaVO gradeDisciplinaVO) {
		for (GradeDisciplinaVO gradeDisciplinaExisnte : getListaDisciplinasPeriodoLetivoAlunoPendente()) {
			if (gradeDisciplinaExisnte.getCodigo().equals(gradeDisciplinaVO.getCodigo())) {
				// se entrar aqui, já existe, entao sai sem fazer nada;
				return;
			}
		}
		getListaDisciplinasPeriodoLetivoAlunoPendente().add(gradeDisciplinaVO);
	}

	public void atualizarListaDisciplinasHistoricoPeriodoLetivoSelecionado() throws Exception {
		limparMensagem();
		// total será utilizado para permitir incluir disciplinas de grupo de
		// optativas de periodos anteriores que o aluno esteja devendo
		setTotalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior(matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().calcularTotalCargaHorariaPendenteAlunoGrupoOptativaAteDeterminadoPeriodoMatrizCurricular(matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() - 1, true));
		if (getPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina().intValue() == -1) {
			setListaDisciplinasPeriodoLetivoAlunoJaAprovado(new ArrayList(0));
			setListaDisciplinasPeriodoLetivoAlunoEstaCursando(matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAlunoCursandoGradeCurricular());
			Integer nrPeriodoLetivo = 0;
			if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoDisciplinaPeriodoFuturo()) {
				if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina() > 0) {
					nrPeriodoLetivo = matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() + getMatriculaVO().getCurso().getConfiguracaoAcademico().getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina();
					if(!matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().isEmpty() && nrPeriodoLetivo > matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().get(matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().size()-1).getPeriodoLetivo()) {
						nrPeriodoLetivo =  matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().get(matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().size()-1).getPeriodoLetivo();
					}
				}else {
					nrPeriodoLetivo = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().isEmpty() ? 0 : matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().get(matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().size()-1).getPeriodoLetivo();
				}
				
			}else {
				nrPeriodoLetivo = matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() - 1 > 1 ? matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() - 1 : 1;
				if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia() && getMatriculaVO().getCurso().getConfiguracaoAcademico().isHabilitarDistribuicaoDisciplinaDependenciaAutomatica()) {
					nrPeriodoLetivo = matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() > 1 ? matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() : 1;
				}	
			}
			
			if(Uteis.isAtributoPreenchido(matriculaVO.getTransferenciaEntrada().getCodigo())) {
				  setListaDisciplinasPeriodoLetivoAlunoPendente(matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getListaGradeDisciplinaVOsPendentesGradeCurricularValidandoDisciplinaAprovadoNaGradeAntigaReferenteAtransferenciaInternaMatricula(nrPeriodoLetivo));
			}else {
			  setListaDisciplinasPeriodoLetivoAlunoPendente(matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getListaGradeDisciplinaVOsPendentesGradeCurricular(nrPeriodoLetivo));
			}
			adicionarDisciplinasRemovidasPeloUsuarioDuranteEdicaoMatricula(getPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina());
			removerDisciplinasPeriodoLetivoAlunoEstaCursandoDoProprioPeriodoRenovacao();
		}else if (getPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina().intValue() == 0) {
			// Se igual a zero é por que o usuário quer ver todos os períodos
			// letivos. Assim, nao listamos
			// todas as disciplinas na qual ele foi aprovado pois a lista é
			// muito grande.
			setListaDisciplinasPeriodoLetivoAlunoJaAprovado(new ArrayList(0));
			setListaDisciplinasPeriodoLetivoAlunoEstaCursando(matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAlunoCursandoGradeCurricular());
			Integer nrPeriodoLetivo = matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() - 1 > 1 ? matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() - 1 : 1;
			if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia() && getMatriculaVO().getCurso().getConfiguracaoAcademico().isHabilitarDistribuicaoDisciplinaDependenciaAutomatica()) {
				nrPeriodoLetivo = matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() > 1 ? matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() : 1;
			}	
			
			if(Uteis.isAtributoPreenchido(matriculaVO.getTransferenciaEntrada().getCodigo())) {
				  setListaDisciplinasPeriodoLetivoAlunoPendente(matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getListaGradeDisciplinaVOsPendentesGradeCurricularValidandoDisciplinaAprovadoNaGradeAntigaReferenteAtransferenciaInternaMatricula(nrPeriodoLetivo));
			}else {
			  setListaDisciplinasPeriodoLetivoAlunoPendente(matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getListaGradeDisciplinaVOsPendentesGradeCurricular(nrPeriodoLetivo));
			}
			adicionarDisciplinasRemovidasPeloUsuarioDuranteEdicaoMatricula(getPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina());
			removerDisciplinasPeriodoLetivoAlunoEstaCursandoDoProprioPeriodoRenovacao();
		} else {
			PeriodoLetivoComHistoricoAlunoVO periodoComHistorico = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina);
			if (periodoComHistorico != null) {
				setListaDisciplinasPeriodoLetivoAlunoJaAprovado(periodoComHistorico.getHistoricosDisciplinasAprovadasAlunoPeriodoLetivo());
				setListaDisciplinasPeriodoLetivoAlunoEstaCursando(periodoComHistorico.getHistoricosDisciplinasAlunoCursandoPeriodoLetivo());
				setListaDisciplinasPeriodoLetivoAlunoPendente(periodoComHistorico.getDisciplinasPendentesAlunoPeriodoLetivo());
			} else {
				setListaDisciplinasPeriodoLetivoAlunoJaAprovado(new ArrayList(0));
				setListaDisciplinasPeriodoLetivoAlunoEstaCursando(new ArrayList(0));
				setListaDisciplinasPeriodoLetivoAlunoPendente(new ArrayList(0));
			}
			adicionarDisciplinasRemovidasPeloUsuarioDuranteEdicaoMatricula(getPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina());
			removerDisciplinasPeriodoLetivoAlunoEstaCursandoDoProprioPeriodoRenovacao();
		}
		sinalizarDisciplinasPendentesJaIncluidasParaNaoPermitirNovaInclusao();

		// ANTES
		// setListaDisciplinasPeriodoLetivoAlunoJaAprovado(matriculaPeriodoVO.getListaDisciplinasPeriodoLetivoAlunoJaAprovado(periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina));
		// ANTES
		// setListaDisciplinasPeriodoLetivoAlunoPendente(matriculaPeriodoVO.getListaDisciplinasPeriodoLetivoAlunoPendente(periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina));

		/**
		 * EDIGAR - COMENTEI NA VERSÃO 5.0 ESTE MONTAR DADO ABAIXO, POIS NAO
		 * CONSEGUI IDENTIFICAR A RAZAO PARA MONTAR A
		 * MATRICULAPERIODOTURMADISCIPLINA DE CADA HISTORICO (POIS AS
		 * INFORMACOES SOBRE COMO O ALUNO FOI APROVADO NA DISCIPLINA PODEM SER
		 * OBTIDAS NO PROPRIO HISTORICO) Iterator i =
		 * getListaDisciplinasPeriodoLetivoAlunoJaAprovado().iterator(); while
		 * (i.hasNext()) { HistoricoVO hist = (HistoricoVO) i.next(); if
		 * (hist.getMatriculaPeriodoTurmaDisciplina().getCodigo() != 0) {
		 * hist.setMatriculaPeriodoTurmaDisciplina
		 * (getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade
		 * ().consultarPorChavePrimaria
		 * (hist.getMatriculaPeriodoTurmaDisciplina().getCodigo(),
		 * Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado())); } }
		 **/
	}

	public void inicializarCargaHorariaCreditosDisciplinasPendentesAtePeriodoAnterior(PeriodoLetivoVO periodoAnterior) throws Exception {
		ConfiguracaoAcademicoVO cfg = getMatriculaVO().getCurso().getConfiguracaoAcademico();
		// mesmo que o usuario nao esteja fazendo este controle, iremos gerar
		// este dados com intuito
		// de ser uma informacao adicional ao usuário. Desta maneira, caso o
		// controle esteja desativado,
		// temos que o valor default será retornado pelo método abaixo.
		String controlarPorCreditoOuCH = cfg.getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH();

		Integer totalPendente = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().calcularTotalCargaHorariaCreditoPendenteAlunoAteDeterminadoPeriodoMatrizCurricular(periodoAnterior.getPeriodoLetivo(), true, controlarPorCreditoOuCH);
		setTotalCHCrPendenteAlunoAtePeriodoAnterior(totalPendente);

		if (getMatriculaPeriodoVO().getPeriodoLetivo().getPrimeiro() && ((!Uteis.isAtributoPreenchido(getMatriculaPeriodoVO()) && getMatriculaVO().getMatriculaPeriodoVOs().isEmpty()) || (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO()) && getMatriculaVO().getMatriculaPeriodoVOs().size() == 1))) {
			if (getMatriculaVO().getFormaIngresso().equals(FormaIngresso.VESTIBULAR.getValor()) || getMatriculaVO().getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()) || getMatriculaVO().getFormaIngresso().equals(FormaIngresso.PROUNI.getValor()) || getMatriculaVO().getFormaIngresso().equals(FormaIngresso.ENEM.getValor())) {		
					getMatriculaPeriodoVO().setAlunoRegularMatrizCurricular(true);		
			} else {
				getMatriculaPeriodoVO().setAlunoRegularMatrizCurricular(false);
			}
			getMatriculaPeriodoVO().setNrDisciplinasPendentesPeriodosAnteriores(0);
			getMatriculaPeriodoVO().setNrDisciplinasPendentesDevemSerIncluidas(0);
		} else {
			if (getMatriculaVO().getCurso().getConfiguracaoAcademico().getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH() && getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CH")
			// &&
			// getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCreditoAlunoPodeCursar()
			// > 0
			) {
				getMatriculaPeriodoVO().setTipoContabilizacaoDisciplinaDependencia(TipoContabilizacaoDisciplinaDependenciaEnum.CARGA_HORARIA);
			} else if (getMatriculaVO().getCurso().getConfiguracaoAcademico().getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH() && getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR")
			// &&
			// getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCreditoAlunoPodeCursar()
			// > 0
			) {
				getMatriculaPeriodoVO().setTipoContabilizacaoDisciplinaDependencia(TipoContabilizacaoDisciplinaDependenciaEnum.CREDITO);
			} else if (!getMatriculaVO().getCurso().getConfiguracaoAcademico().getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH()) {
				getMatriculaPeriodoVO().setTipoContabilizacaoDisciplinaDependencia(TipoContabilizacaoDisciplinaDependenciaEnum.QTDE_DISCIPLINA);
			}
			Integer nrDisciplinasPendentesAtePeriodoAnterior = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().calcularNrDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(periodoAnterior.getPeriodoLetivo(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getConsiderarRegularAlunoDependenciaOptativa());
			getMatriculaPeriodoVO().setNrDisciplinasPendentesPeriodosAnteriores(nrDisciplinasPendentesAtePeriodoAnterior);
			if (nrDisciplinasPendentesAtePeriodoAnterior == 0 && getMatriculaPeriodoVO().getNrDisciplinasIncluidasPeriodosAnteriores() == 0 
					&& (!getMatriculaVO().getCurso().getConfiguracaoAcademico().getConsiderarPortadoDiplomaTransEntradaAlunoIrregular() 
							|| (getMatriculaVO().getCurso().getConfiguracaoAcademico().getConsiderarPortadoDiplomaTransEntradaAlunoIrregular() 
									&& !getMatriculaVO().getFormaIngresso().equals(FormaIngresso.PORTADOR_DE_DIPLOMA.getValor()) 
									&& !getMatriculaVO().getFormaIngresso().equals(FormaIngresso.TRANSFERENCIA_EXTERNA.getValor()) 
									&& !getMatriculaVO().getFormaIngresso().equals(FormaIngresso.TRANSFERENCIA_EXTERNA_OFICIO.getValor())))) {
				getMatriculaPeriodoVO().setAlunoRegularMatrizCurricular(true);
			} else {
				getMatriculaPeriodoVO().setAlunoRegularMatrizCurricular(false);
			}

			if ((cfg.getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia()) && (cfg.getPorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia() > 0.0) && (!getMatriculaPeriodoVO().getLiberadoControleInclusaoObrigatoriaDisciplinaDependencia())) {

				if (getMatriculaPeriodoVO().getTipoContabilizacaoDisciplinaDependencia().equals(TipoContabilizacaoDisciplinaDependenciaEnum.QTDE_DISCIPLINA)) {
					double nrDisciplinasMinimasIncluir = (((double) cfg.getPorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia() / 100) * (double) nrDisciplinasPendentesAtePeriodoAnterior);
					getMatriculaPeriodoVO().setNrDisciplinasPendentesDevemSerIncluidas((int) nrDisciplinasMinimasIncluir);
					if (getMatriculaPeriodoVO().getNrDisciplinasPendentesDevemSerIncluidas() > 0) {
						this.setApresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia(Boolean.TRUE);
					}
				} else if (getMatriculaPeriodoVO().getTipoContabilizacaoDisciplinaDependencia().equals(TipoContabilizacaoDisciplinaDependenciaEnum.CREDITO)) {
					//Integer nrDisciplinasMinimasIncluir = getMatriculaPeriodoVO().getTotalCreditoPendenteAlunoAtePeriodoAnterior();
					Integer nrDisciplinasMinimasIncluir = getTotalCHCrPendenteAlunoAtePeriodoAnterior();
					if (!getMatriculaPeriodoVO().isNovoObj() || getMatriculaVO().getCurso().getConfiguracaoAcademico().getConsiderarRegularAlunoDependenciaOptativa()) {
						nrDisciplinasMinimasIncluir = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().calcularNrCreditoDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(periodoAnterior.getPeriodoLetivo(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getConsiderarRegularAlunoDependenciaOptativa());
						nrDisciplinasMinimasIncluir += getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().calcularNrCreditoIncluidaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricularJaGravada(periodoAnterior.getPeriodoLetivo(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getConsiderarRegularAlunoDependenciaOptativa(), getMatriculaPeriodoVO().getAno(), getMatriculaPeriodoVO().getSemestre());
					}
					if(getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCreditoAlunoPodeCursar() > 0) {
						nrDisciplinasMinimasIncluir = getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCreditoAlunoPodeCursar() - getMatriculaPeriodoVO().getTotalCreditoPadraoMatriculaPeriodo(); 
					}
					nrDisciplinasMinimasIncluir = (cfg.getPorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia() * nrDisciplinasMinimasIncluir) / 100;
					if (nrDisciplinasMinimasIncluir > getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCreditoAlunoPodeCursar()) {
						getMatriculaPeriodoVO().setNrCreditoPendentesDevemSerIncluidas(getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCreditoAlunoPodeCursar());
					} else {
						getMatriculaPeriodoVO().setNrCreditoPendentesDevemSerIncluidas(nrDisciplinasMinimasIncluir);
					}
					this.setApresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia(getMatriculaPeriodoVO().getNrCreditoPendentesDevemSerIncluidas() > 0);
				} else if (getMatriculaPeriodoVO().getTipoContabilizacaoDisciplinaDependencia().equals(TipoContabilizacaoDisciplinaDependenciaEnum.CARGA_HORARIA)) {
					//Integer nrDisciplinasMinimasIncluir = getMatriculaPeriodoVO().getTotalCargaHorariaAlunoAtePeriodoAnterior();
					Integer nrDisciplinasMinimasIncluir = getTotalCHCrPendenteAlunoAtePeriodoAnterior();
					if (!getMatriculaPeriodoVO().isNovoObj() || getMatriculaVO().getCurso().getConfiguracaoAcademico().getConsiderarRegularAlunoDependenciaOptativa()) {
						nrDisciplinasMinimasIncluir = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().calcularNrCargaHorariaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(periodoAnterior.getPeriodoLetivo(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getConsiderarRegularAlunoDependenciaOptativa());
						nrDisciplinasMinimasIncluir += getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().calcularNrCargaHorariaIncluidaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricularJaGravada(periodoAnterior.getPeriodoLetivo(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getConsiderarRegularAlunoDependenciaOptativa(), getMatriculaPeriodoVO().getAno(), getMatriculaPeriodoVO().getSemestre());
					}
					if(getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCargaHorariaAlunoPodeCursar() > 0) {
						nrDisciplinasMinimasIncluir = getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCargaHorariaAlunoPodeCursar() - getMatriculaPeriodoVO().getTotalCargaHorariaAlunoMatriculaPeriodo();
						if(nrDisciplinasMinimasIncluir < 0) {
							nrDisciplinasMinimasIncluir =  0;
						}
					}
					nrDisciplinasMinimasIncluir = (cfg.getPorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia() * nrDisciplinasMinimasIncluir) / 100;
					if (nrDisciplinasMinimasIncluir > getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCargaHorariaAlunoPodeCursar()) {
						getMatriculaPeriodoVO().setNrCargaHorariaPendentesDevemSerIncluidas(getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCargaHorariaAlunoPodeCursar());
					} else {
						getMatriculaPeriodoVO().setNrCargaHorariaPendentesDevemSerIncluidas(nrDisciplinasMinimasIncluir);
					}
					this.setApresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia(getMatriculaPeriodoVO().getNrCargaHorariaPendentesDevemSerIncluidas() > 0);
				}

			}			
			//atualizarListaDisciplinasHistoricoPeriodoLetivoSelecionado();
			realizarVerificacaoDeLiberacaoDisciplinaDenpendenteInclusaoForaPrazo(cfg);
		}
	}

	private void realizarVerificacaoDeLiberacaoDisciplinaDenpendenteInclusaoForaPrazo(ConfiguracaoAcademicoVO cfg) {
		boolean existeDisciplinaRegular = false; 
		for (TurmaDisciplinaVO td : getMatriculaPeriodoVO().getTurma().getTurmaDisciplinaVOs()) {
			if(getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().stream().anyMatch(p-> p.getDisciplina().getCodigo().equals(td.getDisciplina().getCodigo()))) {
				existeDisciplinaRegular = true;
				break;
			}
		}		
		if(!getMatriculaPeriodoVO().getPossuiPermissaoInclusaoExclusaoDisciplina()) {
			getMatriculaPeriodoVO().setPossuiPermissaoInclusaoExclusaoDisciplina(!existeDisciplinaRegular && cfg.isAlunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo());	
		}
	}

//	private void realizarVerificacaoDeDistribuicaoDisciplinaDependenciaAutomatica(ConfiguracaoAcademicoVO cfg) throws Exception {
//		if(cfg.getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia() && cfg.isHabilitarDistribuicaoDisciplinaDependenciaAutomatica()) {
//			for (GradeDisciplinaVO gdPendente : getListaDisciplinasPeriodoLetivoAlunoPendente()) {
//				Integer unidadeEnsinoFiltro = isLiberadoInclusaoTurmaOutroUnidadeEnsino() ? 0 : getMatriculaVO().getUnidadeEnsino().getCodigo();
//				Integer cursoFiltro = getLiberadoInclusaoTurmaOutroCurso() ? 0 : getMatriculaVO().getCurso().getCodigo();
//				Integer matrizCurricularFiltro = getLiberadoInclusaoTurmaOutroMatrizCurricular() ? 0 : getMatriculaVO().getGradeCurricularAtual().getCodigo();
//				if ((getMatriculaPeriodoVO().getTipoContabilizacaoDisciplinaDependencia().equals(TipoContabilizacaoDisciplinaDependenciaEnum.CREDITO) && gdPendente.getNrCreditos() > getMatriculaPeriodoVO().getSaldoCreditoDisponivelInclusaoDisplinas())
//						|| (getMatriculaPeriodoVO().getTipoContabilizacaoDisciplinaDependencia().equals(TipoContabilizacaoDisciplinaDependenciaEnum.CARGA_HORARIA) && gdPendente.getCargaHoraria() > getMatriculaPeriodoVO().getSaldoCHDisponivelInclusaoDisplinas())) {
//					gdPendente.setGradeDisciplinaOfertada(getFacadeFactory().getTurmaFacade().consultaSeExisteTurmaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(gdPendente.getDisciplina().getCodigo(), gdPendente.getCargaHoraria(), unidadeEnsinoFiltro, "AB", false, cursoFiltro, matrizCurricularFiltro, getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), getMatriculaPeriodoVO().getAno(), getMatriculaPeriodoVO().getSemestre(), true, false, false, false, false, getUsuarioLogado(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada()));
//				}else {
//					List<TurmaVO> listaTurmaOfertada = getFacadeFactory().getTurmaFacade().consultaTurmaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(gdPendente.getDisciplina().getCodigo(), gdPendente.getCargaHoraria(), unidadeEnsinoFiltro, "AB", false, cursoFiltro, matrizCurricularFiltro, getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), getMatriculaPeriodoVO().getAno(), getMatriculaPeriodoVO().getSemestre(), true, false, false, false, false, getUsuarioLogado(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada());
//					if (!listaTurmaOfertada.isEmpty()) {
//						gdPendente.setGradeDisciplinaOfertada(true);
//						Optional<TurmaVO> optionalTurma = listaTurmaOfertada.stream().filter(p -> p.getUnidadeEnsino().getCodigo().equals(getMatriculaVO().getUnidadeEnsino().getCodigo())).findFirst();
//						TurmaVO turmaSelecionada = optionalTurma.isPresent() ? optionalTurma.get() : listaTurmaOfertada.get(0);
//						MatriculaPeriodoTurmaDisciplinaVO novoObj = new MatriculaPeriodoTurmaDisciplinaVO();
//						novoObj.setDisciplina(gdPendente.getDisciplina());
//						novoObj.setGradeDisciplinaVO(gdPendente);
//						novoObj.setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
//						novoObj.setTurmaPratica(null);
//						novoObj.setTurmaTeorica(null);
//						novoObj.setSugestaoTurmaPraticaTeoricaRealizada(false);
//						novoObj.setDescricaoChoqueHorarioDisciplina("");
//						getFacadeFactory().getTurmaFacade().carregarDados(turmaSelecionada, NivelMontarDados.TODOS, getUsuarioLogado());
//						novoObj.setTurma(turmaSelecionada);
//						for (TurmaDisciplinaVO td : novoObj.getTurma().getTurmaDisciplinaVOs()) {
//							if (novoObj.getDisciplinaPorEquivalencia() && novoObj.getMapaEquivalenciaDisciplinaCursada().getDisciplinaVO().getCodigo().equals(td.getDisciplina().getCodigo())) {
//								novoObj.setModalidadeDisciplina(td.getModalidadeDisciplina());
//							} else if (!novoObj.getDisciplinaPorEquivalencia() && td.getDisciplina().getCodigo().intValue() == novoObj.getDisciplina().getCodigo().intValue()) {
//								novoObj.setModalidadeDisciplina(td.getModalidadeDisciplina());
//								break;
//							}
//						}
//						getFacadeFactory().getMatriculaPeriodoFacade().adicionarEValidarMatriculaPeriodoTurmaDisciplinaVO(getMatriculaPeriodoVO(), getMatriculaVO(), getPermitirRealizarMatriculaDisciplinaPreRequisito(), novoObj,  false, getUsuarioLogado());
//					}
//				}
//			}
//		}
//	}

	@SuppressWarnings({ "static-access", "static-access" })
	public String navegarAbaDisciplinas() throws Exception {
		try {
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "navegarAbaDisciplinas"+ getMatriculaVO().getMatricula());
			if(getGuiaAba().equals("Documentacao")){
				setOncompleteModal("");
				realizarValidacaoDadosAlunoEnadeCenso();
				if(getAlunoPossuiPendenciaEnadeCenso()) {
					setOncompleteModal("PF('panelAvisoVerificarDadosEnadeCenso').show()");
					return "";
				}
			}
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "navegarAbaDisciplinas1"+ getMatriculaVO().getMatricula());
			if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
				if(getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && getMatriculaVO().getCurso().getConfiguracaoAcademico().getPermitirAlunoRegularIncluirDisciplinaGrupoOptativa()) {
					setLiberadoInclusaoOptativa(true);
				}else if(!getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia()){
					setLiberadoInclusaoOptativa(true);
				}
			}
			getMatriculaPeriodoVO().getMensagemAvisoUsuario().getListaMensagemErro().clear();
			if (getGuiaAba().equals("Inicio") || getGuiaAba().equals("LiberadaMatricula")) {
				getFacadeFactory().getMatriculaPeriodoFacade().validarMatriculaPeriodoPodeSerRealizada(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
			}
			verificarAlunoAceitouTermoAceiteRenovacaoMatricula();
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "navegarAbaDisciplinas2"+ getMatriculaVO().getMatricula());
            getFacadeFactory().getMatriculaFacade().realizarVerificacaoELiberacaoSuspensaoMatricula(getMatriculaVO().getMatricula());			
			if ((this.getRenovandoMatricula()) && (getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaDocumentacao())) {
				// significa que o usuario está na aba de documentacao e está
				// tentando avançar.
				// assim, fazemos a verificacao de documentacao. Caso tenha algo
				// que impeça
				// a renovacao, então uma exceção deve ser gerada e o usuário
				// deve ser mantido
				// na mesma aba.
				getFacadeFactory().getMatriculaFacade().verificarDocumentaoImpediRenovacaoEstaPendente(getMatriculaVO(), getUsuarioLogado());
			}
			setApresentarModalAvisoAlunoReprovado(Boolean.FALSE);
			setMensagemModalAvisoAlunoReprovado("");
			montarListaSelectItemPeriodoLetivoInclusaoDisciplina();
			/*
			 * Alterado por: Wendel Rodrigues - 09/01/2015 Impedir a renovação
			 * pela visão do Aluno caso o mesmo possua Contas a Receber em
			 * atraso.
			 */
//			if(!getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getRegerarFinanceiro() && getPermitirRegerarFinanceiro()) {
//				getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setRegerarFinanceiro(getPermitirRegerarFinanceiro());
//			}
//			if (!Uteis.isAtributoPreenchido(getBanner()) && (getMatriculaPeriodoVO().getCodigo().equals(0) && !getRenovandoMatriculaDaVisaoDoAluno()) || (getMatriculaPeriodoVO().getCodigo().equals(0) && !getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getPermiteRenovarComParcelaVencida() && getRenovandoMatriculaDaVisaoDoAluno())) {
//				setListaContaReceber(getFacadeFactory().getContaReceberFacade().consultaRapidaPorAlunoEMatriculaContasReceberVencidas(getMatriculaVO().getAluno().getCodigo(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()).getUtilizarIntegracaoFinanceira(), getMatriculaPeriodoVO(), getUsuarioLogado()));
//				if ((getListaContaReceber().isEmpty()) || (this.getLiberarRenovacaoComDebitosFinanceiros())) {
//					setGuiaAba("Disciplinas");
//				} else {
//					if (getMatriculaVO().getDataLiberacaoPendenciaFinanceira() != null) {
//						if (getMatriculaVO().getDataLiberacaoPendenciaFinanceira().after(Uteis.obterDataAntiga(new Date(), 2))) {
//							this.liberarRenovacaoComDebitosFinanceiros = true;
//							setGuiaAba("Documentacao");
//						}
//					} else if (getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaPendenciaFinanceira()) {
//						setGuiaAba("PendenciaFinanceira");
//					} else {
//						throw new Exception(UteisJSF.internacionalizar("msg_RenovacaoMatricula_erroPermissaoAbaFinanceiro"));
//					}
//				}
//			} else {
				setGuiaAba("Disciplinas");
//			}
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "navegarAbaDisciplinas3"+ getMatriculaVO().getMatricula());
			verificarPermissaoAlterarDadosAcademicos();
			PeriodoLetivoVO periodoAnterior = null;
			if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia() && getMatriculaVO().getCurso().getConfiguracaoAcademico().isHabilitarDistribuicaoDisciplinaDependenciaAutomatica()) {
				periodoAnterior = getMatriculaPeriodoVO().getPeriodoLetivo();
			}else {
				periodoAnterior = getFacadeFactory().getMatriculaPeriodoFacade().executarObterPeriodoLetivoAnteriorAoPeriodoLetivoMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO());
			}
			
			
			
			if(getMatriculaVO().getCurso().getConfiguracaoAcademico().isHabilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares()) {
				
				getFacadeFactory().getMatriculaPeriodoFacade()
						.inicializarDadosDefinirDisciplinasDependenciaFuturasMatriculaPeriodo(getMatriculaVO(),
								getMatriculaPeriodoVO(), getListaDisciplinasPeriodoLetivoAlunoPendente(),
								periodoAnterior, getPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina(),
								isLiberadoInclusaoTurmaOutroUnidadeEnsino(), getLiberadoInclusaoTurmaOutroCurso(),
								getLiberadoInclusaoTurmaOutroMatrizCurricular(),
								getPermitirRealizarMatriculaDisciplinaPreRequisito(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoDisciplinaPeriodoFuturo(), 
								getUsuarioLogadoClone());
				
				/*
				 * if((getMatriculaVO().getCurso().getConfiguracaoAcademico().
				 * getHabilitarControleInclusaoDisciplinaPeriodoFuturo())) {
				 * 
				 * if(Uteis.isAtributoPreenchido(getMatriculaVO().getCurso().
				 * getConfiguracaoAcademico().
				 * getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina()) ) { Integer
				 * numeroPeriodoLetivo =
				 * getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo() +
				 * getMatriculaVO().getCurso().getConfiguracaoAcademico().
				 * getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina();
				 * 
				 * if(Uteis.isAtributoPreenchido(numeroPeriodoLetivo) && numeroPeriodoLetivo >=
				 * getMatriculaPeriodoVO().getGradeCurricular().getUltimoPeriodoLetivoGrade().
				 * getPeriodoLetivo()) { periodoAnterior =
				 * getMatriculaPeriodoVO().getGradeCurricular().getUltimoPeriodoLetivoGrade();
				 * }else { periodoAnterior =
				 * getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivoPorNumero(
				 * numeroPeriodoLetivo); } }else
				 * if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getGradeCurricular().
				 * getUltimoPeriodoLetivoGrade())){ periodoAnterior =
				 * getMatriculaPeriodoVO().getGradeCurricular().getUltimoPeriodoLetivoGrade(); }
				 * }
				 * 
				 * 
				 * if (getPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina().intValue() ==
				 * 0 || (getMatriculaVO().getCurso().getConfiguracaoAcademico().
				 * getHabilitarControleInclusaoDisciplinaPeriodoFuturo())) {
				 * setListaDisciplinasPeriodoLetivoAlunoPendente(getMatriculaVO().
				 * getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().
				 * getListaGradeDisciplinaVOsPendentesGradeCurricular(periodoAnterior.
				 * getPeriodoLetivo())); } else { PeriodoLetivoComHistoricoAlunoVO
				 * periodoComHistorico = getMatriculaVO().getMatriculaComHistoricoAlunoVO().
				 * getGradeCurricularComHistoricoAlunoVO().
				 * getPeriodoLetivoComHistoricoAlunoVOPorCodigo(
				 * getPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina()); if
				 * (periodoComHistorico != null) {
				 * setListaDisciplinasPeriodoLetivoAlunoPendente(periodoComHistorico.
				 * getDisciplinasPendentesAlunoPeriodoLetivo()); } else {
				 * setListaDisciplinasPeriodoLetivoAlunoPendente(new ArrayList<>(0)); } }
				 * 
				 * 
				 * getFacadeFactory().getMatriculaPeriodoFacade().
				 * realizarVerificacaoDeDistribuicaoDisciplinaDependenciaAutomatica(
				 * getMatriculaVO(),
				 * getMatriculaPeriodoVO(),getListaDisciplinasPeriodoLetivoAlunoPendente(),
				 * isLiberadoInclusaoTurmaOutroUnidadeEnsino(),
				 * getLiberadoInclusaoTurmaOutroCurso(),
				 * getLiberadoInclusaoTurmaOutroMatrizCurricular(),
				 * getPermitirRealizarMatriculaDisciplinaPreRequisito(),getHorarioAlunoTurnoVOs(
				 * ),getUsuarioLogadoClone());
				 * 
				 */
			}else {
				getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosDefinirDisciplinasMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado(), null, false, getPermitirRealizarMatriculaDisciplinaPreRequisito());
			}
				
			getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosAnoSemestreMatricula(getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), false);
//			setPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina(0);
			atualizarListaDisciplinasHistoricoPeriodoLetivoSelecionado();
			inicializarCargaHorariaCreditosDisciplinasPendentesAtePeriodoAnterior(periodoAnterior);
			
			gerarDadosGraficoEvolucaoAcademicaAluno();
			if (getRenovandoMatriculaDaVisaoDoAluno()) {
				prepararDisciplinasGrupoOptativaPeriodoLetivoRenovacaoAluno();
				// para a visão do aluno, também temos que inicializar os dados
				// financeiros
				// para apresentar o valor previsto que o mesmo irá pagar de
				// matrícula e de mensalidade
				
				getFacadeFactory().getMatriculaFacade().verificarDocumentaoImpediRenovacaoEstaPendente(getMatriculaVO(), getUsuarioLogado());
			}

//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "navegarAbaDisciplinas4"+ getMatriculaVO().getMatricula());
			// TODO URGENTE remover esta regra de negocio do controle
//			if (getConfiguracaoGeralPadraoSistema().getControlaQtdDisciplinaExtensao().booleanValue()) {
//				if (getMatriculaVO().getTipoMatricula().equals("EX")) {
//					// obter lista disciplinas que devem ficar
////					List<DisciplinaVO> lista = getFacadeFactory().getHorarioTurmaDiaFacade().consultarDisciplinaUltimaDataAulaProgramadaMenorDataAtual(getMatriculaPeriodoVO().getTurma().getCodigo(), getMatriculaVO().getQtdDisciplinasExtensao());
//					// remover utilizando os metodos exclusão de disciplina
//					List<MatriculaPeriodoTurmaDisciplinaVO> listaRemover = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
//					Iterator i = getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().iterator();
//					if (getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().size() > lista.size()) {
//						while (i.hasNext()) {
//							MatriculaPeriodoTurmaDisciplinaVO mptd = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
//							boolean presenteLista = false;
//							Iterator j = lista.iterator();
//							while (j.hasNext()) {
//								DisciplinaVO disc = (DisciplinaVO) j.next();
//								if (disc.getCodigo().intValue() == mptd.getDisciplina().getCodigo().intValue()) {
//									presenteLista = true;
//								}
//							}
//							if (!presenteLista) {
//								listaRemover.add(mptd);
//							}
//						}
//						removerListaMatriculaPeriodoTurmaDisciplina(listaRemover);
//					}
//				}
//			}

			if ((!getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaDisciplinas())) {
				// se o usuario nao tem permissao para navegarAbaDisciplinas na
				// visao do aluno
				// entao já vai para aba do plano financeiro.
//				navegarAbaPlanoFinanceiroAluno();
			}
			if(getMatriculaPeriodoVO().getMatriculaEspecial() && !getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().isEmpty()){
				getMatriculaPeriodoVO().setMatriculaEspecial(false);
			}
			if (getMatriculaPeriodoVO().getMensagemAvisoUsuario() != null && !getMatriculaPeriodoVO().getMensagemAvisoUsuario().getListaMensagemErro().isEmpty()) {
				setConsistirExceptionMensagemDetalhada("msg_aviso", getMatriculaPeriodoVO().getMensagemAvisoUsuario(), Uteis.ALERTA);
			} else {
				setMensagemDetalhada("");
				setMensagemID("msg_dados_adicionados", Uteis.ALERTA);
			}
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "navegarAbaDisciplinas5"+ getMatriculaVO().getMatricula());
		} catch (Exception e) {
			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return realizarNavegacaoPagina();
	}

	public Boolean getApresentarDisciplinasAluno() {
		if ((this.getRenovandoMatricula()) && (getRenovandoMatriculaDaVisaoDoAluno()) && (!getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaDisciplinas())) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public void limparDisciplinasPorPeriodoLetivo() {
		if(getMatriculaPeriodoVO().isNovoObj()){
			getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().clear();			
		}
	}

	public void atualizarListaControleDocumentacaoCurso() {
		try {
			getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(matriculaVO, getUsuarioLogado());
		} catch (Exception e) {
		}
	}

	public String voltarAbaDocumentacao() {
		LoginControle login = (LoginControle) getControlador("LoginControle");
		if (!login.getPermissaoAcessoMenuVO().getNavegarAbaDocumentacao().booleanValue() || getMatriculaVO().getDocumetacaoMatriculaVOs().isEmpty()) {
			navegarAbaDadosBasicos();
		} else {
			setGuiaAba("Documentacao");		
		}
		return realizarNavegacaoPagina();
		
	}
	
	public void voltarAbaDocumentacaoAluno() {
		voltarAbaDocumentacao();
	}

//	public String navegarAbaDocumentacao() {
//		try {
////			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "navegarAbaDocumentacao" +getMatriculaVO().getMatricula());
//			setOncompleteModal("");
//			Uteis.checkState(isLiberarAlteracaoCampoMatricula() && getFacadeFactory().getMatriculaFacade().validarUnicidade(getMatriculaVO().getMatricula()), "Já existe um aluno cadastrado com esse número de matricula.");
//			realizarValidacaoDadosAlunoEnadeCenso();
//			verificarAlunoAceitouTermoAceiteRenovacaoMatricula();
//			validarPermissaoRenovarMatriculaDoAlunoQuandoPossuirBloqueioBiblioteca();
//			if (!getMatriculaVO().getMatriculaJaRegistrada() 
//					&& (!Uteis.isAtributoPreenchido(getMatriculaVO().getTransferenciaEntrada())
//							|| (Uteis.isAtributoPreenchido(getMatriculaVO().getTransferenciaEntrada())  
//									&& !getMatriculaVO().getTransferenciaEntrada().getTipoTransferenciaEntrada().equals("IN")))) {
//				getFacadeFactory().getMatriculaFacade().verificaAlunoJaMatriculado(getMatriculaVO() ,false, getUsuarioLogado(), false);
//			}
//			if(getAlunoPossuiPendenciaEnadeCenso()) {
//				setOncompleteModal("PF('panelAvisoVerificarDadosEnadeCenso').show()");
//				return "";					
//			}
//			if (getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
//				Boolean aptoMatricula = getFacadeFactory().getMatriculaFacade().validarMatriculaAlunoPosGraduacao(getMatriculaVO().getAluno().getCodigo(), getMatriculaVO().getUnidadeEnsino().getCodigo());
//				if (!aptoMatricula) {
//					throw new Exception("Não é possível realizar a MATRÍCULA deste aluno em um curso de Pós-Graduação pois o mesmo não possui uma formação acadêmica em Graduação");
//				}
//			}
//			if (!Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getProcessoMatricula())) {
//				throw new Exception("O Campo (Processo Matrícula) Deve Ser Informado!");
//			}
//			validarExistenciaMatriculaPeriodoAtivaPreMatricula();
//			if (getMatriculaVO().getMatricula().equals("")) {
//				inicializarMatriculaComHistoricoAluno(false);
//			}
//			setApresentarModalInscricao("");
//			// verificarPermissao obrigar forma ingresso
//			if (getMatriculaVO().getCurso().getConfiguracaoAcademico().getObrigaInformarFormaIngressoMatricula().booleanValue() && !getNomeTelaAtual().endsWith("renovacaoMatriculaAluno.xhtml")) {
//				if (getMatriculaVO().getFormaIngresso().equals("")) {
//					throw new Exception("Deve ser informado a forma de ingresso da matrícula!");
//				}
//			}
////			if (Uteis.isAtributoPreenchido(matriculaVO.getCodigoFinanceiroMatricula()) && getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()).getUtilizarIntegracaoFinanceira()) {
////				matriculaVO.setCodigoFinanceiroMatricula(Uteis.removerZeroEsquerda(matriculaVO.getCodigoFinanceiroMatricula()));
////			}
//
//			// verificar permissao obriga informar origem forma ingresso.
//			if (getMatriculaVO().getCurso().getConfiguracaoAcademico().getObrigaInformarOrigemFormaIngressoMatricula().booleanValue() && !getNomeTelaAtual().endsWith("renovacaoMatriculaAluno.xhtml")) {
//				if (getMatriculaVO().getFormaIngresso().equals("PS") && getMatriculaVO().getInscricao().getCodigo().intValue() == 0) {
//					consultarInscricaoAluno();
//					setApresentarModalInscricao("PF('panelAlunoNovaMatricula_Inscricao_AtualizarDado2').show()");
//					throw new Exception("Ao utilizar essa forma de ingresso ( PROCESSO SELETIVO ), é obrigatório informar o nrº de inscrição do processo seletivo no qual o aluno foi aprovado!");
//				}
//				if (getMatriculaVO().getFormaIngresso().equals("VE") && getMatriculaVO().getInscricao().getCodigo().intValue() == 0) {
//					consultarInscricaoAluno();
//					setApresentarModalInscricao("PF('panelAlunoNovaMatricula_Inscricao_AtualizarDado2').show()");
//					throw new Exception("Ao utilizar essa forma de ingresso ( VESTIBULAR ), é obrigatório informar o nrº de inscrição do processo seletivo no qual o aluno foi aprovado!");
//				}
//				if (getMatriculaVO().getFormaIngresso().equals("TE") && !Uteis.isAtributoPreenchido(getMatriculaVO().getTransferenciaEntrada())) {
//					throw new Exception("Ao utilizar essa forma de ingresso ( TRANSFERÊNCIA EXTERNA ), é obrigatório o registro da transferência externa. Realize o procedimento no menu ( ACADÊMICO=>TRANSFERÊNCIAS/PROTOCOLO=>TRANSFERÊNCIA EXTERNA ) e após gravar o registro, clique em iniciar matrícula  e dê continuidade ao processo!");
//				}
//			}
//			// getFacadeFactory().getMatriculaPeriodoFacade().validarJaExisteMatriculaPeriodo(matriculaVO,
//			// matriculaPeriodoVO);
//			getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().setCodigo(this.getUsuarioLogado().getCodigo());
//			getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().setNome(this.getUsuarioLogado().getNome());
//			getFacadeFactory().getMatriculaPeriodoFacade().validarMatriculaPeriodoPodeSerRealizada(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
//			if (!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getTurma().getCodigo())) {
//				throw new Exception("O Campo (Turma) Deve Ser Informado !");
//			}
//			Uteis.checkState(isObrigatorioContratoPorTurma() && !Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getContratoMatricula()), "O campo Modelo de Contrato deve ser informado! Deve ser definido na turma o CONTRATO DE MATRÍCULA.");
//			if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()).getUtilizarIntegracaoFinanceira() && !Uteis.isAtributoPreenchido(matriculaVO.getCodigoFinanceiroMatricula()) && !matriculaVO.getCodigoFinanceiroMatricula().equals("99999999")) {
//				if (matriculaVO.getCodigoFinanceiroMatricula().equals("0")) {
//					throw new Exception("O (Código Integração Financeira) Deve Ser Diferente de Zero !");
//				}
//				if (matriculaVO.getCodigoFinanceiroMatricula().isEmpty()) {
//					throw new Exception("O (Código Integração Financeira) Deve Ser Informado !");
//				}
//
//			}
//			if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()).getUtilizarIntegracaoFinanceira() && getFacadeFactory().getMatriculaFacade().consultarExistenciaCodigoFinanceiroMatricula(matriculaVO.getCodigoFinanceiroMatricula(), matriculaVO.getMatricula())) {
//				throw new Exception("O Código de Integração Financeira Matrícula (" + matriculaVO.getCodigoFinanceiroMatricula() + ") Já Está Vinculado a uma outra Matrícula.");
//			}
//			gerarDadosGraficoEvolucaoAcademicaAluno();
//			setApresentarModalAvisoAlunoReprovado(Boolean.FALSE);
//			setMensagemModalAvisoAlunoReprovado("");
//			if (getMatriculaPeriodoVO().getNovoObj()) {
//				getFacadeFactory().getMatriculaPeriodoFacade().validarDadosRenovacaoProcessoMatriculaPeriodoLetivo(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
//				getFacadeFactory().getDocumetacaoMatriculaFacade().verificarDocumentosObrigatoriosACadaRenovacao(getMatriculaVO() , getMatriculaPeriodoVO().getAno() , getMatriculaPeriodoVO().getSemestre());
//			}
//			
//			if(!getRenovandoMatricula()) {
//				getFacadeFactory().getMatriculaPeriodoFacade().realizarDefinicaoContratoPadraoSerUsadoMatriculaPeriodo(TipoContratoMatriculaEnum.getEnumPorValor(getMatriculaVO().getTipoMatricula()), getMatriculaPeriodoVO(),  !isObrigatorioContratoPorTurma(), false, getUsuarioLogado());
//			}
//			
//			
//			
////			if (!getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()).getRealizarMatriculaComFinanceiroManualAtivo() && !getMatriculaPeriodoVO().getFinanceiroManual() && !getRenovandoMatricula()) {				
////				getFacadeFactory().getMatriculaFacade().inicializarTextoContratoPlanoFinanceiroAluno(this.getMatriculaVO(), this.getMatriculaPeriodoVO(), !isObrigatorioContratoPorTurma(), getUsuarioLogado());
////			}else if(getRealizandoNovaMatriculaAluno()) {
////				getMatriculaPeriodoVO().setContratoExtensao(null);
////				getMatriculaPeriodoVO().setContratoMatricula(null);
////			}
//			if (!getMatriculaVO().getSituacao().equals("PL")) {
//				getMatriculaVO().setUsuario(getUsuarioLogadoClone());
//			}
//			getFacadeFactory().getMatriculaFacade().gerenciarEntregaDocumentoMatricula(getMatriculaVO(), getUsuarioLogado());
//
//			setProcessoCalendarioMatriculaVO(getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO());
//			if(!getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getRegerarFinanceiro() && getPermitirRegerarFinanceiro()) {
//				getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setRegerarFinanceiro(getPermitirRegerarFinanceiro());
//			}
//			atualizarDataMatriculaComBaseDataMatriculaPeriodo();
////			if (getMatriculaPeriodoVO().getCodigo().equals(0) && !Uteis.isAtributoPreenchido(getBanner()) && !this.getLiberarRenovacaoComDebitosFinanceiros()) {
//				// somente para novos, em caso de alterar nao temos que fazer
//				// esta verificação
////				setMatricula_Erro("");
////				setListaContaReceber(getFacadeFactory().getContaReceberFacade().consultaRapidaPorAlunoEMatriculaContasReceberVencidas(getMatriculaVO().getAluno().getCodigo(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()).getUtilizarIntegracaoFinanceira(), getMatriculaPeriodoVO(), getUsuarioLogado()));
////				setMatricula_Erro(getFacadeFactory().getContaReceberFacade().validarDadosExisteContaReceberRenegociadadaQueNaoCumpriuAcordo(getMatriculaVO().getMatricula(), getMatriculaVO().getAluno().getNome()));
////				if ((getListaContaReceber().isEmpty() && !Uteis.isAtributoPreenchido(getMatricula_Erro())) || (this.getLiberarRenovacaoComDebitosFinanceiros())) {
////					setGuiaAba("Documentacao");
////				} else {
////					if (getMatriculaVO().getDataLiberacaoPendenciaFinanceira() != null && getMatriculaVO().getDataLiberacaoPendenciaFinanceira().after(Uteis.obterDataAntiga(new Date(), 2))) {
////							this.liberarRenovacaoComDebitosFinanceiros = true;
////							setGuiaAba("Documentacao");
////					} else if (getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaPendenciaFinanceira()) {
////						setGuiaAba("PendenciaFinanceira");
////					} else {
////						throw new Exception("O usuário não possui permissão para visualizar para aba de pendência financeira, impossibilitando a navegação para a próxima aba.");
////					}
////				}
////			} else {
//				setGuiaAba("Documentacao");
////			}
//			if (getPortadorDiploma()) {
//				getMatriculaVO().setFormaIngresso("PD");
//			}
//			if (!getListaSelectItemFormacaoAcademicaAluno().isEmpty() && getListaSelectItemFormacaoAcademicaAluno().size() == 2) {
//				Integer valor = (Integer) ((SelectItem) getListaSelectItemFormacaoAcademicaAluno().get(1)).getValue();
//				getMatriculaVO().getFormacaoAcademica().setCodigo(valor);
//			}
//			if (!getListaSelectItemReconhecimentoCurso().isEmpty() && getListaSelectItemReconhecimentoCurso().size() == 2) {
//				Integer valor = (Integer) ((SelectItem) getListaSelectItemReconhecimentoCurso().get(1)).getValue();
//				getMatriculaVO().getAutorizacaoCurso().setCodigo(valor);
//			}
//			LoginControle login = (LoginControle) getControlador("LoginControle");
//			if (!login.getPermissaoAcessoMenuVO().getNavegarAbaDocumentacao().booleanValue() || getMatriculaVO().getDocumetacaoMatriculaVOs().isEmpty()) {
//				navegarAbaDisciplinas();
//			}
//			if (getMatriculaPeriodoVO().getMensagemAvisoUsuario() != null && !getMatriculaPeriodoVO().getMensagemAvisoUsuario().getListaMensagemErro().isEmpty()) {
//				setConsistirExceptionMensagemDetalhada("msg_aviso", getMatriculaPeriodoVO().getMensagemAvisoUsuario(), Uteis.ALERTA);
//			} else {
//				setMensagemID("msg_entre_dados", Uteis.ALERTA);
//				setMensagemDetalhada("");
//			}
//
//		} catch (Exception e) {
//			setGuiaAba("DadosBasicos");
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
//		}
//		return realizarNavegacaoPagina();
//	}

	public List<TransferenciaEntradaDisciplinasAproveitadasVO> getDisciplinasAproveitamento() throws Exception {
		if (getMatriculaPeriodoVO().getTranferenciaEntrada() != 0) {
			TransferenciaEntradaVO transferenciaEntradaVO = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getTranferenciaEntrada(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (transferenciaEntradaVO.getTransferenciaEntradaDisciplinasAproveitadasVOs() != null || !transferenciaEntradaVO.getTransferenciaEntradaDisciplinasAproveitadasVOs().isEmpty()) {
				return transferenciaEntradaVO.getTransferenciaEntradaDisciplinasAproveitadasVOs();
			} else {
				return new ArrayList(0);
			}
		}
		return new ArrayList(0);
	}

	public String navegarAbaDisciplinaMatriculado() throws Exception {
		try {
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "navegarAbaDisciplinaMatriculado" +getMatriculaVO().getMatricula());
			if(getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaDocumentacao() && 
				!getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaDisciplinas() 
				&& !getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaPlanoFinanceiroAluno()) {
				setOncompleteModal("");
				realizarValidacaoDadosAlunoEnadeCenso();
				if(getAlunoPossuiPendenciaEnadeCenso()) {
					setOncompleteModal("PF('panelAvisoVerificarDadosEnadeCenso').show()");
					return "";
				}
			}
			
			executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarPorMatriculaPeriodo();
			setApresentarModalAvisoAlunoReprovado(Boolean.FALSE);
			setMensagemModalAvisoAlunoReprovado("");
			verificarVisualizarCheckBoxFinanceiroManual();
			if (!Uteis.isAtributoPreenchido(getBanner()) && getMatriculaPeriodoVO().getCodigo().equals(0)) { // somente para
				// novos, em
				// caso de
				// alterar
				// nao temos
				// que fazer
				// esta
				// verificação
//			
			} else {
				setGuiaAba("DisciplinaMatriculado");
			}
//			setPlanoFinanceiroAlunoLogVOs(getFacadeFactory().getPlanoFinanceiroAlunoLogFacade().consultarPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo()));
//			getPlanoFinanceiroAlunoLogVOs().add(0, getFacadeFactory().getPlanoFinanceiroAlunoLogFacade());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			
		}
		return realizarNavegacaoPagina();
	}

	public String navegarAbaInicio() {
//		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "navegarAbaInicio" +getMatriculaVO().getMatricula());
		setMatriculaVO(new MatriculaVO());
		setGuiaAba("Inicio");
		return realizarNavegacaoPagina();
	}


	// //matriculaPeriodoVO.descontoTotalMatricula(getPlanoFinanceiroCursoVO(),
	// 
	// getMatriculaPeriodoVO().getValorMatriculaCheio(),
	// getOrdemDesconto(), getCondicaoPagamentoPlanoFinanceiroCursoVO());
	// //matriculaPeriodoVO.descontoTotalMensalidade(getPlanoFinanceiroCursoVO(),
	// 
	// getMatriculaPeriodoVO().getValorMensalidadeCheio(),
	// getOrdemDesconto(), getCondicaoPagamentoPlanoFinanceiroCursoVO());
	// Matricula.montarDadosDescontoProgressivo(matriculaVO,
	// NivelMontarDados.TODOS);
	// matriculaPeriodoVO.setListaDescontosMatricula(getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(
	// true,
	// getMatriculaPeriodoVO().getValorMatriculaCheio(),
	// matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoMatricula(),
	// matriculaVO.getPlanoFinanceiroAluno().getPercDescontoMatricula(),
	// matriculaVO.getPlanoFinanceiroAluno().getValorDescontoMatricula(),
	// new Date(),
	// this.getOrdemDesconto(),
	// matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivo(),
	// matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs(),
	// matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs()));
	// matriculaPeriodoVO.setListaDescontosMensalidade(getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(
	// false,
	// getMatriculaPeriodoVO().getValorMensalidadeCheio(),
	// matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela(),
	// matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela(),
	// matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela(),
	// new Date(),
	// this.getOrdemDesconto(),
	// matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivo(),
	// matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs(),
	// matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs()));
	// } catch (Exception e) {
	// //System.out.println("MENSAGEM => " + e.getMessage());;
	// }
	// }
	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * MatriculaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultarAlunosPreMatriculado() {
		List objs = new ArrayList(0);
		try {
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorNomeCursoTurnoSituacao(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), this.filtroTurno, "PR", "", "", true, getUsuarioLogado(),getControleConsultaOtimizado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml");
		}
	}

	@Override
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Consultar Matricula", "Consultando");
			super.consultar();
			getControleConsultaOtimizado().setLimitePorPagina(8);
			getControleConsultaOtimizado().getListaConsulta().clear();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaUtilizandoLike(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, getControleConsultaOtimizado(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}				
				objs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorRegistroAcademicoPessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, this.getFiltroSituacaoMatriculaPeriodo(), getAno(), getSemestre(), getUsuarioLogado(),getControleConsultaOtimizado());
			}			
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorNomePessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, this.getFiltroSituacaoMatriculaPeriodo(), getAno(), getSemestre(), getUsuarioLogado(),getControleConsultaOtimizado());
			} 
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorNomeCursoTurnoSituacao(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), this.filtroTurno, this.filtroSituacaoMatriculaPeriodo, getAno(), getSemestre(), true, getUsuarioLogado(),getControleConsultaOtimizado());
			}
			if (getControleConsulta().getCampoConsulta().equals("turma")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_turmaVazio"));
				}
				objs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorIdentificadorTurmaSituacaoMatriculaPeriodo(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), this.getSituacaoFiltroTurma(), getAno(), getSemestre(), true, getUsuarioLogado(),getControleConsultaOtimizado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				objs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorData(Uteis.getDateTime(getValorConsultaData(), 0, 0, 0), Uteis.getDateTime(getValorConsultaData(), 23, 59, 59), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado(),getControleConsultaOtimizado());
			}
			if (getControleConsulta().getCampoConsulta().equals("cpf")) {
				if (Uteis.retirarMascaraCPF(getControleConsulta().getValorConsulta()).length() > 14 || getControleConsulta().getValorConsulta().length() == 0) {
					throw new Exception(getMensagemInternalizacao("msg_consultaAlunoCpfMatriculaRenovacao_cpfInvalido"));
				}
				objs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorCpfAluno(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado(),getControleConsultaOtimizado());
			}
			verificarMatrizCurricularIntegralizada(objs);
			getControleConsultaOtimizado().setListaConsulta(objs);
			
			verificarPermissaoUsuarioSuspenderMatricula();
			verificarPermissaoUsuarioLiberarPgtoMatricula();
			verificarPermissaoUsuarioConfirmarCancelarPreMatricula();
			verificarPermissaoReconsiderarSolicitacao();
			setMensagemID("msg_dados_consultados");
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Consultar Matricula", "Consultando");
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml");
		} catch (Exception e) {
			getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml");
		}
	}

	private void verificarMatrizCurricularIntegralizada(List objs) {
		try {
			List<MatriculaPeriodoVO> listaObjs = objs;
			if (Uteis.isAtributoPreenchido(listaObjs)) {
				for (MatriculaPeriodoVO matriculaPeriodo : listaObjs) {
					matriculaPeriodo.getMatriculaVO()
							.setAlunoConcluiuDisciplinasRegulares(getFacadeFactory().getMatriculaFacade()
									.isMatriculaIntegralizada(matriculaPeriodo.getMatriculaVO(),
											matriculaPeriodo.getGradeCurricular().getCodigo(), null, null));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	public String ativarPreMatriculaPeriodo() {
		try {
			setMatriculaForaPrazo(false);
			imprimirContrato = false;
			montarDadosNovoPeriodoLetivo = false;
			matriculaPeriodoVO = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
			boolean existeOutraMatriculaPeriodoAtiva = getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMatriculaPeriodoPorSituacao(matriculaPeriodoVO.getMatricula(), "AT");
			if (existeOutraMatriculaPeriodoAtiva) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfirmacaoPreMatricula_outraAtiva"));
			}
			matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//			ConfiguracaoFinanceiroVO configFinanceiro = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo());
			getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosParaProcessarGeracaoContasReceberReferentesParcelasMatriculaPeriodo(matriculaVO, matriculaPeriodoVO, matriculaPeriodoVO.getProcessoMatriculaCalendarioVO(), getUsuarioLogado());
			getFacadeFactory().getMatriculaPeriodoFacade().validarMatriculaPeriodoPodeSerAtivada(matriculaVO, matriculaPeriodoVO, getUsuarioLogado());
			getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaVOParaAtivada(matriculaVO, matriculaPeriodoVO, getUsuarioLogado());
			inicializarListasSelectItemTodosComboBox();
			apresentarPlanoFinanceiroCurso = true;
			montarListaProcessoMatricula(false);
			navegarAbaDisciplinaMatriculado();
			setMensagemID("msg_ConfirmacaoPreMatricula_ativaRealizada");
			return realizarNavegacaoPagina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml");
		}
	}

	// montarListaSelectItemPeriodoLetivo
	public void montarListaSelectItemPeriodoLetivoAdicionar() {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (getMatriculaPeriodoVO().getGradeCurricular().getCodigo().intValue() == 0) {
				setListaSelectItemPeriodoLetivoAdicionar(new ArrayList(0));
				return;
			}
			resultadoConsulta = matriculaPeriodoVO.getGradeCurricular().getPeriodoLetivosVOs(); // consultarPeriodoLetivoPorGradeCurricular(getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemPeriodoLetivoAdicionar(objs);
			setPeriodoLetivoAdicionar(getMatriculaPeriodoVO().getPeridoLetivo().getCodigo());
		} catch (Exception e) {
			setListaSelectItemPeriodoLetivoAdicionar(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List montarListaRenovarMatriculaMatriculaPeriodo(List objs) {
		List lista = new ArrayList(0);
		Iterator i = objs.iterator();
		MatriculaVO mat = new MatriculaVO();
		while (i.hasNext()) {
			MatriculaVO matricula = (MatriculaVO) i.next();
			Iterator j = matricula.getMatriculaPeriodoVOs().iterator();
			while (j.hasNext()) {
				MatriculaPeriodoVO matriculaPeriodo = (MatriculaPeriodoVO) j.next();
				mat = new MatriculaVO();
				mat = matricula;
				List list = new ArrayList(0);
				list.add(matriculaPeriodo);
				mat.setMatriculaPeriodoVOs(list);
				lista.add(mat);
			}
		}
		return lista;
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>MatriculaVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			setOncompleteModal("");
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Excluindo Matricula", "Excluindo");
			getFacadeFactory().getMatriculaFacade().excluirPreMatriculaERegistrosRelacionados(getMatriculaPeriodoVO(), "Exclusão de Matrícula Período", getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Excluindo Matricula", "Excluindo");
			consultar();
			setMensagemID("Matrícula Período Excluida!", Uteis.SUCESSO, true);
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String uri = request.getRequestURI();
			if (uri.contains("renovarMatriculaConfirmadoForm.xhtml")) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaConfirmadoForm.xhtml");
			} else {
				return getTelaConsulta() ? Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml") : Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
			}
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>MatriculaPeriodo</code> para o objeto <code>matriculaVO</code> da
	 * classe <code>Matricula</code>
	 */
	public void adicionarMatriculaPeriodo() throws Exception {
		if (!getMatriculaVO().getMatricula().equals("")) {
			matriculaPeriodoVO.setMatricula(getMatriculaVO().getMatricula());
		}
		if (getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().getCodigo().intValue() == 0) {
			getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
		}
		getMatriculaVO().adicionarObjMatriculaPeriodoVOs(getMatriculaPeriodoVO());
		setMensagemID("msg_dados_adicionados");
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>MatriculaPeriodo</code> para edição pelo usuário.
	 */
	public String editarMatriculaPeriodo() throws Exception {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
		setMatriculaPeriodoVO(obj);
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>MatriculaPeriodo</code> do objeto <code>matriculaVO</code> da
	 * classe <code>Matricula</code>
	 */
	public String removerMatriculaPeriodo() throws Exception {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
		getMatriculaVO().excluirObjMatriculaPeriodoVOs(obj.getPeridoLetivo().getCodigo());
		setMensagemID("msg_dados_excluidos");
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>DocumetacaoMatricula</code> para o objeto <code>matriculaVO</code>
	 * da classe <code>Matricula</code>
	 */
	public String adicionarDocumetacaoMatricula() throws Exception {
		try {
			if (!getMatriculaVO().getMatricula().equals("")) {
				documetacaoMatriculaVO.setMatricula(getMatriculaVO().getMatricula());
			}
			getMatriculaVO().adicionarObjDocumetacaoMatriculaVOs(getDocumetacaoMatriculaVO());
			this.setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			setMensagemID("msg_dados_adicionados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>DocumetacaoMatricula</code> para edição pelo usuário.
	 */
	public String editarDocumetacaoMatricula() throws Exception {
		DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
		setDocumetacaoMatriculaVO(obj);
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>DocumetacaoMatricula</code> do objeto <code>matriculaVO</code> da
	 * classe <code>Matricula</code>
	 */
	public String removerDocumetacaoMatricula() throws Exception {
		DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
		getMatriculaVO().excluirObjDocumetacaoMatriculaVOs(obj.getTipoDeDocumentoVO().getCodigo());
		setMensagemID("msg_dados_excluidos");
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
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

	
	

	
	
	
	
	public boolean isDebitoFinanceiroAoIncluirConvenioLiberado() {
		return debitoFinanceiroAoIncluirConvenioLiberado;
	}

	public void setDebitoFinanceiroAoIncluirConvenioLiberado(boolean debitoFinanceiroAoIncluirConvenioLiberado) {
		this.debitoFinanceiroAoIncluirConvenioLiberado = debitoFinanceiroAoIncluirConvenioLiberado;
	}

	public boolean isHabilitarRecursoValidacaoDebitoInclusaoConvenio() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_HABILITAR_RECURSO_VALIDACAO_DEBITO_INCLUSAO_CONVENIO, getUsuarioLogadoClone());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>ItemPlanoFinanceiroAluno</code> para edição pelo usuário.
	 */
//	public void editarItemPlanoFinanceiroAluno() throws Exception {
//		ItemPlanoFinanceiroAlunoVO obj = (ItemPlanoFinanceiroAlunoVO) context().getExternalContext().getRequestMap().get("itemPlanoFinanceiroAlunoItens");
//		setItemPlanoFinanceiroAlunoVO(obj);
//	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>PlanoDesconto</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>PlanoDesconto</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
//	public void montarListaSelectItemPlanoDesconto() {
//		try {
//			List resultadoConsulta = consultarPlanoDescontoPorNome("", getPermitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso());
//			Iterator i = resultadoConsulta.iterator();
//			List objs = new ArrayList(0);
//			objs.add(new SelectItem(0, ""));
//			while (i.hasNext()) {
//				PlanoDescontoVO obj = (PlanoDescontoVO) i.next();
//				if (obj.getUnidadeEnsinoVO().getCodigo().equals(0) || obj.getUnidadeEnsinoVO().getCodigo().equals(getMatriculaVO().getUnidadeEnsino().getCodigo())) {
//					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
//				}
//			}
//			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
//			Collections.sort((List) objs, ordenador);
//			setListaSelectItemPlanoDesconto(objs);
//		} catch (Exception e) {
//			// System.out.println(e.getMessage());
//		}
//	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		return itens;
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
//	public List consultarPlanoDescontoPorNome(String nomePrm, Boolean permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso) throws Exception {
//		List lista = new ArrayList();
//		if (!getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getDefinirPlanoDescontoApresentarMatricula() || permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso) {
//			lista = getFacadeFactory().getPlanoDescontoFacade().consultarPlanoDescontoAtivoPorUnidadeEnsinoNivelComboBox(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino(), true, getUsuarioLogado());
//		} else {
//			lista = getFacadeFactory().getPlanoDescontoDisponivelMatriculaFacade().consultarPlanoDescontoPorCodigoPlanoDescontoDisponivelMatricula(getMatriculaPeriodoVO(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//		}
//		return lista;
//	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoItemPlanoFinanceiro</code>
	 */
	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemTipoItemPlanoFinanceiroItemPlanoFinanceiroAluno() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoItemPlanoFinanceiroAlunos = (Hashtable) Dominios.getTipoItemPlanoFinanceiroAluno();
		Enumeration keys = tipoItemPlanoFinanceiroAlunos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoItemPlanoFinanceiroAlunos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	
	

	
	public void montarListaSelectItemPeriodoLetivoInclusaoDisciplina() {
		// listaSelectItemPeriodoLetivoInclusaoDisciplina
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			List objs = new ArrayList(0);
			if ((getMatriculaPeriodoVO().getGradeCurricular().getCodigo().intValue() == 0) || (getMatriculaPeriodoVO().getListaPeriodosLetivosValidosParaMatriculaPeriodo().isEmpty())) {
				setListaSelectItemPeriodoLetivoInclusaoDisciplina(objs);
				return;
			}
			PeriodoLetivoVO periodoAnterior = new PeriodoLetivoVO();
			setPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina(0);
			//getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia() && getMatriculaVO().getCurso().getConfiguracaoAcademico().isHabilitarDistribuicaoDisciplinaDependenciaAutomatica()
			if((getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoDisciplinaPeriodoFuturo())) {
				objs.add(new SelectItem(-1, "Todos Períodos"));
				setPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina(-1);
				periodoAnterior = getMatriculaPeriodoVO().getPeriodoLetivo();
			}else {
				periodoAnterior = getFacadeFactory().getMatriculaPeriodoFacade().executarObterPeriodoLetivoAnteriorAoPeriodoLetivoMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO());
			} 
			objs.add(new SelectItem(0, "PENDENTE(S) Até " + periodoAnterior.getDescricao()));
			for (PeriodoLetivoVO periodo : getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs()) {
				if ((getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoDisciplinaPeriodoFuturo())) {
					if (getMatriculaPeriodoVO().getLiberadoControleInclusaoDisciplinaPeriodoFuturo()) {
						objs.add(new SelectItem(periodo.getCodigo(), periodo.getDescricao()));
					} else {
						int nrPeriodoLetivoMaximoPermitido = getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo() + getMatriculaVO().getCurso().getConfiguracaoAcademico().getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina();
						if (periodo.getPeriodoLetivo().compareTo(nrPeriodoLetivoMaximoPermitido) <= 0) {
							// só adiciona para lista periodos letivos que estão
							// dentro da faixa limite
							// permitida para o aluno em questão.
							objs.add(new SelectItem(periodo.getCodigo(), periodo.getDescricao()));
						} else {
							setApresentarBotaoLiberarControleInclusaoDisciplinaPeriodoFuturo(Boolean.TRUE);
						}
					}
				} else {
					objs.add(new SelectItem(periodo.getCodigo(), periodo.getDescricao()));
				}
			}
			setListaSelectItemPeriodoLetivoInclusaoDisciplina(objs);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PeriodoLetivoMatricula</code>.
	 */
	public void montarListaSelectItemPeriodoLetivo(Integer prm) {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			List objs = new ArrayList(0);
			if ((getMatriculaPeriodoVO().getGradeCurricular().getCodigo().intValue() == 0) || (getMatriculaPeriodoVO().getListaPeriodosLetivosValidosParaMatriculaPeriodo().isEmpty())) {
				setListaSelectItemPeriodoLetivoMatricula(objs);
				return;
			}
			if (getBanner().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			boolean primeiro = true;
			for (PeriodoLetivoVO periodo : getMatriculaPeriodoVO().getListaPeriodosLetivosValidosParaMatriculaPeriodo()) {
				objs.add(new SelectItem(periodo.getCodigo(), periodo.getDescricao()));
				if (primeiro && (!Uteis.isAtributoPreenchido(getMatriculaPeriodoVO()) && !Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getPeriodoLetivo()))) {
					getMatriculaPeriodoVO().setPeridoLetivo((PeriodoLetivoVO)periodo.clone());
					setCodigoPeriodoLetivo(periodo.getCodigo());
					if(getDadosRecuperadosAlteracaoProcessoMatricula().isEmpty() || !getDadosRecuperadosAlteracaoProcessoMatricula().containsKey("PeriodoLetivo")) {
						getDadosRecuperadosAlteracaoProcessoMatricula().put("PeriodoLetivo",	periodo.getCodigo());
					}
					if (getRealizandoNovaMatriculaAluno() && !getMatriculaVO().getLiberarMatriculaTodosPeriodos()) {
						break;
					}
					primeiro = false;
				}else if(primeiro && (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getPeriodoLetivo()))) {
					setCodigoPeriodoLetivo(getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo());
					if(getDadosRecuperadosAlteracaoProcessoMatricula().isEmpty() || !getDadosRecuperadosAlteracaoProcessoMatricula().containsKey("PeriodoLetivo")) {
						getDadosRecuperadosAlteracaoProcessoMatricula().put("PeriodoLetivo",	periodo.getCodigo());
				}
					primeiro = false;
			}
			}
			if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais() || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) && !Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getPeriodoLetivo())) {
				setPermitirMatriculaOnline(false);
			} else {
				setPermitirMatriculaOnline(true);
			}
			setListaSelectItemPeriodoLetivoMatricula(objs);

		} catch (Exception e) {
			// System.out.println(e.getMessage());
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public String getAbrirModalApresentarAvisoAlunoReprovado() {
		if (getApresentarModalAvisoAlunoReprovado()) {
			return "PF('panelAvisoAlunoReprovado').show();";
		}
		return "";
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>sigla</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPeriodoLetivoPorGradeCurricular(Integer gradeCurricular) throws Exception {
		List lista = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(gradeCurricular, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>PeriodoLetivoMatricula</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não
	 * recebe parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemPeriodoLetivo() {
		try {
			montarListaSelectItemPeriodoLetivo(getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>sigla</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPeriodoLetivoPorSigla(Integer siglaPrm) throws Exception {
		List lista = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(siglaPrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void definirGradeCurricularMatriculaPeriodoVO() throws Exception {
		if (this.matriculaPeriodoVO.getGradeCurricular().getCodigo() != 0) {
			// se o codigo é diferente de zero é por que a grade já foi definida
			// para o curso, logo, não
			// a mesma não pode ser redefinida.
			return;
		}
		// List<GradeCurricularVO> listaGradesAtivaCurso =
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PeriodoLetivo</code>.
	 */
	public void montarListaSelectItemGradeCurricular(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (getMatriculaVO().getUnidadeEnsino().getCodigo().equals(0)) {
				setListaSelectItemGradeCurricular(new ArrayList(0));
				return;
			}
			if (getMatriculaVO().getCurso().getCodigo().equals(0)) {
				setListaSelectItemGradeCurricular(new ArrayList(0));
				return;
			}
			resultadoConsulta = consultarGradeCurricularPorDescricao(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (getBanner().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				GradeCurricularVO obj = (GradeCurricularVO) i.next();
				if (this.realizandoNovaMatriculaAluno) {
					// Caso seja uma nova matricula so podemos montar no select
					// item
					// de grades curriculares, grades que ainda estão ativas,
					// nunca grades inativadas ou em construção.
					if (obj.getSituacao().equals("AT") || getMatriculaVO().getTransferenciaEntrada().getGradeCurricular().getCodigo().equals(obj.getCodigo())) {
						objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
					}
					if(getMatriculaVO().getTransferenciaEntrada().getGradeCurricular().getCodigo().equals(obj.getCodigo())) {
						getMatriculaPeriodoVO().setGradeCurricular(obj);
						getMatriculaVO().setGradeCurricularAtual(obj);
						setCodigoGradeCurricular(obj.getCodigo());
						if(getDadosRecuperadosAlteracaoProcessoMatricula().isEmpty() || (!getDadosRecuperadosAlteracaoProcessoMatricula().containsKey("GradeCurricular")) || (getDadosRecuperadosAlteracaoProcessoMatricula().get("GradeCurricular").equals(0))) {
							getDadosRecuperadosAlteracaoProcessoMatricula().put("GradeCurricular",	getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
						}
					}
				} else {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
				if ((obj.getSituacao().equals("AT")) && (this.matriculaPeriodoVO.getGradeCurricular().getCodigo() == 0)) {
					getMatriculaPeriodoVO().setGradeCurricular(obj);
					getMatriculaVO().setGradeCurricularAtual(obj);
					setCodigoGradeCurricular(obj.getCodigo());
					if(getDadosRecuperadosAlteracaoProcessoMatricula().isEmpty() || (!getDadosRecuperadosAlteracaoProcessoMatricula().containsKey("GradeCurricular")) || (getDadosRecuperadosAlteracaoProcessoMatricula().get("GradeCurricular").equals(0))) {
						getDadosRecuperadosAlteracaoProcessoMatricula().put("GradeCurricular",	getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
				}
					
			}
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais() || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) && objs.isEmpty()) {
				setPermitirMatriculaOnline(false);
			} else {
				setPermitirMatriculaOnline(true);
			}
			setListaSelectItemGradeCurricular(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por mostrar quala a Grade Currícular que será feita a
	 * matrícula
	 */
	public void montarGradeCurricular(String prm) throws Exception {
		if (getMatriculaVO().getUnidadeEnsino().getCodigo().equals(0)) {
			setListaSelectItemGradeCurricular(new ArrayList(0));
			return;
		}
		if (getMatriculaVO().getCurso().getCodigo().equals(0)) {
			setListaSelectItemGradeCurricular(new ArrayList(0));
			return;
		}
		List resultadoConsulta = consultarGradeCurricularPorDescricao(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		while (i.hasNext()) {
			GradeCurricularVO obj = (GradeCurricularVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			if (obj.getSituacao().equals("AT")) {
				getMatriculaPeriodoVO().setGradeCurricular(obj);
			}
		}
		setListaSelectItemGradeCurricular(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>PeriodoLetivo</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemGradeCurricular() {
		try {
			montarListaSelectItemGradeCurricular("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>descricao</code> Este atributo é
	 * uma lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarGradeCurricularPorDescricao(String descricaoPrm) throws Exception {
		List lista = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(getMatriculaVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public List getListaSelectItemSituacaoMatriculaPeriodoFiltro() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("TO", "Todas"));
		objs.add(new SelectItem("AT", "Ativas"));
		objs.add(new SelectItem("TR", "Trancados"));
		objs.add(new SelectItem("CA", "Cancelados"));
		return objs;
	}

	public List getListaSelectItemSituacaoFiltroTurma() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("TO", "Todas"));
		objs.add(new SelectItem("AT", "Ativas"));
		objs.add(new SelectItem("TR", "Trancados"));
		objs.add(new SelectItem("CA", "Cancelados"));
		return objs;
	}

	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemSituacaoMatriculaPeriodo() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable matriculaPeriodoSituacaos = (Hashtable) Dominios.getMatriculaPeriodoSituacao();
		Enumeration keys = matriculaPeriodoSituacaos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) matriculaPeriodoSituacaos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemSituacaoDocumetacaoMatricula() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoEntregaDocumentacaos = (Hashtable) Dominios.getSituacaoEntregaDocumentacao();
		Enumeration keys = situacaoEntregaDocumentacaos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoEntregaDocumentacaos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoDocumento</code>
	 */
	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemTipoDocumentoDocumetacaoMatricula() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoDocumentoDocumentacaoMatriculas = (Hashtable) Dominios.getTipoDocumentoDocumentacaoMatricula();
		Enumeration keys = tipoDocumentoDocumentacaoMatriculas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoDocumentoDocumentacaoMatriculas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemSituacaoMatricula() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", " "));
		Hashtable situacaoMatriculas = (Hashtable) Dominios.getSituacaoMatricula();
		Enumeration keys = situacaoMatriculas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoMatriculas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemSituacaoFinanceiraMatricula() throws Exception {
		List objs = new ArrayList(0);
		Hashtable situacaoMatriculas = (Hashtable) Dominios.getSituacaoFinanceiraMatricula();
		Enumeration keys = situacaoMatriculas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoMatriculas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>TipoMidiaCaptacao</code>.
	 */
	public void montarListaSelectItemTipoMidiaCaptacao(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarTipoMidiaCaptacaoPorNomeMidia(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TipoMidiaCaptacaoVO obj = (TipoMidiaCaptacaoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNomeMidia()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemTipoMidiaCaptacao(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>TipoMidiaCaptacao</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>TipoMidiaCaptacao</code>. Esta rotina não recebe
	 * parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemTipoMidiaCaptacao() {
		try {
			montarListaSelectItemTipoMidiaCaptacao("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nomeMidia</code> Este atributo é
	 * uma lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarTipoMidiaCaptacaoPorNomeMidia(String nomeMidiaPrm) throws Exception {
		List lista = getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorNomeMidia(nomeMidiaPrm, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Turno</code>.
	 */
	public void montarListaSelectItemTurno(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (getBanner().equals(0)) {
				resultadoConsulta = consultarTurnoPorNome(prm);
			} else {
				resultadoConsulta = getFacadeFactory().getTurnoFacade().consultarTurnoPorCodigoBanner(getBanner(), getMatriculaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
			}
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (getBanner().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			boolean primeiro = true;
			while (i.hasNext()) {
				TurnoVO obj = (TurnoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				if (primeiro && (getUsuarioLogado().getVisaoLogar().equals("aluno") || getUsuarioLogado().getVisaoLogar().equals("pais") || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador())) {
					getMatriculaVO().setTurno(obj);
					primeiro = false;
				}
			}
			ordenador = new SelectItemOrdemValor();
			if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais() || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) && objs.isEmpty()) {
				setPermitirMatriculaOnline(false);
			} else {
				setPermitirMatriculaOnline(true);
			}
			Collections.sort((List) objs, ordenador);
			setListaSelectItemTurno(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Turno</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Turno</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemTurno() {
		try {
			montarListaSelectItemTurno("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarTurnoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void inicializarMatriculaComDadosInscricao() throws Exception {
		getFacadeFactory().getMatriculaFacade().realizarMontagemDadosProcSeletivoMatricula(matriculaVO, matriculaPeriodoVO, getUsuarioLogado());
		if (matriculaVO.getCurso().getCodigo() > 0) {
			matriculaVO.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(matriculaVO.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
			getDadosRecuperadosAlteracaoProcessoMatricula().put("Curso", matriculaVO.getCurso().getCodigo());
		}
		matriculaPeriodoVO.setData(matriculaVO.getData());
		matriculaPeriodoVO.setResponsavelRenovacaoMatricula(matriculaVO.getUsuario());
		matriculaPeriodoVO.setSituacao(matriculaVO.getSituacaoFinanceira());
		montarListaSelectItemGradeCurricular();
		iniciarNovaMatriculaAluno();
		montarListaProcessoMatricula(false);
		montarListaSelectItemPeriodoLetivo();
		montarListaSelectItemPeriodoLetivoInclusaoDisciplina();
		if (Uteis.isAtributoPreenchido(realizandoNovaMatriculaAluno) && realizandoNovaMatriculaAluno && Uteis.isAtributoPreenchido(matriculaPeriodoVO.getTurma())) {
			processarDadosPertinentesTurmaSelecionada(false);
		}
	}

	public void inicializarInscricaoEDadosRelacionados() {
		matriculaVO.setUnidadeEnsino(new UnidadeEnsinoVO());
		matriculaVO.setCurso(new CursoVO());
		matriculaVO.setTurno(new TurnoVO());
		matriculaVO.setAluno(new PessoaVO());
	}

	

	public String getExibirModal() {
		if (getMatriculaVO().getExistePendenciaFinanceira()) {
			return "PF('panelNegociacao').show();";
		}
		return "PF('panelNegociacao').hide(); PF('panelConfirmarNegociacao').hide();";
	}

	public String getExibirModalVisaoAluno() {
		try {
			if (getMatriculaVO() != null) {
				if (getMatriculaVO().getExistePendenciaFinanceira()) {
					return "PF('panelNegociacao').show();;";
				}
				return "PF('panelNegociacao').hide(); PF('panelConfirmarNegociacao').hide();";
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

	public String getExibirModalConfirmacao() {
		if (getMatriculaVO().getExistePendenciaFinanceira()) {
			return "PF('panelConfirmarNegociacao').show();";
		}
		return "PF('panelNegociacao').hide(); PF('panelConfirmarNegociacao').hide();";
	}

	

	public void inicializarDadosRenovacaoMatricula() throws Exception {
		getFacadeFactory().getMatriculaFacade().inicializarDadosRenovacaoMatricula(getMatriculaVO(),  getMatriculaPeriodoVO(), getUsuarioLogado());
		montarListaSelectItemPeriodoLetivo();
		montarListaSelectItemTurma();
		montarGradeCurricular("");
		this.setMatricula_Erro("");
		setMensagemID("msg_dados_consultados");
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt, matriculaVO.getUnidadeEnsino().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				if (getValorConsultaCurso().trim().isEmpty() || getValorConsultaCurso().trim().contains("%%") || getValorConsultaCurso().trim().length() < 3) {
					throw new Exception("Informe 3 caracteres válidos para realizar a consulta.");
				}
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), matriculaVO.getUnidadeEnsino().getCodigo(), false, "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparListaConsultaCurso() {
		setListaConsultaCurso(new ArrayList(0));
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(unidadeEnsinoCurso.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			if (this.getRealizandoNovaMatriculaAluno()) {
				if (getMatriculaVO().getCurso().getCodigo() != 0 || !getMatriculaVO().getCurso().getCodigo().equals(cursoVO.getCodigo())) {
					getMatriculaPeriodoVO().setGradeCurricular(new GradeCurricularVO());
					getMatriculaPeriodoVO().setProcessoMatricula(0);
					getMatriculaPeriodoVO().setProcessoMatriculaVO(new ProcessoMatriculaVO());
					getMatriculaPeriodoVO().setPeridoLetivo(new PeriodoLetivoVO());
					setCodigoPeriodoLetivo(0);
					if(!Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula())) {
						setCodigoGradeCurricular(0);
						getMatriculaVO().setGradeCurricularAtual(new GradeCurricularVO());
						getMatriculaPeriodoVO().setDigitoTurma("");
					}
					getMatriculaPeriodoVO().getListaPeriodosLetivosValidosParaMatriculaPeriodo().clear();
					getMatriculaPeriodoVO().setTurma(new TurmaVO());
//					getMatriculaPeriodoVO().setPlanoFinanceiroCurso(new PlanoFinanceiroCursoVO());
					getMatriculaPeriodoVO().setCategoriaCondicaoPagamento("");
//					getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(new CondicaoPagamentoPlanoFinanceiroCursoVO());
					getListaSelectItemProcessoMatricula().clear();
					getListaSelectItemGradeCurricular().clear();
					getListaSelectItemPeriodoLetivoMatricula().clear();
					getListaSelectItemTurma().clear();
					getListaSelectItemPlanoFinanceiroCurso().clear();
					getListaSelectItemCategoriaPlanoFinanceiroCurso().clear();
					getMatriculaVO().setMatriculaComHistoricoAlunoVO(null);
					matriculaPeriodoVO.setUnidadeEnsinoCurso(0);
					matriculaPeriodoVO.setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
					setDadosRecuperadosAlteracaoProcessoMatricula(new HashMap<String, Integer>(0));

				}
			}
			setTurma_Erro("");
			getMatriculaVO().setCurso(cursoVO);
			getMatriculaVO().setTurno(unidadeEnsinoCurso.getTurno());
			getDadosRecuperadosAlteracaoProcessoMatricula().put("Curso", cursoVO.getCodigo());
//			if (this.realizandoNovaMatriculaAluno) {
//				if (!getMatriculaVO().getTipoMatricula().equalsIgnoreCase("EX")) {
//					getFacadeFactory().getMatriculaFacade().verificaAlunoJaMatriculado(matriculaVO, getIsPermiteInformarTipoMatricula(), getUsuarioLogado(), false);
//				}
//			}
			setMensagemDetalhada("");
			matriculaPeriodoVO.setUnidadeEnsinoCurso(unidadeEnsinoCurso.getCodigo());
			montarListaSelectItemGradeCurricular();
			if (getMatriculaPeriodoVO().getGradeCurricular().getCodigo() > 0) {
				inicializarMatriculaComHistoricoAluno(true);
			}
			montarListaProcessoMatricula(false);
			getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().clear();
			verificarPermissaoUsuarioInformarTipoMatriculaMatricula();
			selecionarPeriodoLetivo();
		} catch (Exception e) {
			setTurma_Erro("");
			getMatriculaVO().setCurso(new CursoVO());
			getMatriculaVO().setTurno(new TurnoVO());
			getListaSelectItemProcessoMatricula().clear();
			getListaSelectItemGradeCurricular().clear();
			getListaSelectItemPeriodoLetivoMatricula().clear();
			getListaSelectItemTurma().clear();
			getListaSelectItemPlanoFinanceiroCurso().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) throws Exception {
		try {
			CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(unidadeEnsinoCurso.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			if (getMatriculaVO().getCurso().getCodigo() != 0) {
				getMatriculaPeriodoVO().setGradeCurricular(new GradeCurricularVO());
				getMatriculaPeriodoVO().setProcessoMatricula(0);
				getMatriculaPeriodoVO().setPeridoLetivo(new PeriodoLetivoVO());
				setCodigoPeriodoLetivo(0);
				if(!Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula())) {
					setCodigoGradeCurricular(0);
					getMatriculaVO().setGradeCurricularAtual(new GradeCurricularVO());
					getMatriculaPeriodoVO().setDigitoTurma("");
				}
				getMatriculaPeriodoVO().getListaPeriodosLetivosValidosParaMatriculaPeriodo().clear();
				getMatriculaPeriodoVO().setTurma(new TurmaVO());
//				getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(new CondicaoPagamentoPlanoFinanceiroCursoVO());
				getListaSelectItemProcessoMatricula().clear();
				getListaSelectItemGradeCurricular().clear();
				getListaSelectItemPeriodoLetivoMatricula().clear();
				getListaSelectItemTurma().clear();
				getListaSelectItemPlanoFinanceiroCurso().clear();
				setDadosRecuperadosAlteracaoProcessoMatricula(new HashMap<String, Integer>(0));
			}
			getMatriculaVO().setCurso(cursoVO);
			getMatriculaVO().setTurno(unidadeEnsinoCurso.getTurno());
			getDadosRecuperadosAlteracaoProcessoMatricula().put("Curso", cursoVO.getCodigo());
//			if (this.realizandoNovaMatriculaAluno) {
//				if (!getMatriculaVO().getTipoMatricula().equalsIgnoreCase("EX")) {
//					getFacadeFactory().getMatriculaFacade().verificaAlunoJaMatriculado(matriculaVO, getIsPermiteInformarTipoMatricula(), getUsuarioLogado(),false);
//				}
//			}
			setMensagemDetalhada("");
			montarListaSelectItemGradeCurricular();
			montarListaProcessoMatricula(false);
			montarListaSelectItemPeriodoLetivo();
			matriculaPeriodoVO.setUnidadeEnsinoCurso(unidadeEnsinoCurso.getCodigo());
			getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().clear();
			verificarPermissaoUsuarioInformarTipoMatriculaMatricula();
		} catch (Exception e) {
			setTurma_Erro("");
			getMatriculaVO().setCurso(new CursoVO());
			getMatriculaVO().setTurno(new TurnoVO());
			getListaSelectItemProcessoMatricula().clear();
			getListaSelectItemGradeCurricular().clear();
			getListaSelectItemPeriodoLetivoMatricula().clear();
			getListaSelectItemTurma().clear();
			getListaSelectItemPlanoFinanceiroCurso().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void inicializarDefinicaoAlunoCalouroOuVeterano() throws Exception {
		Boolean alunoCalouro = getFacadeFactory().getMatriculaPeriodoFacade().verificarMatriculaDeCalouro(getMatriculaVO().getMatricula(), getMatriculaPeriodoVO().getCodigo(), false, getUsuarioLogado());
		getMatriculaPeriodoVO().setCalouro(alunoCalouro);
	}

	
	/**
	 * Processa dados pertinentes da Turma <br>
	 * Carrega as informcoes do Plano Financeiro <br>
	 * Carrega as informacoes da Condicao de Pagamento - com ou sem categoria (@param) <br>
	 * Valida permissao matricula Online <br>
	 * 
	 * @param buscarCategoriaDaTurma: Caso seja true: A categoria é consultada pelo valor que esta no objeto TurmaVO <br>
	 *								  Caso seja false: A categoria é consultada pelo valor da MatriculaPeriodoVO <br>
	 */
	public void processarDadosPertinentesTurmaSelecionada(Boolean buscarCategoriaDaTurma) {
		try {
//			limparInformacoesDoPlanoFinanceiroPreenchidos();
			if (!getMatriculaVO().getFormaIngresso().equals("PS") && !getMatriculaVO().getFormaIngresso().equals("VE")) {
				getMatriculaVO();
			}
			getListaSelectItemPlanoFinanceiroCurso().clear();
//			if (getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo() != 0) {
//				getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(new CondicaoPagamentoPlanoFinanceiroCursoVO());
//			}
			Integer codigoTurmaCarregarDados = matriculaPeriodoVO.getTurma().getCodigo();
			getMatriculaPeriodoVO().setTurma(null);
			getMatriculaPeriodoVO().getTurma().setCodigo(codigoTurmaCarregarDados);
			if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getTurma().getCodigo())) {
				getFacadeFactory().getTurmaFacade().carregarDados(this.getMatriculaPeriodoVO().getTurma(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado());
				limparDisciplinasPorPeriodoLetivo();				
			}
			codigoTurmaCarregarDados = null;			
			//atualizarSituacaoMatriculaPeriodo();
			getCarregarPermissaoObrigatorioContratoPorTurma();
			
			//Caso ainda nao tenha informacoes de matricula interrompe a rotina e evita de enviar mensagm de erro na tela, ja que o cadastro e novo
			if ((getMatriculaPeriodoVO() == null) || (getMatriculaVO() == null) || (getMatriculaPeriodoVO().getTurma().getCodigo().equals(0))) {
				return;
			}
			
			
			realizarValidacaoPermiteMatriculaOnlineAposAlteracaoDados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void processarDadosPermitinentesTurmaSelecionada() {
		processarDadosPertinentesTurmaSelecionada(true);
	}

	
	
	

	public void inicializarDadosGradeAtualMatricula() {
		if (getMatriculaVO().getMatricula().equals("")) {
			// se for uma matricula nova, logo a gradeCurriculaAtual deve ser a
			// grade que o usuario selecionou na tela
			// de matriculo / renovacao
			getMatriculaVO().setGradeCurricularAtual(getMatriculaPeriodoVO().getGradeCurricular());
		}
	}

	/**
	 * Método responsável por inicializar todos os dados importantes para uma
	 * matrícula / renovação de um aluno. Como por exemplo:
	 * MatriculaComHistoricoAlunoVO, CursoVO, ConfiguracaoAcademicaVO do curso,
	 * ProcessoCalendarioVO
	 */
	public void inicializarMatriculaComHistoricoAluno(Boolean forcarNovoCarregamentoDados) throws Exception {
		if (((getMatriculaVO().getMatriculaComHistoricoAlunoVO().getIsInicializado()) && (!forcarNovoCarregamentoDados))) {
			return;
		}
		inicializarDadosGradeAtualMatricula();
		if (getMatriculaVO().getGradeCurricularAtual().getCodigo().equals(0)) {
			// sem a definicao da grade, não é possível chamar o método de
			// inicialização.
			return;
		}
		if (Uteis.isAtributoPreenchido(getMatriculaVO().getCurso())) {
			getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getMatriculaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
		}
		ConfiguracaoAcademicoVO cfg = getAplicacaoControle().carregarDadosConfiguracaoAcademica(getMatriculaVO().getCurso().getConfiguracaoAcademico().getCodigo());
		getMatriculaVO().getCurso().setConfiguracaoAcademico(cfg);

		MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO = getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(getMatriculaVO(), getMatriculaVO().getGradeCurricularAtual().getCodigo(), false, getMatriculaVO().getCurso().getConfiguracaoAcademico(), getUsuarioLogado());
		getMatriculaVO().setMatriculaComHistoricoAlunoVO(matriculaComHistoricoAlunoVO);
		getMatriculaVO().setGradeCurricularAtual(matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
		getMatriculaPeriodoVO().setGradeCurricular(matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
		getMatriculaPeriodoVO().setPeridoLetivo(matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().consultarObjPeriodoLetivoVOPorCodigo(getMatriculaPeriodoVO().getPeridoLetivo().getCodigo()));
//		if (getMatriculaPeriodoVO().getIsNovaMatriculaPeriodo() || getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().isEmpty()) {
//			getMatriculaPeriodoVO().setMatriculaEspecial(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()).getUtilizarIntegracaoFinanceira() ? false : matriculaComHistoricoAlunoVO.getAlunoEmSituacaoParaMatriculaEspecial());
//		}
		getFacadeFactory().getMatriculaPeriodoFacade().obterListaPeriodosLetivosValidosParaRenovacaoMatriculaInicializandoPeriodoLetivoPadrao(getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaVO().getCurso().getConfiguracaoAcademico(), matriculaVO.getMatriculaComHistoricoAlunoVO(), getRealizandoNovaMatriculaAlunoPartindoTransferenciaInterna(), getUsuarioLogado());
		if (getMatriculaVO().getCurso().getConfiguracaoAcademico().getControlarAvancoPeriodoPorCreditoOuCH() 
				|| getMatriculaVO().getCurso().getConfiguracaoAcademico().getRenovacaoMatriculaSequencial()
				|| ((!getMatriculaVO().getCurso().getConfiguracaoAcademico().getPermiteEvoluirPeriodoLetivoCasoReprovado()) 
				&& getMatriculaVO().getCurso().getConfiguracaoAcademico().getIsPossuiControleDisciplinaReprovacao()
				&& !getFacadeFactory().getHistoricoFacade().executarValidarMatriculaPeriodoExcedeuLimiteMaximoDisciplinaReprovado(getMatriculaVO().getMatricula(), 
						getMatriculaVO().getCurso().getConfiguracaoAcademico().getConsiderarDisciplinasReprovadasPeriodosLetivosAnteriores(), 
						getMatriculaVO().getCurso().getConfiguracaoAcademico().getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo(), getUsuarioLogado()))
				) {
			this.setApresentarBotaoLiberarMatriculaTodosPeriodos(Boolean.TRUE);
		}
	}

	public void executarDefinicaoPeriodoLetivoNovaMatriculaAluno() throws Exception {
		if ((getMatriculaVO().getGradeCurricularAtual().getCodigo().equals(0)) || (!getMatriculaVO().getGradeCurricularAtual().getCodigo().equals(this.getCodigoGradeCurricular()))) {
			getMatriculaPeriodoVO().setPeridoLetivo(new PeriodoLetivoVO());
			setCodigoPeriodoLetivo(0);
			getDadosRecuperadosAlteracaoProcessoMatricula().remove("PeriodoLetivo");
			getDadosRecuperadosAlteracaoProcessoMatricula().remove("Turma");
			getDadosRecuperadosAlteracaoProcessoMatricula().remove("GradeCurricular");
			if(!Uteis.isAtributoPreenchido(getMatriculaPeriodoVO())) {
				getMatriculaPeriodoVO().setTurma(new TurmaVO());
//				limparInformacoesDoPlanoFinanceiroPreenchidos();
			}
			inicializarMensagemVazia();
			GradeCurricularVO novaGradeInformada = new GradeCurricularVO();
			if (!this.getCodigoGradeCurricular().equals(0)) {
				novaGradeInformada.setCodigo(this.getCodigoGradeCurricular());
				this.getMatriculaPeriodoVO().setGradeCurricular(novaGradeInformada);
			} else {
				novaGradeInformada.setCodigo(this.getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
				this.setCodigoGradeCurricular(this.getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
			}
			if(Uteis.isAtributoPreenchido(getCodigoGradeCurricular())) {
				getDadosRecuperadosAlteracaoProcessoMatricula().put("GradeCurricular", getCodigoGradeCurricular());
			}
			getMatriculaVO().setGradeCurricularAtual(novaGradeInformada);
			if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais() || getUsuarioLogado().getIsApresentarVisaoCoordenador() || getUsuarioLogado().getIsApresentarVisaoProfessor())) {
				inicializarMatriculaComHistoricoAluno(false);
			} else {
				inicializarMatriculaComHistoricoAluno(true);
			}
		}
		if (this.getRealizandoNovaMatriculaAluno()) {
			getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().clear();
			if (!((getMatriculaVO().getMatriculaComHistoricoAlunoVO().getIsInicializado()))) {
				inicializarMatriculaComHistoricoAluno(false);
			}
		}
		montarListaSelectItemPeriodoLetivo();
		prepararDadosTurma();
		realizarValidacaoPermiteMatriculaOnlineAposAlteracaoDados();
	}
	
	
	public void atualizarSituacaoMatriculaPeriodoProcessoMatriculaTela() {
		try {
			atualizarSituacaoMatriculaPeriodoPartindoProceMatricula();
			processarDadosPermitinentesTurmaSelecionada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
		
	}
	
	
	
	
	

	public void atualizarSituacaoMatriculaPeriodoPartindoProceMatricula() {
		boolean voltarProcessoMatricula = false;
	try {
		setOncompleteModal("");
		if ((matriculaPeriodoVO.getProcessoMatricula() == null) || (matriculaPeriodoVO.getProcessoMatricula().equals(0))) {
			setMensagemID("msg_entre_dados");
			return;
		}
		String anoSemestre = matriculaPeriodoVO.getAnoSemestreCodigo();
		executarDefinicaoPeriodoLetivoNovaMatriculaAluno();
		gerarDadosGraficoEvolucaoAcademicaAluno();
		getFacadeFactory().getMatriculaPeriodoFacade().validarDadosRenovacaoProcessoMatriculaPeriodoLetivo(this.getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado(), null);
		matriculaPeriodoVO.getProcessoMatriculaVO().setCodigo(matriculaPeriodoVO.getProcessoMatricula());
		getFacadeFactory().getProcessoMatriculaFacade().carregarDados(matriculaPeriodoVO.getProcessoMatriculaVO(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado());
		matriculaPeriodoVO.setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
		getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(matriculaVO, matriculaPeriodoVO, NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado());
		getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosAnoSemestreMatricula(getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), false);

		if (getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
			if (getMatriculaPeriodoVO().getProcessoMatriculaVO().getApresentarTermoAceite() && Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline())) {
				TextoPadraoVO texto = getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline().getCodigo(), 
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getMatriculaPeriodoVO().getProcessoMatriculaVO().setTextoPadraoContratoRenovacaoOnline(texto);
//				if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso())) {
//					getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade()
//							.consultarPorChavePrimaria(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//				}
//				validarApresentacaoTermoAceitePorDesignTextoPadrao();
			}
		}
		
		if (getMatriculaVO().getCurso().getNivelEducacional().equals("GT") || getMatriculaVO().getCurso().getNivelEducacional().equals("SU")) {
			voltarProcessoMatricula = true;
			getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatriculaPeriodoLetivoAtivoAnoSemestre(this.getMatriculaVO().getMatricula(), getMatriculaPeriodoVO().getAno(), getMatriculaPeriodoVO().getSemestre(), getMatriculaPeriodoVO().getCodigo(), getUsuarioLogado());
			voltarProcessoMatricula = false;
		}
		getFacadeFactory().getMatriculaPeriodoFacade().definirSituacaoMatriculaPeriodoComBaseProcesso(this.matriculaVO, this.matriculaPeriodoVO, getUsuarioLogado());
		if(!anoSemestre.equals(matriculaPeriodoVO.getAnoSemestreCodigo()) && Uteis.isAtributoPreenchido(matriculaPeriodoVO.getTurma())){
			getMatriculaPeriodoVO().setTurma(new TurmaVO());
//			getMatriculaPeriodoVO().setPlanoFinanceiroCurso(new PlanoFinanceiroCursoVO());
//			getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(new CondicaoPagamentoPlanoFinanceiroCursoVO());
			prepararDadosTurma();
		}
		
		if ((matriculaPeriodoVO.getTurma() == null) || (matriculaPeriodoVO.getTurma().getCodigo().intValue() == 0)) {
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			return;
		}
		matriculaPeriodoVO.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getTurma().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado()));
		realizarValidacaoPermiteMatriculaOnlineAposAlteracaoDados();
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	} catch (Exception e) {
		if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO()) && voltarProcessoMatricula) {
			getMatriculaPeriodoVO().setProcessoMatricula(getMatriculaPeriodoVO().getProcessoMatriculaOriginal().getCodigo());
			try {
				getMatriculaPeriodoVO().setProcessoMatriculaVO((ProcessoMatriculaVO) getMatriculaPeriodoVO().getProcessoMatriculaOriginal().clone());
				if (getMatriculaPeriodoVO().getAnoSemestreOriginal().length() > 4) {
					getMatriculaPeriodoVO().setAno(getMatriculaPeriodoVO().getAnoSemestreOriginal().substring(0, 4));
				}
				if (getMatriculaPeriodoVO().getAnoSemestreOriginal().length() >= 6) {
					getMatriculaPeriodoVO().setSemestre(getMatriculaPeriodoVO().getAnoSemestreOriginal().substring(5, 6));
				}
				getFacadeFactory().getProcessoMatriculaFacade().carregarDados(matriculaPeriodoVO.getProcessoMatriculaVO(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado());
				matriculaPeriodoVO.setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
				getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(matriculaVO, matriculaPeriodoVO, NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado());
				getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosAnoSemestreMatricula(getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	}
}

	
	
	
	public void montarListaSelectItemTurma(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			
			getListaSelectItemTurma().clear();
			
			List objs = new ArrayList(0);
			if ((matriculaVO.getCurso() == null) || (matriculaVO.getCurso().getCodigo().intValue() == 0)) {
				setListaSelectItemTurma(objs);
				return;
			}
			if ((matriculaVO.getTurno() == null) || (matriculaVO.getTurno().getCodigo().intValue() == 0)) {

				setListaSelectItemTurma(objs);
				return;
			}
			if ((matriculaPeriodoVO.getPeridoLetivo() == null) || (matriculaPeriodoVO.getPeridoLetivo().getCodigo().intValue() == 0)) {
				setListaSelectItemTurma(objs);
				return;
			}
			resultadoConsulta = consultarTurmaPorIdentificadorTurma();
			i = resultadoConsulta.iterator();			
			boolean turmaVinculada = false ;
			while (i.hasNext()) {
				TurmaVO obj = (TurmaVO) i.next();
				if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma()));
				} else if (obj.getApresentarRenovacaoOnline()) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma()));
				}
				if(!turmaVinculada && Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getCodigo()) 
					&& Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getTurma().getCodigo())
					&& getMatriculaPeriodoVO().getTurma().getCodigo().equals(obj.getCodigo())){
					turmaVinculada = true;
				}
			}
			if(!turmaVinculada && Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getCodigo()) 
					&& Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getTurma().getCodigo())){
				    if(!Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getTurma().getGradeCurricularVO().getCodigo())){
				    	getFacadeFactory().getTurmaFacade().carregarDados(getMatriculaPeriodoVO().getTurma(), getUsuarioLogado());
				    }	
					if(getMatriculaPeriodoVO().getTurma().getGradeCurricularVO().getCodigo().equals(getMatriculaVO().getGradeCurricularAtual().getCodigo())
					&& getMatriculaPeriodoVO().getTurma().getPeridoLetivo().getCodigo().equals(getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo())){

						
						//Valida se o objeto ja esta inserido na lista
						boolean present = objs.stream().filter(p-> 
						{
							SelectItem a = (SelectItem) p;
							Integer value = (Integer) a.getValue();
							return value.equals(getMatriculaPeriodoVO().getTurma().getCodigo());
						}).findFirst().isPresent();
						
						if(!present)
							objs.add(new SelectItem(getMatriculaPeriodoVO().getTurma().getCodigo(), getMatriculaPeriodoVO().getTurma().getIdentificadorTurma()));
						
					}
			}
			if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais() || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) && objs.isEmpty()) {
				setPermitirMatriculaOnline(false);
			} else {
				setPermitirMatriculaOnline(true);
			}
			if (objs.isEmpty()) {
				this.setTurma_Erro("Não existe turma cadastrada para o curso ou a configuração financeira não está configurada de forma correta ( Deve ser definida dentro da turma ou no vinculo do curso com a unidade de ensino )!");
				setPermitirMatriculaOnline(false);
				// this.setTurma_Erro("Não existe turma cadastrada para o curso ou a configuração financeira não está definida na turma ou no vinculo do curso com a unidade de ensino!"
				// + getMatriculaVO().getCurso().getNome().toUpperCase() +
				// " no período: " +
				// getMatriculaVO().getTurno().getNome().toUpperCase());
			}
			
			if ((getMatriculaPeriodoVO().getCodigo().equals(0))) {		
				if(!getMatriculaPeriodoVO().getDigitoTurma().equals("")) {
					Iterator turmaVO = null;
					turmaVO = resultadoConsulta.iterator();	
					while (turmaVO.hasNext()) {
						TurmaVO obj = (TurmaVO) turmaVO.next();
						if(getMatriculaPeriodoVO().getDigitoTurma().equals(obj.getDigitoTurma())) {
							getMatriculaPeriodoVO().getTurma().setCodigo(obj.getCodigo());
							break;
						}
						
					}
					
				}
				processarDadosPertinentesTurmaSelecionada(!getUsuarioLogado().getIsApresentarVisaoAdministrativa());
				
			}
			if(getMatriculaPeriodoVO().getTurma().getCodigo().equals(0) && !objs.isEmpty()) {
				SelectItem selectItem = (SelectItem) objs.get(0);
				getMatriculaPeriodoVO().getTurma().setCodigo((Integer) selectItem.getValue());
				if(getDadosRecuperadosAlteracaoProcessoMatricula().isEmpty() || !getDadosRecuperadosAlteracaoProcessoMatricula().containsKey("Turma")) {
					getDadosRecuperadosAlteracaoProcessoMatricula().put("Turma",	getMatriculaPeriodoVO().getTurma().getCodigo());
				}
				processarDadosPertinentesTurmaSelecionada(!getUsuarioLogado().getIsApresentarVisaoAdministrativa());
			}			
			if (getBanner().equals(0) && getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				objs.add(0, new SelectItem(0, ""));
			}
			this.setTurma_Erro("");
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemTurma(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void verificarLayoutPadrao() throws Exception {
		LayoutPadraoVO layoutPadraoVO = layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("comprovanteMatricula", "tipoDesignRelatorioComprovanteMatricula", false, getUsuarioLogado());
		if (!layoutPadraoVO.getValor().equals("")) {
			setTipoLayoutComprovanteMatricula(Integer.parseInt(layoutPadraoVO.getValor()));
		}
	}

	public void selecionarPeriodoLetivo() {
		try {
			PeriodoLetivoVO novoPeriodoSelecionado = new PeriodoLetivoVO();
			novoPeriodoSelecionado.setCodigo(this.codigoPeriodoLetivo);
			this.getMatriculaPeriodoVO().setPeridoLetivo(novoPeriodoSelecionado);
			getMatriculaPeriodoVO().setTurma(new TurmaVO());
//			limparInformacoesDoPlanoFinanceiroPreenchidos();
			getDadosRecuperadosAlteracaoProcessoMatricula().remove("PeriodoLetivo");
			getDadosRecuperadosAlteracaoProcessoMatricula().remove("Turma");
			if(Uteis.isAtributoPreenchido(codigoPeriodoLetivo)) {
				getDadosRecuperadosAlteracaoProcessoMatricula().put("PeriodoLetivo", codigoPeriodoLetivo);
			}
			// Atualizando os dados periodo letivo da MatriculaPeriodo, com os
			// dados já montados da grade me matriculaComHistorico
			PeriodoLetivoVO periodoLetivoComDadosCarregados = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().consultarObjPeriodoLetivoVOPorCodigo(matriculaPeriodoVO.getPeriodoLetivo().getCodigo());
			matriculaPeriodoVO.setPeridoLetivo(periodoLetivoComDadosCarregados);
			limparDisciplinasPorPeriodoLetivo();
			prepararDadosTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void prepararDadosTurma() {
		try {
			montarListaSelectItemTurma();
			if (!getDadosRecuperadosAlteracaoProcessoMatricula().isEmpty()
					&& (getDadosRecuperadosAlteracaoProcessoMatricula().containsKey("GradeCurricular") && getDadosRecuperadosAlteracaoProcessoMatricula().get("GradeCurricular").equals(getMatriculaPeriodoVO().getGradeCurricular().getCodigo()))
					&& (getDadosRecuperadosAlteracaoProcessoMatricula().containsKey("Curso") && getDadosRecuperadosAlteracaoProcessoMatricula().get("Curso").equals(getMatriculaVO().getCurso().getCodigo()))
					&& (getDadosRecuperadosAlteracaoProcessoMatricula().containsKey("PeriodoLetivo") && getDadosRecuperadosAlteracaoProcessoMatricula().get("PeriodoLetivo").equals(getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo()))
					&& (getDadosRecuperadosAlteracaoProcessoMatricula().containsKey("Turma") && getDadosRecuperadosAlteracaoProcessoMatricula().get("Turma").equals(getMatriculaPeriodoVO().getTurma().getCodigo()))) {
				return;
			}
			if(!Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getTurma())) {
//				limparInformacoesDoPlanoFinanceiroPreenchidos();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

//	private void limparInformacoesDoPlanoFinanceiroPreenchidos() {
//		setListaSelectItemPlanoFinanceiroCurso(new ArrayList());
//		setListaSelectItemCategoriaPlanoFinanceiroCurso(new ArrayList());
//		setListaSelectItemCondicoesDePagamentoDoPlanoFinanceiroCurso(new ArrayList());
//		
////		this.getMatriculaPeriodoVO().setPlanoFinanceiroCurso(null);
////		this.getMatriculaPeriodoVO().setCategoriaCondicaoPagamento(null);
////		this.getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(null);
//	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Turma</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Turma</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemTurma() {
		try {
			montarListaSelectItemTurma("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>identificadorTurma</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os
	 * valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarTurmaPorIdentificadorTurma() throws Exception {
		List listaResultado = new ArrayList<TurmaVO>();
		if(Uteis.isAtributoPreenchido(getMatriculaVO().getCurso().getCodigo())
			&& Uteis.isAtributoPreenchido(getMatriculaVO().getUnidadeEnsino().getCodigo())
			&& Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getGradeCurricular().getCodigo())
			&& Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo())
			&& Uteis.isAtributoPreenchido(getMatriculaVO().getTurno().getCodigo())
			&& ((getMatriculaVO().getCurso().getPeriodicidade().equals("SE") && Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getAno()) && Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getSemestre()))
			|| (getMatriculaVO().getCurso().getPeriodicidade().equals("AN") && Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getAno()))
			|| (getMatriculaVO().getCurso().getPeriodicidade().equals("IN")))
				) {
		listaResultado = getFacadeFactory().getTurmaFacade().consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurno(getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo(), getMatriculaVO().getUnidadeEnsino().getCodigo(), getMatriculaVO().getCurso().getCodigo(), getMatriculaVO().getTurno().getCodigo(), getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), this.getRealizandoNovaMatriculaAluno(), getRenovandoMatricula(), getEditandoMatricula(), false, getMatriculaPeriodoVO().getAno(), getMatriculaPeriodoVO().getSemestre(), false, getUsuarioLogado(), getUsuarioLogado().getIsApresentarVisaoAlunoOuPais());
		}
		return listaResultado;
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Inscricao</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarMatriculaPorChavePrimaria() throws Exception {
		try {
			getFacadeFactory().getMatriculaFacade().inicializarDadosRenovacaoMatricula(matriculaVO,  matriculaPeriodoVO, getUsuarioLogado());
		} catch (Exception e) {
			novo();
			setTurma_Erro("");
			setMensagemID("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			inicializarInscricaoEDadosRelacionados();
			this.setMatricula_Erro(getMensagemInternalizacao("msg_erro_matriculanaoencontrada"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Curso</code> por meio de sua respectiva chave primária. Esta rotina
	 * é utilizada fundamentalmente por requisições Ajax, que realizam busca
	 * pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarCursoPorChavePrimaria() {
		try {
			Integer campoConsulta = matriculaVO.getCurso().getCodigo();
			CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			matriculaVO.setCurso(curso);
			// getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(this.getMatriculaVO());
			this.setCurso_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			matriculaVO.setCurso(new CursoVO());
			this.setCurso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (getBanner().equals(0)) {
				resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			} else {
				resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoDoProcessoMatriculaPorCodigoBanner(getBanner(), getUsuarioLogado());
			}
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (getBanner().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			boolean primeiro = true;
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				if (primeiro && getRealizandoNovaMatriculaAluno() && (getUsuarioLogado().getVisaoLogar().equals("aluno") || getUsuarioLogado().getVisaoLogar().equals("pais") || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador())) {
					getMatriculaVO().setUnidadeEnsino(obj);
					primeiro = false;
				}
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais() || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) && objs.size() == 0) {
				setPermitirMatriculaOnline(false);
			} else {
				setPermitirMatriculaOnline(true);
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemTipoMidiaCaptacao();
		montarListaProcessoMatricula(false);
		montarListaSelectItemGradeCurricular();
		montarListaSelectItemPeriodoLetivo();
		montarListaSelectItemPeriodoLetivoInclusaoDisciplina();
		montarListaSelectItemTurma();
	
//		montarListaSelectItemPlanoDesconto();
//		montarListaSelectItemDescontoProgressivo();
		montarListaSelectItemDescontoAlunoMatricula();
//		montarListaSelectItemCategoriaCondicaoPagamentoPlanoFinanceiroCurso();
		montarListaSelectItemPeriodoLetivoAdicionar();
		montarListaSelectItemDisciplina();
		setListaSelectItemTurmaAdicionar(new ArrayList(0));
		setListaSelectItemDisciplinaEquivalenteAdicionar(new ArrayList(0));
		setListaSelectItemMapaEquivalenciaDisciplinaIncluir(new ArrayList(0));
		montarListaSelectItemDescontoAlunoParcela();
		setListaSelectItemDescontoProgresivo(new ArrayList(0));
//		montarListaSelectItemCentroReceita();
//		montarListaSelectItemFormaPagamento();
		
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Pessoa</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = matriculaVO.getAluno().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			matriculaVO.setAluno(pessoa);
			// matriculaVO.getAluno().setNome(pessoa.getNome());
			this.setAluno_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			matriculaVO.getAluno().setCPF("");
			matriculaVO.getAluno().setNome("");
			matriculaVO.getAluno().setCodigo(0);
			this.setAluno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}

	}

	public void consultarAlunoPorCPF() {
		try {
			String campoConsulta = matriculaVO.getAluno().getCPF();
			List resultado = getFacadeFactory().getPessoaFacade().consultarPorCPF(campoConsulta, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			PessoaVO pessoa = (PessoaVO) resultado.get(0);
			matriculaVO.setAluno(pessoa);
			this.setAluno_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			matriculaVO.getAluno().setCPF("");
			matriculaVO.getAluno().setNome("");
			matriculaVO.getAluno().setCodigo(0);
			this.setAluno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}

	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Pessoa</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarUsuarioMatriculaPorChavePrimaria() {
		try {
			Integer campoConsulta = matriculaVO.getUsuario().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			matriculaVO.getUsuario().setNome(pessoa.getNome());
			setUsuario_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			matriculaVO.getUsuario().setNome("");
			matriculaVO.getUsuario().setCodigo(0);
			setUsuario_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public void consultarUsuarioResponsavelMatriculaPorChavePrimaria() {
		try {
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getMatriculaVO().getUsuario().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getMatriculaVO().setUsuario(usuario);
			setUsuario_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			matriculaVO.getUsuario().setNome("");
			matriculaVO.getUsuario().setCodigo(0);
			setUsuario_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Pessoa</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarResponsavelRenovacaoMatriculaPorChavePrimaria() {
		try {
			Integer campoConsulta = matriculaPeriodoVO.getResponsavelRenovacaoMatricula().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			matriculaPeriodoVO.getResponsavelRenovacaoMatricula().setNome(pessoa.getNome());
			setResponsavelRenovacaoMatricula_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			matriculaPeriodoVO.getResponsavelRenovacaoMatricula().setNome("");
			matriculaPeriodoVO.getResponsavelRenovacaoMatricula().setCodigo(0);
			setResponsavelRenovacaoMatricula_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}

	}

	public void selecionarTurma() {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaTelaConsulta(obj);
		getControleConsulta().setValorConsulta(obj.getIdentificadorTurma());
	}

	public Boolean getDisabilitarCampoTurma() {
		if (getControleConsulta().getCampoConsulta().equals("turma")) {
			return true;
		} else {
			return false;
		}
	}

	public String getValidarDadosStyleClassCampo() {
		if (getControleConsulta().getCampoConsulta().equals("turma")) {
			return "camposSomenteLeitura";
		} else {
			return "campos";
		}
	}

	public void consultarTurma() {
		try {
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), 0, this.getUnidadeEnsinoLogado().getCodigo(), false, false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparConsultaTurma() {
		getControleConsulta().setValorConsulta("");
	}

	public Boolean getApresentarComboBoxSituacao() {
		if (getControleConsulta().getCampoConsulta().equals("nomeCurso") || getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarComboBoxFiltroTurno() {
		if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
			return true;
		}
		return false;
	}

	public Boolean getSituacaoFinanceira() {
		if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
			return true;
		}
		return false;
	}

	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','99/99/9999',event);";
		}
		if (getControleConsulta().getCampoConsulta().equals("cpf")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','999.999.999-99',event);";
		}
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("turma", "Turma"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("cpf", "CPF"));
		// itens.add(new SelectItem("situacao", "Situação"));
		// itens.add(new SelectItem("situacaoFinanceira",
		// "Situação Financeira"));
		return itens;
	}

	public List getComboBoxTipoMatricula() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("NO", "CONTRATO PÓS"));
		itens.add(new SelectItem("EX", "CONTRATO EXTENSÃO"));
		// itens.add(new SelectItem("MO", "CONTRATO ESPECIAL"));
		return itens;
	}

	public List getComboBoxTipoLayout() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("1", "Layout 1"));
		itens.add(new SelectItem("2", "Layout 2"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public List getTipoConsultaComboAlunoNovaMatricula() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("Aluno", "Aluno"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		return itens;
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		return itens;
	}

	public List getTipoConsultaComboCandidato() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomeCandidato", "Candidato"));
		itens.add(new SelectItem("cpfCandidato", "CPF Candidato"));
		itens.add(new SelectItem("nrInscricao", "N° Inscrição"));
		return itens;
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public boolean getExisteUnidadeEnsino() {
		if (getMatriculaVO().getUnidadeEnsino().getCodigo().intValue() != 0 || !(getMatriculaVO().getUnidadeEnsino().getNome().equals(""))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		try {
			removerObjetoMemoria(this);
			removerObjetoMemoria(this);
			// setPaginaAtualDeTodas("0/0");
			setListaConsulta(new ArrayList(0));
			// definirVisibilidadeLinksNavegacao(0, 0);
			montarListaSelectItemTurno();
			verificarApresentarBotoesMatricularRenovar();
			consultar();
			setMensagemID("msg_entre_prmconsulta");
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaCons.xhtml");
		}
	}

	public List getListaSelectItemPeriodoLetivoMatricula() {
		if (listaSelectItemPeriodoLetivoMatricula == null) {
			listaSelectItemPeriodoLetivoMatricula = new ArrayList(0);
		}
		return (listaSelectItemPeriodoLetivoMatricula);
	}

	public void setListaSelectItemPeriodoLetivoMatricula(List listaSelectItemPeriodoLetivoMatricula) {
		this.listaSelectItemPeriodoLetivoMatricula = listaSelectItemPeriodoLetivoMatricula;
	}

	public String getResponsavelRenovacaoMatricula_Erro() {
		if (responsavelRenovacaoMatricula_Erro == null) {
			responsavelRenovacaoMatricula_Erro = "";
		}
		return responsavelRenovacaoMatricula_Erro;
	}

	public void setResponsavelRenovacaoMatricula_Erro(String responsavelRenovacaoMatricula_Erro) {
		this.responsavelRenovacaoMatricula_Erro = responsavelRenovacaoMatricula_Erro;
	}

	public List getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList(0);
		}
		return (listaSelectItemGradeCurricular);
	}

	public void setListaSelectItemGradeCurricular(List listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public DocumetacaoMatriculaVO getDocumetacaoMatriculaVO() {
		if (documetacaoMatriculaVO == null) {
			documetacaoMatriculaVO = new DocumetacaoMatriculaVO();
		}
		return documetacaoMatriculaVO;
	}

	public void setDocumetacaoMatriculaVO(DocumetacaoMatriculaVO documetacaoMatriculaVO) {
		this.documetacaoMatriculaVO = documetacaoMatriculaVO;
	}

	public List getListaSelectItemTipoMidiaCaptacao() {
		if (listaSelectItemTipoMidiaCaptacao == null) {
			listaSelectItemTipoMidiaCaptacao = new ArrayList(0);
		}
		return (listaSelectItemTipoMidiaCaptacao);
	}

	public void setListaSelectItemTipoMidiaCaptacao(List listaSelectItemTipoMidiaCaptacao) {
		this.listaSelectItemTipoMidiaCaptacao = listaSelectItemTipoMidiaCaptacao;
	}

	public List getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList(0);
		}
		return (listaSelectItemTurno);
	}

	public void setListaSelectItemTurno(List listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public String getUsuario_Erro() {
		if (usuario_Erro == null) {
			usuario_Erro = "";
		}
		return usuario_Erro;
	}

	public void setUsuario_Erro(String usuario_Erro) {
		this.usuario_Erro = usuario_Erro;
	}

	public String getMatricula_Erro() {
		if (matricula_Erro == null) {
			matricula_Erro = "";
		}
		return matricula_Erro;
	}

	public void setMatricula_Erro(String matricula_Erro) {
		this.matricula_Erro = matricula_Erro;
	}

	public String getCurso_Erro() {
		if (curso_Erro == null) {
			curso_Erro = "";
		}
		return curso_Erro;
	}

	public void setCurso_Erro(String curso_Erro) {
		this.curso_Erro = curso_Erro;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getAluno_Erro() {
		if (aluno_Erro == null) {
			aluno_Erro = "";
		}
		return aluno_Erro;
	}

	public void setAluno_Erro(String aluno_Erro) {
		this.aluno_Erro = aluno_Erro;
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

	
	public String getTurma_Erro() {
		if (turma_Erro == null) {
			turma_Erro = "";
		}
		return turma_Erro;
	}

	public void setTurma_Erro(String turma_Erro) {
		this.turma_Erro = turma_Erro;
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

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>identificadorTurma</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os
	 * valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarTurmaPorIdentificadorTurma(String identificadorTurmaPrm) throws Exception {
		List lista = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(identificadorTurmaPrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	public void liberarMatricula() {
		try {
			//getFacadeFactory().getMatriculaPeriodoFacade().liberarPagamentoMatricula(matriculaVO, matriculaPeriodoVO, getProcessoCalendarioMatriculaVO(), getUsuarioLogado(),  getUsuarioLogado());
			setMensagemID("msg_matricula_liberarMatricula");
		} catch (Exception e) {
			setMatriculaForaPrazo(false);
			matriculaPeriodoVO.setResponsavelLiberacaoMatricula(new UsuarioVO());
			matriculaPeriodoVO.setDataLiberacaoMatricula(null);
			matriculaPeriodoVO.setSituacao("PF");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		// try {
		// getFacadeFactory().getMatriculaPeriodoFacade().liberarPagamentoMatricula(matriculaVO,
		// matriculaPeriodoVO, getProcessoCalendarioMatriculaVO(),
		// getUsuarioLogado());
		// setProcessoCalendarioMatriculaVO(verificarProcessoMatriculaDentroPrazo(getMatriculaPeriodoVO().getUnidadeEnsinoCurso()));
		// if (getMatriculaForaPrazo()) {
		// return;
		// }
		// matriculaVO.atualizarSituacaoDocumentacaoMatriculaVO();
		// adicionarMatriculaPeriodo();
		// matriculaVO.setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
		// if (matriculaVO.isNovoObj().booleanValue()) {
		// matriculaFacade.incluir(matriculaVO,
		// getProcessoCalendarioMatriculaVO(),
		// getCondicaoPagamentoPlanoFinanceiroCursoVO());
		// } else {
		// inicializarUsuarioResponsavelMatriculaUsuarioLogado();
		// matriculaFacade.alterar(matriculaVO,
		// getProcessoCalendarioMatriculaVO(),
		// getCondicaoPagamentoPlanoFinanceiroCursoVO());
		// }
		//
		// setMensagemID("msg_matricula_liberarMatricula");
		// } catch (Exception e) {
		// matriculaPeriodoVO.setResponsavelLiberacaoMatricula(new UsuarioVO());
		// matriculaPeriodoVO.setDataLiberacaoMatricula(null);
		// matriculaPeriodoVO.setSituacao("PF");
		// setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		// }
	}

	
	

	public void imprimirContrato(String tipoContrato) throws Exception {		
		setCaminhoRelatorio(getFacadeFactory().getImpressaoContratoFacade().imprimirContratoRenovarMatricula(tipoContrato, getMatriculaVO(), getMatriculaPeriodoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
		if (!Uteis.isAtributoPreenchido(getCaminhoRelatorio())) {
			setImprimirContrato(true);
			setFazerDownload(false);
		}
		if (Uteis.isAtributoPreenchido(getCaminhoRelatorio())) {
			setImprimirContrato(false);
			setFazerDownload(true);
		}
	}

	public void emitirContratoMatricula() {
		try {
			setMensagemID("msg_dados_consultados");
			setTipoContratoMatricula(TipoContratoMatriculaEnum.NORMAL);
			setAbrirModalSelecaoContrato(false);
			getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().clear();
			setQuantidadeDocumentoAssinadoMatriculaPeriodo(null);
//			if (matriculaPeriodoVO.getPlanoFinanceiroCurso().getCodigo().intValue() == 0) {
//				setMensagemID("msg_matricula_semcontratodefinido");
//				setImprimirContrato(false);
//				return;
//			} else {
//				realizarCarregamentoPossiveisContratos();
//				if(!getAbrirModalSelecaoContrato()) {
//				setImprimirContrato(true);
//				if (validarAssinaturaDigitalHabilitada(getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getMatriculaPeriodoVO().getContratoMatricula().getCodigo())) {
//					getMatriculaPeriodoVO().setListaDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//					setQuantidadeDocumentoAssinadoMatriculaPeriodo(getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().size());
//					setImprimirContrato(false);
//					if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() && getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().isEmpty()) {
//						imprimirContratoAssinaturaDigital(true);
//					}
//					
//					if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() && !getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().isEmpty()) {
//						setDocumentoAssinadoVO(getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().get(0));
//						if(getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorCertisign() && 								
//							getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(
//										documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && !documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))) {
//							getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(getDocumentoAssinadoVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
//							setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"+ getDocumentoAssinadoVO().getArquivo().getPastaBaseArquivo().replace("\\", "/") +"/"+ getDocumentoAssinadoVO().getArquivo().getNome());
//						}
//						if (getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
//							getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(getDocumentoAssinadoVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
//							setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"+ getDocumentoAssinadoVO().getArquivo().getPastaBaseArquivo().replace("\\", "/") +"/"+ getDocumentoAssinadoVO().getArquivo().getNome());
//							setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + getDocumentoAssinadoVO().getArquivo().getNome());
//						}
//						else if (getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorSei()) {
//							setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + getDocumentoAssinadoVO().getArquivo().getNome());
//						}
//												
//					}
//					setHabilitadoAssinarEletronicamente(true);
//				}else {
//					imprimirContrato("MA");
//				}
//			}
//		}
		} catch (Exception e) {
			setImprimirContrato(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), true);
		}
	}

	public String getContrato() {
		String retorno = "";
		if(getAbrirModalSelecaoContrato()) {			
			retorno = "PF('panelSelecionarContrato').show()";
		}else if (getImprimirContrato() && (getUsuarioLogado().getIsApresentarVisaoPais() || getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoCoordenador() || getUsuarioLogado().getIsApresentarVisaoProfessor()) ) {
			retorno = "abrirPopup('../VisualizarContrato', 'RelatorioContrato', 730, 545)";
		}else if (getImprimirContrato() && (getUsuarioLogado().getIsApresentarVisaoAdministrativa())) {
			retorno = "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545)";
		}else if (getHabilitadoAssinarEletronicamente() && getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().isEmpty()) {
			return "PF('panelAssinarDocumento').show()";
		}else if (getHabilitadoAssinarEletronicamente()) {
			if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
				if(getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorCertisign() && 
						getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(
								documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))) {
					return "SignSingleDocument('"+getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().filter(
							documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)).findFirst().get().getUrlAssinatura()+"');";
				}
				if(getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorTechCert() &&
						getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(
								documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))) {
					return "SignSingleDocument('"+getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().filter(
							documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)).findFirst().get().getUrlAssinatura()+"');";
				}
				return "PF('panelDocumentoContratoAssinado').show()";
			}else {
				return "PF('panelDocumentoAssinado').show()";
			}
//		}else if (getFazerDownload() && !getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().isEmpty() && getMatriculaPeriodoVO().getRegerarDocumentoAssinado()) {
//			return getUrlDonloadSV();
		}else if (getFazerDownload()) {
			return getDownload();
		}
		return retorno;
	}

	public void emitirContratoFiador() {
		try {
			setTipoContratoMatricula(TipoContratoMatriculaEnum.FIADOR);
			setAbrirModalSelecaoContrato(false);
			if (matriculaPeriodoVO.getContratoFiador().getCodigo().equals(0)) {
				setMensagemID("msg_matricula_semcontratodefinido");
				setImprimirContrato(false);
				return;
			} else {
				realizarCarregamentoPossiveisContratos();
				if(!getAbrirModalSelecaoContrato()) {
				setImprimirContrato(true);
				imprimirContrato("FI");
			}
			}
		} catch (Exception e) {
			setImprimirContrato(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void emitirContratoExtensao() {
		try {
			setTipoContratoMatricula(TipoContratoMatriculaEnum.EXTENSAO);
			setAbrirModalSelecaoContrato(false);
			if (matriculaPeriodoVO.getContratoExtensao().getCodigo().equals(0)) {
				setMensagemID("msg_matricula_semcontratodefinido");
				setImprimirContrato(false);
				return;
			} else {
				realizarCarregamentoPossiveisContratos();
				if(!getAbrirModalSelecaoContrato()) {
				setImprimirContrato(true);
				imprimirContrato("EX");
			}
			}
		} catch (Exception e) {
			setImprimirContrato(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getRelatorioMatricula() {
		String retorno = "";
		if (isExibirMatricula()) {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema = new ConfiguracaoGeralSistemaVO();
			try {
				configuracaoGeralSistema = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getMatriculaVO().getUnidadeEnsino().getCodigo());
			} catch (Exception e) {
				e.printStackTrace();
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
			if(getUsuarioLogado().getIsApresentarVisaoAluno()|| getUsuarioLogado().getIsApresentarVisaoAluno()){
				retorno= "abrirPopup('../DadosMatriculaSV?matriculaPeriodo=" + matriculaPeriodoVO.getCodigo() + "&titulo=matricula&configuracaoGeralSistema=" + configuracaoGeralSistema.getCodigo() + "', 'dadosMatricula', 780, 585)";	
			} else if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				retorno= "abrirPopup('../../DadosMatriculaSV?matriculaPeriodo=" + matriculaPeriodoVO.getCodigo() + "&titulo=matricula&configuracaoGeralSistema=" + configuracaoGeralSistema.getCodigo() + "', 'dadosMatricula', 780, 585)";
			}
			
		}
		return retorno;
	}

	public void imprimirPlanoEstudoMatricula() {
		try {
			String titulo = null;
			String design = null;
			List listaObjetos = null;
			titulo = " PLANO DE ESTUDO ";
			design = DadosMatriculaRel.getDesignIReportRelatorio();
			listaObjetos = getFacadeFactory().getDadosMatriculaRelFacade().criarObjeto(getMatriculaPeriodoVO().getCodigo(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(DadosMatriculaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(DadosMatriculaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getNome());
				getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				setMensagemID("msg_relatorio_ok");
				realizarImpressaoRelatorio();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void imprimirComprovanteMatricula() {
		String titulo = null;
		String design = null;
		String nomeRelatorio = null;
		String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
		List listaObjetos = null;
		try {
			if (getTipoLayoutComprovanteMatricula() == 2) {
				titulo = "FICHA DE INSCRIÇÃO";
				design = ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio().substring(0, ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio().lastIndexOf(".")) + getTipoLayoutComprovanteMatricula() + ".jrxml";
				nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidade() + getTipoLayoutComprovanteMatricula();
			} else {
				if (getMatriculaVO().getCurso().getNivelEducacional().equals("PO") || getMatriculaVO().getCurso().getNivelEducacional().equals("EX")) {
					titulo = "FICHA DE INSCRIÇÃO";
					design = ComprovanteRenovacaoMatriculaRel.getDesignIReportRelatorio();
					nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidade();
				} else {
					titulo = "COMPROVANTE DE RENOVAÇÃO DE MATRÍCULA";
					ConfiguracoesVO configuracoesVO = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, super.getUnidadeEnsinoLogado().getCodigo());
					if (configuracoesVO == null || configuracoesVO.getLayoutPadraoComprovanteMatricula() == null || configuracoesVO.getLayoutPadraoComprovanteMatricula().equals(LayoutComprovanteMatriculaEnum.LAYOUT_01)) {
						design = ComprovanteRenovacaoMatriculaRel.getDesignComprovanteMatriculaIReportRelatorio();
						nomeRelatorio = ComprovanteRenovacaoMatriculaRel.getIdEntidadeComprovanteMatricula();
					} else if (configuracoesVO.getLayoutPadraoComprovanteMatricula().equals(LayoutComprovanteMatriculaEnum.LAYOUT_02)) {
						design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "ComprovanteRenovacaoMatriculaRelLayout2" + ".jrxml");
						nomeRelatorio = "ComprovanteRenovacaoMatriculaRelLayout2";
					}

				}
			}

			listaObjetos = getFacadeFactory().getComprovanteRenovacaoMatriculaRelFacade().criarObjeto(getMatriculaPeriodoVO().getCodigo(), matriculaVO.getMatricula(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(ComprovanteRenovacaoMatriculaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(ComprovanteRenovacaoMatriculaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
				getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
				UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(matriculaVO.getUnidadeEnsino().getCodigo(), getUsuarioLogado());
				if (ue.getExisteLogoRelatorio()) {
					String urlLogoUnidadeEnsinoRelatorio = ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogoRelatorio();
					String urlLogo = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
					getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", urlLogo);
				} else {
					getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
				}
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				setMensagemID("msg_relatorio_ok");
				realizarImpressaoRelatorio();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	public void emitirDadosMatricula() {
		try {
			imprimirMatricula();
			setExibirMatricula(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void imprimirMatricula() throws Exception {
//		DadosMatriculaSV mat = new DadosMatriculaSV();
//		mat.setMatriculaPeriodo(matriculaPeriodoVO.getCodigo());
	}

	public String getEmitirComprovanteRenovacaoMatricula() {
		String retorno="";
		try {
			if(getUsuarioLogado().getIsApresentarVisaoAluno()|| getUsuarioLogado().getIsApresentarVisaoAluno()){
				retorno="abrirPopup('../ComprovanteRenovacaoMatriculaSV?tipoLayout=2&nivelEducacional=" + matriculaVO.getCurso().getNivelEducacional() + "&matricula=" + matriculaVO.getMatricula() + "&matriculaPeriodo=" + matriculaPeriodoVO.getCodigo() + "&titulo=matricula&unidadeEnsino=" + getUnidadeEnsinoLogado().getCodigo() + "', 'comprovanteRenovacaoMatricula', 780, 585)";	
			}else if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				retorno = "abrirPopup('../../ComprovanteRenovacaoMatriculaSV?tipoLayout=" + getTipoLayoutComprovanteMatricula() + "&nivelEducacional=" + matriculaVO.getCurso().getNivelEducacional() + "&matricula=" + matriculaVO.getMatricula() + "&matriculaPeriodo=" + matriculaPeriodoVO.getCodigo() + "&titulo=matricula&unidadeEnsino=" + getUnidadeEnsinoLogado().getCodigo() + "', 'comprovanteRenovacaoMatricula', 780, 585)";
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return retorno;
	}

//	public void montarListaSelectItemDescontoProgressivo(String prm) throws Exception {
//		SelectItemOrdemValor ordenador = null;
//		List resultadoConsulta = null;
//		Iterator i = null;
//		try {
//			resultadoConsulta = consultarDescontoProgressivoPorNome(prm);
//			i = resultadoConsulta.iterator();
//			List objs = new ArrayList(0);
//			objs.add(new SelectItem(0, ""));
//			while (i.hasNext()) {
//				DescontoProgressivoVO obj = (DescontoProgressivoVO) i.next();
//				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
//			}
//			ordenador = new SelectItemOrdemValor();
//			Collections.sort((List) objs, ordenador);
//			setListaSelectItemDescontoProgresivo(objs);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			ordenador = null;
//			Uteis.liberarListaMemoria(resultadoConsulta);
//			i = null;
//		}
//	}

//	public void montarListaSelectItemDescontoProgressivo() {
//		try {
//			montarListaSelectItemDescontoProgressivo("");
//		} catch (Exception e) {
//			// System.out.println(e.getMessage());
//		}
//	}
//
//	public List consultarDescontoProgressivoPorNome(String nomePrm) throws Exception {
//		List lista = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
//		return lista;
//	}

	public String getResponsavelLiberacaoMatricula_Erro() {
		if (responsavelLiberacaoMatricula_Erro == null) {
			responsavelLiberacaoMatricula_Erro = "";
		}
		return responsavelLiberacaoMatricula_Erro;
	}

	public void setResponsavelLiberacaoMatricula_Erro(String responsavelLiberacaoMatricula_Erro) {
		this.responsavelLiberacaoMatricula_Erro = responsavelLiberacaoMatricula_Erro;
	}

//	public ItemPlanoFinanceiroAlunoVO getItemPlanoFinanceiroAlunoVO() {
//		if (itemPlanoFinanceiroAlunoVO == null) {
//			itemPlanoFinanceiroAlunoVO = new ItemPlanoFinanceiroAlunoVO();
//		}
//		return itemPlanoFinanceiroAlunoVO;
//	}
//
//	public void setItemPlanoFinanceiroAlunoVO(ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO) {
//		this.itemPlanoFinanceiroAlunoVO = itemPlanoFinanceiroAlunoVO;
//	}
	
	

	public List getListaSelectItemParceiro() {
		if (listaSelectItemParceiro == null) {
			listaSelectItemParceiro = new ArrayList(0);
		}
		return listaSelectItemParceiro;
	}

	public void setListaSelectItemParceiro(List listaSelectItemParceiro) {
		this.listaSelectItemParceiro = listaSelectItemParceiro;
	}
	
	

	public List getListaSelectItemConvenio() {
		if (listaSelectItemConvenio == null) {
			listaSelectItemConvenio = new ArrayList(0);
		}
		return listaSelectItemConvenio;
	}

	public void setListaSelectItemConvenio(List listaSelectItemConvenio) {
		this.listaSelectItemConvenio = listaSelectItemConvenio;
	}

	public List getListaSelectItemDescontoProgresivo() {
		if (listaSelectItemDescontoProgresivo == null) {
			listaSelectItemDescontoProgresivo = new ArrayList(0);
		}
		return listaSelectItemDescontoProgresivo;
	}

	public void setListaSelectItemDescontoProgresivo(List listaSelectItemDescontoProgresivo) {
		this.listaSelectItemDescontoProgresivo = listaSelectItemDescontoProgresivo;
	}

	public List getListaSelectItemPlanoDesconto() {
		if (listaSelectItemPlanoDesconto == null) {
			listaSelectItemPlanoDesconto = new ArrayList(0);
		}
		return listaSelectItemPlanoDesconto;
	}

	public void setListaSelectItemPlanoDesconto(List listaSelectItemPlanoDesconto) {
		this.listaSelectItemPlanoDesconto = listaSelectItemPlanoDesconto;
	}

	// public PlanoFinanceiroAlunoVO getPlanoFinanceiroAlunoVO() {
	// return planoFinanceiroAlunoVO;
	// }
	//
	// public void setPlanoFinanceiroAlunoVO(PlanoFinanceiroAlunoVO
	// planoFinanceiroAlunoVO) {
	// this.planoFinanceiroAlunoVO = planoFinanceiroAlunoVO;
	// }
	public ProcessoMatriculaCalendarioVO getProcessoCalendarioMatriculaVO() {
		if (processoCalendarioMatriculaVO == null) {
			processoCalendarioMatriculaVO = new ProcessoMatriculaCalendarioVO();
		}
		return processoCalendarioMatriculaVO;
	}

	public void setProcessoCalendarioMatriculaVO(ProcessoMatriculaCalendarioVO processoCalendarioMatriculaVO) {
		this.processoCalendarioMatriculaVO = processoCalendarioMatriculaVO;
	}

	public Boolean getMatriculaForaPrazo() {
		if (matriculaForaPrazo == null) {
			matriculaForaPrazo = Boolean.FALSE;
		}
		return matriculaForaPrazo;
	}

	public void setMatriculaForaPrazo(Boolean matriculaForaPrazo) {
		this.matriculaForaPrazo = matriculaForaPrazo;
	}

	@Override
	public String getSenha() {
		if (senha == null) {
			senha = "";
		}
		return senha;
	}

	@Override
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getUserName() {
		if (userName == null) {
			userName = "";
		}
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		matriculaVO = null;
		aluno_Erro = null;
		Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
		curso_Erro = null;
		matricula_Erro = null;
		usuario_Erro = null;
		responsavelLiberacaoMatricula_Erro = null;
		Uteis.liberarListaMemoria(listaSelectItemTurno);
		Uteis.liberarListaMemoria(listaSelectItemTipoMidiaCaptacao);
		Uteis.liberarListaMemoria(listaSelectItemTurma);
		documetacaoMatriculaVO = null;
		matriculaPeriodoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemGradeCurricular);
		responsavelRenovacaoMatricula_Erro = null;
		Uteis.liberarListaMemoria(listaSelectItemPeriodoLetivoMatricula);
		turma_Erro = null;
//		resultadoProcessoSeletivoVO = null;
//		opcaoPagamentoDividaVO = null;
	}

	

	public Boolean getImprimir() {
		if (imprimir == null) {
			imprimir = Boolean.FALSE;
		}
		return imprimir;
	}

	public void setImprimir(Boolean imprimir) {
		this.imprimir = imprimir;
	}

	/**
	 * @return the listaSelectItemMatriculaVisaoAluno
	 */
	public List getListaSelectItemMatriculaVisaoAluno() {
		return listaSelectItemMatriculaVisaoAluno;
	}

	/**
	 * @param listaSelectItemMatriculaVisaoAluno
	 *            the listaSelectItemMatriculaVisaoAluno to set
	 */
	public void setListaSelectItemMatriculaVisaoAluno(List listaSelectItemMatriculaVisaoAluno) {
		this.listaSelectItemMatriculaVisaoAluno = listaSelectItemMatriculaVisaoAluno;
	}

	/**
	 * @return the matriculaVisaoAluno
	 */
	public String getMatriculaVisaoAluno() {
		return matriculaVisaoAluno;
	}

	/**
	 * @param matriculaVisaoAluno
	 *            the matriculaVisaoAluno to set
	 */
	public void setMatriculaVisaoAluno(String matriculaVisaoAluno) {
		this.matriculaVisaoAluno = matriculaVisaoAluno;
	}

	/**
	 * @return the exibirRenovacaoMatricula
	 */
	public Boolean getExibirRenovacaoMatricula() {
		return exibirRenovacaoMatricula;
	}

	/**
	 * @param exibirRenovacaoMatricula
	 *            the exibirRenovacaoMatricula to set
	 */
	public void setExibirRenovacaoMatricula(Boolean exibirRenovacaoMatricula) {
		this.exibirRenovacaoMatricula = exibirRenovacaoMatricula;
	}

	/**
	 * @return the inscricao_Erro
	 */
	public String getInscricao_Erro() {
		return inscricao_Erro;
	}

	/**
	 * @param inscricao_Erro
	 *            the inscricao_Erro to set
	 */
	public void setInscricao_Erro(String inscricao_Erro) {
		this.inscricao_Erro = inscricao_Erro;
	}

	/**
	 * @return the guiaAba
	 */
	public String getGuiaAba() {
		if (guiaAba == null) {
			return "";
		}
		return guiaAba;
	}

	/**
	 * @param guiaAba
	 *            the guiaAba to set
	 */
	public void setGuiaAba(String guiaAba) {
		this.guiaAba = guiaAba;
	}

	/**
	 * @return the listaGradeDisciplinas
	 */
	public List getListaGradeDisciplinas() {
		return listaGradeDisciplinas;
	}

	/**
	 * @param listaGradeDisciplinas
	 *            the listaGradeDisciplinas to set
	 */
	public void setListaGradeDisciplinas(List listaGradeDisciplinas) {
		this.listaGradeDisciplinas = listaGradeDisciplinas;
	}

	/**
	 * @return the listaSelectItemPeriodoLetivo
	 */
	public List getListaSelectItemPeriodoLetivo() {
		return listaSelectItemPeriodoLetivo;
	}

	/**
	 * @param listaSelectItemPeriodoLetivo
	 *            the listaSelectItemPeriodoLetivo to set
	 */
	public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
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
	 * @return the listaConsultaCurso
	 */
	public List getListaConsultaCurso() {
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
	 * @return the listaConsultaConvenio
	 */
	public List getListaConsultaConvenio() {
		return listaConsultaConvenio;
	}

	/**
	 * @param listaConsultaConvenio
	 *            the listaConsultaConvenio to set
	 */
	public void setListaConsultaConvenio(List listaConsultaConvenio) {
		this.listaConsultaConvenio = listaConsultaConvenio;
	}

	/**
	 * @return the listaConsultaPlanoDesconto
	 */
	public List getListaConsultaPlanoDesconto() {
		return listaConsultaPlanoDesconto;
	}

	/**
	 * @param listaConsultaPlanoDesconto
	 *            the listaConsultaPlanoDesconto to set
	 */
	public void setListaConsultaPlanoDesconto(List listaConsultaPlanoDesconto) {
		this.listaConsultaPlanoDesconto = listaConsultaPlanoDesconto;
	}

	/**
	 * @return the campoConsultaCurso
	 */
	public String getCampoConsultaCurso() {
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
	 * @return the listaDisciplinasGradeCurricular
	 */
	public List getListaDisciplinasGradeCurricular() {
		return listaDisciplinasGradeCurricular;
	}

	/**
	 * @param listaDisciplinasGradeCurricular
	 *            the listaDisciplinasGradeCurricular to set
	 */
	public void setListaDisciplinasGradeCurricular(List listaDisciplinasGradeCurricular) {
		this.listaDisciplinasGradeCurricular = listaDisciplinasGradeCurricular;
	}

	/**
	 * @return the planoFinanceiroCursoVO
	 */
//	public PlanoFinanceiroCursoVO getPlanoFinanceiroCursoVO() {
//		return planoFinanceiroCursoVO;
//	}
//
//	/**
//	 * @param planoFinanceiroCursoVO
//	 *            the planoFinanceiroCursoVO to set
//	 */
//	public void setPlanoFinanceiroCursoVO(PlanoFinanceiroCursoVO planoFinanceiroCursoVO) {
//		this.planoFinanceiroCursoVO = planoFinanceiroCursoVO;
//	}

	/**
	 * @return the codigoUnidadeEnsinoCurso
	 */
	public Integer getCodigoUnidadeEnsinoCurso() {
		return codigoUnidadeEnsinoCurso;
	}

	/**
	 * @param codigoUnidadeEnsinoCurso
	 *            the codigoUnidadeEnsinoCurso to set
	 */
	public void setCodigoUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso) {
		this.codigoUnidadeEnsinoCurso = codigoUnidadeEnsinoCurso;
	}

	/**
	 * @return the validarCadastrarAluno
	 */
	public boolean isValidarCadastrarAluno() {
		return validarCadastrarAluno;
	}

	/**
	 * @param validarCadastrarAluno
	 *            the validarCadastrarAluno to set
	 */
	public void setValidarCadastrarAluno(boolean validarCadastrarAluno) {
		this.validarCadastrarAluno = validarCadastrarAluno;
	}

	/**
	 * @return the liberarAvancar
	 */
	public boolean isLiberarAvancar() {
		return liberarAvancar;
	}

	/**
	 * @param liberarAvancar
	 *            the liberarAvancar to set
	 */
	public void setLiberarAvancar(boolean liberarAvancar) {
		this.liberarAvancar = liberarAvancar;
	}

	/**
	 * @return the pedirLiberacaoMatricula
	 */
	public boolean isPedirLiberacaoMatricula() {
		return pedirLiberacaoMatricula;
	}

	/**
	 * @param pedirLiberacaoMatricula
	 *            the pedirLiberacaoMatricula to set
	 */
	public void setPedirLiberacaoMatricula(boolean pedirLiberacaoMatricula) {
		this.pedirLiberacaoMatricula = pedirLiberacaoMatricula;
	}

	/**
	 * @return the turmaComVagasPreenchidas
	 */
	public Boolean getTurmaComVagasPreenchidas() {
		return turmaComVagasPreenchidas;
	}

	/**
	 * @param turmaComVagasPreenchidas
	 *            the turmaComVagasPreenchidas to set
	 */
	public void setTurmaComVagasPreenchidas(Boolean turmaComVagasPreenchidas) {
		this.turmaComVagasPreenchidas = turmaComVagasPreenchidas;
	}

	/**
	 * @return the turmaComLotacaoPreenchida
	 */
	public Boolean getTurmaComLotacaoPreenchida() {
		return turmaComLotacaoPreenchida;
	}

	/**
	 * @param turmaComLotacaoPreenchida
	 *            the turmaComLotacaoPreenchida to set
	 */
	public void setTurmaComLotacaoPreenchida(Boolean turmaComLotacaoPreenchida) {
		this.turmaComLotacaoPreenchida = turmaComLotacaoPreenchida;
	}

	/**
	 * @return the exibirMatricula
	 */
	public Boolean isExibirMatricula() {
		return exibirMatricula;
	}

	/**
	 * @param exibirMatricula
	 *            the exibirMatricula to set
	 */
	public void setExibirMatricula(Boolean exibirMatricula) {
		this.exibirMatricula = exibirMatricula;
	}

	/**
	 * @return the condicaoPagamentoPlanoFinanceiroCursoVO
	 */
//	public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoVO() {
//		if (condicaoPagamentoPlanoFinanceiroCursoVO == null) {
//			condicaoPagamentoPlanoFinanceiroCursoVO = new CondicaoPagamentoPlanoFinanceiroCursoVO();
//		}
//		return condicaoPagamentoPlanoFinanceiroCursoVO;
//	}
//
//	/**
//	 * @param condicaoPagamentoPlanoFinanceiroCursoVO
//	 *            the condicaoPagamentoPlanoFinanceiroCursoVO to set
//	 */
//	public void setCondicaoPagamentoPlanoFinanceiroCursoVO(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO) {
//		this.condicaoPagamentoPlanoFinanceiroCursoVO = condicaoPagamentoPlanoFinanceiroCursoVO;
//	}

	/**
	 * @return the listaSelectItemProcessoMatricula
	 */
	public List getListaSelectItemProcessoMatricula() {
		return listaSelectItemProcessoMatricula;
	}

	/**
	 * @param listaSelectItemProcessoMatricula
	 *            the listaSelectItemProcessoMatricula to set
	 */
	public void setListaSelectItemProcessoMatricula(List listaSelectItemProcessoMatricula) {
		this.listaSelectItemProcessoMatricula = listaSelectItemProcessoMatricula;
	}

	public Boolean getApresentarPlanoFinanceiroCurso() {
		return apresentarPlanoFinanceiroCurso;
	}

	public void setApresentarPlanoFinanceiroCurso(Boolean apresentarPlanoFinanceiroCurso) {
		this.apresentarPlanoFinanceiroCurso = apresentarPlanoFinanceiroCurso;
	}

	/**
	 * @return the listaSelectItemPlanoFinanceiroCurso
	 */
	public List getListaSelectItemPlanoFinanceiroCurso() {
		if (listaSelectItemPlanoFinanceiroCurso == null) {
			listaSelectItemPlanoFinanceiroCurso = new ArrayList();
		}
		return listaSelectItemPlanoFinanceiroCurso;
	}
	
	
	/**
	 * @param listaSelectItemPlanoFinanceiroCurso
	 *            the listaSelectItemPlanoFinanceiroCurso to set
	 */
	public void setListaSelectItemPlanoFinanceiroCurso(List listaSelectItemPlanoFinanceiroCurso) {
		this.listaSelectItemPlanoFinanceiroCurso = listaSelectItemPlanoFinanceiroCurso;
	}

	/**
	 * @return the matriculaPeriodoTurmaDisciplinaVOAdicionar
	 */
	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVOAdicionar() {
		if (matriculaPeriodoTurmaDisciplinaVOAdicionar == null) {
			matriculaPeriodoTurmaDisciplinaVOAdicionar = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVOAdicionar;
	}

	/**
	 * @param matriculaPeriodoTurmaDisciplinaVOAdicionar
	 *            the matriculaPeriodoTurmaDisciplinaVOAdicionar to set
	 */
	public void setMatriculaPeriodoTurmaDisciplinaVOAdicionar(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVOAdicionar) {
		this.matriculaPeriodoTurmaDisciplinaVOAdicionar = matriculaPeriodoTurmaDisciplinaVOAdicionar;
	}

	/**
	 * @return the gradeDisciplinaAdicionar
	 */
	public Integer getGradeDisciplinaAdicionar() {
		if (gradeDisciplinaAdicionar == null) {
			gradeDisciplinaAdicionar = 0;
		}
		return gradeDisciplinaAdicionar;
	}

	/**
	 * @param gradeDisciplinaAdicionar
	 *            the gradeDisciplinaAdicionar to set
	 */
	public void setGradeDisciplinaAdicionar(Integer gradeDisciplinaAdicionar) {
		this.gradeDisciplinaAdicionar = gradeDisciplinaAdicionar;
	}

	/**
	 * @param listaSelectItemTurmaAdicionar
	 *            the listaSelectItemTurmaAdicionar to set
	 */
	public void setListaSelectItemTurmaAdicionar(List listaSelectItemTurmaAdicionar) {
		this.listaSelectItemTurmaAdicionar = listaSelectItemTurmaAdicionar;
	}

	/**
	 * @param listaSelectItemDisciplinaAdicionar
	 *            the listaSelectItemDisciplinaAdicionar to set
	 */
	public void setListaSelectItemDisciplinaAdicionar(List listaSelectItemDisciplinaAdicionar) {
		this.listaSelectItemDisciplinaAdicionar = listaSelectItemDisciplinaAdicionar;
	}

	/**
	 * @return the listaSelectItemDisciplinaAdicionar
	 */
	public List getListaSelectItemDisciplinaAdicionar() {
		if (listaSelectItemDisciplinaAdicionar == null) {
			listaSelectItemDisciplinaAdicionar = new ArrayList(0);
		}
		return listaSelectItemDisciplinaAdicionar;
	}

	/**
	 * @return the periodoLetivoAdicionar
	 */
	public Integer getPeriodoLetivoAdicionar() {
		if (periodoLetivoAdicionar == null) {
			periodoLetivoAdicionar = 0;
		}
		return periodoLetivoAdicionar;
	}

	/**
	 * @return the listaSelectItemPeriodoLetivoAdicionar
	 */
	public List getListaSelectItemPeriodoLetivoAdicionar() {
		if (listaSelectItemPeriodoLetivoAdicionar == null) {
			listaSelectItemPeriodoLetivoAdicionar = new ArrayList(0);
		}
		return listaSelectItemPeriodoLetivoAdicionar;
	}

	/**
	 * @param listaSelectItemPeriodoLetivoAdicionar
	 *            the listaSelectItemPeriodoLetivoAdicionar to set
	 */
	public void setListaSelectItemPeriodoLetivoAdicionar(List listaSelectItemPeriodoLetivoAdicionar) {
		this.listaSelectItemPeriodoLetivoAdicionar = listaSelectItemPeriodoLetivoAdicionar;
	}

	/**
	 * @return the listaSelectItemTurmaAdicionar
	 */
	public List getListaSelectItemTurmaAdicionar() {
		if (listaSelectItemTurmaAdicionar == null) {
			listaSelectItemTurmaAdicionar = new ArrayList(0);
		}
		return listaSelectItemTurmaAdicionar;
	}

	public void setPeriodoLetivoAdicionar(Integer periodoLetivoAdicionar) {
		this.periodoLetivoAdicionar = periodoLetivoAdicionar;
	}

	/**
	 * @return the listaSelectItemDisciplinaEquivalenteAdicionar
	 */
	public List getListaSelectItemDisciplinaEquivalenteAdicionar() {
		if (listaSelectItemDisciplinaEquivalenteAdicionar == null) {
			listaSelectItemDisciplinaEquivalenteAdicionar = new ArrayList(0);
		}
		return listaSelectItemDisciplinaEquivalenteAdicionar;
	}

	/**
	 * @param listaSelectItemDisciplinaEquivalenteAdicionar
	 *            the listaSelectItemDisciplinaEquivalenteAdicionar to set
	 */
	public void setListaSelectItemDisciplinaEquivalenteAdicionar(List listaSelectItemDisciplinaEquivalenteAdicionar) {
		this.listaSelectItemDisciplinaEquivalenteAdicionar = listaSelectItemDisciplinaEquivalenteAdicionar;
	}

	/**
	 * @return the listaSelectItemDescontoAlunoMatricula
	 */
	public List getListaSelectItemDescontoAlunoMatricula() {
		if (listaSelectItemDescontoAlunoMatricula == null) {
			listaSelectItemDescontoAlunoMatricula = new ArrayList(0);
		}
		return listaSelectItemDescontoAlunoMatricula;
	}

	/**
	 * @param listaSelectItemDescontoAlunoMatricula
	 *            the listaSelectItemDescontoAlunoMatricula to set
	 */
	public void setListaSelectItemDescontoAlunoMatricula(List listaSelectItemDescontoAlunoMatricula) {
		this.listaSelectItemDescontoAlunoMatricula = listaSelectItemDescontoAlunoMatricula;
	}

	/**
	 * @return the listaSelectItemDescontoAlunoParcela
	 */
	public List getListaSelectItemDescontoAlunoParcela() {
		if (listaSelectItemDescontoAlunoParcela == null) {
			listaSelectItemDescontoAlunoParcela = new ArrayList(0);
		}
		return listaSelectItemDescontoAlunoParcela;
	}

	/**
	 * @param listaSelectItemDescontoAlunoParcela
	 *            the listaSelectItemDescontoAlunoParcela to set
	 */
	public void setListaSelectItemDescontoAlunoParcela(List listaSelectItemDescontoAlunoParcela) {
		this.listaSelectItemDescontoAlunoParcela = listaSelectItemDescontoAlunoParcela;
	}

	/**
	 * @return the ordemDescontoVO
	 */
	

	/**
	 * @param ordemDescontoVO
	 *            the ordemDescontoVO to set
	 */
	
	/**
	 * @param ordemDesconto
	 *            the ordemDesconto to set
	 */
	

	/**
	 * @param ordemDesconto
	 *            the ordemDesconto to set
	 */
	

	/**
	 * @return the listaContaReceber
	 */
	

	/**
	 * @param listaContaReceber
	 *            the listaContaReceber to set
	 */
	

	/**
	 * @return the apresentarBotaoRenegociacao
	 */
	public Boolean getApresentarBotaoRenegociacao() {
		if (apresentarBotaoRenegociacao == null) {
			apresentarBotaoRenegociacao = Boolean.FALSE;
		}
		return apresentarBotaoRenegociacao;
	}

	/**
	 * @param apresentarBotaoRenegociacao
	 *            the apresentarBotaoRenegociacao to set
	 */
	public void setApresentarBotaoRenegociacao(Boolean apresentarBotaoRenegociacao) {
		this.apresentarBotaoRenegociacao = apresentarBotaoRenegociacao;
	}

	/**
	 * @return the imprimirContrato
	 */
	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = true;
		}
		return imprimirContrato;
	}

	/**
	 * @param imprimirContrato
	 *            the imprimirContrato to set
	 */
	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

	

	

	public String alterarControleFinanceiroMatriculaParaManual() throws Exception {
		getFacadeFactory().getMatriculaPeriodoFacade().alterarMatriculaPeriodoFinanceiroManual(matriculaPeriodoVO, getUsuarioLogado().getCodigo().intValue(), true);
		getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA_PERIODO, getMatriculaPeriodoVO().getCodigo().toString(), OperacaoFuncionalidadeEnum.ATIVAR_FINANCEIRO_MANUAL, getUsuarioLogado(), ""));
		return "";
	}

	
	


	/*Devido a rotina gerar falhas nas contas receber pois nao segue todas as regras que foram criadas para a mesma foi removido o  recurso de dentro da renovacao matricula e passou a ser direto na tela de conta receber. Pedro Andrade
	 * public void gravarMatriculaPeriodoVencimento() throws Exception {
		try {
			// if (getNovaMatriculaPeriodoVencimento()) {
			// setMatriculaPeriodoVencimentoVoEditada(getFacade().getMatriculaPeriodoVencimento().preencherNovaMatriculaPeriodoVencimentoVo(getMatriculaPeriodoVencimentoVoEditada(),
			// getMatriculaPeriodoVO()));
			// getFacade().getMatriculaPeriodoVencimento().incluir(getMatriculaPeriodoVencimentoVoEditada());
			// getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs().add(getMatriculaPeriodoVencimentoVoEditada());
			// } else {
			// getMatriculaPeriodoVencimentoVoEditada().setValidarParcela(true);
			// getFacade().getMatriculaPeriodoVencimento().alterar(getMatriculaPeriodoVencimentoVoEditada());
			// if
			// (getMatriculaPeriodoVencimentoVoEditada().getSituacao().getValor().equals("GE"))
			// {
			// getMatriculaPeriodoVencimentoVoEditada().setContaReceber(getFacade().getContaReceber().alterarContaReceberPelaMatriculaPeriodoVencimento(getMatriculaPeriodoVencimentoVoEditada()));
			// getFacade().getContaReceber().alterar(getMatriculaPeriodoVencimentoVoEditada().getContaReceber(),
			// false);
			// }
			// for (int i = 0; i <
			// getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs().size();
			// i++) {
			// if
			// (getMatriculaPeriodoVencimentoVoEditada().getCodigo().intValue()
			// ==
			// getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs().get(i).getCodigo().intValue())
			// {
			// getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs().set(i,
			// getMatriculaPeriodoVencimentoVoEditada());
			// break;
			// }
			// }
			// }
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Incluindo Matricula - Matricula Periodo Vencimento", "Incluindo");
			getFacadeFactory().getMatriculaPeriodoVencimentoFacade().gravarMatriculaPeriodoVencimento(getMatriculaPeriodoVO(), getMatriculaPeriodoVencimentoVoEditada(), getIsNovaMatriculaPeriodoVencimento(), getUsuarioLogado());
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Incluindo Matricula - Matricula Periodo Vencimento", "Incluindo");
			setPermitirFecharRichModalMatriculaPeriodoVencimento(true);
		} catch (Exception e) {
			setPermitirFecharRichModalMatriculaPeriodoVencimento(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}*/

	public String getFecharRichModalMatriculaPeriodoVencimento() {
		if (getIsPermitirFecharRichModalMatriculaPeriodoVencimento()) {
			return "PF('panelMatriculaPeriodoVencimento').hide()";
		} else {
			return "";
		}
	}

	public Boolean getPermitirGerarContaReceber() throws Exception {
		return getFacadeFactory().getMatriculaPeriodoFacade().getPermitirGerarContaReceber(getMatriculaPeriodoVO());
	}

	public String gerarContasReceber() throws Exception {
		try {
			boolean possuiContasReceberParaGerar = getFacadeFactory().getMatriculaPeriodoFacade().verificarMatriculaPeriodoPossuiContasReceberParaGerar(getMatriculaPeriodoVO().getCodigo(), getUsuarioLogado());
			if (possuiContasReceberParaGerar) {
				getFacadeFactory().getMatriculaPeriodoFacade().processarGeracaoContasReceberAposConfirmacaoPagamentoMatricula(getMatriculaVO(), getMatriculaPeriodoVO().getCodigo(), null,  getMatriculaPeriodoVO().getBloqueioPorFechamentoMesLiberado(), getUsuarioLogado());
				setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				setMensagemID("msg_dados_gravados");
			} else {
				setMensagemDetalhada("msg_erro", "Não existem contas para serem geradas.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	
	public void realizarMatriculaVindoDaTelaDoAluno() throws Exception {
		if (context().getExternalContext().getSessionMap().get("pessoaItem") != null) {
			inicializarManagedBeanIniciarProcessamento();
			getMatriculaVO().setAluno((PessoaVO) context().getExternalContext().getSessionMap().get("pessoaItem"));
			context().getExternalContext().getSessionMap().remove("pessoaItem");
			setRealizandoNovaMatriculaAluno(true);
			novaMatriculaAluno(false);
		}
	}

	
	public void realizarMatriculaVindoDaTelaDoPortadorDiplima() throws Exception {
		if (context().getExternalContext().getSessionMap().get("alunoPortadorDiploma") != null) {
			getMatriculaVO().setAluno((PessoaVO) context().getExternalContext().getSessionMap().get("alunoPortadorDiploma"));
			PortadorDiplomaVO portadorDiplomaVO = ((PortadorDiplomaVO) context().getExternalContext().getSessionMap().get("codigoPortadorDiploma"));
			context().getExternalContext().getSessionMap().remove("alunoPortadorDiploma");
			context().getExternalContext().getSessionMap().remove("codigoPortadorDiploma");
			inicializarUsuarioResponsavelMatriculaUsuarioLogado();
			setCodigoPortadorDiploma(portadorDiplomaVO.getCodigo());
			setRealizandoNovaMatriculaAluno(true);
			setPortadorDiploma(true);
			novaMatriculaAluno(false);
		}
	}

	
	public String renovarMatriculaReativada() {
		try {
			if (context().getExternalContext().getSessionMap().get("reativarMatriculaItem") == null || !(context().getExternalContext().getSessionMap().get("reativarMatriculaItem") instanceof MatriculaVO)) {
				return "";
			}
			novo();
			setMatriculaVO((MatriculaVO) context().getExternalContext().getSessionMap().get("reativarMatriculaItem")); 
			context().getExternalContext().getSessionMap().remove("reativarMatriculaItem");
			setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), getMatriculaVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			iniciarRenovacaoMatricula();
		} catch (Exception e) {
			setMatriculaVO(new MatriculaVO());
			setMatriculaPeriodoVO(new MatriculaPeriodoVO());
			montarListaProcessoMatricula(false);
			montarListaSelectItemGradeCurricular();
			montarListaSelectItemPeriodoLetivo(0);
			montarListaSelectItemTurma();
			setGuiaAba("DadosBasicos");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
		return "";
	}

	/**
	 * Método comentado aguarando definir regra de negocio pra renovar - trancar
	 * - reativar. Responsável: Rodrigo Jayme
	 */
	// public void verificarUltimoPeriodoLetivoAprovado(MatriculaVO matriculaVO)
	// throws Exception {
	// // MatriculaPeriodoVO matriculaPeriodoVo =
	// getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(matriculaVO.getMatricula(),
	// false,
	// Uteis.NIVELMONTARDADOS_TODOS);
	// MatriculaPeriodoVO matriculaPeriodoVo =
	// getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaOuTrancadaPorMatriculaSemExcecao(matriculaVO.getMatricula(),
	// false,
	// Uteis.NIVELMONTARDADOS_TODOS);
	// if (!matriculaPeriodoVo.getCodigo().equals(0)) {
	// getFacadeFactory().getMatriculaPeriodoFacade().executarFinalizarMatriculaPeriodo(matriculaPeriodoVo);
	// List<HistoricoVO> listaHistoricoVO =
	// getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoSituacao(matriculaPeriodoVo.getCodigo(),
	// "R", false,
	// Uteis.NIVELMONTARDADOS_TODOS);
	// int periodoLetivo =
	// getMatriculaVO().getUltimoMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo();
	// if
	// (getMatriculaVO().getCurso().getConfiguracaoAcademico().getIsPossuiControleDisciplinaReprovacao())
	// {
	// if (listaHistoricoVO.size() >=
	// getMatriculaVO().getCurso().getConfiguracaoAcademico().getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo())
	// {
	// if
	// (!getMatriculaVO().getCurso().getConfiguracaoAcademico().getPermiteEvoluirPeriodoLetivoCasoReprovado())
	// {
	// getMatriculaVO().getUltimoMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(periodoLetivo);
	// } else {
	// getMatriculaVO().getUltimoMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(periodoLetivo
	// + 1);
	// }
	// } else {
	// getMatriculaVO().getUltimoMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(periodoLetivo
	// + 1);
	// }
	// } else {
	// getMatriculaVO().getUltimoMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(periodoLetivo
	// + 1);
	// }
	// }
	// }
	public String inicializarListasTodosComboBoxRenovacaoMatriculaReativada() throws Exception {
		montarListaSelectItemTipoMidiaCaptacao();
		//montarListaSelectItemConvenio();
//		montarListaSelectItemParceiro();
//		montarListaSelectItemPlanoDesconto();
//		montarListaSelectItemDescontoProgressivo();
//		montarListaSelectItemDescontoAlunoMatricula();
//		montarListaSelectItemPlanoFinanceiroCurso(true);
		montarListaSelectItemPeriodoLetivoAdicionar();
		montarListaSelectItemDisciplina();
//		montarListaSelectItemDescontoAlunoParcela();
		montarListaSelectItemPeriodoLetivo(0);
		montarListaSelectItemTurma();
		
		return "";
	}

	public void consultarDisciplinasPreRequisitos() {
		setListaDisciplinasPreRequisitos(new ArrayList(0));
		setMensagemDetalhada("");
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVo = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaItens");
		setListaDisciplinasPreRequisitos(matriculaPeriodoTurmaDisciplinaVo.getListaDisciplinasPreRequisitos());
	}

	public void consultarDisciplinasDoPeriodoLetivo() throws Exception {
		List listaGradeDisciplina = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getMatriculaPeriodoVO().getPeridoLetivo().getCodigo(), false, getUsuarioLogado(), null);
		getMatriculaPeriodoVO().getPeridoLetivo().setGradeDisciplinaVOs(listaGradeDisciplina);
	}

	public void verificarTransferenciaEntrada() throws Exception {
		getMatriculaVO().getTransferenciaEntrada().setSituacao("EF");
		getFacadeFactory().getTransferenciaEntradaFacade().verificarTransferenciaEntrada(getMatriculaVO().getTransferenciaEntrada(), getMatriculaVO().getTransferenciaEntrada().getSituacao(), getUsuarioLogado());
	}

	/**
	 * @return the editarParcela
	 */
	public boolean getIsEditarParcela() {
		return editarParcela;
	}

	/**
	 * @param editarParcela
	 *            the editarParcela to set
	 */
	public void setEditarParcela(boolean editarParcela) {
		this.editarParcela = editarParcela;
	}

	/**
	 * @return the permitirFecharRichModalMatriculaPeriodoVencimento
	 */
	public boolean getIsPermitirFecharRichModalMatriculaPeriodoVencimento() {
		return permitirFecharRichModalMatriculaPeriodoVencimento;
	}

	/**
	 * @param permitirFecharRichModalMatriculaPeriodoVencimento
	 *            the permitirFecharRichModalMatriculaPeriodoVencimento to set
	 */
	public void setPermitirFecharRichModalMatriculaPeriodoVencimento(boolean permitirFecharRichModalMatriculaPeriodoVencimento) {
		this.permitirFecharRichModalMatriculaPeriodoVencimento = permitirFecharRichModalMatriculaPeriodoVencimento;
	}

	

	/**
	 * @return the novaMatriculaPeriodoVencimento
	 */
	public boolean getIsNovaMatriculaPeriodoVencimento() {
		return novaMatriculaPeriodoVencimento;
	}

	/**
	 * @param novaMatriculaPeriodoVencimento
	 *            the novaMatriculaPeriodoVencimento to set
	 */
	public void setNovaMatriculaPeriodoVencimento(boolean novaMatriculaPeriodoVencimento) {
		this.novaMatriculaPeriodoVencimento = novaMatriculaPeriodoVencimento;
	}

	public boolean getIsDesabilitarCampoPeriodoLetivo() {
		// if (!getMatriculaPeriodoVO().getIsNovaMatriculaPeriodo()) {
		// return false;
		// }
		if ((getMatriculaPeriodoVO().getSituacaoAtiva()) || (getMatriculaPeriodoVO().getSituacaoPreMatricula())) {
			return false;
		} else {
			return true;
		}
	}

	public boolean getIsDesabilitarCampoTurma() {
		// if (!getMatriculaPeriodoVO().getIsNovaMatriculaPeriodo()) {
		// return false;
		// }
		if ((getMatriculaPeriodoVO().getSituacaoAtiva()) || (getMatriculaPeriodoVO().getSituacaoPreMatricula())) {
			return false;
		} else {
			return true;
		}
	}

	public boolean getIsDesabilitarCamposTela() {
		if (!getMatriculaPeriodoVO().getIsDesabilitarCamposTela()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return the listaDisciplinasPreRequisitos
	 */
	public List getListaDisciplinasPreRequisitos() {
		if (listaDisciplinasPreRequisitos == null) {
			listaDisciplinasPreRequisitos = new ArrayList(0);
		}
		return listaDisciplinasPreRequisitos;
	}

	/**
	 * @param listaDisciplinasPreRequisitos
	 *            the listaDisciplinasPreRequisitos to set
	 */
	public void setListaDisciplinasPreRequisitos(List listaDisciplinasPreRequisitos) {
		this.listaDisciplinasPreRequisitos = listaDisciplinasPreRequisitos;
	}

	/**
	 * @return the liberarRenovacaoComDebitosFinanceiros
	 */
	public Boolean getLiberarRenovacaoComDebitosFinanceiros() {
		if (liberarRenovacaoComDebitosFinanceiros == null) {
			liberarRenovacaoComDebitosFinanceiros = Boolean.FALSE;
		}
		return liberarRenovacaoComDebitosFinanceiros;
	}

	/**
	 * @param liberarRenovacaoComDebitosFinanceiros
	 *            the liberarRenovacaoComDebitosFinanceiros to set
	 */
	public void setLiberarRenovacaoComDebitosFinanceiros(Boolean liberarRenovacaoComDebitosFinanceiros) {
		this.liberarRenovacaoComDebitosFinanceiros = liberarRenovacaoComDebitosFinanceiros;
	}

	/**
	 * @return the usernameLiberarRenovacaoComDebitosFinanceiros
	 */
	public String getUsernameLiberarRenovacaoComDebitosFinanceiros() {
		if (usernameLiberarRenovacaoComDebitosFinanceiros == null) {
			usernameLiberarRenovacaoComDebitosFinanceiros = "";
		}
		return usernameLiberarRenovacaoComDebitosFinanceiros;
	}

	/**
	 * @param usernameLiberarRenovacaoComDebitosFinanceiros
	 *            the usernameLiberarRenovacaoComDebitosFinanceiros to set
	 */
	public void setUsernameLiberarRenovacaoComDebitosFinanceiros(String usernameLiberarRenovacaoComDebitosFinanceiros) {
		this.usernameLiberarRenovacaoComDebitosFinanceiros = usernameLiberarRenovacaoComDebitosFinanceiros;
	}

	/**
	 * @return the senhaLiberarRenovacaoComDebitosFinanceiros
	 */
	public String getSenhaLiberarRenovacaoComDebitosFinanceiros() {
		if (senhaLiberarRenovacaoComDebitosFinanceiros == null) {
			senhaLiberarRenovacaoComDebitosFinanceiros = "";
		}
		return senhaLiberarRenovacaoComDebitosFinanceiros;
	}

	/**
	 * @param senhaLiberarRenovacaoComDebitosFinanceiros
	 *            the senhaLiberarRenovacaoComDebitosFinanceiros to set
	 */
	public void setSenhaLiberarRenovacaoComDebitosFinanceiros(String senhaLiberarRenovacaoComDebitosFinanceiros) {
		this.senhaLiberarRenovacaoComDebitosFinanceiros = senhaLiberarRenovacaoComDebitosFinanceiros;
	}

	/**
	 * @return the periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina
	 */
	public Integer getPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina() {
		if (periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina == null) {
			periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina = 0;
		}
		return periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina;
	}

	/**
	 * @param periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina
	 *            the periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina to
	 *            set
	 */
	public void setPeriodoLetivoVisualizarHistoricoAlunoIncluirDisciplina(Integer periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina) {
		this.periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina = periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina;
	}

	/**
	 * @return the listaDisciplinasPeriodoLetivoAlunoJaAprovado
	 */
	public List<HistoricoVO> getListaDisciplinasPeriodoLetivoAlunoJaAprovado() {
		if (listaDisciplinasPeriodoLetivoAlunoJaAprovado == null) {
			listaDisciplinasPeriodoLetivoAlunoJaAprovado = new ArrayList<HistoricoVO>(0);
		}
		return listaDisciplinasPeriodoLetivoAlunoJaAprovado;
	}

	/**
	 * @param listaDisciplinasPeriodoLetivoAlunoJaAprovado
	 *            the listaDisciplinasPeriodoLetivoAlunoJaAprovado to set
	 */
	public void setListaDisciplinasPeriodoLetivoAlunoJaAprovado(List<HistoricoVO> listaDisciplinasPeriodoLetivoAlunoJaAprovado) {
		this.listaDisciplinasPeriodoLetivoAlunoJaAprovado = listaDisciplinasPeriodoLetivoAlunoJaAprovado;
	}

	/**
	 * @return the listaDisciplinasPeriodoLetivoAlunoPendente
	 */
	public List<GradeDisciplinaVO> getListaDisciplinasPeriodoLetivoAlunoPendente() {
		if (listaDisciplinasPeriodoLetivoAlunoPendente == null) {
			listaDisciplinasPeriodoLetivoAlunoPendente = new ArrayList<GradeDisciplinaVO>(0);
		}
		return listaDisciplinasPeriodoLetivoAlunoPendente;
	}

	/**
	 * @param listaDisciplinasPeriodoLetivoAlunoPendente
	 *            the listaDisciplinasPeriodoLetivoAlunoPendente to set
	 */
	public void setListaDisciplinasPeriodoLetivoAlunoPendente(List<GradeDisciplinaVO> listaDisciplinasPeriodoLetivoAlunoPendente) {
		this.listaDisciplinasPeriodoLetivoAlunoPendente = listaDisciplinasPeriodoLetivoAlunoPendente;
	}

	public void inicializarListasSelectItemTodosComboBoxDadosFinanceiros() {
//		
////		montarListaSelectItemParceiro();
//		//montarListaSelectItemConvenio();
//		montarListaSelectItemPlanoDesconto();
//		montarListaSelectItemDescontoProgressivo();
//		montarListaSelectItemDescontoAlunoMatricula();
//		// montarListaSelectItemPeriodoLetivoAdicionar();
//		// montarListaSelectItemDisciplina();
//		montarListaSelectItemDescontoAlunoParcela();
////		montarListaSelectItemCentroReceita();
		
	}

	


	public Boolean getRenovandoMatriculaDaVisaoDoAluno() {
		if (((getUsuarioLogado().getIsApresentarVisaoAluno()) && (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle") != null)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public void validarDadosQtdeDisciplinaCursarRenovacaoOnline() throws Exception {
		if (getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("aluno") && (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle") != null) {
			Integer qtdeDisciplinaPeriodo = getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().size();
			Integer qtdeDisciplinaConfigurado = getMatriculaPeriodoVO().getProcessoMatriculaVO().getQtdeMininaDisciplinaCursar();
			if (qtdeDisciplinaPeriodo < qtdeDisciplinaConfigurado) {
				throw new Exception("A quantidade mínima de Disciplina não pode ser inferior a " + qtdeDisciplinaConfigurado + ". Para prosseguir será necessário adicionar mais " + (qtdeDisciplinaConfigurado - qtdeDisciplinaPeriodo) + " disciplina(s).");
			}
			alterarTagsTermoAceiteContratoRenovacaoOnline("MA");
			realizarDefinicaoApresentacaoModalTermoAceite();
		}
	}

	/**
	 * @return the realizandoNovaMatriculaAluno
	 */
	public void carregarQtdConfiguracao() {
		try {
			if (getConfiguracaoGeralPadraoSistema().getControlaQtdDisciplinaExtensao().booleanValue()) {
				getMatriculaVO().setQtdDisciplinasExtensao(getConfiguracaoGeralPadraoSistema().getQtdDisciplinaExtensao());
			}
		} catch (Exception e) {
		}
	}

	public Boolean getRealizandoNovaMatriculaAluno() {
		return realizandoNovaMatriculaAluno;
	}

	/**
	 * @param realizandoNovaMatriculaAluno
	 *            the realizandoNovaMatriculaAluno to set
	 */
	public void setRealizandoNovaMatriculaAluno(Boolean realizandoNovaMatriculaAluno) {
		this.realizandoNovaMatriculaAluno = realizandoNovaMatriculaAluno;
	}

	/**
	 * @return the listaConsultaAlunoNovaMatricula
	 */
	public List getListaConsultaAlunoNovaMatricula() {
		return listaConsultaAlunoNovaMatricula;
	}

	/**
	 * @param listaConsultaAlunoNovaMatricula
	 *            the listaConsultaAlunoNovaMatricula to set
	 */
	public void setListaConsultaAlunoNovaMatricula(List listaConsultaAlunoNovaMatricula) {
		this.listaConsultaAlunoNovaMatricula = listaConsultaAlunoNovaMatricula;
	}

	/**
	 * @return the campoConsultaAlunoNovaMatricula
	 */
	public String getCampoConsultaAlunoNovaMatricula() {
		if (campoConsultaAlunoNovaMatricula == null) {
			campoConsultaAlunoNovaMatricula = "";
		}
		return campoConsultaAlunoNovaMatricula;
	}

	/**
	 * @param campoConsultaAlunoNovaMatricula
	 *            the campoConsultaAlunoNovaMatricula to set
	 */
	public void setCampoConsultaAlunoNovaMatricula(String campoConsultaAlunoNovaMatricula) {
		this.campoConsultaAlunoNovaMatricula = campoConsultaAlunoNovaMatricula;
	}

	/**
	 * @return the valorConsultaAlunoNovaMatricula
	 */
	public String getValorConsultaAlunoNovaMatricula() {
		return valorConsultaAlunoNovaMatricula;
	}

	/**
	 * @param valorConsultaAlunoNovaMatricula
	 *            the valorConsultaAlunoNovaMatricula to set
	 */
	public void setValorConsultaAlunoNovaMatricula(String valorConsultaAlunoNovaMatricula) {
		this.valorConsultaAlunoNovaMatricula = valorConsultaAlunoNovaMatricula;
	}

	/**
	 * @param apresentarDisciplinasEquivalentesIncluir
	 *            the apresentarDisciplinasEquivalentesIncluir to set
	 */
	public void setApresentarDisciplinasEquivalentesIncluir(Boolean apresentarDisciplinasEquivalentesIncluir) {
		this.apresentarDisciplinasEquivalentesIncluir = apresentarDisciplinasEquivalentesIncluir;
	}

	/**
	 * @return the gradeDisciplinaEquivalenteAdicionar
	 */
	public Integer getGradeDisciplinaEquivalenteAdicionar() {
		return gradeDisciplinaEquivalenteAdicionar;
	}

	/**
	 * @param gradeDisciplinaEquivalenteAdicionar
	 *            the gradeDisciplinaEquivalenteAdicionar to set
	 */
	public void setGradeDisciplinaEquivalenteAdicionar(Integer gradeDisciplinaEquivalenteAdicionar) {
		this.gradeDisciplinaEquivalenteAdicionar = gradeDisciplinaEquivalenteAdicionar;
	}

	

	/**
	 * @return the anoGerarParcelaAvulsa
	 */
	

	/**
	 * @param anoGerarParcelaAvulsa
	 *            the anoGerarParcelaAvulsa to set
	 */
	

	/**
	 * @return the mesGerarParcelaAvulsa
	 */
	

	/**
	 * @param mesGerarParcelaAvulsa
	 *            the mesGerarParcelaAvulsa to set
	 */
	public void setMesGerarParcelaAvulsa(String mesGerarParcelaAvulsa) {
		this.mesGerarParcelaAvulsa = mesGerarParcelaAvulsa;
	}

	/**
	 * @return the listaDisciplinasExcluidas
	 */
	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaDisciplinasExcluidas() {
		if (listaDisciplinasExcluidas == null) {
			listaDisciplinasExcluidas = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaDisciplinasExcluidas;
	}

	/**
	 * @param listaDisciplinasExcluidas
	 *            the listaDisciplinasExcluidas to set
	 */
	public void setListaDisciplinasExcluidas(List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasExcluidas) {
		this.listaDisciplinasExcluidas = listaDisciplinasExcluidas;
	}

	/**
	 * @return the listaDisciplinasTurma
	 */
	public List<DisciplinaVO> getListaDisciplinasTurma() {
		if (listaDisciplinasTurma == null) {
			listaDisciplinasTurma = new ArrayList<DisciplinaVO>(0);
		}
		return listaDisciplinasTurma;
	}

	/**
	 * @param listaDisciplinasTurma
	 *            the listaDisciplinasTurma to set
	 */
	public void setListaDisciplinasTurma(List<DisciplinaVO> listaDisciplinasTurma) {
		this.listaDisciplinasTurma = listaDisciplinasTurma;
	}

	public void visualizarDisciplinasExcluidas() {
		listaDisciplinasExcluidas = getFacadeFactory().getMatriculaPeriodoFacade().executarObterListaDisciplinasExcluidasGradeAluno(this.getMatriculaPeriodoVO(), this.getMatriculaVO(), getUsuarioLogado());
	}

	public void visualizarDisciplinasProgramadasTurma() {
		List<TurmaDisciplinaVO> listaTurmaDisciplinaVO = this.matriculaPeriodoVO.getTurma().getTurmaDisciplinaVOs();
		List<DisciplinaVO> listaDisciplinaVO = new ArrayList<DisciplinaVO>(0);
		for (TurmaDisciplinaVO turmaDisciplinaVO : listaTurmaDisciplinaVO) {
			listaDisciplinaVO.add(turmaDisciplinaVO.getDisciplina());
		}
		listaDisciplinasTurma = listaDisciplinaVO;
		alertaGradeTurmaDiferenteGradeAluno = "";
		if (!this.getMatriculaPeriodoVO().getGradeCurricular().getCodigo().equals(this.matriculaPeriodoVO.getTurma().getGradeCurricularVO().getCodigo())) {
			alertaGradeTurmaDiferenteGradeAluno = "Matriz Curricular utilizada na turma é diferente da matriz " + "definida na matrícula do aluno. ";
			// +
			// this.matriculaPeriodoVO.getTurma().getGradeCurricularVO().getNome();
		}
	}

	public Boolean getApresentarAlertaGradeTurmaDiferenteGradeAluno() {
		if (getAlertaGradeTurmaDiferenteGradeAluno().equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * @return the alertaGradeTurmaDiferenteGradeAluno
	 */
	public String getAlertaGradeTurmaDiferenteGradeAluno() {
		if (alertaGradeTurmaDiferenteGradeAluno == null) {
			alertaGradeTurmaDiferenteGradeAluno = "";
		}
		return alertaGradeTurmaDiferenteGradeAluno;
	}

	/**
	 * @param alertaGradeTurmaDiferenteGradeAluno
	 *            the alertaGradeTurmaDiferenteGradeAluno to set
	 */
	public void setAlertaGradeTurmaDiferenteGradeAluno(String alertaGradeTurmaDiferenteGradeAluno) {
		this.alertaGradeTurmaDiferenteGradeAluno = alertaGradeTurmaDiferenteGradeAluno;
	}

	/**
	 * @return the filtroSituacaoMatriculaPeriodo
	 */
	public String getFiltroSituacaoMatriculaPeriodo() {
		return filtroSituacaoMatriculaPeriodo;
	}

	/**
	 * @param filtroSituacaoMatriculaPeriodo
	 *            the filtroSituacaoMatriculaPeriodo to set
	 */
	public void setFiltroSituacaoMatriculaPeriodo(String filtroSituacaoMatriculaPeriodo) {
		this.filtroSituacaoMatriculaPeriodo = filtroSituacaoMatriculaPeriodo;
	}

	/**
	 * @return the filtroTurno
	 */
	public Integer getFiltroTurno() {
		return filtroTurno;
	}

	/**
	 * @param filtroTurno
	 *            the filtroTurno to set
	 */
	public void setFiltroTurno(Integer filtroTurno) {
		this.filtroTurno = filtroTurno;
	}

	public void setMatriculaProuni(Boolean matriculaProuni) {
		if (matriculaProuni) {
			getMatriculaVO().setFormaIngresso(FormaIngresso.PROUNI.getValor());
		} else {
			getMatriculaVO().setFormaIngresso(FormaIngresso.OUTROS_TIPOS_SELECAO.getValor());
		}
	}

	public void imprimirTermoCompromissoDocumentacao() {
		List listaObjetos = null;
		String design = null;
		String nomeEntidade = null;
		String titulo = null;
		try {
//			getFacadeFactory().getTermoCompromissoDocumentacaoPendenteRelFacade().ValidarDados(getMatriculaVO());
			titulo = "Termo de Compromisso para Documentação Pendente";
			nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
//			design = TermoCompromissoDocumentacaoPendenteRel.getDesignIReportRelatorio();
//			listaObjetos = getFacadeFactory().getTermoCompromissoDocumentacaoPendenteRelFacade().criarObjeto(matriculaVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
//				getSuperParametroRelVO().setSubReport_Dir(TermoCompromissoDocumentacaoPendenteRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
//				getSuperParametroRelVO().setCaminhoBaseRelatorio(TermoCompromissoDocumentacaoPendenteRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
		}

	}

	public Boolean getMatriculaProuni() {
		if (getMatriculaVO().getFormaIngresso().equals(FormaIngresso.PROUNI.getValor())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return the listaConsultaCandidato
	 */
	public List getListaConsultaCandidato() {
		return listaConsultaCandidato;
	}

	/**
	 * @param listaConsultaCandidato
	 *            the listaConsultaCandidato to set
	 */
	public void setListaConsultaCandidato(List listaConsultaCandidato) {
		this.listaConsultaCandidato = listaConsultaCandidato;
	}

	/**
	 * @return the campoConsultaCandidato
	 */
	public String getCampoConsultaCandidato() {
		return campoConsultaCandidato;
	}

	/**
	 * @param campoConsultaCandidato
	 *            the campoConsultaCandidato to set
	 */
	public void setCampoConsultaCandidato(String campoConsultaCandidato) {
		this.campoConsultaCandidato = campoConsultaCandidato;
	}

	/**
	 * @return the valorConsultaCandidato
	 */
	public String getValorConsultaCandidato() {
		return valorConsultaCandidato;
	}

	/**
	 * @param valorConsultaCandidato
	 *            the valorConsultaCandidato to set
	 */
	public void setValorConsultaCandidato(String valorConsultaCandidato) {
		this.valorConsultaCandidato = valorConsultaCandidato;
	}

	public boolean getIsPermiteAlterarOrdemDesconto() {
		return getFacadeFactory().getMatriculaFacade().executarVerificacaoPermissaoAlterarOrdemDesconto();
	}

	public boolean getPortadorDiploma() {
		return portadorDiploma;
	}

	public void setPortadorDiploma(boolean portadorDiploma) {
		this.portadorDiploma = portadorDiploma;
	}

	public Integer getCodigoPortadorDiploma() {
		if (codigoPortadorDiploma == null) {
			codigoPortadorDiploma = 0;
		}
		return codigoPortadorDiploma;
	}

	public void setCodigoPortadorDiploma(Integer codigoPortadorDiploma) {
		this.codigoPortadorDiploma = codigoPortadorDiploma;
	}

	/**
	 * Método responsável por apresentar os valores do Enum(Forma de Ingresso)
	 * quando apresentarNaMatricula(Booleano criado no Enum) for TRUE. Nesse
	 * caso não irá aparecer os valores Transferencia Interna e Transferencia
	 * Externa.
	 * 
	 * @return
	 * @Autor Carlos
	 */
	public List<SelectItem> getListaSelectItemFormaIngresso() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("", ""));
		for (FormaIngresso formaIngresso : FormaIngresso.values()) {
			if (formaIngresso.getApresentarNaMatricula() && verificaPermissaoApresentarFormaIngresso(formaIngresso)) {
				itens.add(new SelectItem(formaIngresso.getValor(), formaIngresso.getDescricao()));
			}else if(Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula()) 
					&& getMatriculaVO().getFormaIngresso().equals(formaIngresso.getValor())) {
				itens.add(new SelectItem(formaIngresso.getValor(), formaIngresso.getDescricao()));
			}
		}
		Collections.sort(itens, new SelectItemOrdemValor());
		return itens;
	}

	public void verificarVisuaizarAbaDescontos() {
//		try {
//			verificarVisuaizarAbaDescontoPlanoFinanceiro();
//			Matricula.verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "VisualizarAbaDescontos");
//			setMostrarAbaDescontos(true);
//		} catch (Exception e) {
			setMostrarAbaDescontos(false);
//		}
	}

	public boolean verificaPermissaoApresentarFormaIngresso(FormaIngresso formaIngresso) {
		try {
			if (formaIngresso.getValor().equals("ET")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoEntrevista");
			}
			if (formaIngresso.getValor().equals("PD")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoPortadorDiplina");
			}
			if (formaIngresso.getValor().equals("PS")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoProcessoSeletivo");
			}
			if (formaIngresso.getValor().equals("VE")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoVestibular");
			}
			if (formaIngresso.getValor().equals("TE")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoTransferenciaExterna");
			}
			if (formaIngresso.getValor().equals("RE")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoReingresso");
			}
			if (formaIngresso.getValor().equals("PR")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoProuni");
			}
			if (formaIngresso.getValor().equals("EN")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoEnem");
			}
			if (formaIngresso.getValor().equals("OS")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoOutrosTiposdeSelecao");
			}
			if (formaIngresso.getValor().equals("DJ")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoDecisaoJudicial");
			}
			if (formaIngresso.getValor().equals("AS")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoAvaliacaoSeriada");
			}
			if (formaIngresso.getValor().equals("SS")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoSelecaoSimplificada");
			}
			if (formaIngresso.getValor().equals("PE")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoPEC-G");
			}
			if (formaIngresso.getValor().equals("TO")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoTransferenciaExOfficio");
			}
			if (formaIngresso.getValor().equals("VR")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoVagasRemanescentes");
			}
			if (formaIngresso.getValor().equals("VP")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoVagasdeProgramasEspeciais");
			}
			if (formaIngresso.getValor().equals("FI")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "Matricula_PermitirApresentarFormaIngressoVagasdeProgramasEspeciaisFies");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void verificarVisuaizarAbaDescontoPlanoFinanceiro() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "VisualizarAbaDescontoPlanoFinanceiro");
			setMostrarAbaDescontoPlanoFinanceiro(true);
		} catch (Exception e) {
			setMostrarAbaDescontoPlanoFinanceiro(false);
		}
	}

	public void verificarPermissaoBloquearLancamentoDescontoMatriculaParcela() {
//		try {
//			Matricula.verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "MatriculaBloqueiaDescontoParcelaMatricula");
//			setMatriculaBloqueiaDescontoParcelaMatricula(true);
//		} catch (Exception e) {
			setMatriculaBloqueiaDescontoParcelaMatricula(false);
			//setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public void verificarVisualizarCheckBoxFinanceiroManual() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "AtivarFinanceiroManual");
			setMostrarCheckBoxFinanceiroManual(true);
		} catch (Exception e) {
			setMostrarCheckBoxFinanceiroManual(false);
			// setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void verificarPermissaoAlterarDadosAcademicos() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "MatriculaAlterarDadosAcademicos");
			setPermissaoAlterarDadosAcademicos(true);
		} catch (Exception e) {
			setPermissaoAlterarDadosAcademicos(false);
		//	setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getMostrarAbaDescontos() {
		if (mostrarAbaDescontos == null) {
			mostrarAbaDescontos = false;
		}
		return mostrarAbaDescontos;
	}

	public void setMostrarAbaDescontos(Boolean mostrarAbaDescontos) {
		this.mostrarAbaDescontos = mostrarAbaDescontos;
	}

	public Boolean getMostrarAbaDescontoPlanoFinanceiro() {
		if (mostrarAbaDescontoPlanoFinanceiro == null) {
			mostrarAbaDescontoPlanoFinanceiro = false;
		}
		return mostrarAbaDescontoPlanoFinanceiro;
	}

	public void setMostrarAbaDescontoPlanoFinanceiro(Boolean mostrarAbaDescontoPlanoFinanceiro) {
		this.mostrarAbaDescontoPlanoFinanceiro = mostrarAbaDescontoPlanoFinanceiro;
	}

	public Boolean getPermissaoAlterarDadosAcademicos() {
		if (permissaoAlterarDadosAcademicos == null) {
			permissaoAlterarDadosAcademicos = false;
		}
		return permissaoAlterarDadosAcademicos;
	}

	public void setPermissaoAlterarDadosAcademicos(Boolean permissaoAlterarDadosAcademicos) {
		this.permissaoAlterarDadosAcademicos = permissaoAlterarDadosAcademicos;
	}

	public String getMascaraConsultaAluno() {
		if (getCampoConsultaAlunoNovaMatricula().equals("CPF")) {
			return "return mascara(this.form,'formAlunoNovaMatricula:valorConsultaAluno','999.999.999-99',event)";
		}
		return "";
	}

	/**
	 * @return the valorConsultaData
	 */
	public Date getValorConsultaData() {
		if (valorConsultaData == null) {
			valorConsultaData = new Date();
		}
		return valorConsultaData;
	}

	/**
	 * @param valorConsultaData
	 *            the valorConsultaData to set
	 */
	public void setValorConsultaData(Date valorConsultaData) {
		this.valorConsultaData = valorConsultaData;
	}

	public boolean getApresentarCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}

	public boolean getApresentarCampoTurma() {
		if (getControleConsulta().getCampoConsulta().equals("turma")) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarCampoAlterarFormaIngresso() {
		if (!getMatriculaVO().getFormaIngresso().equals("TI") && !getMatriculaVO().getFormaIngresso().equals("TE")) {
			return true;
		}
		return false;
	}

	public Boolean getMostrarCheckBoxFinanceiroManual() {
		if (mostrarCheckBoxFinanceiroManual == null) {
			mostrarCheckBoxFinanceiroManual = Boolean.FALSE;
		}
		return mostrarCheckBoxFinanceiroManual;
	}

	public void setMostrarCheckBoxFinanceiroManual(Boolean mostrarCheckBoxFinanceiroManual) {
		this.mostrarCheckBoxFinanceiroManual = mostrarCheckBoxFinanceiroManual;
	}

	public Integer getPrimeiroPeriodoLetivoSelecionado() {
		if (primeiroPeriodoLetivoSelecionado == null) {
			primeiroPeriodoLetivoSelecionado = 0;
		}
		return primeiroPeriodoLetivoSelecionado;
	}

	public void setPrimeiroPeriodoLetivoSelecionado(Integer primeiroPeriodoLetivoSelecionado) {
		this.primeiroPeriodoLetivoSelecionado = primeiroPeriodoLetivoSelecionado;
	}

	public Integer getPrimeiraTurmaSelecionada() {
		if (primeiraTurmaSelecionada == null) {
			primeiraTurmaSelecionada = 0;
		}
		return primeiraTurmaSelecionada;
	}

	public void setPrimeiraTurmaSelecionada(Integer primeiraTurmaSelecionada) {
		this.primeiraTurmaSelecionada = primeiraTurmaSelecionada;
	}

	/**
	 * @return the usernameLiberarInclusaoDisciplinaForaPrazo
	 */
	public String getUsernameLiberarInclusaoDisciplinaForaPrazo() {
		if (usernameLiberarInclusaoDisciplinaForaPrazo == null) {
			usernameLiberarInclusaoDisciplinaForaPrazo = "";
		}
		return usernameLiberarInclusaoDisciplinaForaPrazo;
	}

	/**
	 * @param usernameLiberarInclusaoDisciplinaForaPrazo
	 *            the usernameLiberarInclusaoDisciplinaForaPrazo to set
	 */
	public void setUsernameLiberarInclusaoDisciplinaForaPrazo(String usernameLiberarInclusaoDisciplinaForaPrazo) {
		this.usernameLiberarInclusaoDisciplinaForaPrazo = usernameLiberarInclusaoDisciplinaForaPrazo;
	}

	/**
	 * @return the senhaLiberarInclusaoDisciplinaForaPrazo
	 */
	public String getSenhaLiberarInclusaoDisciplinaForaPrazo() {
		if (senhaLiberarInclusaoDisciplinaForaPrazo == null) {
			senhaLiberarInclusaoDisciplinaForaPrazo = "";
		}
		return senhaLiberarInclusaoDisciplinaForaPrazo;
	}

	/**
	 * @param senhaLiberarInclusaoDisciplinaForaPrazo
	 *            the senhaLiberarInclusaoDisciplinaForaPrazo to set
	 */
	public void setSenhaLiberarInclusaoDisciplinaForaPrazo(String senhaLiberarInclusaoDisciplinaForaPrazo) {
		this.senhaLiberarInclusaoDisciplinaForaPrazo = senhaLiberarInclusaoDisciplinaForaPrazo;
	}

	/**
	 * @return the apresentarRichAvisoContaReceber
	 */
	public String getApresentarRichAvisoContaReceber() {
		if (apresentarRichAvisoContaReceber == null) {
			apresentarRichAvisoContaReceber = "";
		}
		return apresentarRichAvisoContaReceber;
	}

	/**
	 * @param apresentarRichAvisoContaReceber
	 *            the apresentarRichAvisoContaReceber to set
	 */
	public void setApresentarRichAvisoContaReceber(String apresentarRichAvisoContaReceber) {
		this.apresentarRichAvisoContaReceber = apresentarRichAvisoContaReceber;
	}

	

	public boolean isAvisoRenovandoMatriculaExistente() {
		return avisoRenovandoMatriculaExistente;
	}

	public void setAvisoRenovandoMatriculaExistente(boolean avisoRenovandoMatriculaExistente) {
		this.avisoRenovandoMatriculaExistente = avisoRenovandoMatriculaExistente;
	}

	public Boolean getRenovandoMatricula() {
		if (renovandoMatricula == null) {
			renovandoMatricula = false;
		}
		return renovandoMatricula;
	}

	public void setRenovandoMatricula(Boolean renovandoMatricula) {
		this.renovandoMatricula = renovandoMatricula;
	}

	public List getListaSelectItemFinanciamentoEstudantil() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		for (FinanciamentoEstudantil item : FinanciamentoEstudantil.values()) {
			String value = (String) item.getValor();
			String label = (String) item.getDescricao().toUpperCase();
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public void setListaSelectItemFinanciamentoEstudantil(List<SelectItem> listaSelectItemFinanciamentoEstudantil) {
		this.listaSelectItemFinanciamentoEstudantil = listaSelectItemFinanciamentoEstudantil;
	}

	public Boolean getIsMostrarFinanciamentoEstudantil() {
		if (isMostrarFinanciamentoEstudantil == null) {
			isMostrarFinanciamentoEstudantil = Boolean.FALSE;
		}
		return isMostrarFinanciamentoEstudantil;
	}

	public void setIsMostrarFinanciamentoEstudantil(Boolean isMostrarFinanciamentoEstudantil) {
		this.isMostrarFinanciamentoEstudantil = isMostrarFinanciamentoEstudantil;
	}

	/**
	 * @return the listaSelectItemFormacaoAcademicaAluno
	 */
	public Boolean getApresentarFormacaoAcademica() {
		if (apresentarFormacaoAcademica == null) {
			apresentarFormacaoAcademica = Boolean.FALSE;
		}
		return apresentarFormacaoAcademica;
	}

	public boolean verificarFormacaoAcademica() {
		if (getMatriculaVO().getCurso().getCodigo().intValue() != 0) {
			try {
				getMatriculaVO().getCurso().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				getFacadeFactory().getCursoFacade().carregarDados(getMatriculaVO().getCurso(), NivelMontarDados.BASICO, getUsuarioLogado());
			} catch (Exception e) {
				getMatriculaVO().setFormacaoAcademica(new FormacaoAcademicaVO());
				return false;
			}
			return getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao();
		} else {
			getMatriculaVO().setFormacaoAcademica(new FormacaoAcademicaVO());
			return false;
		}
	}

	public boolean getApresentarReconhecimentoCurso() {
		if (getListaSelectItemReconhecimentoCurso().isEmpty()) {
			return false;
		} else if (getListaSelectItemReconhecimentoCurso().size() > 2) {
			return true;
		} else {
			return false;
		}
	}

	public void selecionarReconhecimentoCurso() {
		getMatriculaVO().getAutorizacaoCurso().getCodigo();
		// System.out.print("selecionou reconhecimento curso");
	}

	public List getListaSelectItemFormacaoAcademicaAluno() {
		if (getMatriculaVO().getAluno() != null) {
			if (getMatriculaVO().getAluno().getCodigo().intValue() != 0) {
				setListaSelectItemFormacaoAcademicaAluno(new ArrayList(0));
				getMatriculaVO().getAluno().setFormacaoAcademicaVOs(new ArrayList<FormacaoAcademicaVO>(0));
				try {
					// getFacadeFactory().getPessoaFacade().carregarDados(getMatriculaVO().getAluno(),
					// getUsuarioLogado());
					getMatriculaVO().getAluno().setFormacaoAcademicaVOs(getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(getMatriculaVO().getAluno().getCodigo(), getUsuarioLogado(), 0));
				} catch (Exception e) {
				}
				Iterator i = getMatriculaVO().getAluno().getFormacaoAcademicaVOs().iterator();
				List objs = new ArrayList(0);
				objs.add(new SelectItem(0, ""));
				while (i.hasNext()) {
					FormacaoAcademicaVO obj = (FormacaoAcademicaVO) i.next();
					objs.add(new SelectItem(obj.getCodigo(), obj.getCurso() + " - " + obj.getInstituicao()));
				}
				SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
				Collections.sort((List) objs, ordenador);
				// setListaSelectItemFormacaoAcademicaAluno(objs);
				return objs;
			} else {
				return new ArrayList(0);
			}
		} else {
			return new ArrayList(0);
		}
		// return listaSelectItemFormacaoAcademicaAluno;
	}

	/**
	 * @param listaSelectItemFormacaoAcademicaAluno
	 *            the listaSelectItemFormacaoAcademicaAluno to set
	 */
	public void setListaSelectItemFormacaoAcademicaAluno(List listaSelectItemFormacaoAcademicaAluno) {
		this.listaSelectItemFormacaoAcademicaAluno = listaSelectItemFormacaoAcademicaAluno;
	}

	public List getListaSelectItemReconhecimentoCurso() {
		if (getMatriculaVO().getCurso() != null) {
			if (getMatriculaVO().getCurso().getCodigo().intValue() != 0) {
				setListaSelectItemReconhecimentoCurso(new ArrayList(0));
				List listaCurso = new ArrayList(0);
				// getMatriculaVO().getAluno().setFormacaoAcademicaVOs(new
				// ArrayList<FormacaoAcademicaVO>(0));
				try {
					listaCurso = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCurso(getMatriculaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				} catch (Exception e) {
					setListaSelectItemReconhecimentoCurso(new ArrayList(0));
				}
				Iterator i = listaCurso.iterator();
				List objs = new ArrayList(0);
				objs.add(new SelectItem(0, ""));
				while (i.hasNext()) {
					AutorizacaoCursoVO obj = (AutorizacaoCursoVO) i.next();
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
				SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
				Collections.sort((List) objs, ordenador);
				return objs;
			} else {
				return new ArrayList(0);
			}
		} else {
			return new ArrayList(0);
		}
	}

	public void setListaSelectItemReconhecimentoCurso(List listaSelectItemReconhecimentoCurso) {
		this.listaSelectItemReconhecimentoCurso = listaSelectItemReconhecimentoCurso;
	}

	/**
	 * @return the liberarAlteracaoDescontoProgressivoPrimeiraParcela
	 */
	public Boolean getLiberarAlteracaoDescontoProgressivoPrimeiraParcela() {
		if (liberarAlteracaoDescontoProgressivoPrimeiraParcela == null) {
			liberarAlteracaoDescontoProgressivoPrimeiraParcela = Boolean.FALSE;
		}
		return liberarAlteracaoDescontoProgressivoPrimeiraParcela;
	}

	/**
	 * @param liberarAlteracaoDescontoProgressivoPrimeiraParcela
	 *            the liberarAlteracaoDescontoProgressivoPrimeiraParcela to set
	 */
	public void setLiberarAlteracaoDescontoProgressivoPrimeiraParcela(Boolean liberarAlteracaoDescontoProgressivoPrimeiraParcela) {
		this.liberarAlteracaoDescontoProgressivoPrimeiraParcela = liberarAlteracaoDescontoProgressivoPrimeiraParcela;
	}

	/**
	 * @return the valorConsultaTurma
	 */
	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	/**
	 * @param valorConsultaTurma
	 *            the valorConsultaTurma to set
	 */
	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	/**
	 * @return the campoConsultaTurma
	 */
	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	/**
	 * @param campoConsultaTurma
	 *            the campoConsultaTurma to set
	 */
	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	/**
	 * @return the listaConsultaTurma
	 */
	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList(0);
		}
		return listaConsultaTurma;
	}

	/**
	 * @param listaConsultaTurma
	 *            the listaConsultaTurma to set
	 */
	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	/**
	 * @return the situacaoFiltroTurma
	 */
	public String getSituacaoFiltroTurma() {
		if (situacaoFiltroTurma == null) {
			situacaoFiltroTurma = "";
		}
		return situacaoFiltroTurma;
	}

	/**
	 * @param situacaoFiltroTurma
	 *            the situacaoFiltroTurma to set
	 */
	public void setSituacaoFiltroTurma(String situacaoFiltroTurma) {
		this.situacaoFiltroTurma = situacaoFiltroTurma;
	}

	

	public Boolean getMostrarBotaoContratoExtensao() {
		if (getMatriculaPeriodoVO().getContratoExtensao() != null && getMatriculaPeriodoVO().getContratoExtensao().getCodigo() != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	

	/**
	 * @return the campoConsultaFuncionario
	 */
	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	/**
	 * @param campoConsultaFuncionario
	 *            the campoConsultaFuncionario to set
	 */
	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	/**
	 * @return the valorConsultaFuncionario
	 */
	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	/**
	 * @param valorConsultaFuncionario
	 *            the valorConsultaFuncionario to set
	 */
	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	/**
	 * @return the listaConsultaFuncionario
	 */
	public List getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList(0);
		}
		return listaConsultaFuncionario;
	}

	/**
	 * @param listaConsultaFuncionario
	 *            the listaConsultaFuncionario to set
	 */
	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public boolean getApresentarBotaoConsultor() {
		return getMatriculaVO().getMatricula().equals("");
	}

	/**
	 * @return the autorizacaoApresentarConsultorMatricula
	 */
	public Boolean getAutorizacaoApresentarConsultorMatricula() {
		if (autorizacaoApresentarConsultorMatricula == null) {
			autorizacaoApresentarConsultorMatricula = Boolean.FALSE;
		}
		return autorizacaoApresentarConsultorMatricula;
	}

	/**
	 * @param autorizacaoApresentarConsultorMatricula
	 *            the autorizacaoApresentarConsultorMatricula to set
	 */
	public void setAutorizacaoApresentarConsultorMatricula(Boolean autorizacaoApresentarConsultorMatricula) {
		this.autorizacaoApresentarConsultorMatricula = autorizacaoApresentarConsultorMatricula;
	}

	/**
	 * @return the AutorizacaoApresentarConsultorMatricula
	 */
	public String getCaminhoImagemComprovanteFichaInscricao() {
		if (getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			return "/resources/imagens/botaoEmitirFichaInscricao.png";
		}
		return "/resources/imagens/botaoEmitirComprovante.png";
	}

	public Boolean getPermiteInformarTipoMatricula() {
		if (permiteInformarTipoMatricula == null) {
			permiteInformarTipoMatricula = false;
		}
		return permiteInformarTipoMatricula;
	}

	public void setPermiteInformarTipoMatricula(Boolean permiteInformarTipoMatricula) {
		this.permiteInformarTipoMatricula = permiteInformarTipoMatricula;
	}

	public Boolean getIsPermiteInformarTipoMatricula() {
		// if (getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao() &&
		// getPermiteInformarTipoMatricula()) {
		if (getPermiteInformarTipoMatricula() && getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getPermiteAnexarImagemDocumentosEntregues() {
		if (permiteAnexarImagemDocumentosEntregues == null) {
			permiteAnexarImagemDocumentosEntregues = false;
		}
		return permiteAnexarImagemDocumentosEntregues;
	}

	public void setPermiteAnexarImagemDocumentosEntregues(Boolean permiteAnexarImagemDocumentosEntregues) {
		this.permiteAnexarImagemDocumentosEntregues = permiteAnexarImagemDocumentosEntregues;
	}

	public Boolean getEditandoMatricula() {
		if (editandoMatricula == null) {
			editandoMatricula = false;
		}
		return editandoMatricula;
	}

	public void setEditandoMatricula(Boolean editandoMatricula) {
		this.editandoMatricula = editandoMatricula;
	}

	/**
	 * @return the controlarSuspensaoMatriculaPendenciaDocumentos
	 */
	public Boolean getControlarSuspensaoMatriculaPendenciaDocumentos() {
		if (controlarSuspensaoMatriculaPendenciaDocumentos == null) {
			controlarSuspensaoMatriculaPendenciaDocumentos = Boolean.FALSE;
		}
		return controlarSuspensaoMatriculaPendenciaDocumentos;
	}

	/**
	 * @param controlarSuspensaoMatriculaPendenciaDocumentos
	 *            the controlarSuspensaoMatriculaPendenciaDocumentos to set
	 */
	public void setControlarSuspensaoMatriculaPendenciaDocumentos(Boolean controlarSuspensaoMatriculaPendenciaDocumentos) {
		this.controlarSuspensaoMatriculaPendenciaDocumentos = controlarSuspensaoMatriculaPendenciaDocumentos;
	}

	

	/**
	 * @return the matriculaBloqueiaDescontoParcelaMatricula
	 */
	public boolean isMatriculaBloqueiaDescontoParcelaMatricula() {
		return matriculaBloqueiaDescontoParcelaMatricula;
	}

	/**
	 * @param matriculaBloqueiaDescontoParcelaMatricula
	 *            the matriculaBloqueiaDescontoParcelaMatricula to set
	 */
	public void setMatriculaBloqueiaDescontoParcelaMatricula(boolean matriculaBloqueiaDescontoParcelaMatricula) {
		this.matriculaBloqueiaDescontoParcelaMatricula = matriculaBloqueiaDescontoParcelaMatricula;
	}

	public Integer getTipoLayoutComprovanteMatricula() {
		if (tipoLayoutComprovanteMatricula == null) {
			tipoLayoutComprovanteMatricula = 2;
		}
		return tipoLayoutComprovanteMatricula;
	}

	public void setTipoLayoutComprovanteMatricula(Integer tipoLayoutComprovanteMatricula) {
		this.tipoLayoutComprovanteMatricula = tipoLayoutComprovanteMatricula;
	}

	public List<SelectItem> getListaSelectItemModalidadeDisciplina() {
		return ModalidadeDisciplinaEnum.getListaSelectItemModalidadeDisciplinaEscolhaMatricula();
	}

	/**
	 * @return the PermiteSuspenderMatricula
	 */
	public boolean getPermiteSuspenderMatricula() {
		return permiteSuspenderMatricula;
	}

	/**
	 * @param PermiteSuspenderMatricula
	 *            the PermiteSuspenderMatricula to set
	 */
	public void setPermiteSuspenderMatricula(boolean permiteSuspenderMatricula) {
		this.permiteSuspenderMatricula = permiteSuspenderMatricula;
	}

	/**
	 * @return the apresentarModalAvisoAlunoReprovado
	 */
	public Boolean getApresentarModalAvisoAlunoReprovado() {
		if (apresentarModalAvisoAlunoReprovado == null) {
			apresentarModalAvisoAlunoReprovado = Boolean.FALSE;
		}
		return apresentarModalAvisoAlunoReprovado;
	}

	/**
	 * @param apresentarModalAvisoAlunoReprovado
	 *            the apresentarModalAvisoAlunoReprovado to set
	 */
	public void setApresentarModalAvisoAlunoReprovado(Boolean apresentarModalAvisoAlunoReprovado) {
		this.apresentarModalAvisoAlunoReprovado = apresentarModalAvisoAlunoReprovado;
	}

	/**
	 * @return the mensagemModalAvisoAlunoReprovado
	 */
	public String getMensagemModalAvisoAlunoReprovado() {
		if (mensagemModalAvisoAlunoReprovado == null) {
			mensagemModalAvisoAlunoReprovado = "";
		}
		return mensagemModalAvisoAlunoReprovado;
	}

	/**
	 * @param mensagemModalAvisoAlunoReprovado
	 *            the mensagemModalAvisoAlunoReprovado to set
	 */
	public void setMensagemModalAvisoAlunoReprovado(String mensagemModalAvisoAlunoReprovado) {
		this.mensagemModalAvisoAlunoReprovado = mensagemModalAvisoAlunoReprovado;
	}

	public void atualizarDataMatriculaComBaseDataMatriculaPeriodo() {
		try {
			if (this.getMatriculaPeriodoVO().getIsPrimeiroPeriodoLetivo()) {
				this.getMatriculaVO().setData(this.getMatriculaPeriodoVO().getData());
			}
		} catch (Exception e) {
		}
	}

	public Boolean getRenovacaoRecusada() {
		if (renovacaoRecusada == null) {
			renovacaoRecusada = Boolean.FALSE;
		}
		return renovacaoRecusada;
	}

	public void setRenovacaoRecusada(Boolean renovacaoRecusada) {
		this.renovacaoRecusada = renovacaoRecusada;
	}

	public Boolean getApresentarModalTermoAceite() {
		if (apresentarModalTermoAceite == null) {
			apresentarModalTermoAceite = Boolean.FALSE;
		}
		return apresentarModalTermoAceite;
	}

	public void setApresentarModalTermoAceite(Boolean apresentarModalTermoAceite) {
		this.apresentarModalTermoAceite = apresentarModalTermoAceite;
	}

	public String getNomeArquivo() {
		if (nomeArquivo == null) {
			nomeArquivo = "";
		}
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

//	public List<PlanoFinanceiroAlunoLogVO> getPlanoFinanceiroAlunoLogVOs() {
//		if (planoFinanceiroAlunoLogVOs == null) {
//			planoFinanceiroAlunoLogVOs = new ArrayList<PlanoFinanceiroAlunoLogVO>();
//		}
//		return planoFinanceiroAlunoLogVOs;
//	}
//
//	public void setPlanoFinanceiroAlunoLogVOs(List<PlanoFinanceiroAlunoLogVO> planoFinanceiroAlunoLogVOs) {
//		this.planoFinanceiroAlunoLogVOs = planoFinanceiroAlunoLogVOs;
//	}

	public boolean isPermiteConfirmarCancelarPreMatricula() {
		return permiteConfirmarCancelarPreMatricula;
	}

	public void setPermiteConfirmarCancelarPreMatricula(boolean permiteConfirmarCancelarPreMatricula) {
		this.permiteConfirmarCancelarPreMatricula = permiteConfirmarCancelarPreMatricula;
	}

//	public List<HorarioTurmaDisciplinaSemanalVO> getListaHorarioTurmaDisciplinaSemanalVOs() {
//		if (listaHorarioTurmaDisciplinaSemanalVOs == null) {
//			listaHorarioTurmaDisciplinaSemanalVOs = new ArrayList<HorarioTurmaDisciplinaSemanalVO>(0);
//		}
//		return listaHorarioTurmaDisciplinaSemanalVOs;
//	}
//
//	public void setListaHorarioTurmaDisciplinaSemanalVOs(List<HorarioTurmaDisciplinaSemanalVO> listaHorarioTurmaDisciplinaSemanalVOs) {
//		this.listaHorarioTurmaDisciplinaSemanalVOs = listaHorarioTurmaDisciplinaSemanalVOs;
//	}

	public Date getDataInicioHorario() {
		if (dataInicioHorario == null) {
			dataInicioHorario = new Date();
		}
		return dataInicioHorario;
	}

	public String getDataInicioHorario_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataInicioHorario());
	}

	public void setDataInicioHorario(Date dataInicioHorario) {
		this.dataInicioHorario = dataInicioHorario;
	}

	public Date getDataTerminoHorario() {
		if (dataTerminoHorario == null) {
			dataTerminoHorario = new Date();
		}
		return dataTerminoHorario;
	}

	public String getDataTerminoHorario_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataTerminoHorario());
	}

	public void setDataTerminoHorario(Date dataTerminoHorario) {
		this.dataTerminoHorario = dataTerminoHorario;
	}

	public Boolean getNaoApresentarBotoesMatriculaRenovacao() {
		if (naoApresentarBotoesMatriculaRenovacao == null) {
			naoApresentarBotoesMatriculaRenovacao = Boolean.FALSE;
		}
		return naoApresentarBotoesMatriculaRenovacao;
	}

	public void setNaoApresentarBotoesMatriculaRenovacao(Boolean naoApresentarBotoesMatriculaRenovacao) {
		this.naoApresentarBotoesMatriculaRenovacao = naoApresentarBotoesMatriculaRenovacao;
	}

	public boolean getPermiteLiberarPgtoMatricula() {
		return permiteLiberarPgtoMatricula;
	}

	public void setPermiteLiberarPgtoMatricula(boolean permiteLiberarPgtoMatricula) {
		this.permiteLiberarPgtoMatricula = permiteLiberarPgtoMatricula;
	}

	public Boolean getPermitirRealizarMatriculaDisciplinaPreRequisito() {
		if (permitirRealizarMatriculaDisciplinaPreRequisito == null) {
			permitirRealizarMatriculaDisciplinaPreRequisito = Boolean.FALSE;
		}
		return permitirRealizarMatriculaDisciplinaPreRequisito;
	}

	public void setPermitirRealizarMatriculaDisciplinaPreRequisito(Boolean permitirRealizarMatriculaDisciplinaPreRequisito) {
		this.permitirRealizarMatriculaDisciplinaPreRequisito = permitirRealizarMatriculaDisciplinaPreRequisito;
	}

	public String getUsernamePermitirRealizarMatriculaDisciplinaPreRequisito() {
		if (usernamePermitirRealizarMatriculaDisciplinaPreRequisito == null) {
			usernamePermitirRealizarMatriculaDisciplinaPreRequisito = "";
		}
		return usernamePermitirRealizarMatriculaDisciplinaPreRequisito;
	}

	public void setUsernamePermitirRealizarMatriculaDisciplinaPreRequisito(String usernamePermitirRealizarMatriculaDisciplinaPreRequisito) {
		this.usernamePermitirRealizarMatriculaDisciplinaPreRequisito = usernamePermitirRealizarMatriculaDisciplinaPreRequisito;
	}

	public String getSenhaPermitirRealizarMatriculaDisciplinaPreRequisito() {
		if (senhaPermitirRealizarMatriculaDisciplinaPreRequisito == null) {
			senhaPermitirRealizarMatriculaDisciplinaPreRequisito = "";
		}
		return senhaPermitirRealizarMatriculaDisciplinaPreRequisito;
	}

	public void setSenhaPermitirRealizarMatriculaDisciplinaPreRequisito(String senhaPermitirRealizarMatriculaDisciplinaPreRequisito) {
		this.senhaPermitirRealizarMatriculaDisciplinaPreRequisito = senhaPermitirRealizarMatriculaDisciplinaPreRequisito;
	}

	public void setApresentarFormacaoAcademica(Boolean apresentarFormacaoAcademica) {
		this.apresentarFormacaoAcademica = apresentarFormacaoAcademica;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs() {
		if (listaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs == null) {
			listaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs;
	}

	public void setListaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs(List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs) {
		this.listaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs = listaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs;
	}

	/**
	 * @return the codigoGradeCurricular
	 */
	public Integer getCodigoGradeCurricular() {
		if (codigoGradeCurricular == null) {
			codigoGradeCurricular = 0;
		}
		return codigoGradeCurricular;
	}

	/**
	 * @param codigoGradeCurricular
	 *            the codigoGradeCurricular to set
	 */
	public void setCodigoGradeCurricular(Integer codigoGradeCurricular) {
		this.codigoGradeCurricular = codigoGradeCurricular;
	}

	/**
	 * @return the codigoPeriodoLetivo
	 */
	public Integer getCodigoPeriodoLetivo() {
		if (codigoPeriodoLetivo == null) {
			codigoPeriodoLetivo = 0;
		}
		return codigoPeriodoLetivo;
	}

	/**
	 * @param codigoPeriodoLetivo
	 *            the codigoPeriodoLetivo to set
	 */
	public void setCodigoPeriodoLetivo(Integer codigoPeriodoLetivo) {
		this.codigoPeriodoLetivo = codigoPeriodoLetivo;
	}

	/**
	 * @return the listaSelectItemMapaEquivalenciaDisciplinaIncluir
	 */
	public List getListaSelectItemMapaEquivalenciaDisciplinaIncluir() {
		if (listaSelectItemMapaEquivalenciaDisciplinaIncluir == null) {
			listaSelectItemMapaEquivalenciaDisciplinaIncluir = new ArrayList(0);
		}
		return listaSelectItemMapaEquivalenciaDisciplinaIncluir;
	}

	/**
	 * @param listaSelectItemMapaEquivalenciaDisciplinaIncluir
	 *            the listaSelectItemMapaEquivalenciaDisciplinaIncluir to set
	 */
	public void setListaSelectItemMapaEquivalenciaDisciplinaIncluir(List listaSelectItemMapaEquivalenciaDisciplinaIncluir) {
		this.listaSelectItemMapaEquivalenciaDisciplinaIncluir = listaSelectItemMapaEquivalenciaDisciplinaIncluir;
	}

	public void preparaIncluirDisciplina() {
		setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());
		this.gradeDisciplinaAdicionar = 0;
		this.setGradeDisciplinaEquivalenteAdicionar((Integer) 0);
		montarListaSelectItemPeriodoLetivoAdicionar();
		montarListaSelectItemDisciplina();
		setListaSelectItemTurmaAdicionar(new ArrayList(0));
		setListaSelectItemMapaEquivalenciaDisciplinaIncluir(new ArrayList(0));
		setMensagemID("msg_dados_editar");
	}

	public void montarListaSelectItemPeriodoLetivoComGrupoOptativas() {
		try {
			List objs = new ArrayList(0);
			for (PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistorico : matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOs()) {
				if (periodoLetivoComHistorico.getPeriodoLetivoVO().getControleOptativaGrupo()) {

					if ((getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoDisciplinaPeriodoFuturo())) {
						if (getMatriculaPeriodoVO().getLiberadoControleInclusaoDisciplinaPeriodoFuturo()) {
							objs.add(new SelectItem(periodoLetivoComHistorico.getPeriodoLetivoVO().getCodigo(), periodoLetivoComHistorico.getPeriodoLetivoVO().getDescricao()));
						} else {
							int nrPeriodoLetivoMaximoPermitido = getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo() + getMatriculaVO().getCurso().getConfiguracaoAcademico().getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina();
							if (periodoLetivoComHistorico.getPeriodoLetivoVO().getPeriodoLetivo().compareTo(nrPeriodoLetivoMaximoPermitido) <= 0) {
								// só adiciona para lista periodos letivos que
								// estão dentro da faixa limite
								// permitida para o aluno em questão.
								objs.add(new SelectItem(periodoLetivoComHistorico.getPeriodoLetivoVO().getCodigo(), periodoLetivoComHistorico.getPeriodoLetivoVO().getDescricao()));
							} else {
								setApresentarBotaoLiberarControleInclusaoDisciplinaPeriodoFuturo(Boolean.TRUE);
							}
						}
					} else {
						objs.add(new SelectItem(periodoLetivoComHistorico.getPeriodoLetivoVO().getCodigo(), periodoLetivoComHistorico.getPeriodoLetivoVO().getDescricao()));
					}
				}
			}
			setListaSelectItemPeriodoLetivoComGrupoOptativas(objs);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	public void prepararDisciplinasGrupoOptativaPendentesPeriodosAnteriores() {
		try {
			montarListaSelectItemPeriodoLetivoComGrupoOptativas();
			if (getListaSelectItemPeriodoLetivoComGrupoOptativas().isEmpty()) {
				throw new Exception("Não existem Períodos com Grupo de Eletivas Definido para Inclusão de Disciplinas.");
			}
			SelectItem primeiraPeriodoComGrupo = (SelectItem) getListaSelectItemPeriodoLetivoComGrupoOptativas().get(0);
			Integer codigoPrimeiroPeriodoComOptativa = 0;
			if (primeiraPeriodoComGrupo != null) {
				codigoPrimeiroPeriodoComOptativa = (Integer) primeiraPeriodoComGrupo.getValue();
			}
//			if (codigoPrimeiroPeriodoComOptativa.equals(0)) {
//				throw new Exception("Não existem Períodos com Grupo de Optativas Definido para Inclusão de Disciplinas.");
//			}
			// vamos obter o primeiro no qual o aluno tenha pendencias
			setCodigoPeriodoLetivoIncluirDisciplinasGrupoOptativas(codigoPrimeiroPeriodoComOptativa);
			prepararDisciplinasGrupoOptativa();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void prepararDisciplinasGrupoOptativaPeriodoLetivoRenovacaoAluno() {
		montarListaSelectItemPeriodoLetivoComGrupoOptativas();
		if(matriculaPeriodoVO.getPeriodoLetivo().getControleOptativaGrupo()){
			setCodigoPeriodoLetivoIncluirDisciplinasGrupoOptativas(matriculaPeriodoVO.getPeriodoLetivo().getCodigo());
		}else if(!getListaSelectItemPeriodoLetivoComGrupoOptativas().isEmpty()){			
			setCodigoPeriodoLetivoIncluirDisciplinasGrupoOptativas((Integer)((SelectItem)getListaSelectItemPeriodoLetivoComGrupoOptativas().get(0)).getValue());
		}	
		prepararDisciplinasGrupoOptativa();
	}

	public void prepararDisciplinasGrupoOptativa() {
		try {
			PeriodoLetivoComHistoricoAlunoVO periodoComHistoricoVO = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(getCodigoPeriodoLetivoIncluirDisciplinasGrupoOptativas());
			if(periodoComHistoricoVO != null && Uteis.isAtributoPreenchido(periodoComHistoricoVO.getPeriodoLetivoVO()) && Uteis.isAtributoPreenchido(periodoComHistoricoVO.getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getCodigo())){
				Integer codigoGradeCurricularGrupoOptativas = periodoComHistoricoVO.getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getCodigo();
				if(periodoComHistoricoVO.getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().isEmpty()) {
					GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO = getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(codigoGradeCurricularGrupoOptativas, NivelMontarDados.TODOS, getUsuarioLogado());
					periodoComHistoricoVO.getPeriodoLetivoVO().setGradeCurricularGrupoOptativa(gradeCurricularGrupoOptativaVO);
				}
				Integer unidadeEnsinoFiltro = isLiberadoInclusaoTurmaOutroUnidadeEnsino() ? 0 : getMatriculaVO().getUnidadeEnsino().getCodigo();
				Integer cursoFiltro = getLiberadoInclusaoTurmaOutroCurso() ? 0 : getMatriculaVO().getCurso().getCodigo();
				Integer matrizCurricularFiltro = getLiberadoInclusaoTurmaOutroMatrizCurricular() ? 0 : getMatriculaVO().getGradeCurricularAtual().getCodigo();
				for(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO: periodoComHistoricoVO.getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
					gradeCurricularGrupoOptativaDisciplinaVO.setDisciplinaJaAdicionada(getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().stream().anyMatch(t -> t.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(gradeCurricularGrupoOptativaDisciplinaVO.getCodigo())));
					if(!gradeCurricularGrupoOptativaDisciplinaVO.getOfertaJaValida()) {
						getFacadeFactory().getMatriculaPeriodoFacade().realizarVerificacaoOfertaDisciplinaGrupoOptativa(getMatriculaVO(), getMatriculaPeriodoVO(), gradeCurricularGrupoOptativaDisciplinaVO, isLiberadoInclusaoTurmaOutroUnidadeEnsino(), getLiberadoInclusaoTurmaOutroCurso(), getLiberadoInclusaoTurmaOutroMatrizCurricular(), true,  getUsuarioLogado().getIsApresentarVisaoAlunoOuPais(), getUsuarioLogado());
					}
				}
				this.setPeriodoLetivoComHistoricoVOIncluirGrupoOptativas(periodoComHistoricoVO);
			}
		} catch (Exception e) {
			this.setPeriodoLetivoComHistoricoVOIncluirGrupoOptativas(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarDisciplinaGrupoOptativaMatriculaPeriodo() {
		try {
			this.setPaginaDestinoInclusaoDisciplina("PF('panelGrupoOptativa').hide(); PF('panelIncluirDisciplinas').show();");
			GradeCurricularGrupoOptativaDisciplinaVO gradeOptativaAdicionar = (GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("grupoOptativaDisciplinaItens");
			
			if (getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && getMatriculaVO().getCurso().getConfiguracaoAcademico().getBloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular() && (!getMatriculaPeriodoVO().getLiberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular())) {
				// se entrar aqui é por que o aluno é regular (nao deve nenhuma
				// materia do passado) e o
				// bloqueio para inclusao de disciplina está ativo. Logo, nao
				// pode ser incluida disciplina para
				// este aluno até que o usuário com senha tenha permissão para
				// fazer isto.
				if ((getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && getPermiteIncluirDisciplinaOptativa() && gradeOptativaAdicionar.getGradeCurricularGrupoOptativa().getCodigo().equals(getMatriculaPeriodoVO().getPeriodoLetivo().getGradeCurricularGrupoOptativa().getCodigo())) || (!getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && getPermiteIncluirDisciplinaOptativa())) {
					this.setApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular(false);
				} else {
					this.setApresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular(true);
					this.setPaginaDestinoInclusaoDisciplina("");
					if (!getRenovandoMatriculaDaVisaoDoAluno()) {
						setPaginaDestinoInclusaoDisciplina("");
						throw new Exception("Não é permitido Incluir uma Disciplina para um Aluno Regular (Aluno que não está devendo nenhuma disciplina dos períodos anteriores). Clique no botão Liberar Inclusão Disciplinas para realizar esta operação.");
					} else {
						setPaginaDestinoInclusaoDisciplina("");
						throw new Exception("Não é permitido Incluir uma Disciplina para um Aluno Regular (Aluno que não está devendo nenhuma disciplina dos períodos anteriores).");
					}
				}
			}	
			// Este if abaixo verifica se existe a obrigatoriedade de incluir as disciplinas de DP primeiro que as regulares em comparação com a disciplina optativa selecionado para inclusão,
			// então é verificado se existe oferta de DP ou de Regular e se existe alguma DP ou Regular que tem a qtde de horas dentro do saldo de horas disponível,
			// caso não seja possível adicionar é lançado uma exceção  		
			if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia() && getMatriculaVO().getCurso().getConfiguracaoAcademico().getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH()) {
				List<GradeDisciplinaVO> listaDisciplinasPeriodoLetivoAlunoPendente = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getListaGradeDisciplinaVOsPendentesGradeCurricular(getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo());
				if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR") && listaDisciplinasPeriodoLetivoAlunoPendente.stream().anyMatch(c -> c.isGradeDisciplinaOfertada() && !c.getJaIncluidaRenovacao() && c.getNrCreditos() <= getMatriculaPeriodoVO().getSaldoCreditoDisponivelInclusaoDisplinas() &&   c.getPeriodoLetivoVO().getPeriodoLetivo() <= getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo()   )) {
					setPaginaDestinoInclusaoDisciplina("");
					throw new Exception("Só é possível incluir disciplina eletiva após incluir as disciplinas de dependência ou regular ofertadas.");
				}else if(listaDisciplinasPeriodoLetivoAlunoPendente.stream().anyMatch(c -> c.isGradeDisciplinaOfertada() && !c.getJaIncluidaRenovacao() && c.getCargaHoraria() <= getMatriculaPeriodoVO().getSaldoCHDisponivelInclusaoDisplinas() &&   c.getPeriodoLetivoVO().getPeriodoLetivo() <= getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo() )) {
					setPaginaDestinoInclusaoDisciplina("");
					throw new Exception("Só é possível incluir disciplina eletiva após incluir as disciplinas de dependência ou regular ofertadas.");	
				}
				
			}
			
			// Este regra verificada se o grupo de disciplina incluida é diferente do grupo de optativas do periodo letivo da matricula periodo, e se corresponde a um período letivo do futuro, caso seja é verificado se existem ofertas de disciplinas do periodo letivo atual disponível para inclusão, caso tenha o aluno deverá incluir as disciplinas do periodo letivo atual primeiro
			if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getPeriodoLetivo().getGradeCurricularGrupoOptativa().getCodigo()) && getMatriculaPeriodoVO().getPeriodoLetivo().getControleOptativaGrupo() && getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroCargaHorariaOptativa() > 0 
					&& getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getPeriodoLetivo() > getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo()
					&& !getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getCodigo().equals(getMatriculaPeriodoVO().getPeriodoLetivo().getGradeCurricularGrupoOptativa().getCodigo())) {
				Integer totalChEletivaGrupoPeriodoLetivoAtual = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().stream().filter(
					p -> p.getControleOptativaGrupo() && p.getGradeCurricularGrupoOptativa().getCodigo().equals(getMatriculaPeriodoVO().getPeriodoLetivo().getGradeCurricularGrupoOptativa().getCodigo()) && p.getPeriodoLetivo() <= getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo() && p.getNumeroCargaHorariaOptativa() > 0).mapToInt(p -> p.getNumeroCargaHorariaOptativa()).sum();
				Integer totalCumpridaGrupoPeriodoLetivoAtual = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaDisciplinaGrupoOptativaCumprida(getMatriculaPeriodoVO().getPeriodoLetivo().getGradeCurricularGrupoOptativa().getCodigo());
				if(((totalCumpridaGrupoPeriodoLetivoAtual +  
					getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> d.getDisciplinaReferenteAUmGrupoOptativa()  && d.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeCurricularGrupoOptativa().getCodigo().equals(getMatriculaPeriodoVO().getPeriodoLetivo().getGradeCurricularGrupoOptativa().getCodigo()) && !d.getHistoricoVO().getSituacao().equals("AP") && !d.getHistoricoVO().getSituacao().equals("AE")).mapToInt(d -> d.getCargaHoraria()).sum())
					< totalChEletivaGrupoPeriodoLetivoAtual)) {
					PeriodoLetivoComHistoricoAlunoVO periodoComHistoricoVO = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(getCodigoPeriodoLetivoIncluirDisciplinasGrupoOptativas());
					if(periodoComHistoricoVO.getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().stream().anyMatch(d -> !d.getDisciplinaJaAdicionada() && d.getDisciplinaOfertada() && d.getCargaHoraria() <= getMatriculaPeriodoVO().getSaldoCHDisponivelInclusaoDisplinas())) {
						setPaginaDestinoInclusaoDisciplina("");
						throw new Exception("Só é possível incluir as optativas do perído letivo  "+getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getPeriodoLetivo()+" após incluir as optativas do período letivo "+getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo()+".");
					}
				}
			}
			
			// Esta regra calcula todas as horas parametrizadas do grupo de disciplina de optativa selecionado que é exigido na matriz curricular e compara com o que o aluno já curso ou está cursando, caso o aluno já tenha estas horas de disciplinas incluida é informado que o mesmo não poderá incluir disciplinas deste grupo.
			Integer totalChEletivaGrupo = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().stream().filter(
					p -> p.getControleOptativaGrupo() && p.getGradeCurricularGrupoOptativa().getCodigo().equals(gradeOptativaAdicionar.getGradeCurricularGrupoOptativa().getCodigo()) && (p.getPeriodoLetivo() <= getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo() || 
					(getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoDisciplinaPeriodoFuturo() && 
							(getMatriculaVO().getCurso().getConfiguracaoAcademico().getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina().equals(0) || 
									(p.getPeriodoLetivo() <= 
									(getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo() + getMatriculaVO().getCurso().getConfiguracaoAcademico().getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina()))))) && p.getNumeroCargaHorariaOptativa() > 0).mapToInt(p -> p.getNumeroCargaHorariaOptativa()).sum();
			Integer totalCumpridaGrupo = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaDisciplinaGrupoOptativaCumprida(gradeOptativaAdicionar.getGradeCurricularGrupoOptativa().getCodigo());
			if(((totalCumpridaGrupo +  
					getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> d.getDisciplinaReferenteAUmGrupoOptativa()  && d.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeCurricularGrupoOptativa().getCodigo().equals(gradeOptativaAdicionar.getGradeCurricularGrupoOptativa().getCodigo()) && !d.getHistoricoVO().getSituacao().equals("AP") && !d.getHistoricoVO().getSituacao().equals("AE")).mapToInt(d -> d.getCargaHoraria()).sum())
					>= totalChEletivaGrupo)) {
				setPaginaDestinoInclusaoDisciplina("");
				throw new Exception("Você já atingiu o limite máximo de disciplinas eletivas a serem cumpridas até período letivo "+getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getPeriodoLetivo()+" que é de "+totalChEletivaGrupo+" horas, portanto não é possível incluir novas disciplinas eletivas deste período.");
			}
			
			// Esta regra verifica se o aluno já cumpriu todas as horas de eletiva até o período letivo atual e barra a inclusão de novas disciplinas optativas.
			Integer totalChEletiva = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().stream().filter(
					p -> p.getControleOptativaGrupo() && (p.getPeriodoLetivo() <= getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo() || 
					(getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoDisciplinaPeriodoFuturo() && 
							(getMatriculaVO().getCurso().getConfiguracaoAcademico().getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina().equals(0) || 
									(p.getPeriodoLetivo() <= 
									(getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo() + getMatriculaVO().getCurso().getConfiguracaoAcademico().getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina()))))) && p.getNumeroCargaHorariaOptativa() > 0).mapToInt(p -> p.getNumeroCargaHorariaOptativa()).sum();
			if(  totalChEletiva > 0 && ((getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaDisciplinaOptativaCumprida() +  
					getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> (d.getDisciplinaReferenteAUmGrupoOptativa() || d.getDisciplinaOptativa()) && !d.getHistoricoVO().getSituacao().equals("AP") && !d.getHistoricoVO().getSituacao().equals("AE") ).mapToInt(d -> d.getCargaHoraria()).sum())
					>= totalChEletiva)) {
				setPaginaDestinoInclusaoDisciplina("");
				throw new Exception("Você já atingiu o limite máximo de disciplinas eletivas a serem cumpridas até período letivo "+getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo()+" que é de "+totalChEletiva+" horas, portanto não é possível incluir novas disciplinas eletivas.");
			}
			// Esta regra verifica se o aluno já cumpriu as horas eletivas exigida na matríz curricular, caso já tenha não poderá incluir novas disciplinas.
			if(((getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaDisciplinaOptativaCumprida() +  
					getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> (d.getDisciplinaReferenteAUmGrupoOptativa() || d.getDisciplinaOptativa()) && !d.getHistoricoVO().getSituacao().equals("AP") && !d.getHistoricoVO().getSituacao().equals("AE")).mapToInt(d -> d.getCargaHoraria()).sum())
					>= getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCargaHorariaOptativaExigida())) {
				setPaginaDestinoInclusaoDisciplina("");
				throw new Exception("Você já atingiu o limite máximo de disciplinas eletivas a serem cumpridas ("+getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCargaHorariaOptativaExigida()+"H) na matriz curricular, portanto não é possível incluir novas disciplinas eletivas.");
			}
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina(gradeOptativaAdicionar.getDisciplina());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurma(new TurmaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeDisciplinaVO(new GradeDisciplinaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeCurricularGrupoOptativaDisciplinaVO(gradeOptativaAdicionar);
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeCurricularGrupoOptativaDisciplinaVO().getPeriodoLetivoDisciplinaReferenciada().setCodigo(getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getCodigo());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeCurricularGrupoOptativaDisciplinaVO().getPeriodoLetivoDisciplinaReferenciada().setDescricao(getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getDescricao());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeCurricularGrupoOptativaDisciplinaVO().getPeriodoLetivoDisciplinaReferenciada().setPeriodoLetivo(getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getPeriodoLetivo());		
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaReferenteAUmGrupoOptativa(Boolean.TRUE);
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setModalidadeDisciplina(gradeOptativaAdicionar.getModalidadeDisciplina());
			setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
			setCodigoMapaEquivalenciaDisciplinaCursar(0);
			setMensagemID("msg_informe_dados"); // tem que ficar antes, pois o
			// montar da turma pode disparar
			// um excption importante que
			// deve prevalecar
			montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
//			if (getHorarioAlunoTurnoVOs().isEmpty()) {
//				prepararHorarioAulaAluno();
//			} else {
//				getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
//			}
			
			setPaginaDestinoInclusaoDisciplina("");
			if(getListaSelectItemTurmaAdicionar().isEmpty()) {
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaPorEquivalencia(true);					
				montarListaSelectItemMapaEquivalenciaDisciplina();
				selecionarTurmaIncluirRenovacaoOnline(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
			}else {
				selecionarTurmaIncluirRenovacaoOnline(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());					
			}	
			gradeOptativaAdicionar.setDisciplinaJaAdicionada(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		// realizarMontagemQuadroHorarioTurmaDisciplina();

	}
	
	public void adicionarDisciplinaGrupoOptativaVisaoAluno() {
		try {
			this.setPaginaDestinoInclusaoDisciplina("PF('panelGrupoOptativa').hide();");
			GradeCurricularGrupoOptativaDisciplinaVO gradeOptativaAdicionar = (GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("grupoOptativaDisciplinaItens");
			if (getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && getMatriculaVO().getCurso().getConfiguracaoAcademico().getBloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular() && (!getMatriculaPeriodoVO().getLiberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular())) {
				// se entrar aqui é por que o aluno é regular (nao deve nenhuma
				// materia do passado) e o
				// bloqueio para inclusao de disciplina está ativo. Logo, nao
				// pode ser incluida disciplina para
				// este aluno até que o usuário com senha tenha permissão para
				// fazer isto.
				if ((getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && getPermiteIncluirDisciplinaOptativa() && gradeOptativaAdicionar.getGradeCurricularGrupoOptativa().getCodigo().equals(getMatriculaPeriodoVO().getPeriodoLetivo().getGradeCurricularGrupoOptativa().getCodigo())) || (!getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && getPermiteIncluirDisciplinaOptativa())) {
					this.setApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular(false);
				} else {
					this.setApresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular(true);
					this.setPaginaDestinoInclusaoDisciplina("");
					if (!getRenovandoMatriculaDaVisaoDoAluno()) {
						throw new Exception("Não é permitido Incluir uma Disciplina para um Aluno Regular (Aluno que não está devendo nenhuma disciplina dos períodos anteriores). Clique no botão Liberar Inclusão Disciplinas para realizar esta operação.");
					} else {
						throw new Exception("Não é permitido Incluir uma Disciplina para um Aluno Regular (Aluno que não está devendo nenhuma disciplina dos períodos anteriores).");
					}
				}
			}

			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());
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
			montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
			getListaSelectItemTurmaAdicionar().get(0);
			selecionarTurmaIncluirRenovacaoOnline(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
//			if (getHorarioAlunoTurnoVOs().isEmpty()) {
//				prepararHorarioAulaAluno();
//			} else {
//				getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
//			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		// realizarMontagemQuadroHorarioTurmaDisciplina();

	}

	public void prepararAdicionarDisciplinaMatriculaPeriodo() {
		try {
			limparMensagem();
			GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaPendenteItens");
			this.setPaginaDestinoInclusaoDisciplina("PF('panelIncluirDisciplinas').show()");
			if (getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && getMatriculaVO().getCurso().getConfiguracaoAcademico().getBloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular() && (!getMatriculaPeriodoVO().getLiberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular()) && obj.getPeriodoLetivoVO().getPeriodoLetivo() > getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo()) {
				// se entrar aqui é por que o aluno é regular (nao deve nenhuma
				// materia do passado) e o
				// bloqueio para inclusao de disciplina está ativo. Logo, nao
				// pode ser incluida disciplina para
				// este aluno até que o usuário com senha tenha permissão para
				// fazer isto.
				this.setApresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular(true);
				this.setPaginaDestinoInclusaoDisciplina("");
				if (!getRenovandoMatriculaDaVisaoDoAluno()) {
					throw new Exception("Não é permitido Incluir uma Disciplina para um Aluno Regular (Aluno que não está devendo nenhuma disciplina dos períodos anteriores). Clique no botão Liberar Inclusão Disciplinas para realizar esta operação.");
				} else {
					throw new Exception("Não é permitido Incluir uma Disciplina para um Aluno Regular (Aluno que não está devendo nenhuma disciplina dos períodos anteriores).");
				}
			}
			MatriculaPeriodoTurmaDisciplinaVO novoObj = new MatriculaPeriodoTurmaDisciplinaVO();
			novoObj.setDisciplina(obj.getDisciplina());
			novoObj.setGradeDisciplinaVO(obj);
			novoObj.setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
			novoObj.setTurma(new TurmaVO());
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(novoObj);
			setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
			setCodigoMapaEquivalenciaDisciplinaCursar(0);
			setListaSelectItemMapaEquivalenciaDisciplinaIncluir(null);
			setListaSelectItemDisciplinaEquivalenteAdicionar(null);
			montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
//			if (getHorarioAlunoTurnoVOs().isEmpty()) {
//				prepararHorarioAulaAluno();
//			} else {
//				getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
//			}
			// realizarMontagemQuadroHorarioTurmaDisciplina();
			setMensagemID("msg_informe_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void prepararAdicionarDisciplinarRenovacaoOnline() {
		try {
			limparMensagem();
			GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaPendenteItens");
			this.setPaginaDestinoInclusaoDisciplina("PF('panelIncluirDisciplinas').show()");
			if (getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() && getMatriculaVO().getCurso().getConfiguracaoAcademico().getBloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular() && (!getMatriculaPeriodoVO().getLiberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular()) && obj.getPeriodoLetivoVO().getPeriodoLetivo() > getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo()) {
				// se entrar aqui é por que o aluno é regular (nao deve nenhuma
				// materia do passado) e o
				// bloqueio para inclusao de disciplina está ativo. Logo, nao
				// pode ser incluida disciplina para
				// este aluno até que o usuário com senha tenha permissão para
				// fazer isto.
				this.setApresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular(true);
				this.setPaginaDestinoInclusaoDisciplina("");
				if (!getRenovandoMatriculaDaVisaoDoAluno()) {
					throw new Exception("Não é permitido Incluir uma Disciplina para um Aluno Regular (Aluno que não está devendo nenhuma disciplina dos períodos anteriores). Clique no botão Liberar Inclusão Disciplinas para realizar esta operação.");
				} else {
					throw new Exception("Não é permitido Incluir uma Disciplina para um Aluno Regular (Aluno que não está devendo nenhuma disciplina dos períodos anteriores).");
				}
			}
			// Este if abaixo verifica se existe a obrigatoriedade de incluir as disciplinas de DP primeiro que as regulares em comparação com a disciplina selecionado (regular ou adaptação) para inclusão,
			// então é verificado se existe oferta de DP e se existe alguma DP que tem a qtde de horas dentro do saldo de horas disponível,
			// caso não seja possível adicionar é lançado uma exceção  			
			if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia() 
					&& getMatriculaVO().getCurso().getConfiguracaoAcademico().getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH()) {
				List<GradeDisciplinaVO> listaDisciplinasPeriodoLetivoAlunoPendente = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getListaGradeDisciplinaVOsPendentesGradeCurricular(getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo());
				if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR") && obj.getPeriodoLetivoVO().getPeriodoLetivo().equals(getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo()) 
						&& !getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO()
						.getHistoricosDisciplinasAlunoReprovouGradeCurricular().stream().anyMatch(h -> h.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo()))
						&& getMatriculaVO().getCurso().getConfiguracaoAcademico().isHabilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares()
						&& listaDisciplinasPeriodoLetivoAlunoPendente.stream().anyMatch(c -> c.isGradeDisciplinaOfertada() && !c.getJaIncluidaRenovacao() 
								&& c.getNrCreditos() <= getMatriculaPeriodoVO().getSaldoCreditoDisponivelInclusaoDisplinas()
								&& c.getPeriodoLetivoVO().getPeriodoLetivo() < getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo())) {
					setPaginaDestinoInclusaoDisciplina("");
					throw new Exception("Só é possível incluir disciplina regular após incluir as disciplinas de dependência ofertadas.");
				}else if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR") && obj.getPeriodoLetivoVO().getPeriodoLetivo() > getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo() 
						&& listaDisciplinasPeriodoLetivoAlunoPendente.stream().anyMatch(c -> c.isGradeDisciplinaOfertada() && !c.getJaIncluidaRenovacao() 
								&& c.getNrCreditos() <= getMatriculaPeriodoVO().getSaldoCreditoDisponivelInclusaoDisplinas()
								&& c.getPeriodoLetivoVO().getPeriodoLetivo() <= getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo())) {
					setPaginaDestinoInclusaoDisciplina("");
					throw new Exception("Só é possível incluir disciplina de adaptação após incluir as disciplinas de dependência ou regular ofertadas.");
				}else if(!getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR") && obj.getPeriodoLetivoVO().getPeriodoLetivo().equals(getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo()) 
						&& !getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO()
						.getHistoricosDisciplinasAlunoReprovouGradeCurricular().stream().anyMatch(h -> h.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo()))
						&& getMatriculaVO().getCurso().getConfiguracaoAcademico().isHabilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares()
						&& listaDisciplinasPeriodoLetivoAlunoPendente.stream().anyMatch(c -> c.isGradeDisciplinaOfertada() && !c.getJaIncluidaRenovacao() 
								&& c.getCargaHoraria() <= getMatriculaPeriodoVO().getSaldoCHDisponivelInclusaoDisplinas()
								&& c.getPeriodoLetivoVO().getPeriodoLetivo() < getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo())) {
					setPaginaDestinoInclusaoDisciplina("");
					throw new Exception("Só é possível incluir disciplina regular após incluir as disciplinas de dependência ofertadas.");
				}else if(!getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR") && obj.getPeriodoLetivoVO().getPeriodoLetivo() > getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo() 
						&& listaDisciplinasPeriodoLetivoAlunoPendente.stream().anyMatch(c -> c.isGradeDisciplinaOfertada() && !c.getJaIncluidaRenovacao() 
								&& c.getCargaHoraria() <= getMatriculaPeriodoVO().getSaldoCHDisponivelInclusaoDisplinas()
								&& c.getPeriodoLetivoVO().getPeriodoLetivo() <= getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo())) {
					setPaginaDestinoInclusaoDisciplina("");
					throw new Exception("Só é possível incluir disciplina de adaptação após incluir as disciplinas de dependência ou regular ofertadas.");
				}	else if(!getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR") && obj.getPeriodoLetivoVO().getPeriodoLetivo() > getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo()) {
					if((getLiberadoInclusaoOptativa()) && ((getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaDisciplinaOptativaCumprida() +  
							getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> (d.getDisciplinaReferenteAUmGrupoOptativa() || d.getDisciplinaOptativa()) && !d.getHistoricoVO().getSituacao().equals("AP")  && !d.getHistoricoVO().getSituacao().equals("AE") ).mapToInt(d -> d.getCargaHoraria()).sum())
							< getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCargaHorariaOptativaExigida())) {
						
						Integer totalChEletiva = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().stream().filter(
								p -> p.getControleOptativaGrupo() && p.getPeriodoLetivo() <= getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo() && p.getNumeroCargaHorariaOptativa() > 0).mapToInt(p -> p.getNumeroCargaHorariaOptativa()).sum();
						
						if(totalChEletiva > 0 && getMatriculaPeriodoVO().getSaldoCHDisponivelInclusaoDisplinas() > 0 
								&& getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> (d.getDisciplinaReferenteAUmGrupoOptativa() || d.getDisciplinaOptativa()) && !d.getHistoricoVO().getSituacao().equals("AP") && !d.getHistoricoVO().getSituacao().equals("AE")  ).mapToInt(d -> d.getCargaHoraria()).sum()  < getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroCargaHorariaOptativa()) {
							Integer chOfertada = 0;
							for(PeriodoLetivoVO periodoLetivoVO: getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs()) {
								if(periodoLetivoVO.getControleOptativaGrupo() && Uteis.isAtributoPreenchido(periodoLetivoVO.getGradeCurricularGrupoOptativa()) && periodoLetivoVO.getPeriodoLetivo() <= getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo()) {
									chOfertada +=getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().stream().filter(d -> !d.getDisciplinaJaAdicionada() && d.getDisciplinaOfertada() && d.getCargaHoraria() <= getMatriculaPeriodoVO().getSaldoCHDisponivelInclusaoDisplinas()).mapToInt(d -> d.getCargaHoraria()).sum();
								}
							}
							//Integer chOfertada = getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().stream().filter(d -> !d.getDisciplinaJaAdicionada() && d.getDisciplinaOfertada() && d.getCargaHoraria() <= getMatriculaPeriodoVO().getSaldoCHDisponivelInclusaoDisplinas()).mapToInt(d -> d.getCargaHoraria()).sum();
							Integer chAdicionada = getMatriculaPeriodoVO().getChTotalDisciplinaOptativaMatriculaPeriodo();
							Integer chPendente = totalChEletiva > chOfertada ? chOfertada : totalChEletiva - chAdicionada;
							if(chPendente > 0) {	
								setPaginaDestinoInclusaoDisciplina("");
								throw new Exception("Só é possível incluir uma disciplina de adaptação após incluir "+chPendente+" horas de disciplinas eletivas ofertas.");								
							}
						}
					}
				}								
			}
			MatriculaPeriodoTurmaDisciplinaVO novoObj = new MatriculaPeriodoTurmaDisciplinaVO();
			novoObj.setDisciplina(obj.getDisciplina());
			novoObj.setGradeDisciplinaVO(obj);
			novoObj.setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
			novoObj.setTurma(new TurmaVO());
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(novoObj);
			setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
			setCodigoMapaEquivalenciaDisciplinaCursar(0);
			setListaSelectItemMapaEquivalenciaDisciplinaIncluir(null);
			setListaSelectItemDisciplinaEquivalenteAdicionar(null);
			montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
//			if (getHorarioAlunoTurnoVOs().isEmpty()) {
//				prepararHorarioAulaAluno();
//			} else {
//				getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
//			}
			
			setPaginaDestinoInclusaoDisciplina("");
			if(getListaSelectItemTurmaAdicionar().isEmpty()) {
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaPorEquivalencia(true);					
				montarListaSelectItemMapaEquivalenciaDisciplina();
				selecionarTurmaIncluirRenovacaoOnline(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
			}else {
				selecionarTurmaIncluirRenovacaoOnline(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
			}
			setMensagemID("msg_dados_adicionado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void fecharPainelDisciplinaGrupoOptativa() {
	}

	public void prepararDadosHorarioTurmaIncluirDisciplina() {
		TurmaVO turmaSelecionada = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaIncluirList");
		// System.out.println(turmaSelecionada.getIdentificadorTurma());
	}

	/**
	 * @return the codigoMapaEquivalenciaDisciplinaCursar
	 */
	public Integer getCodigoMapaEquivalenciaDisciplinaCursar() {
		if (codigoMapaEquivalenciaDisciplinaCursar == null) {
			codigoMapaEquivalenciaDisciplinaCursar = 0;
		}
		return codigoMapaEquivalenciaDisciplinaCursar;
	}

	/**
	 * @param codigoMapaEquivalenciaDisciplinaCursar
	 *            the codigoMapaEquivalenciaDisciplinaCursar to set
	 */
	public void setCodigoMapaEquivalenciaDisciplinaCursar(Integer codigoMapaEquivalenciaDisciplinaCursar) {
		this.codigoMapaEquivalenciaDisciplinaCursar = codigoMapaEquivalenciaDisciplinaCursar;
	}

	/**
	 * @return the codigoMapaEquivalenciaDisciplinaVOIncluir
	 */
	public Integer getCodigoMapaEquivalenciaDisciplinaVOIncluir() {
		if (codigoMapaEquivalenciaDisciplinaVOIncluir == null) {
			codigoMapaEquivalenciaDisciplinaVOIncluir = 0;
		}
		return codigoMapaEquivalenciaDisciplinaVOIncluir;
	}

	/**
	 * @param codigoMapaEquivalenciaDisciplinaVOIncluir
	 *            the codigoMapaEquivalenciaDisciplinaVOIncluir to set
	 */
	public void setCodigoMapaEquivalenciaDisciplinaVOIncluir(Integer codigoMapaEquivalenciaDisciplinaVOIncluir) {
		this.codigoMapaEquivalenciaDisciplinaVOIncluir = codigoMapaEquivalenciaDisciplinaVOIncluir;
	}

	/**
	 * @return the mapaEquivalenciaDisciplinaVisualizar
	 */
	public MapaEquivalenciaDisciplinaVO getMapaEquivalenciaDisciplinaVisualizar() {
		if (mapaEquivalenciaDisciplinaVisualizar == null) {
			mapaEquivalenciaDisciplinaVisualizar = new MapaEquivalenciaDisciplinaVO();
		}
		return mapaEquivalenciaDisciplinaVisualizar;
	}

	/**
	 * @param mapaEquivalenciaDisciplinaVisualizar
	 *            the mapaEquivalenciaDisciplinaVisualizar to set
	 */
	public void setMapaEquivalenciaDisciplinaVisualizar(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVisualizar) {
		this.mapaEquivalenciaDisciplinaVisualizar = mapaEquivalenciaDisciplinaVisualizar;
	}

	/**
	 * @return the onCompleteIncluirDisciplinaTurma
	 */
	public String getOnCompleteIncluirDisciplinaTurma() {
		if (onCompleteIncluirDisciplinaTurma == null) {
			onCompleteIncluirDisciplinaTurma = "PF('panelIncluirDisciplinas').hide();";
		}
		return onCompleteIncluirDisciplinaTurma;
	}

	/**
	 * @param onCompleteIncluirDisciplinaTurma
	 *            the onCompleteIncluirDisciplinaTurma to set
	 */
	public void setOnCompleteIncluirDisciplinaTurma(String onCompleteIncluirDisciplinaTurma) {
		this.onCompleteIncluirDisciplinaTurma = onCompleteIncluirDisciplinaTurma;
	}

	/**
	 * @return the listaSelectItemPeriodoLetivoInclusaoDisciplina
	 */
	public List getListaSelectItemPeriodoLetivoInclusaoDisciplina() {
		if (listaSelectItemPeriodoLetivoInclusaoDisciplina == null) {
			listaSelectItemPeriodoLetivoInclusaoDisciplina = new ArrayList(0);
		}
		return listaSelectItemPeriodoLetivoInclusaoDisciplina;
	}

	/**
	 * @param listaSelectItemPeriodoLetivoInclusaoDisciplina
	 *            the listaSelectItemPeriodoLetivoInclusaoDisciplina to set
	 */
	public void setListaSelectItemPeriodoLetivoInclusaoDisciplina(List listaSelectItemPeriodoLetivoInclusaoDisciplina) {
		this.listaSelectItemPeriodoLetivoInclusaoDisciplina = listaSelectItemPeriodoLetivoInclusaoDisciplina;
	}

	/**
	 * @return the totalCHCrPendenteAlunoAtePeriodoAnterior
	 */
	public Integer getTotalCHCrPendenteAlunoAtePeriodoAnterior() {
		if (totalCHCrPendenteAlunoAtePeriodoAnterior == 0) {
			totalCHCrPendenteAlunoAtePeriodoAnterior = 0;
		}
		return totalCHCrPendenteAlunoAtePeriodoAnterior;
	}

	/**
	 * @param totalCHCrPendenteAlunoAtePeriodoAnterior
	 *            the totalCHCrPendenteAlunoAtePeriodoAnterior to set
	 */
	public void setTotalCHCrPendenteAlunoAtePeriodoAnterior(Integer totalCHCrPendenteAlunoAtePeriodoAnterior) {
		this.totalCHCrPendenteAlunoAtePeriodoAnterior = totalCHCrPendenteAlunoAtePeriodoAnterior;
	}

	/**
	 * @return the listaDisciplinasPeriodoLetivoAlunoEstaCursando
	 */
	public List<HistoricoVO> getListaDisciplinasPeriodoLetivoAlunoEstaCursando() {
		if (listaDisciplinasPeriodoLetivoAlunoEstaCursando == null) {
			listaDisciplinasPeriodoLetivoAlunoEstaCursando = new ArrayList(0);
		}
		return listaDisciplinasPeriodoLetivoAlunoEstaCursando;
	}

	/**
	 * @param listaDisciplinasPeriodoLetivoAlunoEstaCursando
	 *            the listaDisciplinasPeriodoLetivoAlunoEstaCursando to set
	 */
	public void setListaDisciplinasPeriodoLetivoAlunoEstaCursando(List<HistoricoVO> listaDisciplinasPeriodoLetivoAlunoEstaCursando) {
		this.listaDisciplinasPeriodoLetivoAlunoEstaCursando = listaDisciplinasPeriodoLetivoAlunoEstaCursando;
	}

	/**
	 * @return the totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior
	 */
	public Integer getTotalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior() {
		if (totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior == null) {
			totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior = new Integer(0);
		}
		return totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior;
	}

	/**
	 * @param totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior
	 *            the totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior to set
	 */
	public void setTotalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior(Integer totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior) {
		this.totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior = totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior;
	}

	/**
	 * @return the codigoPeriodoLetivoIncluirDisciplinasGrupoOptativas
	 */
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

	/**
	 * @return the periodoLetivoComHistoricoVOIncluirGrupoOptativas
	 */
	public PeriodoLetivoComHistoricoAlunoVO getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas() {
		if (periodoLetivoComHistoricoVOIncluirGrupoOptativas == null) {
			periodoLetivoComHistoricoVOIncluirGrupoOptativas = new PeriodoLetivoComHistoricoAlunoVO();
		}
		return periodoLetivoComHistoricoVOIncluirGrupoOptativas;
	}

	/**
	 * @param periodoLetivoComHistoricoVOIncluirGrupoOptativas
	 *            the periodoLetivoComHistoricoVOIncluirGrupoOptativas to set
	 */
	public void setPeriodoLetivoComHistoricoVOIncluirGrupoOptativas(PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoVOIncluirGrupoOptativas) {
		this.periodoLetivoComHistoricoVOIncluirGrupoOptativas = periodoLetivoComHistoricoVOIncluirGrupoOptativas;
	}

	/**
	 * @return the listaSelectItemPeriodoLetivoComGrupoOptativas
	 */
	public List getListaSelectItemPeriodoLetivoComGrupoOptativas() {
		if (listaSelectItemPeriodoLetivoComGrupoOptativas == null) {
			listaSelectItemPeriodoLetivoComGrupoOptativas = new ArrayList(0);
		}
		return listaSelectItemPeriodoLetivoComGrupoOptativas;
	}

	/**
	 * @param listaSelectItemPeriodoLetivoComGrupoOptativas
	 *            the listaSelectItemPeriodoLetivoComGrupoOptativas to set
	 */
	public void setListaSelectItemPeriodoLetivoComGrupoOptativas(List listaSelectItemPeriodoLetivoComGrupoOptativas) {
		this.listaSelectItemPeriodoLetivoComGrupoOptativas = listaSelectItemPeriodoLetivoComGrupoOptativas;
	}

	public void fecharPanelHorarioAulaAluno() {
	}

	public void prepararHorarioAulaAluno() {
		try {
//			setHorarioAlunoTurnoVOs(getFacadeFactory().getHorarioAlunoFacade().consultarHorarioAlunoPorMatriculaPeriodoDisciplina(getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs(), getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
//
//	public List<HorarioAlunoTurnoVO> getHorarioAlunoTurnoVOs() {
//		if (horarioAlunoTurnoVOs == null) {
//			horarioAlunoTurnoVOs = new ArrayList<HorarioAlunoTurnoVO>();
//		}
//		return horarioAlunoTurnoVOs;
//	}
//
//	public void setHorarioAlunoTurnoVOs(List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs) {
//		this.horarioAlunoTurnoVOs = horarioAlunoTurnoVOs;
//	}

	public void registrarAlunoConcordaTermosRenovacaoOnline() {
		try {
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "registrarAlunoConcordaTermosRenovacaoOnline"+ getMatriculaVO().getMatricula());
			executarValidacaoSimulacaoVisaoAluno();
			adicionarRegistroFolowUpAlunoAceitouTermoAceiteLancandoExcecao();
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "registrarAlunoConcordaTermosRenovacaoOnline1"+ getMatriculaVO().getMatricula());
			getMatriculaPeriodoVO().setAlunoConcordaComTermoRenovacaoOnline(Boolean.TRUE);
			getMatriculaPeriodoVO().getProcessoMatriculaVO().setMensagemApresentarVisaoAluno("");
			getMatriculaPeriodoVO().getProcessoMatriculaVO().setTermoAceite("");
//			getMatriculaPeriodoVO().setDataAceitouTermoContratoRenovacaoOnline(getInteracaoWorkflowVO().getDataInicio());
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "registrarAlunoConcordaTermosRenovacaoOnline2"+ getMatriculaVO().getMatricula());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void registrarAvisoRenovacaoMatriculaExistente() {
		try {
			setAvisoRenovandoMatriculaExistente(false);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String realizarNavegacaoTelaInicialVisaoAluno() {
		try {
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "telaInicialVisaoAluno.xhtml?faces-redirect=true;";
	}

	/**
	 * @return the apresentarBotaoLiberarMatriculaTodosPeriodos
	 */
	public Boolean getApresentarBotaoLiberarMatriculaTodosPeriodos() {
		if (apresentarBotaoLiberarMatriculaTodosPeriodos == null) {
			apresentarBotaoLiberarMatriculaTodosPeriodos = Boolean.FALSE;
		}
		return apresentarBotaoLiberarMatriculaTodosPeriodos;
	}

	/**
	 * @param apresentarBotaoLiberarMatriculaTodosPeriodos
	 *            the apresentarBotaoLiberarMatriculaTodosPeriodos to set
	 */
	public void setApresentarBotaoLiberarMatriculaTodosPeriodos(Boolean apresentarBotaoLiberarMatriculaTodosPeriodos) {
		this.apresentarBotaoLiberarMatriculaTodosPeriodos = apresentarBotaoLiberarMatriculaTodosPeriodos;
	}

	/**
	 * @return the usernameLiberarMatriculaTodosPeriodos
	 */
	public String getUsernameLiberarMatriculaTodosPeriodos() {
		if (usernameLiberarMatriculaTodosPeriodos == null) {
			usernameLiberarMatriculaTodosPeriodos = "";
		}
		return usernameLiberarMatriculaTodosPeriodos;
	}

	/**
	 * @param usernameLiberarMatriculaTodosPeriodos
	 *            the usernameLiberarMatriculaTodosPeriodos to set
	 */
	public void setUsernameLiberarMatriculaTodosPeriodos(String usernameLiberarMatriculaTodosPeriodos) {
		this.usernameLiberarMatriculaTodosPeriodos = usernameLiberarMatriculaTodosPeriodos;
	}

	/**
	 * @return the senhaLiberarMatriculaTodosPeriodos
	 */
	public String getSenhaLiberarMatriculaTodosPeriodos() {
		if (senhaLiberarMatriculaTodosPeriodos == null) {
			senhaLiberarMatriculaTodosPeriodos = "";
		}
		return senhaLiberarMatriculaTodosPeriodos;
	}

	/**
	 * @param senhaLiberarMatriculaTodosPeriodos
	 *            the senhaLiberarMatriculaTodosPeriodos to set
	 */
	public void setSenhaLiberarMatriculaTodosPeriodos(String senhaLiberarMatriculaTodosPeriodos) {
		this.senhaLiberarMatriculaTodosPeriodos = senhaLiberarMatriculaTodosPeriodos;
	}

	

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaMatriculaPeriodoTurmaDisciplinaFazParteComposicao() {
		if (listaMatriculaPeriodoTurmaDisciplinaFazParteComposicao == null) {
			listaMatriculaPeriodoTurmaDisciplinaFazParteComposicao = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaMatriculaPeriodoTurmaDisciplinaFazParteComposicao;
	}

	public void setListaMatriculaPeriodoTurmaDisciplinaFazParteComposicao(List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaFazParteComposicao) {
		this.listaMatriculaPeriodoTurmaDisciplinaFazParteComposicao = listaMatriculaPeriodoTurmaDisciplinaFazParteComposicao;
	}

	public List<GradeDisciplinaCompostaVO> getGradeDisciplinaCompostaVOs() {
		if (gradeDisciplinaCompostaVOs == null) {
			gradeDisciplinaCompostaVOs = new ArrayList<GradeDisciplinaCompostaVO>(0);
		}
		return gradeDisciplinaCompostaVOs;
	}

	public void setGradeDisciplinaCompostaVOs(List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) {
		this.gradeDisciplinaCompostaVOs = gradeDisciplinaCompostaVOs;
	}

	/**
	 * @return the usernameLiberarRenovacaoDocumentoPendente
	 */
	public String getUsernameLiberarRenovacaoDocumentoPendente() {
		if (usernameLiberarRenovacaoDocumentoPendente == null) {
			usernameLiberarRenovacaoDocumentoPendente = "";
		}
		return usernameLiberarRenovacaoDocumentoPendente;
	}

	/**
	 * @param usernameLiberarRenovacaoDocumentoPendente
	 *            the usernameLiberarRenovacaoDocumentoPendente to set
	 */
	public void setUsernameLiberarRenovacaoDocumentoPendente(String usernameLiberarRenovacaoDocumentoPendente) {
		this.usernameLiberarRenovacaoDocumentoPendente = usernameLiberarRenovacaoDocumentoPendente;
	}

	/**
	 * @return the senhaLiberarRenovacaoDocumentoPendente
	 */
	public String getSenhaLiberarRenovacaoDocumentoPendente() {
		if (senhaLiberarRenovacaoDocumentoPendente == null) {
			senhaLiberarRenovacaoDocumentoPendente = "";
		}
		return senhaLiberarRenovacaoDocumentoPendente;
	}

	/**
	 * @param senhaLiberarRenovacaoDocumentoPendente
	 *            the senhaLiberarRenovacaoDocumentoPendente to set
	 */
	public void setSenhaLiberarRenovacaoDocumentoPendente(String senhaLiberarRenovacaoDocumentoPendente) {
		this.senhaLiberarRenovacaoDocumentoPendente = senhaLiberarRenovacaoDocumentoPendente;
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

	/**
	 * @return the senhaLiberarMatriculaAcimaNrVagas
	 */
	public String getSenhaLiberarMatriculaAcimaNrVagas() {
		if (senhaLiberarMatriculaAcimaNrVagas == null) {
			senhaLiberarMatriculaAcimaNrVagas = "";
		}
		return senhaLiberarMatriculaAcimaNrVagas;
	}

	/**
	 * @param senhaLiberarMatriculaAcimaNrVagas
	 *            the senhaLiberarMatriculaAcimaNrVagas to set
	 */
	public void setSenhaLiberarMatriculaAcimaNrVagas(String senhaLiberarMatriculaAcimaNrVagas) {
		this.senhaLiberarMatriculaAcimaNrVagas = senhaLiberarMatriculaAcimaNrVagas;
	}

	public void prepararLiberarMatriculaAcimaNrMaximoVagas() {
		try {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaItens");
			// getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(),
			// getUsuarioLogado());

			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().atualizarNrAlunosMatriculadosTurmaDisciplina(getMatriculaPeriodoVO(), obj, obj.getDisciplina(), obj.getAno(), obj.getSemestre(), true, false);

			setMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * @return the matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas
	 */
	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas() {
		if (matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas == null) {
			matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas;
	}

	/**
	 * @param matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas
	 *            the matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas to set
	 */
	public void setMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas) {
		this.matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas = matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas;
	}

	/**
	 * @return the liberarTodasDisciplinasComIndisponibilidadeVagas
	 */
	public Boolean getLiberarTodasDisciplinasComIndisponibilidadeVagas() {
		if (liberarTodasDisciplinasComIndisponibilidadeVagas == null) {
			liberarTodasDisciplinasComIndisponibilidadeVagas = Boolean.TRUE;
		}
		return liberarTodasDisciplinasComIndisponibilidadeVagas;
	}

	/**
	 * @param liberarTodasDisciplinasComIndisponibilidadeVagas
	 *            the liberarTodasDisciplinasComIndisponibilidadeVagas to set
	 */
	public void setLiberarTodasDisciplinasComIndisponibilidadeVagas(Boolean liberarTodasDisciplinasComIndisponibilidadeVagas) {
		this.liberarTodasDisciplinasComIndisponibilidadeVagas = liberarTodasDisciplinasComIndisponibilidadeVagas;
	}

	public boolean getExisteDisciplinaComControleVagas() {
		if (this.getMatriculaPeriodoVO().getIsNovaMatriculaPeriodo()) {
			return true;
		}
		boolean exigeNovaDisciplina = this.getMatriculaPeriodoVO().getExisteNovaMatriculaPeriodoTurmaDisciplina();
		return exigeNovaDisciplina;
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

	public List getListaSelectItemSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1°"));
		lista.add(new SelectItem("2", "2°"));
		return lista;
	}

	/**
	 * Método remove convênio e adiciona o mesmo para lista Convenio que
	 * precisam ser processados para remover restituições (CONTA A PAGAR)
	 * relacionadas ao mesmo. Esta lista é processada ao final do método que
	 * grava (ALTERA) a matriculaPeriodo do aluno.
	 */


	public Boolean getAutorizacaoMatriculaAcimaRealizada() {
		if (autorizacaoMatriculaAcimaRealizada == null) {
			autorizacaoMatriculaAcimaRealizada = Boolean.FALSE;
		}
		return autorizacaoMatriculaAcimaRealizada;
	}

	public void setAutorizacaoMatriculaAcimaRealizada(Boolean autorizacaoMatriculaAcimaRealizada) {
		this.autorizacaoMatriculaAcimaRealizada = autorizacaoMatriculaAcimaRealizada;
	}

	public Boolean getApresentarUsuarioSenhaAutorizarMatriculaAcimaRealizada() {
		if (apresentarUsuarioSenhaAutorizarMatriculaAcimaRealizada == null) {
			apresentarUsuarioSenhaAutorizarMatriculaAcimaRealizada = Boolean.FALSE;
		}
		return apresentarUsuarioSenhaAutorizarMatriculaAcimaRealizada;
	}

	public void setApresentarUsuarioSenhaAutorizarMatriculaAcimaRealizada(Boolean apresentarUsuarioSenhaAutorizarMatriculaAcimaRealizada) {
		this.apresentarUsuarioSenhaAutorizarMatriculaAcimaRealizada = apresentarUsuarioSenhaAutorizarMatriculaAcimaRealizada;
	}

	public String getMensagemUsuarioSenhaAutorizarMatriculaAcimaRealizada() {
		if (mensagemUsuarioSenhaAutorizarMatriculaAcimaRealizada == null) {
			mensagemUsuarioSenhaAutorizarMatriculaAcimaRealizada = "";
		}
		return mensagemUsuarioSenhaAutorizarMatriculaAcimaRealizada;
	}

	public void setMensagemUsuarioSenhaAutorizarMatriculaAcimaRealizada(String mensagemUsuarioSenhaAutorizarMatriculaAcimaRealizada) {
		this.mensagemUsuarioSenhaAutorizarMatriculaAcimaRealizada = mensagemUsuarioSenhaAutorizarMatriculaAcimaRealizada;
	}

	public String getUsernameLiberarMatriculaAcimaNrAulas() {
		if (usernameLiberarMatriculaAcimaNrAulas == null) {
			usernameLiberarMatriculaAcimaNrAulas = "";
		}
		return usernameLiberarMatriculaAcimaNrAulas;
	}

	public void setUsernameLiberarMatriculaAcimaNrAulas(String usernameLiberarMatriculaAcimaNrAulas) {
		this.usernameLiberarMatriculaAcimaNrAulas = usernameLiberarMatriculaAcimaNrAulas;
	}

	public String getSenhaLiberarMatriculaAcimaNrAulas() {
		if (senhaLiberarMatriculaAcimaNrAulas == null) {
			senhaLiberarMatriculaAcimaNrAulas = "";
		}
		return senhaLiberarMatriculaAcimaNrAulas;
	}

	public void setSenhaLiberarMatriculaAcimaNrAulas(String senhaLiberarMatriculaAcimaNrAulas) {
		this.senhaLiberarMatriculaAcimaNrAulas = senhaLiberarMatriculaAcimaNrAulas;
	}

	public String getApresentarModalUsuarioSenhaAutorizarMatriculaAcimaRealizada() {
		if (apresentarModalUsuarioSenhaAutorizarMatriculaAcimaRealizada == null) {
			apresentarModalUsuarioSenhaAutorizarMatriculaAcimaRealizada = "";
		}
		return apresentarModalUsuarioSenhaAutorizarMatriculaAcimaRealizada;
	}

	public void setApresentarModalUsuarioSenhaAutorizarMatriculaAcimaRealizada(String apresentarModalUsuarioSenhaAutorizarMatriculaAcimaRealizada) {
		this.apresentarModalUsuarioSenhaAutorizarMatriculaAcimaRealizada = apresentarModalUsuarioSenhaAutorizarMatriculaAcimaRealizada;
	}

	

	/**
	 * @author Victor Hugo de Paula Costa
	 */
	public void realizarInicializacaoDadosNovaMatricula(Integer banner) {
		try {
			setRealizandoNovaMatriculaAluno(true);
			Map<String, Integer> codigoCurso = new LinkedHashMap<String, Integer>();
			if (getFacadeFactory().getProcessoMatriculaFacade().verificarPossibilidadeMatriculaOnline(banner, null, null, null, null, null, codigoCurso, getUsuarioLogado())) {
				setPermitirMatriculaOnline(true);
			} else {
				codigoCurso.put("codigoCurso", getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarCodigoCurso(banner, false, 0, getUsuarioLogado()));
			}
			setMatriculaVO(new MatriculaVO());
			if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
				getMatriculaVO().setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getUsuarioLogado().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} else if (getUsuarioLogado().getIsApresentarVisaoPais()) {
				getMatriculaVO().setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getVisaoAlunoControle().getMatricula().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMatriculaPeriodoVO(new MatriculaPeriodoVO());
//			setPlanoFinanceiroCursoVO(new PlanoFinanceiroCursoVO());
			getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(codigoCurso.get("codigoCurso"), Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
			getDadosRecuperadosAlteracaoProcessoMatricula().put("Curso", getMatriculaVO().getCurso().getCodigo());
			getMatriculaVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			getMatriculaVO().getUnidadeEnsino().setNome(getUnidadeEnsinoLogado().getNome());
			inicializarUsuarioResponsavelMatriculaUsuarioLogado();
			if(getPermitirMatriculaOnline()){
				List<ProcessoMatriculaVO> processoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultarProcessosMatriculasPorCodigoBanner(banner, getMatriculaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
				if(processoMatriculaVOs != null && !processoMatriculaVOs.isEmpty()){
					getMatriculaVO().getUnidadeEnsino().setCodigo(processoMatriculaVOs.get(0).getProcessoMatriculaUnidadeEnsinoVOs().get(0).getUnidadeEnsinoVO().getCodigo());
					getMatriculaVO().getUnidadeEnsino().setNome(processoMatriculaVOs.get(0).getProcessoMatriculaUnidadeEnsinoVOs().get(0).getUnidadeEnsinoVO().getNome());
				}
			}
			iniciarNovaMatriculaAluno();
			executarDefinicaoPeriodoLetivoNovaMatriculaAluno();
			realizarValidacaoPermiteMatriculaOnlineAposAlteracaoDados();
			setMensagemID("msg_MatriculaOnline_bemVindo", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getPermitirMatriculaOnline() {
		if (permitirMatriculaOnline == null) {
			permitirMatriculaOnline = true;
		}
		return permitirMatriculaOnline;
	}

	public void setPermitirMatriculaOnline(Boolean permitirMatriculaOnline) {
		this.permitirMatriculaOnline = permitirMatriculaOnline;
	}

	public Integer getBanner() {
		if (banner == null) {
			banner = 0;
		}
		return banner;
	}

	public void setBanner(Integer banner) {
		this.banner = banner;
	}


	public Boolean getIsApresentarListaSelectItemUnidadeEnsino() {
		return getListaSelectItemUnidadeEnsino().size() > 1;
	}

	public Boolean getIsApresentarListaSelectItemProcessoMatricula() {
		return getListaSelectItemProcessoMatricula().size() > 1;
	}

	public Boolean getIsApresentarListaSelectItemGradeCurricular() {
		return getListaSelectItemGradeCurricular().size() > 1;
	}

	public Boolean getIsApresentarListaSelectItemTurma() {
		return getListaSelectItemTurma().size() > 1;
	}

	public Boolean getIsApresentarListaSelectItemTurno() {
		return getListaSelectItemTurno().size() > 1;
	}

	public Boolean getIsApresentarListaSelectItemCondicoesPagamento() {
		return getListaSelectItemPlanoFinanceiroCurso().size() > 1 && !getMatriculaPeriodoVO().getFinanceiroManual();
	}

	public Boolean getTermoAceiteContrato() {
		if (termoAceiteContrato == null) {
			termoAceiteContrato = false;
		}
		return termoAceiteContrato;
	}

	public void setTermoAceiteContrato(Boolean termoAceiteContrato) {
		this.termoAceiteContrato = termoAceiteContrato;
	}

	private String modalPagamentoOnline;
	private String modalAssinarDocumento;

	public String getModalPagamentoOnline() {
		if (modalPagamentoOnline == null) {
			modalPagamentoOnline = "";
		}
		return modalPagamentoOnline;
	}

	public void setModalPagamentoOnline(String modalPagamentoOnline) {
		this.modalPagamentoOnline = modalPagamentoOnline;
	}

	public String getModalAssinarDocumento() {
		if (modalAssinarDocumento == null) {
			modalAssinarDocumento = "";
		}
		return modalAssinarDocumento;
	}

	public void setModalAssinarDocumento(String modalAssinarDocumento) {
		this.modalAssinarDocumento = modalAssinarDocumento;
	}

	

	


	

	

	private Boolean matriculaConfirmada;
	
	public Boolean getMatriculaConfirmada() {
		if (matriculaConfirmada == null) {
			matriculaConfirmada = false;
		}
		return matriculaConfirmada;
	}


	
	public void executarVerificacaoUsuarioPossuiPermissaoLiberacaoUsuarioDesconsiderarDiferencaValorRateio() {
		try {
			setUsernameLiberarOperacaoFuncionalidade("");
			setSenhaLiberarOperacaoFuncionalidade("");
			setOncompleteOperacaoFuncionalidade("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	
	public String novoNumeroMatriculaManualmente;
	public boolean permitirInformaNumeroMatriculaManualmente = false;
	
	public boolean isPermitirInformaNumeroMatriculaManualmente() {
		return permitirInformaNumeroMatriculaManualmente;
	}

	public void setPermitirInformaNumeroMatriculaManualmente(boolean permitirInformaNumeroMatriculaManualmente) {
		this.permitirInformaNumeroMatriculaManualmente = permitirInformaNumeroMatriculaManualmente;
	}
	
	public String getNovoNumeroMatriculaManualmente() {
		if(novoNumeroMatriculaManualmente == null) {
			novoNumeroMatriculaManualmente ="";
		}
		return novoNumeroMatriculaManualmente;
	}

	public void setNovoNumeroMatriculaManualmente(String novoNumeroMatriculaManualmente) {
		this.novoNumeroMatriculaManualmente = novoNumeroMatriculaManualmente;
	}

	public void executarVerificacaoPermitirInformarNumeroMatriculaManualmente() {
		try {
			UsuarioVO usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(getUsernameLiberarOperacaoFuncionalidade(), getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_PERMITIR_INFORMAR_NUMERO_MATRICULA_MANUALMENTE, usuarioVerif);
			OperacaoFuncionalidadeVO operacao = getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_PERMITIR_INFORMAR_NUMERO_MATRICULA_MANUALMENTE, usuarioVerif, "");
			if(Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula())) {
				getFacadeFactory().getMatriculaFacade().executarAlteracaoNumeroMatriculaManualmente(getMatriculaVO(), getNovoNumeroMatriculaManualmente(), operacao, getUsuarioLogadoClone());
			}else {
				throw new Exception("A matrícula não está gravada para que seja feita alteração nela.");
			}
			setNovoNumeroMatriculaManualmente("");
			setOncompleteOperacaoFuncionalidade("PF('panelPermitirInformaNumeroMatriculaManualmente').hide();");
		} catch (Exception e) {
			setOncompleteOperacaoFuncionalidade("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void limparCamposLiberacaoPermitirInformarNumeroMatriculaManualmente() {
		try {
			setUsernameLiberarOperacaoFuncionalidade("");
			setSenhaLiberarOperacaoFuncionalidade("");
			setOncompleteOperacaoFuncionalidade("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void validarUnicidadeMatriculaManualmente() {
		try {
			Uteis.checkState(getFacadeFactory().getMatriculaFacade().validarUnicidade(getMatriculaVO().getMatricula()), "Já existe um aluno cadastrado para a matrícula informada!");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void validarPermissaoInformarNumeroMatriculaManualmente() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_PERMITIR_INFORMAR_NUMERO_MATRICULA_MANUALMENTE, getUsuarioLogadoClone());
			setPermitirInformaNumeroMatriculaManualmente(true);
		} catch (Exception e) {
			setPermitirInformaNumeroMatriculaManualmente(false);
		}		
	}
	
	public boolean isLiberarAlteracaoCampoMatricula() {
		return isPermitirInformaNumeroMatriculaManualmente() && getMatriculaVO().isNovoObj() ;
				
	}
	

	public RenovarMatriculaControle(Boolean matriculaOnlineExterna) {
		setRealizandoNovaMatriculaAluno(matriculaOnlineExterna);
	}

	public Boolean getRealizouTransferenciaMatrizCurricular() {
		if (realizouTransferenciaMatrizCurricular == null) {
			realizouTransferenciaMatrizCurricular = false;
		}
		return realizouTransferenciaMatrizCurricular;
	}

	public void setRealizouTransferenciaMatrizCurricular(Boolean realizouTransferenciaMatrizCurricular) {
		this.realizouTransferenciaMatrizCurricular = realizouTransferenciaMatrizCurricular;
	}

	public boolean isPermitirUsuarioDesconsiderarDiferencaValorRateio() {
		return permitirUsuarioDesconsiderarDiferencaValorRateio;
	}

	public void setPermitirUsuarioDesconsiderarDiferencaValorRateio(boolean permitirUsuarioDesconsiderarDiferencaValorRateio) {
		this.permitirUsuarioDesconsiderarDiferencaValorRateio = permitirUsuarioDesconsiderarDiferencaValorRateio;
	}

	public Boolean getAbrirPainelParcelaExtra() {
		if (abrirPainelParcelaExtra == null) {
			abrirPainelParcelaExtra = false;
		}
		return abrirPainelParcelaExtra;
	}

	public void setAbrirPainelParcelaExtra(Boolean abrirPainelParcelaExtra) {
		this.abrirPainelParcelaExtra = abrirPainelParcelaExtra;
	}	

	public Double getValorTotalParcelasInclusao() {
		if (valorTotalParcelasInclusao == null) {
			valorTotalParcelasInclusao = 0.0;
		}
		return valorTotalParcelasInclusao;
	}

	public void setValorTotalParcelasInclusao(Double valorTotalParcelasInclusao) {
		this.valorTotalParcelasInclusao = valorTotalParcelasInclusao;
	}

	public String getUsernameLiberarDesativacaoFinanceiroManual() {
		if (usernameLiberarDesativacaoFinanceiroManual == null) {
			usernameLiberarDesativacaoFinanceiroManual = "";
		}
		return usernameLiberarDesativacaoFinanceiroManual;
	}

	public void setUsernameLiberarDesativacaoFinanceiroManual(String usernameLiberarDesativacaoFinanceiroManual) {
		this.usernameLiberarDesativacaoFinanceiroManual = usernameLiberarDesativacaoFinanceiroManual;
	}

	public String getSenhaLiberarDesativacaoFinanceiroManual() {
		if (senhaLiberarDesativacaoFinanceiroManual == null) {
			senhaLiberarDesativacaoFinanceiroManual = "";
		}
		return senhaLiberarDesativacaoFinanceiroManual;
	}

	public void setSenhaLiberarDesativacaoFinanceiroManual(String senhaLiberarDesativacaoFinanceiroManual) {
		this.senhaLiberarDesativacaoFinanceiroManual = senhaLiberarDesativacaoFinanceiroManual;
	}

	public void executarVerificacaoUsuarioPodeLiberarDesativacaoFinanceiroManual() {
		try {
			UsuarioVO usuarioVerificar = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(getUsernameLiberarDesativacaoFinanceiroManual(), getSenhaLiberarDesativacaoFinanceiroManual(), true, Uteis.NIVELMONTARDADOS_TODOS);
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirDesativarFinanceiroManual", usuarioVerificar);
			getMatriculaPeriodoVO().setFinanceiroManual(false);
			getFacadeFactory().getMatriculaPeriodoFacade().alterarMatriculaPeriodoFinanceiroManual(getMatriculaPeriodoVO(), getUsuarioLogado().getCodigo(), getMatriculaPeriodoVO().getFinanceiroManual());
			getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA_PERIODO, getMatriculaPeriodoVO().getCodigo().toString(), OperacaoFuncionalidadeEnum.DESATIVAR_FINANCEIRO_MANUAL, getUsuarioLogado(), ""));
			setApresentarPanelDesativarFinanceiroManual(false);
			setMensagemID("msg_ConfirmacaoLiberacaoRenovarMatriculaComDocumentosPendentes");
		} catch (Exception e) {
			getMatriculaPeriodoVO().setFinanceiroManual(true);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			setUsernameLiberarDesativacaoFinanceiroManual("");
			setSenhaLiberarDesativacaoFinanceiroManual("");
		}
	}

	public void executarVerificacaoUsuarioLogadoPodeLiberarDesativacaoFinanceiroManual() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirDesativarFinanceiroManual", getUsuarioLogado());
			getMatriculaPeriodoVO().setFinanceiroManual(false);
			getFacadeFactory().getMatriculaPeriodoFacade().alterarMatriculaPeriodoFinanceiroManual(getMatriculaPeriodoVO(), getUsuarioLogado().getCodigo(), getMatriculaPeriodoVO().getFinanceiroManual());
			getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA_PERIODO, getMatriculaPeriodoVO().getCodigo().toString(), OperacaoFuncionalidadeEnum.DESATIVAR_FINANCEIRO_MANUAL, getUsuarioLogado(), ""));
			setApresentarPanelDesativarFinanceiroManual(false);
		} catch (Exception e) {
			setApresentarPanelDesativarFinanceiroManual(true);
			getMatriculaPeriodoVO().setFinanceiroManual(true);
			setMensagemID("msg_entre_prmlogin");
		}
	}

	public Boolean getApresentarPanelDesativarFinanceiroManual() {
		if (apresentarPanelDesativarFinanceiroManual == null) {
			apresentarPanelDesativarFinanceiroManual = false;
		}
		return apresentarPanelDesativarFinanceiroManual;
	}

	public void setApresentarPanelDesativarFinanceiroManual(Boolean apresentarPanelDesativarFinanceiroManual) {
		this.apresentarPanelDesativarFinanceiroManual = apresentarPanelDesativarFinanceiroManual;
	}


	public String getUserNameLiberarDescontoAluno() {
		if (userNameLiberarDescontoAluno == null) {
			userNameLiberarDescontoAluno = "";
		}
		return userNameLiberarDescontoAluno;
	}

	public void setUserNameLiberarDescontoAluno(String userNameLiberarDescontoAluno) {
		this.userNameLiberarDescontoAluno = userNameLiberarDescontoAluno;
	}

	public String getSenhaLiberarDescontoAluno() {
		if (senhaLiberarDescontoAluno == null) {
			senhaLiberarDescontoAluno = "";
		}
		return senhaLiberarDescontoAluno;
	}

	public void setSenhaLiberarDescontoAluno(String senhaLiberarDescontoAluno) {
		this.senhaLiberarDescontoAluno = senhaLiberarDescontoAluno;
	}

	public Boolean getApresentarPanelDigitarSenhaLiberacaoDescontoAluno() {
		if (apresentarPanelDigitarSenhaLiberacaoDescontoAluno == null) {
			apresentarPanelDigitarSenhaLiberacaoDescontoAluno = Boolean.FALSE;
		}
		return apresentarPanelDigitarSenhaLiberacaoDescontoAluno;
	}

	public void setApresentarPanelDigitarSenhaLiberacaoDescontoAluno(Boolean apresentarPanelDigitarSenhaLiberacaoDescontoAluno) {
		this.apresentarPanelDigitarSenhaLiberacaoDescontoAluno = apresentarPanelDigitarSenhaLiberacaoDescontoAluno;
	}

	public void limparUserNameSenhaLiberacaoDesconto() {
		setUserNameLiberarDescontoAluno("");
		setSenhaLiberarDescontoAluno("");
	}

	public Boolean getApresentarBotaoLiberarDescontoAluno() {
		return matriculaBloqueiaDescontoParcelaMatricula;

	}

	public String getStyleCampoValorDescontoAluno() {
		if (matriculaBloqueiaDescontoParcelaMatricula) {
			return "campoSomenteLeitura";
		}
		return "campos";
	}

	public String getAbrirModalDescontoAluno() {
		if (getUsuarioPossuiPermissaoParaDigitarDescontoAluno()) {
			return "PF('panelDescontoAluno').show()";
		}
		return "";
	}

	public Boolean getUsuarioPossuiPermissaoParaDigitarDescontoAluno() {
		if (usuarioPossuiPermissaoParaDigitarDescontoAluno == null) {
			usuarioPossuiPermissaoParaDigitarDescontoAluno = Boolean.FALSE;
		}
		return usuarioPossuiPermissaoParaDigitarDescontoAluno;
	}

	public void setUsuarioPossuiPermissaoParaDigitarDescontoAluno(Boolean usuarioPossuiPermissaoParaDigitarDescontoAluno) {
		this.usuarioPossuiPermissaoParaDigitarDescontoAluno = usuarioPossuiPermissaoParaDigitarDescontoAluno;
	}


	public Double getPercDescontoMatricula() {
		if (percDescontoMatricula == null) {
			percDescontoMatricula = 0.0;
		}
		return percDescontoMatricula;
	}

	public void setPercDescontoMatricula(Double percDescontoMatricula) {
		this.percDescontoMatricula = percDescontoMatricula;
	}

	public Double getValorDescontoMatricula() {
		if (valorDescontoMatricula == null) {
			valorDescontoMatricula = 0.0;
		}
		return valorDescontoMatricula;
	}

	public void setValorDescontoMatricula(Double valorDescontoMatricula) {
		this.valorDescontoMatricula = valorDescontoMatricula;
	}

	public Double getPercDescontoParcela() {
		if (percDescontoParcela == null) {
			percDescontoParcela = 0.0;
		}
		return percDescontoParcela;
	}

	public void setPercDescontoParcela(Double percDescontoParcela) {
		this.percDescontoParcela = percDescontoParcela;
	}

	public Double getValorDescontoParcela() {
		if (valorDescontoParcela == null) {
			valorDescontoParcela = 0.0;
		}
		return valorDescontoParcela;
	}

	public void setValorDescontoParcela(Double valorDescontoParcela) {
		this.valorDescontoParcela = valorDescontoParcela;
	}

	public String getTipoDescontoMatricula() {
		if (tipoDescontoMatricula == null) {
			tipoDescontoMatricula = "";
		}
		return tipoDescontoMatricula;
	}

	public void setTipoDescontoMatricula(String tipoDescontoMatricula) {
		this.tipoDescontoMatricula = tipoDescontoMatricula;
	}

	public String getTipoDescontoParcela() {
		if (tipoDescontoParcela == null) {
			tipoDescontoParcela = "";
		}
		return tipoDescontoParcela;
	}

	public void setTipoDescontoParcela(String tipoDescontoParcela) {
		this.tipoDescontoParcela = tipoDescontoParcela;
	}

	public void alterarTipoDescontoAlunoMatriculaModal() {
		try {
			if (getTipoDescontoMatricula().equals("PO")) {
				setPercDescontoMatricula(getValorDescontoMatricula());
				setValorDescontoMatricula(0.0);
			} else if (getTipoDescontoMatricula().equals("VA")) {
				setValorDescontoMatricula(getPercDescontoMatricula());
				setPercDescontoMatricula(0.0);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarTipoDescontoAlunoParcelaModal() {
		try {
			if (getTipoDescontoParcela().equals("PO")) {
				setPercDescontoParcela(getValorDescontoParcela());
				setValorDescontoParcela(0.0);
			} else if (getTipoDescontoParcela().equals("VA")) {
				setValorDescontoParcela(getPercDescontoParcela());
				setPercDescontoParcela(0.0);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public  void verificarPermissaoUsuarioAlterarTurnoRenovacao(UsuarioVO usuario, String nomeEntidade) throws Exception {
		getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public void verificarUsuarioPodeAlterarTurnoRenovacao() {
		Boolean liberar = false;
		try {
			verificarPermissaoUsuarioAlterarTurnoRenovacao(getUsuarioLogado(), "PermitirAlterarTurnoRenovacao");
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setPermitirAlterarTurnoRenovacao(liberar);
	}

	public Boolean getPermitirAlterarTurnoRenovacao() {
		if (permitirAlterarTurnoRenovacao == null) {
			permitirAlterarTurnoRenovacao = Boolean.FALSE;
		}
		return permitirAlterarTurnoRenovacao;
	}

	public void setPermitirAlterarTurnoRenovacao(Boolean permitirAlterarTurnoRenovacao) {
		this.permitirAlterarTurnoRenovacao = permitirAlterarTurnoRenovacao;
	}

	public Boolean getApresentarBotaoLiberarControleInclusaoDisciplinaPeriodoFuturo() {
		if (apresentarBotaoLiberarControleInclusaoDisciplinaPeriodoFuturo == null) {
			apresentarBotaoLiberarControleInclusaoDisciplinaPeriodoFuturo = Boolean.FALSE;
		}
		return apresentarBotaoLiberarControleInclusaoDisciplinaPeriodoFuturo;
	}

	public void setApresentarBotaoLiberarControleInclusaoDisciplinaPeriodoFuturo(Boolean valor) {
		this.apresentarBotaoLiberarControleInclusaoDisciplinaPeriodoFuturo = valor;
	}

	public Boolean getApresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular() {
		if (apresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular == null) {
			apresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular = Boolean.FALSE;
		}
		return apresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular;
	}

	public void setApresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular(Boolean valor) {
		this.apresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular = valor;
	}

	public Boolean getApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular() {
		if (apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular == null) {
			apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular = Boolean.FALSE;
		}
		return apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular;
	}

	public void setApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular(Boolean valor) {
		this.apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular = valor;
	}

	public Boolean getApresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia() {
		if (apresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia == null) {
			apresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia = Boolean.FALSE;
		}
		return apresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia;
	}

	public void setApresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia(Boolean valor) {
		this.apresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia = valor;
	}

	public String getTipoOperacaoFuncionalidadeLiberar() {
		if (tipoOperacaoFuncionalidadeLiberar == null) {
			tipoOperacaoFuncionalidadeLiberar = "";
		}
		return tipoOperacaoFuncionalidadeLiberar;
	}

	public void setTipoOperacaoFuncionalidadeLiberar(String valor) {
		this.tipoOperacaoFuncionalidadeLiberar = valor;
	}

	public String getUsernameLiberarOperacaoFuncionalidade() {
		if (usernameLiberarOperacaoFuncionalidade == null) {
			usernameLiberarOperacaoFuncionalidade = "";
		}
		return usernameLiberarOperacaoFuncionalidade;
	}

	public void setUsernameLiberarOperacaoFuncionalidade(String valor) {
		this.usernameLiberarOperacaoFuncionalidade = valor;
	}

	public String getSenhaLiberarOperacaoFuncionalidade() {
		if (senhaLiberarOperacaoFuncionalidade == null) {
			senhaLiberarOperacaoFuncionalidade = "";
		}
		return senhaLiberarOperacaoFuncionalidade;
	}

	public void setSenhaLiberarOperacaoFuncionalidade(String valor) {
		this.senhaLiberarOperacaoFuncionalidade = valor;
	}

	public String getPaginaDestinoInclusaoDisciplina() {
		if (paginaDestinoInclusaoDisciplina == null) {
			paginaDestinoInclusaoDisciplina = "";
		}
		return paginaDestinoInclusaoDisciplina;
	}

	public void setPaginaDestinoInclusaoDisciplina(String valor) {
		this.paginaDestinoInclusaoDisciplina = valor;
	}

	public void prepararLiberarControleInclusaoDisciplinaPeriodoFuturo() {
		setTipoOperacaoFuncionalidadeLiberar("Liberar Controle Inclusão Disciplinas Períodos Futuros");
	}

	public void prepararLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular() {
		setTipoOperacaoFuncionalidadeLiberar("Liberar Inclusão Disciplinas Aluno Regular");
	}

	public void prepararLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular() {
		setTipoOperacaoFuncionalidadeLiberar("Liberar Exclusão Disciplinas Aluno Regular");
	}
	
	public void prepararLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular() {
		setTipoOperacaoFuncionalidadeLiberar("Liberar Exclusão Disciplinas Aluno Irregular");
	}

	public void prepararLiberarControleInclusaoObrigatoriaDisciplinaDependencia() {
		setTipoOperacaoFuncionalidadeLiberar("Liberar Controle Inclusão Disciplinas Obrigatórias de Dependência");
	}
	
	public void prepararLiberarAlteracaoCategoriaCondicaoDePagamentoDoPlanoFinanceiroCurso() {
		setTipoOperacaoFuncionalidadeLiberar("per_MatriculaLiberarAlteracaoCategoriaPlanoFinanceiro");
	}

	public void prepararLiberarValidacaoDadosEnadeCenso() {
		setTipoOperacaoFuncionalidadeLiberar("Liberar Validação Dados do Enade e Censo");
	}
	
	public String getApresentarModalInscricao() {
		if (apresentarModalInscricao == null) {
			apresentarModalInscricao = "";
		}
		return apresentarModalInscricao;
	}
	
	public String navegarAbaDocumentacao() {
		try {
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "navegarAbaDocumentacao" +getMatriculaVO().getMatricula());
			setOncompleteModal("");
			Uteis.checkState(isLiberarAlteracaoCampoMatricula() && getFacadeFactory().getMatriculaFacade().validarUnicidade(getMatriculaVO().getMatricula()), "Já existe um aluno cadastrado com esse número de matricula.");
			realizarValidacaoDadosAlunoEnadeCenso();
			verificarAlunoAceitouTermoAceiteRenovacaoMatricula();
			validarPermissaoRenovarMatriculaDoAlunoQuandoPossuirBloqueioBiblioteca();
//			if (!getMatriculaVO().getMatriculaJaRegistrada() 
//					&& (!Uteis.isAtributoPreenchido(getMatriculaVO().getTransferenciaEntrada())
//							|| (Uteis.isAtributoPreenchido(getMatriculaVO().getTransferenciaEntrada())  
//									&& !getMatriculaVO().getTransferenciaEntrada().getTipoTransferenciaEntrada().equals("IN")))) {
//				getFacadeFactory().getMatriculaFacade().verificaAlunoJaMatriculado(getMatriculaVO(), false, null, getUsuarioLogado(), false, false);
//			}
			if(getAlunoPossuiPendenciaEnadeCenso()) {
				setOncompleteModal("PF('panelAvisoVerificarDadosEnadeCenso').show()");
				return "";					
			}
			if (getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
				Boolean aptoMatricula = getFacadeFactory().getMatriculaFacade().validarMatriculaAlunoPosGraduacao(getMatriculaVO().getAluno().getCodigo(), getMatriculaVO().getUnidadeEnsino().getCodigo());
				if (!aptoMatricula) {
					throw new Exception("Não é possível realizar a MATRÍCULA deste aluno em um curso de Pós-Graduação pois o mesmo não possui uma formação acadêmica em Graduação");
				}
			}
			if (!Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getProcessoMatricula())) {
				throw new Exception("O Campo (Processo Matrícula) Deve Ser Informado!");
			}
			validarExistenciaMatriculaPeriodoAtivaPreMatricula();
			if (getMatriculaVO().getMatricula().equals("")) {
				inicializarMatriculaComHistoricoAluno(false);
			}
//			setApresentarModalInscricao("");
			// verificarPermissao obrigar forma ingresso
			if (getMatriculaVO().getCurso().getConfiguracaoAcademico().getObrigaInformarFormaIngressoMatricula().booleanValue() && !getNomeTelaAtual().endsWith("renovacaoMatriculaAluno.xhtml")) {
				if (getMatriculaVO().getFormaIngresso().equals("")) {
					throw new Exception("Deve ser informado a forma de ingresso da matrícula!");
				}
			}
//			if (Uteis.isAtributoPreenchido(matriculaVO.getCodigoFinanceiroMatricula()) && getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()).getUtilizarIntegracaoFinanceira()) {
//				matriculaVO.setCodigoFinanceiroMatricula(Uteis.removerZeroEsquerda(matriculaVO.getCodigoFinanceiroMatricula()));
//			}

			// verificar permissao obriga informar origem forma ingresso.
			if (getMatriculaVO().getCurso().getConfiguracaoAcademico().getObrigaInformarOrigemFormaIngressoMatricula().booleanValue() && !getNomeTelaAtual().endsWith("renovacaoMatriculaAluno.xhtml")) {
				
				if (getMatriculaVO().getFormaIngresso().equals("TE") && !Uteis.isAtributoPreenchido(getMatriculaVO().getTransferenciaEntrada())) {
					throw new Exception("Ao utilizar essa forma de ingresso ( TRANSFERÊNCIA EXTERNA ), é obrigatório o registro da transferência externa. Realize o procedimento no menu ( ACADÊMICO=>TRANSFERÊNCIAS/PROTOCOLO=>TRANSFERÊNCIA EXTERNA ) e após gravar o registro, clique em iniciar matrícula  e dê continuidade ao processo!");
				}
			}
			// getFacadeFactory().getMatriculaPeriodoFacade().validarJaExisteMatriculaPeriodo(matriculaVO,
			// matriculaPeriodoVO);
			getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().setCodigo(this.getUsuarioLogado().getCodigo());
			getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().setNome(this.getUsuarioLogado().getNome());
			getFacadeFactory().getMatriculaPeriodoFacade().validarMatriculaPeriodoPodeSerRealizada(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
			if (!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getTurma().getCodigo())) {
				throw new Exception("O Campo (Turma) Deve Ser Informado !");
			}
			Uteis.checkState(isObrigatorioContratoPorTurma() && !Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getContratoMatricula()), "O campo Modelo de Contrato deve ser informado! Deve ser definido na turma o CONTRATO DE MATRÍCULA.");
			
			gerarDadosGraficoEvolucaoAcademicaAluno();
			setApresentarModalAvisoAlunoReprovado(Boolean.FALSE);
			setMensagemModalAvisoAlunoReprovado("");
			if (getMatriculaPeriodoVO().getNovoObj()) {
				getFacadeFactory().getMatriculaPeriodoFacade().validarDadosRenovacaoProcessoMatriculaPeriodoLetivo(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				getFacadeFactory().getDocumetacaoMatriculaFacade().verificarDocumentosObrigatoriosACadaRenovacao(getMatriculaVO() , getMatriculaPeriodoVO().getAno() , getMatriculaPeriodoVO().getSemestre());
			}
			
			if(!getRenovandoMatricula()) {
				getFacadeFactory().getMatriculaPeriodoFacade().realizarDefinicaoContratoPadraoSerUsadoMatriculaPeriodo(TipoContratoMatriculaEnum.getEnumPorValor(getMatriculaVO().getTipoMatricula()), getMatriculaPeriodoVO(),  !isObrigatorioContratoPorTurma(), false, getUsuarioLogado());
			}
			
			
			
//			if (!getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()).getRealizarMatriculaComFinanceiroManualAtivo() && !getMatriculaPeriodoVO().getFinanceiroManual() && !getRenovandoMatricula()) {				
//				getFacadeFactory().getMatriculaFacade().inicializarTextoContratoPlanoFinanceiroAluno(this.getMatriculaVO(), this.getMatriculaPeriodoVO(), !isObrigatorioContratoPorTurma(), getUsuarioLogado());
//			}else if(getRealizandoNovaMatriculaAluno()) {
//				getMatriculaPeriodoVO().setContratoExtensao(null);
//				getMatriculaPeriodoVO().setContratoMatricula(null);
//			}
			if (!getMatriculaVO().getSituacao().equals("PL")) {
				getMatriculaVO().setUsuario(getUsuarioLogadoClone());
			}
			getFacadeFactory().getMatriculaFacade().gerenciarEntregaDocumentoMatricula(getMatriculaVO(), getUsuarioLogado());

			setProcessoCalendarioMatriculaVO(getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO());
//			if(!getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getRegerarFinanceiro() && getPermitirRegerarFinanceiro()) {
//				getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setRegerarFinanceiro(getPermitirRegerarFinanceiro());
//			}
			atualizarDataMatriculaComBaseDataMatriculaPeriodo();
//			if (getMatriculaPeriodoVO().getCodigo().equals(0) && !Uteis.isAtributoPreenchido(getBanner()) && !this.getLiberarRenovacaoComDebitosFinanceiros()) {
				// somente para novos, em caso de alterar nao temos que fazer
				// esta verificação
//				setMatricula_Erro("");
//				setListaContaReceber(getFacadeFactory().getContaReceberFacade().consultaRapidaPorAlunoEMatriculaContasReceberVencidas(getMatriculaVO().getAluno().getCodigo(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()).getUtilizarIntegracaoFinanceira(), getMatriculaPeriodoVO(), getUsuarioLogado()));
//				setMatricula_Erro(getFacadeFactory().getContaReceberFacade().validarDadosExisteContaReceberRenegociadadaQueNaoCumpriuAcordo(getMatriculaVO().getMatricula(), getMatriculaVO().getAluno().getNome()));
//				if ((getListaContaReceber().isEmpty() && !Uteis.isAtributoPreenchido(getMatricula_Erro())) || (this.getLiberarRenovacaoComDebitosFinanceiros())) {
//					setGuiaAba("Documentacao");
//				} else {
//					if (getMatriculaVO().getDataLiberacaoPendenciaFinanceira() != null && getMatriculaVO().getDataLiberacaoPendenciaFinanceira().after(Uteis.obterDataAntiga(new Date(), 2))) {
//							this.liberarRenovacaoComDebitosFinanceiros = true;
//							setGuiaAba("Documentacao");
//					} else if (getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaPendenciaFinanceira()) {
//						setGuiaAba("PendenciaFinanceira");
//					} else {
//						throw new Exception("O usuário não possui permissão para visualizar para aba de pendência financeira, impossibilitando a navegação para a próxima aba.");
//					}
//				}
//			} else {
				setGuiaAba("Documentacao");
//			}
			if (getPortadorDiploma()) {
				getMatriculaVO().setFormaIngresso("PD");
			}
			if (!getListaSelectItemFormacaoAcademicaAluno().isEmpty() && getListaSelectItemFormacaoAcademicaAluno().size() == 2) {
				Integer valor = (Integer) ((SelectItem) getListaSelectItemFormacaoAcademicaAluno().get(1)).getValue();
				getMatriculaVO().getFormacaoAcademica().setCodigo(valor);
			}
			if (!getListaSelectItemReconhecimentoCurso().isEmpty() && getListaSelectItemReconhecimentoCurso().size() == 2) {
				Integer valor = (Integer) ((SelectItem) getListaSelectItemReconhecimentoCurso().get(1)).getValue();
				getMatriculaVO().getAutorizacaoCurso().setCodigo(valor);
			}
			LoginControle login = (LoginControle) getControlador("LoginControle");
			if (!login.getPermissaoAcessoMenuVO().getNavegarAbaDocumentacao().booleanValue() || getMatriculaVO().getDocumetacaoMatriculaVOs().isEmpty()) {
				navegarAbaDisciplinas();
			}
			if (getMatriculaPeriodoVO().getMensagemAvisoUsuario() != null && !getMatriculaPeriodoVO().getMensagemAvisoUsuario().getListaMensagemErro().isEmpty()) {
				setConsistirExceptionMensagemDetalhada("msg_aviso", getMatriculaPeriodoVO().getMensagemAvisoUsuario(), Uteis.ALERTA);
			} else {
				setMensagemID("msg_entre_dados", Uteis.ALERTA);
				setMensagemDetalhada("");
			}

		} catch (Exception e) {
			setGuiaAba("DadosBasicos");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
		return realizarNavegacaoPagina();
	}
	
	
	public String validarLiberarOperacaoFuncionalidade() {
		try {
			setOncompleteOperacaoFuncionalidade("");
			UsuarioVO usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.getUsernameLiberarOperacaoFuncionalidade(), this.getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			if (getTipoOperacaoFuncionalidadeLiberar().equals("Liberar Controle Inclusão Disciplinas Períodos Futuros")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_LiberarControleInclusaoDisciplinaPeriodoFuturo", usuarioVerif);
				getMatriculaPeriodoVO().setLiberadoControleInclusaoDisciplinaPeriodoFuturo(Boolean.TRUE);
				this.setApresentarBotaoLiberarControleInclusaoDisciplinaPeriodoFuturo(Boolean.FALSE);
				montarListaSelectItemPeriodoLetivoInclusaoDisciplina();
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARCONTROLEINCLUSAODISCIPLINAPERIODOFUTURO, usuarioVerif, ""));
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals("Liberar Obrigatoriedade Aluno Aceitar TERMO ACEITE DE RENOVAÇÃO DE MATRÍCULA no Portal do Aluno")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_LiberarObrigatoriedadeAlunoAceitarTermoAceiteRenovacao", usuarioVerif);
				setApresentarBotaoLiberarRenovacaoSemAceiteTermo(Boolean.FALSE);
				setPermitirRenovacaoSemAceiteTemo(Boolean.TRUE);					
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERAR_OBRIGATORIEDADE_ALUNO_ACEITAR_TERMO_ACEITE, usuarioVerif, ""));
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals(UteisJSF.internacionalizar("prt_Matricula_LiberarInclusaoDisciplinaPorEquivalencia"))) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirIncluirDisciplinaPorEquivalencia", usuarioVerif);
				setLiberadoInclusaoPorEquivalencia(true);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARINCLUSAODISCIPLINAPOREQUIVALENCIA, usuarioVerif, ""));
				setOncompleteOperacaoFuncionalidade("PF('liberarOperacaoFuncionalidade').hide()");
				return"";
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals(UteisJSF.internacionalizar("prt_Matricula_LiberarInclusaoDisciplinaOptativa"))) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirIncluirDisciplinaOptativa", usuarioVerif);
				setLiberadoInclusaoOptativa(true);
				prepararDisciplinasGrupoOptativaPeriodoLetivoRenovacaoAluno();
				setOncompleteOperacaoFuncionalidade("PF('panelGrupoOptativa').show();");
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARINCLUSAODISCIPLINAOPTATIVA, usuarioVerif, ""));
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals(UteisJSF.internacionalizar("prt_Matricula_LiberarInclusaoDisciplinaAcimaMaximo"))) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_AutorizarInclusaoDisciplinaAcimaLimitePeriodo", usuarioVerif);
				this.getMatriculaPeriodoVO().setUsuarioLiberouIncluaoDisciplinaAcimaLimiteMaxPeriodoLetivo(Boolean.TRUE);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERAR_INCLUSAO_DISCIPLINA_ACIMA_MAXIMO_PERMITIDO, usuarioVerif, ""));
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals(UteisJSF.internacionalizar("prt_Matricula_LiberarInclusaoDisciplinaAbaixoMinimo"))) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_AUTORIZAR_INCLUSAO_DISCIPLINA_ABAIXO_LIMITE_PERIODO, usuarioVerif);
				this.getMatriculaPeriodoVO().setLiberadoControleInclusaoMinimaObrigatoriaDisciplina(true);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERAR_INCLUSAO_DISCIPLINA_ABAIXO_MINIMO_PERMITIDO, usuarioVerif, ""));
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals(UteisJSF.internacionalizar("prt_Matricula_LiberarInclusaoDisciplinaTurmaOutroUnidadeEnsino"))) {
				try {
					/**
					 * Neste caso quando não tem esta funcionalidade então é
					 * liberado turmas de todas as Unidade de Ensino, caso contrário é
					 * lançado exceção.
					 */
					getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.INCLUIR_DISCIPLINA_APENAS_TURMA_PROPRIO_UNIDADE_ENSINO.getValor(), usuarioVerif);
				} catch (Exception e) {
					setLiberadoInclusaoTurmaOutroUnidadeEnsino(true);
					getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARINCLUSAODISCIPLINATURMAOUTROUNIDADEENSINO, usuarioVerif, ""));
					montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
					setMensagemID("msg_funcionalidadeLiberadaComSucesso", Uteis.SUCESSO);
					setOncompleteOperacaoFuncionalidade("PF('liberarOperacaoFuncionalidade').hide()");
					return"";
				}
				throw (new AcessoException("USUÁRIO" + usuarioVerif.getNome() + " não possui permissão para a funcionalidade INCLUIR DISCIPLINA NA TURMA DE OUTRO UNIDADE DE ENSINO."));
				
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals(UteisJSF.internacionalizar("prt_Matricula_LiberarInclusaoDisciplinaTurmaOutroCurso"))) {
				try {
					/**
					 * Neste caso quando não tem esta funcionalidade então é
					 * liberado turmas de todos os curso, caso contrário é
					 * lançado exceção.
					 */
					getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("IncluirDisciplinaApenasTurmaProprioCurso", usuarioVerif);
				} catch (Exception e) {
					setLiberadoInclusaoTurmaOutroCurso(true);
					getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARINCLUSAODISCIPLINATURMAOUTROCURSO, usuarioVerif, ""));
					montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
					setMensagemID("msg_funcionalidadeLiberadaComSucesso", Uteis.SUCESSO);
					setOncompleteOperacaoFuncionalidade("PF('liberarOperacaoFuncionalidade').hide()");
					return"";
				}
				throw (new AcessoException("USUÁRIO" + usuarioVerif.getNome() + " não possui permissão para a funcionalidade INCLUIR DISCIPLINA NA TURMA DE OUTRO CURSO."));	
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals("Liberar Inclusão Disciplinas Aluno Regular")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_LiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular", usuarioVerif);
				getMatriculaPeriodoVO().setLiberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular(Boolean.TRUE);
				this.setApresentarBotaoLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular(Boolean.FALSE);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARINCLUSAODISCIPLINAPERIODOLETIVOFUTUROALUNOREGULAR, usuarioVerif, ""));
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals("Liberar Exclusão Disciplinas Aluno Regular")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_LiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular", usuarioVerif);
				getMatriculaPeriodoVO().setLiberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular(Boolean.TRUE);
				this.setApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular(Boolean.FALSE);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERAREXCLUSAODISCIPLINAPERIODOLETIVOATUALALUNOREGULAR, usuarioVerif, ""));
			}
			  else if (getTipoOperacaoFuncionalidadeLiberar().equals("Liberar Exclusão Disciplinas Aluno Irregular")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_LiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular", usuarioVerif);
				getMatriculaPeriodoVO().setLiberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular(Boolean.TRUE);
				this.setApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular(Boolean.FALSE);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERAREXCLUSAODISCIPLINAPERIODOLETIVOATUALALUNOIRREGULAR, usuarioVerif, ""));
			}
			else if (getTipoOperacaoFuncionalidadeLiberar().equals("Liberar Controle Inclusão Disciplinas Obrigatórias de Dependência")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_LiberarControleInclusaoObrigatoriaDisciplinaDependencia", usuarioVerif);
				getMatriculaPeriodoVO().setLiberadoControleInclusaoObrigatoriaDisciplinaDependencia(Boolean.TRUE);
				getMatriculaPeriodoVO().setNrDisciplinasPendentesDevemSerIncluidas(0);
				this.setApresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia(Boolean.FALSE);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARCONTROLEINCLUSAOOBRIGATORIADISCIPLINADEPENDENCIA, usuarioVerif, ""));
			}
			else if (getTipoOperacaoFuncionalidadeLiberar().equals("prt_Matricula_LiberarMatriculaTurmaBaseSemVagaSemAula")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_LiberarMatriculaTurmaBaseSemVagaSemAula", usuarioVerif);
				setLiberarMatriculaTurmaSemVagaSemAula(true);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARMATRICULATURMABASESEMVAGASEMAULA, usuarioVerif, ""));
				montarListaSelectItemTurma();
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals("per_MatriculaLiberarAlteracaoCategoriaPlanoFinanceiro")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_LiberarAlterarCategoriaCondicaoPagamentoPlanoFinanceiroCurso", usuarioVerif);
				setLiberarAlteracaoCategoriaPlanoFinanceiro(true);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARALTERACAOCATEGORIAPLANOFINANCEIRO, usuarioVerif, ""));
				setPermiteAlterarPlanoCondicaoFinanceiraCurso(true);
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals(UteisJSF.internacionalizar("per_MatriculaPermitirAlterarContratoMatricula_titulo"))) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_PermitirAlterarContratoMatricula", usuarioVerif);
				setPermitirAlterarContratoMatricula(true);
				OperacaoFuncionalidadeVO operacaoFuncionalidadeVO = getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARALTERACAOCONTRATO, usuarioVerif, "");
				getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(operacaoFuncionalidadeVO);
				setOncompleteOperacaoFuncionalidade("PF('panelSelecionarContrato').show()");
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals(UteisJSF.internacionalizar("prt_Matricula_LiberarValidacaoDadosEnadeCenso"))) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_LiberarValidacaoDadosEnadeCenso", usuarioVerif);
				setPermiteLiberarValidacaoDadosEnadeCenso(true);
				OperacaoFuncionalidadeVO operacaoFuncionalidadeVO = getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERAR_VALIDACAO_DADOS_ENADE_CENSO, usuarioVerif, "");
				getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(operacaoFuncionalidadeVO);
//				setOncompleteOperacaoFuncionalidade("PF('panelAvisoVerificarDadosEnadeCenso').hide()");
				setOncompleteOperacaoFuncionalidade("PF('liberarOperacaoFuncionalidade').hide();"+getOncompleteOperacaoFuncionalidade());
				if(getLoginControle().getPermissaoAcessoMenuVO().getNavegarAbaDocumentacao()) {
					getApresentarModalInscricao();
					return navegarAbaDocumentacao();
				}
			} else if (getTipoOperacaoFuncionalidadeLiberar().equals("Permitir Liberar Validação Aluno Com Bloqueio na Biblioteca")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_PermitirLiberarValidacaoAlunoComBloqueioBiblioteca", usuarioVerif);
				setApresentarBotaoLiberarValidacaoAlunoBloqueioBiblioteca(Boolean.FALSE);
				setPermiteLiberarValidacaoAlunoBloqueioBiblioteca(Boolean.TRUE);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_PERMITIR_LIBERAR_VALIDACAO_ALUNO_COM_BLOQUEIO_BIBLIOTECA, usuarioVerif, ""));
			}else if (getTipoOperacaoFuncionalidadeLiberar().equals("prt_Matricula_PermitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso")) {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso", usuarioVerif);
				setPermitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso(true);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_PERMITIR_INCLUIR_PLANO_DESCONTO_DIFERENTE_CONDICAO_PAGAMENTO_DO_CURSO, usuarioVerif, ""));
//				montarListaSelectItemPlanoDesconto();
			}
			if(getOncompleteOperacaoFuncionalidade().trim().isEmpty()) {
				setOncompleteOperacaoFuncionalidade("PF('liberarOperacaoFuncionalidade').hide()");
			}else {
				setOncompleteOperacaoFuncionalidade("PF('liberarOperacaoFuncionalidade').hide();"+getOncompleteOperacaoFuncionalidade());
			}
			setMensagemID("msg_funcionalidadeLiberadaComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
		return navegarAbaDocumentacao();
	}

	public void verificarExisteBloqueioDisciplinasDeDependenciaMinimaDevemSerCursadasPeloAluno() throws Exception {
		if ((getApresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia()) && (!getMatriculaPeriodoVO().getLiberadoControleInclusaoObrigatoriaDisciplinaDependencia())) {
			if (getMatriculaVO().getCurso().getConfiguracaoAcademico().getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH() && ((getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR") && getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCreditoAlunoPodeCursar() > 0) || (getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CH") && getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCargaHorariaAlunoPodeCursar() > 0))) {
				PeriodoLetivoVO periodoAnterior = getFacadeFactory().getMatriculaPeriodoFacade().executarObterPeriodoLetivoAnteriorAoPeriodoLetivoMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO());
				Integer crChIncluir = getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR") ? getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCreditoAlunoPodeCursar() : getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCargaHorariaAlunoPodeCursar();
				getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().verificaExistenciarDisciplinaPendenteAlunoAteDeterminadoPeriodoMatrizCurricularPodeSerIncluida(periodoAnterior.getPeriodoLetivo(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getConsiderarRegularAlunoDependenciaOptativa(), crChIncluir, getMatriculaPeriodoVO().getAno(), getMatriculaPeriodoVO().getSemestre(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR"), getMatriculaVO().getCurso().getConfiguracaoAcademico().getPorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia());
			} else if (getMatriculaPeriodoVO().getTipoContabilizacaoDisciplinaDependencia().equals(TipoContabilizacaoDisciplinaDependenciaEnum.QTDE_DISCIPLINA) && getMatriculaPeriodoVO().getNrDisciplinasIncluidasPeriodosAnteriores().compareTo(getMatriculaPeriodoVO().getNrDisciplinasPendentesDevemSerIncluidas()) < 0) {
				throw new Exception("É obrigatório que sejam incluídas ao menos " + getMatriculaPeriodoVO().getNrDisciplinasPendentesDevemSerIncluidas() + " disciplina(s) pendente(s) dos períodos anteriores. Disciplina(s) pendente(s) já adicionada(s): " + getMatriculaPeriodoVO().getNrDisciplinasIncluidasPeriodosAnteriores() + ".");
			}
		}
	}
	
	public void verificarExisteBloqueioDisciplinasDeDependenciaMaximaOfertadaDevemSerCursadasPeloAluno() throws Exception {
		
		if (((getApresentarBotaoLiberarControleInclusaoObrigatoriaDisciplinaDependencia() && getUsuarioLogado().getIsApresentarVisaoAdministrativa()) || getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) 
				&& !getMatriculaPeriodoVO().getLiberadoControleInclusaoObrigatoriaDisciplinaDependencia()
			    && getMatriculaVO().getCurso().getConfiguracaoAcademico().getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH()) { 
			     
			if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR")) {				
				Integer totalCreditoPendenteOfertado = getListaDisciplinasPeriodoLetivoAlunoPendente().stream().filter(p-> p.isGradeDisciplinaOfertada() && p.getPeriodoLetivoVO().getPeriodoLetivo() < getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo()).mapToInt(GradeDisciplinaVO::getNrCreditos).reduce(0,(a, b) ->a + b);
				Integer totalCreditoPendenteVerificar = totalCreditoPendenteOfertado <  getMatriculaPeriodoVO().getNrCreditoPendentesDevemSerIncluidas() ? totalCreditoPendenteOfertado : getMatriculaPeriodoVO().getNrCreditoPendentesDevemSerIncluidas();
				if(getMatriculaPeriodoVO().getTotalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior() < totalCreditoPendenteVerificar) {
					PeriodoLetivoVO periodoAnterior = getFacadeFactory().getMatriculaPeriodoFacade().executarObterPeriodoLetivoAnteriorAoPeriodoLetivoMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO());
					throw new Exception( "O Nr. de crédito das disciplinas de dependência a incluir é de " + getMatriculaPeriodoVO().getNrCreditoPendentesDevemSerIncluidas()+". Sendo assim obrigatório que sejam incluídas pelo menos mais " + (totalCreditoPendenteVerificar - getMatriculaPeriodoVO().getTotalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior())  +" crédito(s) de disciplina(s) pendente(s) dos períodos anteriores ao "+(periodoAnterior.getPeriodoLetivo()+1)+"º período.");
				}
			}else if(getMatriculaVO().getCurso().getConfiguracaoAcademico().getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CH")) {
				Integer totalChPendenteOfertado = getListaDisciplinasPeriodoLetivoAlunoPendente().stream().filter(p-> p.isGradeDisciplinaOfertada() && p.getPeriodoLetivoVO().getPeriodoLetivo() < getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo()).mapToInt(GradeDisciplinaVO::getCargaHoraria).reduce(0,(a, b) ->a + b);
				Integer totalCrPendenteVerificar = totalChPendenteOfertado <  getMatriculaPeriodoVO().getNrCargaHorariaPendentesDevemSerIncluidas() ? totalChPendenteOfertado : getMatriculaPeriodoVO().getNrCargaHorariaPendentesDevemSerIncluidas();
				if(getMatriculaPeriodoVO().calcularCargaHorariaDisciplinasPeriodosAnterioresAdicionadasParaMatriculaPeriodo(getMatriculaVO()) < totalCrPendenteVerificar) {
					PeriodoLetivoVO periodoAnterior = getFacadeFactory().getMatriculaPeriodoFacade().executarObterPeriodoLetivoAnteriorAoPeriodoLetivoMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO());			
					throw new Exception( "O Nr. de carga horária das disciplinas de dependência a incluir é de " + getMatriculaPeriodoVO().getNrCargaHorariaPendentesDevemSerIncluidas()+". Sendo assim obrigatório que sejam incluídas pelo menos mais " + (totalCrPendenteVerificar - getMatriculaPeriodoVO().calcularCargaHorariaDisciplinasPeriodosAnterioresAdicionadasParaMatriculaPeriodo(getMatriculaVO()))  +" hora(s) de disciplina(s) pendente(s) dos períodos anteriores ao "+(periodoAnterior.getPeriodoLetivo())+"º período.");
				}
			}
		}
		
		if((getLiberadoInclusaoOptativa()) && ((getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaDisciplinaOptativaCumprida() +  
				getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> (d.getDisciplinaReferenteAUmGrupoOptativa() || d.getDisciplinaOptativa()) && !d.getHistoricoVO().getSituacao().equals("AP") && !d.getHistoricoVO().getSituacao().equals("AE") ).mapToInt(d -> d.getCargaHoraria()).sum())
				< getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCargaHorariaOptativaExigida())) {
			Integer totalChEletiva = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().stream().filter(
					p -> p.getControleOptativaGrupo() && p.getPeriodoLetivo() <= getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo() && p.getNumeroCargaHorariaOptativa() > 0).mapToInt(p -> p.getNumeroCargaHorariaOptativa()).sum();			
			totalChEletiva = totalChEletiva - getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaDisciplinaOptativaCumprida();
			if(totalChEletiva > 0 && getMatriculaPeriodoVO().getSaldoCHDisponivelInclusaoDisplinas() > 0 
					&& getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> (d.getDisciplinaReferenteAUmGrupoOptativa() || d.getDisciplinaOptativa())  && !d.getHistoricoVO().getSituacao().equals("AP") && !d.getHistoricoVO().getSituacao().equals("AE") ).mapToInt(d -> d.getCargaHoraria()).sum()
				< totalChEletiva) {
				
				Integer chOfertada = 0, totalPendenciaAcumulada = 0;
				List<String> periodosLetivosComExigenciaOptativas = new ArrayList<>();
				for(PeriodoLetivoVO periodoLetivoVO: getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs()) {
					if(periodoLetivoVO.getControleOptativaGrupo() && Uteis.isAtributoPreenchido(periodoLetivoVO.getGradeCurricularGrupoOptativa()) && periodoLetivoVO.getPeriodoLetivo() <= getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo()) {
						Integer chPendenteOptativas = null;
						if (!periodoLetivoVO.getPeriodoLetivo().equals(getMatriculaPeriodoVO().getPeriodoLetivo().getPeriodoLetivo())
								&& getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorNumeroPeriodo(periodoLetivoVO.getPeriodoLetivo()) != null
								&& Uteis.isAtributoPreenchido(chPendenteOptativas = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorNumeroPeriodo(periodoLetivoVO.getPeriodoLetivo()).getCargaHorariaPendentePeriodoComRelacaoGrupoOptativa())
								&& chPendenteOptativas > 0) {
							periodosLetivosComExigenciaOptativas.add(periodoLetivoVO.getPeriodoLetivo() + "º");
							totalPendenciaAcumulada += chPendenteOptativas;
						}
						chOfertada += getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas().getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().stream().filter(d -> !d.getDisciplinaJaAdicionada() && d.getDisciplinaOfertada() && d.getCargaHoraria() <= getMatriculaPeriodoVO().getSaldoCHDisponivelInclusaoDisplinas()).mapToInt(d -> d.getCargaHoraria()).sum();
					}
				}
				if(chOfertada > 0) {
					Integer chAdicionada = getMatriculaPeriodoVO().getChTotalDisciplinaOptativaMatriculaPeriodo();
					Integer chPendente = totalChEletiva > chOfertada ? chOfertada : totalChEletiva - chAdicionada;
					if (Uteis.isAtributoPreenchido(periodosLetivosComExigenciaOptativas) && getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroCargaHorariaOptativa() > 0) {
						if(chPendente > 0) {
							throw new Exception("Devem ser incluídas "+ chPendente + " horas de disciplinas eletivas ofertadas para inclusão. Há pendências de horas acumuladas dos seguintes períodos anteriores: " 
									+ periodosLetivosComExigenciaOptativas.stream().collect(Collectors.joining(", ")) + ". Exigência do atual período letivo: (" 
									+ getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroCargaHorariaOptativa()
									+ " horas) + Pendência dos períodos anteriores (" + totalPendenciaAcumulada + " horas).");
						}
					} else {
						if(chPendente > 0) {
							throw new Exception("Devem ser incluídas "+ chPendente + " horas de disciplinas eletivas ofertadas para inclusão.");
						}
					}
				}
			}
		}
		
			
	}
	
	
	public void verificarExisteBloqueioDisciplinasComControleInclusaoMinimaDevemSerCursadasPeloAluno() {
		if (!getMatriculaPeriodoVO().getLiberadoControleInclusaoMinimaObrigatoriaDisciplina() 
				&& getMatriculaVO().getCurso().getConfiguracaoAcademico().getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH()) {
			getFacadeFactory().getMatriculaPeriodoFacade().validarDisciplinaIncluidasPodeSerAdicionadaMatriculaPeriodoConformeLimiteMinimoPeriodoLetivo(getMatriculaPeriodoVO(), getMatriculaVO(), getUsuarioLogado());
		}
	}

	/**
	 * @return the liberadoInclusaoPorEquivalencia
	 */
	public Boolean getLiberadoInclusaoPorEquivalencia() {
		if (liberadoInclusaoPorEquivalencia == null) {
			liberadoInclusaoPorEquivalencia = false;
		}
		return liberadoInclusaoPorEquivalencia;
	}

	/**
	 * @param liberadoInclusaoPorEquivalencia
	 *            the liberadoInclusaoPorEquivalencia to set
	 */
	public void setLiberadoInclusaoPorEquivalencia(Boolean liberadoInclusaoPorEquivalencia) {
		this.liberadoInclusaoPorEquivalencia = liberadoInclusaoPorEquivalencia;
	}

	/**
	 * @return the liberadoInclusaoOptativa
	 */
	public Boolean getLiberadoInclusaoOptativa() {
		if (liberadoInclusaoOptativa == null) {
			liberadoInclusaoOptativa = false;
		}
		return liberadoInclusaoOptativa;
	}

	/**
	 * @param liberadoInclusaoOptativa
	 *            the liberadoInclusaoOptativa to set
	 */
	public void setLiberadoInclusaoOptativa(Boolean liberadoInclusaoOptativa) {
		this.liberadoInclusaoOptativa = liberadoInclusaoOptativa;
	}	

	public boolean isLiberadoInclusaoTurmaOutroUnidadeEnsino() {
		return liberadoInclusaoTurmaOutroUnidadeEnsino;
	}

	public void setLiberadoInclusaoTurmaOutroUnidadeEnsino(boolean liberadoInclusaoTurmaOutroUnidadeEnsino) {
		this.liberadoInclusaoTurmaOutroUnidadeEnsino = liberadoInclusaoTurmaOutroUnidadeEnsino;
	}

	/**
	 * @return the liberadoInclusaoTurmaOutroCurso
	 */
	public Boolean getLiberadoInclusaoTurmaOutroCurso() {
		if (liberadoInclusaoTurmaOutroCurso == null) {
			liberadoInclusaoTurmaOutroCurso = false;
		}
		return liberadoInclusaoTurmaOutroCurso;
	}

	/**
	 * @param liberadoInclusaoTurmaOutroCurso
	 *            the liberadoInclusaoTurmaOutroCurso to set
	 */
	public void setLiberadoInclusaoTurmaOutroCurso(Boolean liberadoInclusaoTurmaOutroCurso) {
		this.liberadoInclusaoTurmaOutroCurso = liberadoInclusaoTurmaOutroCurso;
	}
	

	public Boolean getLiberadoInclusaoTurmaOutroMatrizCurricular() {
		if (liberadoInclusaoTurmaOutroMatrizCurricular == null) {
			liberadoInclusaoTurmaOutroMatrizCurricular = false;
		}
		return liberadoInclusaoTurmaOutroMatrizCurricular;
	}

	public void setLiberadoInclusaoTurmaOutroMatrizCurricular(Boolean liberadoInclusaoTurmaOutroMatrizCurricular) {
		this.liberadoInclusaoTurmaOutroMatrizCurricular = liberadoInclusaoTurmaOutroMatrizCurricular;
	}

	/**
	 * @return the oncompleteOperacaoFuncionalidade
	 */
	public String getOncompleteOperacaoFuncionalidade() {
		if (oncompleteOperacaoFuncionalidade == null) {
			oncompleteOperacaoFuncionalidade = "";
		}
		return oncompleteOperacaoFuncionalidade;
	}

	/**
	 * @param oncompleteOperacaoFuncionalidade
	 *            the oncompleteOperacaoFuncionalidade to set
	 */
	public void setOncompleteOperacaoFuncionalidade(String oncompleteOperacaoFuncionalidade) {
		this.oncompleteOperacaoFuncionalidade = oncompleteOperacaoFuncionalidade;
	}

	public void verificarApresentarMensagemAntesGravarVisaoAluno()  {
		try {
			setPaginaDestinoConfirmacao("");
			verificarExisteBloqueioDisciplinasDeDependenciaMaximaOfertadaDevemSerCursadasPeloAluno();
			if (getMatriculaPeriodoVO().getProcessoMatriculaVO().getMensagemConfirmacaoRenovacaoAluno().trim().equals("")) {
				if (getGuiaAba().equals("Disciplinas")) {
					boolean renovacaoAutomatica =  getMatriculaVO().getAluno().getRenovacaoAutomatica();
//					navegarAbaPlanoFinanceiroAluno();
					getMatriculaVO().getAluno().setRenovacaoAutomatica(renovacaoAutomatica);
				}
				if(!getMensagemDetalhada().isEmpty()) {
					throw new Exception(getMensagemDetalhada());
				}
				gravarVisaoAluno();
				if(!getMensagemDetalhada().isEmpty()) {
					throw new Exception(getMensagemDetalhada());
				}
				setPaginaDestinoConfirmacao("");
			} else {
				setPaginaDestinoConfirmacao("PF('panelMensagemConfirmacaoRenovacao2').show()");
			}	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void verificarApresentarMensagemConfirmacaoNavegarAbaPlanoFinanceiroAluno() throws Exception {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			if (getMatriculaPeriodoVO().getProcessoMatriculaVO().getMensagemConfirmacaoRenovacaoAluno().trim().equals("")) {
//				navegarAbaPlanoFinanceiroAluno();
				setPaginaDestinoConfirmacao("");
			} else {
				setPaginaDestinoConfirmacao("PF('panelMensagemConfirmacaoRenovacao').show()");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * @return the paginaDestinoConfirmacao
	 */
	public String getPaginaDestinoConfirmacao() {
		if (paginaDestinoConfirmacao == null) {
			paginaDestinoConfirmacao = "";
		}
		return paginaDestinoConfirmacao;
	}

	/**
	 * @param paginaDestinoConfirmacao
	 *            the paginaDestinoConfirmacao to set
	 */
	public void setPaginaDestinoConfirmacao(String paginaDestinoConfirmacao) {
		this.paginaDestinoConfirmacao = paginaDestinoConfirmacao;
	}

	private boolean fecharPainelDisciplinaComposta = false;

	public void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar() {
		try {
			setFecharPainelDisciplinaComposta(true);
			executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar(getListaMatriculaPeriodoTurmaDisciplinaFazParteComposicao());
			setMensagemID("msg_dados_selecionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setFecharPainelDisciplinaComposta(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarPorMatriculaPeriodo() throws Exception {
		for (MatriculaPeriodoTurmaDisciplinaVO mptdVO : getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs()) {
			if (mptdVO.getDisciplinaComposta()) {
				executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(mptdVO, getMatriculaPeriodoVO()));
			}
		}
	}

	private void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) throws Exception {
		if (!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVOs)) {
			return;
		}
		Integer numeroMaximoDisciplinaComposicaoEstudar = 0;
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVOs.get(0).getGradeDisciplinaCompostaVO().getGradeDisciplina().getCodigo())) {
			GradeDisciplinaVO gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVOs.get(0).getGradeDisciplinaCompostaVO().getGradeDisciplina().getCodigo(), getUsuarioLogado());
			if(TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA.equals(gradeDisciplinaVO.getTipoControleComposicao())){
				for (MatriculaPeriodoTurmaDisciplinaVO mptdVO : matriculaPeriodoTurmaDisciplinaVOs) {
					numeroMaximoDisciplinaComposicaoEstudar += Uteis.isAtributoPreenchido(mptdVO.getTurma()) ? 1 : 0;
				
				}
				
				if (numeroMaximoDisciplinaComposicaoEstudar < (gradeDisciplinaVO.getNumeroMinimoDisciplinaComposicaoEstudar())
						|| numeroMaximoDisciplinaComposicaoEstudar > gradeDisciplinaVO.getNumeroMaximoDisciplinaComposicaoEstudar()) {
					throw new Exception(UteisJSF.internacionalizar("msg_GradeDisciplina_numeroMaximoDisciplinaComposicaoEstudar").replace("{0}", 
							gradeDisciplinaVO.getDisciplina().getNome()).replace("{1}", numeroMaximoDisciplinaComposicaoEstudar.toString())
							.replace("{2}",gradeDisciplinaVO.getNumeroMinimoDisciplinaComposicaoEstudar().toString())
							.replace("{3}",gradeDisciplinaVO.getNumeroMaximoDisciplinaComposicaoEstudar().toString()));
				}
				}	
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
			return "PF('panelDisciplinasComposta').hide()";
		}
		return "";
	}

	public void executarInicializacaoDadosAdicionarTurmaGradeDisciplinaComposta() {
		try {
			setPaginaDestinoInclusaoDisciplina("PF('panelIncluirDisciplinasFazParteComposicao').show()");
			setMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO((MatriculaPeriodoTurmaDisciplinaVO) getRequestMap().get("disciplinaCompostaItens"));
			executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarAdicionarDisciplina(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getMatriculaPeriodoTurmaDisciplinaVOAdicionar(), getMatriculaPeriodoVO()));
			setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
			setCodigoMapaEquivalenciaDisciplinaCursar(0);
			setListaSelectItemMapaEquivalenciaDisciplinaIncluir(null);
			setListaSelectItemDisciplinaEquivalenteAdicionar(null);
			montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO());
//			if (getHorarioAlunoTurnoVOs().isEmpty()) {
//				prepararHorarioAulaAluno();
//			} else {
//				getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
//			}
			setMensagemID("msg_informe_dados");
		} catch (Exception e) {
			setPaginaDestinoInclusaoDisciplina("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarMatriculaPeriodoTurmaDiscipinaFazParteComposicaoRemover() {
		try {
			setMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO((MatriculaPeriodoTurmaDisciplinaVO) getRequestMap().get("disciplinaCompostaItens"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerMatriculaPeriodoTurmaDisciplinaComposta() {
		try {
			getFacadeFactory().getMatriculaPeriodoFacade().removerMatriculaPeriodoTurmaDisciplinaObjEspecifico(getMatriculaPeriodoVO(), getMatriculaVO(), getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO(), getUsuarioLogado());
			setListaMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getMatriculaPeriodoTurmaDisciplinaVOAdicionar(), getMatriculaPeriodoVO()));
//			if(!getHorarioAlunoTurnoVOs().isEmpty()){
//				prepararHorarioAulaAluno();
//			}
			setMensagemID("msg_dados_removidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO() {
		if (matriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO == null) {
			matriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO) {
		this.matriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO = matriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO;
	}

	public void adicionarMatriculaPeriodoTurmaDisciplinaFazParteComposicaoPorInclusao() {
		try {
			adicionarMatriculaPeriodoTurmaDisciplinaPorInclusao(getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemTurmaAdicionar() {
		try {
			montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTurmaIncluirDisciplinaFazParteComposicao() {
		try {
			selecionarTurmaIncluirDisciplina(getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO());
		} catch (Exception e) {
			if (e instanceof ConsistirException) {
				ConsistirException consistirException = (ConsistirException) e;
				setMensagemDetalhada("msg_erro", consistirException.getToStringMensagemErro(), Uteis.ERRO);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	private void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarAdicionarDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) throws Exception {
		if (!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVOs)) {
			return;
		}
		Integer numeroMaximoDisciplinaComposicaoEstudar = 0;
		GradeDisciplinaVO gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVOs.get(0).getGradeDisciplinaCompostaVO().getGradeDisciplina().getCodigo(), getUsuarioLogado());
		for (MatriculaPeriodoTurmaDisciplinaVO mptdVO : matriculaPeriodoTurmaDisciplinaVOs) {
			numeroMaximoDisciplinaComposicaoEstudar += Uteis.isAtributoPreenchido(mptdVO.getTurma()) ? 1 : 0;
		}
		if (numeroMaximoDisciplinaComposicaoEstudar >= gradeDisciplinaVO.getNumeroMaximoDisciplinaComposicaoEstudar() && TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA.equals(gradeDisciplinaVO.getTipoControleComposicao())) {
			throw new Exception(UteisJSF.internacionalizar("msg_GradeDisciplina_numeroMaximoDisciplinaComposicaoEstudar").replace("{0}", 
					gradeDisciplinaVO.getDisciplina().getNome()).replace("{1}", numeroMaximoDisciplinaComposicaoEstudar.toString())
					.replace("{2}", gradeDisciplinaVO.getNumeroMinimoDisciplinaComposicaoEstudar().toString())
			        .replace("{3}", gradeDisciplinaVO.getNumeroMaximoDisciplinaComposicaoEstudar().toString()));
		}
	}

	public List<OperacaoFuncionalidadeVO> getOperacaoFuncionalidadePersistirVOs() {
		if (operacaoFuncionalidadePersistirVOs == null) {
			operacaoFuncionalidadePersistirVOs = new ArrayList<OperacaoFuncionalidadeVO>(0);
		}
		return operacaoFuncionalidadePersistirVOs;
	}

	public void setOperacaoFuncionalidadePersistirVOs(List<OperacaoFuncionalidadeVO> operacaoFuncionalidadePersistirVOs) {
		this.operacaoFuncionalidadePersistirVOs = operacaoFuncionalidadePersistirVOs;
	}

	
	public void validarExistenciaMatriculaPeriodoAtivaPreMatricula() throws Exception {
		if (getMatriculaVO().getCurso().getNivelEducacional().equals("GT") || getMatriculaVO().getCurso().getNivelEducacional().equals("SU")) {
			getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatriculaPeriodoLetivoAtivoAnoSemestre(this.getMatriculaVO().getMatricula(), getMatriculaPeriodoVO().getAno(), getMatriculaPeriodoVO().getSemestre(), getMatriculaPeriodoVO().getCodigo(), getUsuarioLogado());
		}
		if (getMatriculaPeriodoVO().isNovoObj() && getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMatriculaPeriodoPorMatriculaAnoSemestre(this.getMatriculaVO().getMatricula(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getAnoReferenciaPeriodoLetivo(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getSemestreReferenciaPeriodoLetivo(), getMatriculaVO().getCurso().getPeriodicidade(), getUsuarioLogado())) {
			if (!getMatriculaVO().getCurso().getNivelEducacional().equals("EX")) {
				throw new Exception("Já Existe Uma Matrícula Ativa ou Pré-Matrícula Para Esse Aluno, Nesse Curso e nesse Ano/Semestre!");
			}
		}
	}


	

	public void executarMontagemListaSelectItemSubturmaPraticaTeoricaTransferir() {
		try {
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar((MatriculaPeriodoTurmaDisciplinaVO) getRequestMap().get("turmaDisciplinaItens"));
			List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultarPorSubTurma(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado(), TipoSubTurmaEnum.PRATICA, getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre(), "AB");
			setListaSelectItemSubturmaPratica(new ArrayList<SelectItem>(0));
			getListaSelectItemSubturmaPratica().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : turmaVOs) {
				if (turmaVO.getSituacao().equals("AB") && !turmaVO.getCodigo().equals(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurmaPratica().getCodigo()))
					getListaSelectItemSubturmaPratica().add(new SelectItem(turmaVO.getCodigo(), turmaVO.getIdentificadorTurma()));
			}
			Collections.sort(getListaSelectItemSubturmaPratica(), new SelectItemOrdemValor());

			turmaVOs = getFacadeFactory().getTurmaFacade().consultarPorSubTurma(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado(), TipoSubTurmaEnum.TEORICA, getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre(), "AB");
			setListaSelectItemSubturmaTeorica(new ArrayList<SelectItem>(0));
			getListaSelectItemSubturmaTeorica().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : turmaVOs) {
				if (turmaVO.getSituacao().equals("AB") && !turmaVO.getCodigo().equals(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurmaTeorica().getCodigo()))
					getListaSelectItemSubturmaTeorica().add(new SelectItem(turmaVO.getCodigo(), turmaVO.getIdentificadorTurma()));
			}
			Collections.sort(getListaSelectItemSubturmaTeorica(), new SelectItemOrdemValor());
			setAbrirPanelAlterarSubturmaPraticaTeorica(true);
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setAbrirPanelAlterarSubturmaPraticaTeorica(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarVerificacaoQtdeMaximaAlunosTurmaPraticaTeoricaChoqueHorarioRegistroAulaTransferir() {
		boolean sucessoSubturmaTeorica = true;
		boolean sucessoSubturmaPratica = true;
		try {
			limparMensagem();
			ConsistirException ce = new ConsistirException();
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setAlterandoSubturmaPraticaTeorica(false);
			sucessoSubturmaTeorica = getFacadeFactory().getMatriculaPeriodoFacade().executarVerificacaoQtdeMaximaAlunosTurmaTeoricaChoqueHorarioRegistroAulaTransferir(getMatriculaPeriodoVO(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar(), getSubturmaTeoricaDestino(), false, getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno(), getUsuarioLogado(), ce);
			sucessoSubturmaPratica = getFacadeFactory().getMatriculaPeriodoFacade().executarVerificacaoQtdeMaximaAlunosTurmaPraticaChoqueHorarioRegistroAulaTransferir(getMatriculaPeriodoVO(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar(), getSubturmaPraticaDestino(), false, getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno(), getUsuarioLogado(), ce);
			if (Uteis.isAtributoPreenchido(ce.getListaMensagemErro()))
				throw ce;
			setAbrirPanelAlterarSubturmaPraticaTeorica(false);
			setMensagemID("msg_dados_consultados");
		} catch (ConsistirException e) {
			if (!sucessoSubturmaPratica)
				setSubturmaPraticaDestino(new TurmaVO());
			if (!sucessoSubturmaTeorica)
				setSubturmaTeoricaDestino(new TurmaVO());
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarVerificacaoQtdeMaximaAlunosTurmaPraticaTeoricaChoqueHorarioRegistroAulaTransferirVisaoAluno() {
		boolean sucessoSubturmaTeorica = true;
		boolean sucessoSubturmaPratica = true;
		try {
			limparMensagem();
			ConsistirException ce = new ConsistirException();
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setAlterandoSubturmaPraticaTeorica(false);
			sucessoSubturmaTeorica = getFacadeFactory().getMatriculaPeriodoFacade().executarVerificacaoQtdeMaximaAlunosTurmaTeoricaChoqueHorarioRegistroAulaTransferir(getMatriculaPeriodoVO(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar(), getSubturmaTeoricaDestino(), true, getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno(), getUsuarioLogado(), ce);
			sucessoSubturmaPratica = getFacadeFactory().getMatriculaPeriodoFacade().executarVerificacaoQtdeMaximaAlunosTurmaPraticaChoqueHorarioRegistroAulaTransferir(getMatriculaPeriodoVO(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar(), getSubturmaPraticaDestino(), true, getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno(), getUsuarioLogado(), ce);
			if (Uteis.isAtributoPreenchido(ce.getListaMensagemErro()))
				throw ce;
			setAbrirPanelAlterarSubturmaPraticaTeorica(false);
			setMensagemID("msg_dados_consultados");
		} catch (ConsistirException e) {
			if (!sucessoSubturmaPratica)
				setSubturmaPraticaDestino(new TurmaVO());
			if (!sucessoSubturmaTeorica)
				setSubturmaTeoricaDestino(new TurmaVO());
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemSubturmaPratica() {
		if (listaSelectItemSubturmaPratica == null) {
			listaSelectItemSubturmaPratica = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSubturmaPratica;
	}

	public void setListaSelectItemSubturmaPratica(List<SelectItem> listaSelectItemSubturmaPratica) {
		this.listaSelectItemSubturmaPratica = listaSelectItemSubturmaPratica;
	}

	public List<SelectItem> getListaSelectItemSubturmaTeorica() {
		if (listaSelectItemSubturmaTeorica == null) {
			listaSelectItemSubturmaTeorica = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSubturmaTeorica;
	}

	public void setListaSelectItemSubturmaTeorica(List<SelectItem> listaSelectItemSubturmaTeorica) {
		this.listaSelectItemSubturmaTeorica = listaSelectItemSubturmaTeorica;
	}

	public TurmaVO getSubturmaPraticaDestino() {
		if (subturmaPraticaDestino == null) {
			subturmaPraticaDestino = new TurmaVO();
		}
		return subturmaPraticaDestino;
	}

	public void setSubturmaPraticaDestino(TurmaVO subturmaPraticaDestino) {
		this.subturmaPraticaDestino = subturmaPraticaDestino;
	}

	public TurmaVO getSubturmaTeoricaDestino() {
		if (subturmaTeoricaDestino == null) {
			subturmaTeoricaDestino = new TurmaVO();
		}
		return subturmaTeoricaDestino;
	}

	public void setSubturmaTeoricaDestino(TurmaVO subturmaTeoricaDestino) {
		this.subturmaTeoricaDestino = subturmaTeoricaDestino;
	}

	public boolean isAbrirPanelAlterarSubturmaPraticaTeorica() {
		return abrirPanelAlterarSubturmaPraticaTeorica;
	}

	public void setAbrirPanelAlterarSubturmaPraticaTeorica(boolean abrirPanelAlterarSubturmaPraticaTeorica) {
		this.abrirPanelAlterarSubturmaPraticaTeorica = abrirPanelAlterarSubturmaPraticaTeorica;
	}

	public boolean getIsApresentarAno() {
		if ((getApresentarCampoTurma()) && getTurmaTelaConsulta().getCodigo().intValue() > 0) {
			if ((getTurmaTelaConsulta().getCurso().getPeriodicidade().equals("SE") || getTurmaTelaConsulta().getCurso().getPeriodicidade().equals("AN")) && !getTurmaTelaConsulta().getCurso().getNivelEducacionalPosGraduacao()) {
				return true;
			}
		} else if (getApresentarComboBoxSituacao()) {
			return true;
		} else {
			setAno("");
			setSemestre("");
		}
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if ((getApresentarCampoTurma() || getApresentarComboBoxSituacao()) && getTurmaTelaConsulta().getCodigo().intValue() > 0) {
			if (getTurmaTelaConsulta().getCurso().getPeriodicidade().equals("SE") && !getTurmaTelaConsulta().getCurso().getNivelEducacionalPosGraduacao()) {
				return true;
			}
		} else if (getApresentarComboBoxSituacao()) {
			return true;
		} else {
			setSemestre("");
		}
		return false;
	}

	public void limparCamposBusca() {
		setTurmaTelaConsulta(null);
		setAno("");
		setSemestre("");
		getControleConsulta().setValorConsulta("");
	}

	public TurmaVO getTurmaTelaConsulta() {
		if (turmaTelaConsulta == null) {
			turmaTelaConsulta = new TurmaVO();
		}
		return turmaTelaConsulta;
	}

	public void setTurmaTelaConsulta(TurmaVO turmaTelaConsulta) {
		this.turmaTelaConsulta = turmaTelaConsulta;
	}

	
	private PessoaVO pessoaVO;
	private Boolean encontrouProspectAluno;
	private List<CidadeVO> listaConsultaNaturalidade;
	private String campoConsultaNaturalidade;
	private String valorConsultaNaturalidade;
	private String modalPreInscricao;
	
	private String campoConsultaCidade;
	private String valorConsultaCidade;
	private List listaConsultaCidade;
	
	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public void inicializarDadosPessoaExiste() {
		try {
			setPessoaVO(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getMatriculaVO().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setEncontrouProspectAluno(Boolean.TRUE);
			setModalPreInscricao("PF('panelPreInscricao').show();");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getStyleClassCamposEncontrouProspect() {
		if (this.getEncontrouProspectAluno()) {
			return "form-control camposSomenteLeitura";
		}
		return "form-control campos";
	}

	/**
	 * @return the encontrouProspectAluno
	 */
	public Boolean getEncontrouProspectAluno() {
		if (encontrouProspectAluno == null) {
			encontrouProspectAluno = Boolean.FALSE;
		}
		return encontrouProspectAluno;
	}

	public void setEncontrouProspectAluno(Boolean encontrouProspectAluno) {
		this.encontrouProspectAluno = encontrouProspectAluno;
	}

	public List getListaSelectItemSexoPessoa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable sexos = (Hashtable) Dominios.getSexo();
		Enumeration keys = sexos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) sexos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	public List getListaSelectItemTipoPessoaPessoa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoPessoaBasicoPessoas = (Hashtable) Dominios.getTipoPessoaBasicoPessoa();
		Enumeration keys = tipoPessoaBasicoPessoas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoPessoaBasicoPessoas.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	public List getListaSelectItemEstadoEmissaoRGPessoa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable estados = (Hashtable) Dominios.getEstado();
		Enumeration keys = estados.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) estados.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	public List getListaSelectItemEstadoCivilPessoa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable estadoCivils = (Hashtable) Dominios.getEstadoCivil();
		Enumeration keys = estadoCivils.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) estadoCivils.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/**
	 * Método responsável por selecionar o objeto CidadeVO em Naturalidade
	 * <code>Cidade/code>.
	 */
	public void selecionarNaturalidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("naturalidade");
		getPessoaVO().setNaturalidade(obj);
		getListaConsultaNaturalidade().clear();
		this.setValorConsultaNaturalidade("");
		this.setCampoConsultaNaturalidade("");
	}

	/**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de
	 * Cidade para Naturalidade <code>Cidade/code>.
	 */
	public List getTipoConsultaNaturalidade() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * @return the listaConsultaNaturalidade
	 */
	public List getListaConsultaNaturalidade() {
		if (listaConsultaNaturalidade == null) {
			listaConsultaNaturalidade = new ArrayList(0);
		}
		return listaConsultaNaturalidade;
	}

	/**
	 * @param listaConsultaNaturalidade
	 *            the listaConsultaNaturalidade to set
	 */
	public void setListaConsultaNaturalidade(List listaConsultaNaturalidade) {
		this.listaConsultaNaturalidade = listaConsultaNaturalidade;
	}

	/**
	 * @return the campoConsultaNaturalidade
	 */
	public String getCampoConsultaNaturalidade() {
		if (campoConsultaNaturalidade == null) {
			campoConsultaNaturalidade = "";
		}
		return campoConsultaNaturalidade;
	}

	/**
	 * @param campoConsultaNaturalidade
	 *            the campoConsultaNaturalidade to set
	 */
	public void setCampoConsultaNaturalidade(String campoConsultaNaturalidade) {
		this.campoConsultaNaturalidade = campoConsultaNaturalidade;
	}

	/**
	 * @return the valorConsultaNaturalidade
	 */
	public String getValorConsultaNaturalidade() {
		if (valorConsultaNaturalidade == null) {
			valorConsultaNaturalidade = "";
		}
		return valorConsultaNaturalidade;
	}

	/**
	 * @param valorConsultaNaturalidade
	 *            the valorConsultaNaturalidade to set
	 */
	public void setValorConsultaNaturalidade(String valorConsultaNaturalidade) {
		this.valorConsultaNaturalidade = valorConsultaNaturalidade;
	}

	/**
	 * Método responsável por consultar Naturalidade <code>Cidade/code>.
	 * Buscando todos os objetos correspondentes a entidade
	 * <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void consultarNaturalidade() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaNaturalidade().equals("codigo")) {
				if (getValorConsultaNaturalidade().equals("")) {
					setValorConsultaNaturalidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaNaturalidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaNaturalidade().equals("nome")) {
				if (getValorConsultaNaturalidade().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaNaturalidade(), false, getUsuarioLogado());
			}

			setListaConsultaNaturalidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaNaturalidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public String getModalPreInscricao() {
		if (modalPreInscricao == null) {
			modalPreInscricao = "";
		}
		return modalPreInscricao;
	}

	public void setModalPreInscricao(String modalPreInscricao) {
		this.modalPreInscricao = modalPreInscricao;
	}

//	public void consultaProspectPessoaCPF() {
//		if (!getPessoaVO().getCPF().equalsIgnoreCase("")) {
//			getProspectVO().setCpf(getPessoaVO().getCPF());
//			getProspectVO().setTipoProspect(TipoProspectEnum.FISICO);
//			try {
//				ProspectsVO pro = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(getProspectVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
//				if (pro.getCodigo().intValue() != 0) {
//					this.setEncontrouProspectAluno(Boolean.TRUE);
//					getFacadeFactory().getProspectsFacade().carregarDados(pro, null);
//					setProspectVO(pro);
//					getPessoaVO().setCodProspect(pro.getCodigo());
//					getPessoaVO().setNovoObj(Boolean.FALSE);
//					getPessoaVO().setCPF(pro.getCpf());
//					getPessoaVO().setNome(pro.getNome());
//					getPessoaVO().setSexo(pro.getSexo());
//					getPessoaVO().setEmail(pro.getEmailPrincipal());
//					getPessoaVO().setTelefoneRes(pro.getTelefoneResidencial());
//					getPessoaVO().setCelular(pro.getCelular());
//					getPessoaVO().setTelefoneComer(pro.getTelefoneComercial());
//					getPessoaVO().setDataNasc(pro.getDataNascimento());
//					getPessoaVO().setCEP(pro.getCEP());
//					getPessoaVO().setEndereco(pro.getEndereco());
//					getPessoaVO().setSetor(pro.getSetor());
//					getPessoaVO().setCidade(pro.getCidade());
//					getPessoaVO().setRG(pro.getRg());
//					getPessoaVO().setDataEmissaoRG(pro.getDataExpedicao());
//					getPessoaVO().setOrgaoEmissor(pro.getOrgaoEmissor());
//					getPessoaVO().setEstadoEmissaoRG(pro.getEstadoEmissor());
//					getPessoaVO().setEstadoCivil(pro.getEstadoCivil());
//					getPessoaVO().setNaturalidade(pro.getNaturalidade());
//				} else {
//					PessoaVO p = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(getPessoaVO().getCPF(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
//					if (p.getCodigo().intValue() != 0) {
//						this.setEncontrouProspectAluno(Boolean.TRUE);
//						setPessoaVO(p);
//					} else {
//						this.setEncontrouProspectAluno(Boolean.FALSE);
//						limparDadosReferenteTipoProspect();
//					}
//				}
//			} catch (Exception e) {
//				e.getMessage();
//			}
//		}
//	}

//	public void limparDadosReferenteTipoProspect() throws Exception {
//		getPessoaVO().setCodProspect(0);
//		getPessoaVO().setNome("");
//		getPessoaVO().setCEP("");
//		getPessoaVO().setEndereco("");
//		getPessoaVO().setSetor("");
//		getPessoaVO().setCidade(new CidadeVO());
//		if (!getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, getUsuarioLogado()).getOcultarCPFPreInscricao()) {
//			getPessoaVO().setEmail("");
//		} else {
//			getPessoaVO().setCPF("");
//		}
//		getProspectVO().setEmailSecundario("");
//		getPessoaVO().setCelular("");
//		getPessoaVO().setTelefoneComer("");
//		getPessoaVO().setTelefoneRecado("");
//		getPessoaVO().setTelefoneRes("");
//		getPessoaVO().setSexo("");
//		getPessoaVO().setRG("");
//		getPessoaVO().setDataEmissaoRG(new Date());
//		getPessoaVO().setEstadoEmissaoRG("");
//		getPessoaVO().setOrgaoEmissor("");
//		getPessoaVO().setDataNasc(new Date());
//	}

	

	/**
	 * Método responsável por consultar Cidade <code>Cidade/code>.
	 * Buscando todos os objetos correspondentes a entidade
	 * <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void consultarCidade() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("nome")) {
				if (getValorConsultaCidade().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
			}

			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	/**
	 * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
	 */
	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
		getPessoaVO().setCidade(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

	/**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de
	 * Cidade <code>Cidade/code>.
	 */
	public List getTipoConsultaCidade() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public String getCampoConsultaCidade() {
		if (campoConsultaCidade == null) {
			campoConsultaCidade = "";
		}
		return campoConsultaCidade;
	}

	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	public String getValorConsultaCidade() {
		if (valorConsultaCidade == null) {
			valorConsultaCidade = "";
		}
		return valorConsultaCidade;
	}

	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	public List getListaConsultaCidade() {
		if (listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<CidadeVO>();
		}
		return listaConsultaCidade;
	}

	public void setListaConsultaCidade(List listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}

	public void prepararLiberarMatriculaAcimaNrMaximoVagasDisciplinaComposta() {
		try {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaCompostaItens");
			// getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(),
			// getUsuarioLogado());

			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().atualizarNrAlunosMatriculadosTurmaDisciplina(getMatriculaPeriodoVO(), obj, obj.getDisciplina(), obj.getAno(), obj.getSemestre(), true, false);

			setMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getLiberarAtivacaoUltimaMatriculaPeriodo() {
		if (liberarAtivacaoUltimaMatriculaPeriodo == null) {
			liberarAtivacaoUltimaMatriculaPeriodo = Boolean.FALSE;
		}
		return liberarAtivacaoUltimaMatriculaPeriodo;
	}

	public void setLiberarAtivacaoUltimaMatriculaPeriodo(Boolean liberarAtivacaoUltimaMatriculaPeriodo) {
		this.liberarAtivacaoUltimaMatriculaPeriodo = liberarAtivacaoUltimaMatriculaPeriodo;
	}

	public void verificarApresentarBotaoAtivarUltimaMatriculaPeriodo() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirAtivarSituacaoUltimoPeriodoAluno", getUsuarioLogado());
			setLiberarAtivacaoUltimaMatriculaPeriodo(Boolean.TRUE);
		} catch (Exception e) {
			setLiberarAtivacaoUltimaMatriculaPeriodo(Boolean.FALSE);
		}
	}

	public void realizarVerificacaoUsuarioAtivarUltimaMatriculaPeriodo() throws Exception {
		try {
			UsuarioVO usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.getUsernameAtivarUltimaMatriculaPeriodo(), this.getSenhaAtivarUltimaMatriculaPeriodo(), true, Uteis.NIVELMONTARDADOS_TODOS);
			verificarPermissaoUsuarioAtivarSituacaoUltimaMatriculaPeriodoAluno(usuarioVerif, "PermitirAtivarSituacaoUltimoPeriodoAluno");
			this.realizarAtivacaoUltimaMatriculaPeriodo(usuarioVerif);
			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA_PERIODO, getMatriculaPeriodoVO().getCodigo().toString(), OperacaoFuncionalidadeEnum.ATIVAR_SITUACAO_ULTIMA_MATRICULAPERIODO_ALUNO, usuarioVerif, ""));
			setListaConsulta(null);
			this.consultar();
			setMensagemID("msg_ConfirmacaoAtivarUltimaMatriculaPeriodoAluno");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setMatriculaPeriodoVO(null);
			setUsernameAtivarUltimaMatriculaPeriodo("");
			setSenhaAtivarUltimaMatriculaPeriodo("");
		}
	}

	public  void verificarPermissaoUsuarioAtivarSituacaoUltimaMatriculaPeriodoAluno(UsuarioVO usuario, String nomeEntidade) throws Exception {
		getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public String getUsernameAtivarUltimaMatriculaPeriodo() {
		if (usernameAtivarUltimaMatriculaPeriodo == null) {
			usernameAtivarUltimaMatriculaPeriodo = "";
		}
		return usernameAtivarUltimaMatriculaPeriodo;
	}

	public void setUsernameAtivarUltimaMatriculaPeriodo(String usernameAtivarUltimaMatriculaPeriodo) {
		this.usernameAtivarUltimaMatriculaPeriodo = usernameAtivarUltimaMatriculaPeriodo;
	}

	public String getSenhaAtivarUltimaMatriculaPeriodo() {
		if (senhaAtivarUltimaMatriculaPeriodo == null) {
			senhaAtivarUltimaMatriculaPeriodo = "";
		}
		return senhaAtivarUltimaMatriculaPeriodo;
	}

	public void setSenhaAtivarUltimaMatriculaPeriodo(String senhaAtivarUltimaMatriculaPeriodo) {
		this.senhaAtivarUltimaMatriculaPeriodo = senhaAtivarUltimaMatriculaPeriodo;
	}

	public void realizarAtivacaoUltimaMatriculaPeriodo(UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getMatriculaPeriodoFacade().executarAtivacaoUltimaMatriculaPeriodo(getMatriculaPeriodoVO(), usuarioVO);
	}

	public void carregarDadosMatriculaPeriodoAlterarUltimaSituacao() {
		matriculaPeriodoVO = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
	}

	public void loginComSimulacaoAcessoVisaoAluno() {
		MatriculaPeriodoVO matriculaPeriodoVO = null;
		UsuarioVO usuarioVO = null;
		SimularAcessoAlunoVO simulacao = new SimularAcessoAlunoVO();
		try {
			matriculaPeriodoVO = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
			usuarioVO = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuarioSimulacaoVisaoAluno(matriculaPeriodoVO.getMatriculaVO().getAluno().getCodigo(), true);
			simulacao.setDataSimulacao(new Date());
			simulacao.setResponsavelSimulacaoAluno(new UsuarioVO());
			simulacao.getResponsavelSimulacaoAluno().setCodigo(getUsuarioLogado().getCodigo());
			simulacao.getResponsavelSimulacaoAluno().setUsername(getUsuarioLogado().getUsername());
			simulacao.getResponsavelSimulacaoAluno().setNome(getUsuarioLogado().getNome());
			simulacao.getResponsavelSimulacaoAluno().setSenha(getUsuarioLogado().getSenha());
			simulacao.getResponsavelSimulacaoAluno().setTipoUsuario(getUsuarioLogado().getTipoUsuario());
			simulacao.getResponsavelSimulacaoAluno().setUnidadeEnsinoLogado(getUnidadeEnsinoLogadoClone());

			LoginControle login = (LoginControle) context().getExternalContext().getSessionMap().get("LoginControle");
			simulacao.setOpcaoLogin(login.getOpcao());

			String retorno = executarLogin(usuarioVO.getUsername(), usuarioVO.getSenha(), "");
			if (!Uteis.isAtributoPreenchido(retorno)) {
				throw new Exception("Foi encontrada uma irregularidade em sua matrícula. Entre em contato com o departamento pedagógico.");
			}
			simulacao.setUsuarioSimulado(getUsuarioLogadoClone());
			getFacadeFactory().getSimularAcessoAlunoFacade().incluir(simulacao, false, getUsuarioLogado());
			getUsuarioLogado().setSimularAcessoAluno(simulacao);
			alunoSimulacaoValido = true;
		} catch (Exception e) {
			try {
				if (Uteis.isAtributoPreenchido(simulacao.getResponsavelSimulacaoAluno())) {
					executarLogin(simulacao.getResponsavelSimulacaoAluno().getUsername(), simulacao.getResponsavelSimulacaoAluno().getSenha(), simulacao.getResponsavelSimulacaoAluno().getTipoUsuario());
				}
			} catch (Exception e2) {
				setMensagemDetalhada("msg_erro", e2.getMessage(), Uteis.ERRO);
			}
			alunoSimulacaoValido = false;
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	public String executarLogin(String userName, String senha, String tipoUsuarioSimulacaoAcesso) throws Exception {
		LoginControle loginControle = (LoginControle) getControlador("LoginControle");
		inativarUsuarioControleAtividadesUsuarioVO(getUsuarioLogadoClone());
		Uteis.removerObjetoMemoria(context().getExternalContext().getSessionMap().remove("usuarioLogado"));
		loginControle.setUsuario(new UsuarioVO());
		loginControle.getUsuario().setPerfilAcesso(new PerfilAcessoVO());
		loginControle.setUsername(userName);
		loginControle.setSenha(senha);
		if (tipoUsuarioSimulacaoAcesso.equals("DM")) {
			String retorno = loginControle.loginSistema(true, false);
			return retorno.isEmpty() ? loginControle.logarDiretamenteComoDiretorMultiCampus() : retorno;
		} else if (tipoUsuarioSimulacaoAcesso.equals("FU")) {
			String retorno = loginControle.loginSistema(true, false);
			return retorno.isEmpty() ? loginControle.logarDiretamenteComoFuncionario() : retorno;
		}
		String retorno = loginControle.loginSistema(true, false);
		return retorno.isEmpty() ? loginControle.logarDiretamenteComoAluno(true) : retorno;
	}

	public boolean isAlunoSimulacaoValido() {
		return alunoSimulacaoValido;
	}

	public void setAlunoSimulacaoValido(boolean alunoSimulacaoValido) {
		this.alunoSimulacaoValido = alunoSimulacaoValido;
	}

	public String getExecutarNavegacaoSimulacaoVisaoAluno() {
		return isAlunoSimulacaoValido() ? !getLoginControle().getOpcao().contains("telaInicialVisaoAluno") ? "removerPopup('"+getLoginControle().getFinalizarPopups()+"');simularAcessoFichaAlunoAvaliacaoInst();" : "removerPopup('"+getLoginControle().getFinalizarPopups()+"');simularAcessoFichaAluno();window.close();" : "";
	}

	public Boolean getPermiteIncluirDisciplinaOptativa() {
		return ((getPermitirIncluirDisciplinaNaVisaoAluno() && getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) || !getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) && getLoginControle().getPermissaoAcessoMenuVO().getPermitirIncluirDisciplinaOptativa() 
				&& ((getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular() 
						&& getMatriculaVO().getCurso().getConfiguracaoAcademico().getPermitirAlunoRegularIncluirDisciplinaGrupoOptativa()) 
						|| (!getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular()));
	}

	public boolean isAbrirPanelAlterarSubturmaPraticaTeoricaFilhaComposicao() {
		return abrirPanelAlterarSubturmaPraticaTeoricaFilhaComposicao;
	}

	public void setAbrirPanelAlterarSubturmaPraticaTeoricaFilhaComposicao(boolean abrirPanelAlterarSubturmaPraticaTeoricaFilhaComposicao) {
		this.abrirPanelAlterarSubturmaPraticaTeoricaFilhaComposicao = abrirPanelAlterarSubturmaPraticaTeoricaFilhaComposicao;
	}

	public void executarMontagemListaSelectItemSubturmaPraticaTeoricaFilhaComposicaoTransferir() {
		try {
			setMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO((MatriculaPeriodoTurmaDisciplinaVO) getRequestMap().get("disciplinaCompostaItens"));
			List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultarPorSubTurma(getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO().getTurma(), getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO().getDisciplina().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado(), TipoSubTurmaEnum.PRATICA, getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO().getSemestre(), "AB");
			setListaSelectItemSubturmaPratica(new ArrayList<SelectItem>(0));
			getListaSelectItemSubturmaPratica().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : turmaVOs) {
				if (turmaVO.getSituacao().equals("AB") && !turmaVO.getCodigo().equals(getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO().getTurmaPratica().getCodigo()))
					getListaSelectItemSubturmaPratica().add(new SelectItem(turmaVO.getCodigo(), turmaVO.getIdentificadorTurma()));
			}
			Collections.sort(getListaSelectItemSubturmaPratica(), new SelectItemOrdemValor());

			turmaVOs = getFacadeFactory().getTurmaFacade().consultarPorSubTurma(getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO().getTurma(), getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO().getDisciplina().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado(), TipoSubTurmaEnum.TEORICA, getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO().getSemestre(), "AB");
			setListaSelectItemSubturmaTeorica(new ArrayList<SelectItem>(0));
			getListaSelectItemSubturmaTeorica().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : turmaVOs) {
				if (turmaVO.getSituacao().equals("AB") && !turmaVO.getCodigo().equals(getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO().getTurmaTeorica().getCodigo()))
					getListaSelectItemSubturmaTeorica().add(new SelectItem(turmaVO.getCodigo(), turmaVO.getIdentificadorTurma()));
			}
			Collections.sort(getListaSelectItemSubturmaTeorica(), new SelectItemOrdemValor());
			setAbrirPanelAlterarSubturmaPraticaTeoricaFilhaComposicao(true);
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setAbrirPanelAlterarSubturmaPraticaTeoricaFilhaComposicao(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarVerificacaoQtdeMaximaAlunosTurmaPraticaTeoricaFilaComposicaoChoqueHorarioRegistroAulaTransferir() {
		boolean sucessoSubturmaTeorica = true;
		boolean sucessoSubturmaPratica = true;
		try {
			limparMensagem();
			ConsistirException ce = new ConsistirException();
			getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO().setAlterandoSubturmaPraticaTeorica(false);
			sucessoSubturmaTeorica = getFacadeFactory().getMatriculaPeriodoFacade().executarVerificacaoQtdeMaximaAlunosTurmaTeoricaChoqueHorarioRegistroAulaTransferir(getMatriculaPeriodoVO(), getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO(), getSubturmaTeoricaDestino(), getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno(), getUsuarioLogado(), ce);
			sucessoSubturmaPratica = getFacadeFactory().getMatriculaPeriodoFacade().executarVerificacaoQtdeMaximaAlunosTurmaPraticaChoqueHorarioRegistroAulaTransferir(getMatriculaPeriodoVO(), getMatriculaPeriodoTurmaDisciplinaFazParteComposicaoSelecionadaVO(), getSubturmaPraticaDestino(), getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais(), getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno(), getUsuarioLogado(), ce);
			if (Uteis.isAtributoPreenchido(ce.getListaMensagemErro()))
				throw ce;
			setAbrirPanelAlterarSubturmaPraticaTeoricaFilhaComposicao(false);
			setMensagemID("msg_dados_consultados");
		} catch (ConsistirException e) {
			if (!sucessoSubturmaPratica)
				setSubturmaPraticaDestino(new TurmaVO());
			if (!sucessoSubturmaTeorica)
				setSubturmaTeoricaDestino(new TurmaVO());
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarValidacaoPermiteMatriculaOnlineAposAlteracaoDados() {
		if (Uteis.isAtributoPreenchido(getBanner())) {
			if (Uteis.isAtributoPreenchido(getMatriculaVO().getUnidadeEnsino().getCodigo()) 
					&& Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getProcessoMatriculaVO().getCodigo()) 
					&& Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo()) 
					&& Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getTurma().getCodigo()) 
//					&& (  (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo())
//						  && Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo()))
//						|| getMatriculaPeriodoVO().getFinanceiroManual())
				) {
				setPermitirMatriculaOnline(true);
			} else {
				setPermitirMatriculaOnline(false);
			}
		}
	}

	
	public TransferenciaMatrizCurricularMatriculaVO getTransferenciaMatrizCurricularMatriculaVO() {
		return transferenciaMatrizCurricularMatriculaVO;
	}

	public void setTransferenciaMatrizCurricularMatriculaVO(TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatriculaVO) {
		this.transferenciaMatrizCurricularMatriculaVO = transferenciaMatrizCurricularMatriculaVO;
	}

	public void verificarExistemDisciplinasCursandoPorCorrespodenciaQuePrecisamSerMigradasNovaTurma() throws Exception {
		if (verificarExistemDisciplinasPendentesEliminarCorrespodencia()) {
			throw new Exception("Ainda existem disciplinas que estão sendo cursadas por correspondência que precisam ser migradas para uma turma da nova matriz.");
		}
	}

	public Boolean getVerificarPodeRetornarTransferenciaMatricula() {
		if (getMatriculaPeriodoVO().getTransferenciaMatrizCurricularMatriculaVO() != null) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean verificarExistemDisciplinasPendentesEliminarCorrespodencia() {
		if ((getMatriculaPeriodoVO().getTransferenciaMatrizCurricularMatriculaVO() != null) && (getMatriculaPeriodoVO().getTransferenciaMatrizCurricularMatriculaVO().getListaDisciplinasPorCorrespondencia().size() > 0)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public String getPaginaRetornoAdicionarDisciplinaCursandoCorrespodencia() {
		if (paginaRetornoAdicionarDisciplinaCursandoCorrespodencia == null) {
			paginaRetornoAdicionarDisciplinaCursandoCorrespodencia = "";
		}
		return paginaRetornoAdicionarDisciplinaCursandoCorrespodencia;
	}

	public void setPaginaRetornoAdicionarDisciplinaCursandoCorrespodencia(String paginaRetornoAdicionarDisciplinaCursandoCorrespodencia) {
		this.paginaRetornoAdicionarDisciplinaCursandoCorrespodencia = paginaRetornoAdicionarDisciplinaCursandoCorrespodencia;
	}

	public void prepararAdicionarDisciplinaMatriculaPeriodoCursandoPorCorrespodencia() {
		try {
			limparMensagem();
			// prepararAdicionarDisciplinaMatriculaPeriodo();
			MatriculaPeriodoTurmaDisciplinaVO objSubstituir = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoTurmaDisciplinaMigrar");
			GradeDisciplinaVO gradeDisciplinaVOCorrespodente = getMatriculaVO().getGradeCurricularAtual().consultarObjGradeDisciplinaVOPorCodigoDisciplina(objSubstituir.getDisciplina().getCodigo());
			if (gradeDisciplinaVOCorrespodente != null) {
				// se encontramos gradeDisciplina correspondente entao vamos
				// direcionar o aluno para adiciona-la as disciplinas da
				// matriculaPeriodo
				setPaginaRetornoAdicionarDisciplinaCursandoCorrespodencia("PF('panelDisciplinasPorCorrespondencia').hide(); PF('panelIncluirDisciplinas').show();");

				setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina(gradeDisciplinaVOCorrespodente.getDisciplina());
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurma(new TurmaVO());
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeDisciplinaVO(gradeDisciplinaVOCorrespodente);
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaReferenteAUmGrupoOptativa(Boolean.FALSE);
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setModalidadeDisciplina(gradeDisciplinaVOCorrespodente.getModalidadeDisciplina());
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setIgnorarValidacaoDisciplinaSendoCursadaAluno(Boolean.TRUE);
				setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
				setCodigoMapaEquivalenciaDisciplinaCursar(0);
				setMensagemID("msg_informe_dados"); // tem que ficar antes, pois
													// o montar da turma pode
													// disparar um excption
													// importante que deve
													// prevalecar
				montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
//				if (getHorarioAlunoTurnoVOs().isEmpty()) {
//					prepararHorarioAulaAluno();
//				} else {
//					getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
//				}
			} else {
				GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaVOCorrespodente = getMatriculaVO().getGradeCurricularAtual().obterGradeCurricularGrupoOptativaCorrespondente(objSubstituir.getDisciplina().getCodigo(), objSubstituir.getCargaHoraria());
				if (gradeCurricularGrupoOptativaVOCorrespodente != null) {
					// se encontramos gradeDisciplina correspondente entao vamos
					// direcionar o aluno para adiciona-la as disciplinas da
					// matriculaPeriodo
					setPaginaRetornoAdicionarDisciplinaCursandoCorrespodencia("PF('panelDisciplinasPorCorrespondencia').hide(); PF('panelIncluirDisciplinas').show();");

					setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina(gradeCurricularGrupoOptativaVOCorrespodente.getDisciplina());
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurma(new TurmaVO());
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeDisciplinaVO(new GradeDisciplinaVO());
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeCurricularGrupoOptativaDisciplinaVO(gradeCurricularGrupoOptativaVOCorrespodente);
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaReferenteAUmGrupoOptativa(Boolean.TRUE);
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setModalidadeDisciplina(gradeCurricularGrupoOptativaVOCorrespodente.getModalidadeDisciplina());
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setIgnorarValidacaoDisciplinaSendoCursadaAluno(Boolean.TRUE);
					setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
					setCodigoMapaEquivalenciaDisciplinaCursar(0);
					setMensagemID("msg_informe_dados"); // tem que ficar antes,
														// pois o montar da
														// turma pode disparar
														// um excption
														// importante que deve
														// prevalecar
					montarListaSelectItemTurmaAdicionar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
//					if (getHorarioAlunoTurnoVOs().isEmpty()) {
//						prepararHorarioAulaAluno();
//					} else {
//						getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
//					}

				} else {
					setPaginaRetornoAdicionarDisciplinaCursandoCorrespodencia("");
					throw new Exception("Não foi encontrada na Matriz Curricular atual do aluno uma disciplina com mesmo código e/ou carga horária. A mesma pode ser removida e incluída por meio de um mapa de equivalência para a disciplina equivalente na matriz atual");
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerMatriculaPeriodoTurmaDisciplinaListaPendenciasDisciplinasPorCorrespondencia() {
		try {
			MatriculaPeriodoTurmaDisciplinaVO objRemover = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoTurmaDisciplinaMigrar");
			int k = getMatriculaPeriodoVO().getTransferenciaMatrizCurricularMatriculaVO().getListaDisciplinasPorCorrespondencia().size() - 1;
			while (k >= 0) {
				MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = getMatriculaPeriodoVO().getTransferenciaMatrizCurricularMatriculaVO().getListaDisciplinasPorCorrespondencia().get(k);
				if (matriculaPeriodoTurmaDisciplinaVO.getCodigo().equals(objRemover.getCodigo())) {
					inicializarDadosHistoricoASerExcluidoAposRemoverMatriculaPeriodoTurmaDisciplinaRemovida(getMatriculaPeriodoVO(), matriculaPeriodoTurmaDisciplinaVO);
					getMatriculaPeriodoVO().getTransferenciaMatrizCurricularMatriculaVO().getListaDisciplinasPorCorrespondencia().remove(k);
					break;
				}
				k = k - 1;
			}
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarDadosHistoricoASerExcluidoAposRemoverMatriculaPeriodoTurmaDisciplinaRemovida(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaRemoverVO) {
		matriculaPeriodoVO.getTransferenciaMatrizCurricularMatriculaVO().getListaExcluirDeAcordoMatriculaPeriodoTurmaDisciplinaRemovidaUsuarioVOs().add(matriculaPeriodoTurmaDisciplinaRemoverVO);
	}

	public String retornarTransferenciaMatrizCurricular() {
		context().getExternalContext().getSessionMap().put("retornarTransferenciaMatricula", getMatriculaPeriodoVO().getTransferenciaMatrizCurricularMatriculaVO());
		removerControleMemoriaFlash("TransferenciaMatrizCurricularControle");
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaMatrizCurricularForm.xhtml");
	}

	private Boolean telaConsulta;

	public Boolean getTelaConsulta() {
		if (telaConsulta == null) {
			telaConsulta = false;
		}
		return telaConsulta;
	}

	
	public void setTelaConsulta(Boolean telaConsulta) {
		this.telaConsulta = telaConsulta;
	}

	public void editarMatriculaPeriodoParaExclusao() {
		setTelaConsulta(true);
		setMatriculaPeriodoVO((MatriculaPeriodoVO) getRequestMap().get("matriculaPeriodoItens"));
		realizarVerificacaoRestricoesParaExclusaoMatriculaPeriodo();
	}

	public void realizarVerificacaoRestricoesParaExclusaoMatriculaPeriodo() {
		setOncompleteModal("");
		try {
			limparMensagem();
			getFacadeFactory().getMatriculaPeriodoFacade().realizarVerificacaoRestricoesParaExclusaoMatriculaPeriodo(getMatriculaPeriodoVO(), getUsuarioLogado());
			setOncompleteModal("PF('panelConfirmarExclusao').show()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

//	public void imprimirHorarioAluno() {
//		prepararHorarioAulaAluno();
//		List<CronogramaDeAulasRelVO> listaObjetos = new ArrayList<CronogramaDeAulasRelVO>(0);
//		try {
//			List<ProgramacaoTutoriaOnlineVO> programacaoTutoriaOnlineVOs = getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().consultarPorMatriculaPeriodoTurmaDisciplina(getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), false);
//			listaObjetos = getFacadeFactory().getHorarioAlunoFacade().realizarCriacaoHorarioAlunoModeloMatricula(getHorarioAlunoTurnoVOs());
//			if (!listaObjetos.isEmpty() || Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVOs)) {
//				getSuperParametroRelVO().setNomeDesignIreport(HorarioAluno.getDesignIReportRelatorio());
//				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
//				getSuperParametroRelVO().setSubReport_Dir(HorarioAluno.getCaminhoBaseRelatorio());
//				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
//				getSuperParametroRelVO().setTituloRelatorio("Horário do Aluno");
//				Ordenacao.ordenarLista(listaObjetos, "modulo");
//				getSuperParametroRelVO().setListaObjetos(listaObjetos);
//				getSuperParametroRelVO().setCaminhoBaseRelatorio(HorarioAluno.getCaminhoBaseRelatorio());
//				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
//				getSuperParametroRelVO().adicionarParametro("ocultarData", true);
//				if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
//					getSuperParametroRelVO().setAluno(getMatriculaVO().getAluno().getNome());
//					getSuperParametroRelVO().setUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getNome());
//					getSuperParametroRelVO().setCurso(getMatriculaVO().getCurso().getNome());
//					getSuperParametroRelVO().adicionarParametro("dataMatricula", Uteis.getData(getFacadeFactory().getMatriculaPeriodoFacade().consultarDataMatriculaPeriodoPorMatriculaAnoSemestre(getMatriculaVO().getMatricula(), getAno(), getSemestre())));
//				} else if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
//					getSuperParametroRelVO().setAluno(getVisaoAlunoControle().getMatricula().getAluno().getNome());
//					getSuperParametroRelVO().setUnidadeEnsino(getVisaoAlunoControle().getMatricula().getUnidadeEnsino().getNome());
//					getSuperParametroRelVO().setCurso(getVisaoAlunoControle().getMatricula().getCurso().getNome());
//					getSuperParametroRelVO().adicionarParametro("dataMatricula", Uteis.getData(getFacadeFactory().getMatriculaPeriodoFacade().consultarDataMatriculaPeriodoPorMatriculaAnoSemestre(getVisaoAlunoControle().getMatricula().getMatricula(), getAno(), getSemestre())));
//				}
//				getSuperParametroRelVO().adicionarParametro("listaProgramacaoTutoriaOnline", programacaoTutoriaOnlineVOs);
//				getSuperParametroRelVO().adicionarParametro("isListaProgramacaoTutoriaOnlineVazia", !Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVOs));
//				getSuperParametroRelVO().adicionarParametro("isListaObjetosVazia", listaObjetos.isEmpty());
//				getSuperParametroRelVO().adicionarParametro("periodicidadeMatriz", getMatriculaVO().getCurso().getPeriodicidade());
//				getSuperParametroRelVO().adicionarParametro("anoAtualMatricula", getMatriculaPeriodoVO().getAno());
//				getSuperParametroRelVO().adicionarParametro("semestreAtualMatricula", getMatriculaPeriodoVO().getSemestre());
//				getSuperParametroRelVO().adicionarParametro("nivelEducacional", getMatriculaVO().getCurso().getNivelEducacional());
//				realizarImpressaoRelatorio();
//				setMensagemID("msg_relatorio_ok");
//			} else {
//				setMensagemID("msg_relatorio_sem_dados");
//			}
//		} catch (Exception ex) {
//			setMensagemDetalhada("msg_erro", ex.getMessage());
//		} finally {
//			Uteis.liberarListaMemoria(listaObjetos);
//		}
//	}

	
	public void realizarMatriculaVindoDaTelaDaTransferenciaInterna() {
		if (context().getExternalContext().getSessionMap().get("TransferenciaInternaVO") != null && context().getExternalContext().getSessionMap().get("TransferenciaInternaVO") instanceof TransferenciaEntradaVO) {
			try {
				TransferenciaEntradaVO transferenciaEntradaVO = (TransferenciaEntradaVO) context().getExternalContext().getSessionMap().get("TransferenciaInternaVO");
				context().getExternalContext().getSessionMap().remove("TransferenciaInternaVO");							
				inicializarManagedBeanIniciarProcessamento();
				getMatriculaVO().setGuiaAba("DadosBasicos");
//				getFacadeFactory().getTransferenciaEntradaFacade().montarListaDisciplinasAproveitadas(transferenciaEntradaVO, getUsuarioLogado());				
				getMatriculaVO().setTransferenciaEntrada(transferenciaEntradaVO);
				getMatriculaVO().setAluno(transferenciaEntradaVO.getPessoa());
				getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(transferenciaEntradaVO.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
				getMatriculaVO().setTurno(transferenciaEntradaVO.getTurno());
				getMatriculaVO().setUnidadeEnsino(transferenciaEntradaVO.getUnidadeEnsino());
				getMatriculaVO().setFormaIngresso(FormaIngresso.TRANSFERENCIA_INTERNA.getValor());
				getMatriculaVO().setDiaSemanaAula(transferenciaEntradaVO.getMatricula().getDiaSemanaAula());
				getMatriculaPeriodoVO().setGradeCurricular(transferenciaEntradaVO.getGradeCurricular());
				getMatriculaVO().setGradeCurricularAtual(transferenciaEntradaVO.getGradeCurricular());
				getMatriculaPeriodoVO().setPeridoLetivo(transferenciaEntradaVO.getPeridoLetivo());
				setCodigoPeriodoLetivo(transferenciaEntradaVO.getPeridoLetivo().getCodigo());
				setCodigoGradeCurricular(getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
				getMatriculaPeriodoVO().setTurma(transferenciaEntradaVO.getTurma());
				getMatriculaPeriodoVO().setUnidadeEnsinoCurso(transferenciaEntradaVO.getUnidadeEnsinoCurso());
				getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().setCodigo(transferenciaEntradaVO.getUnidadeEnsinoCurso());
				getFacadeFactory().getUnidadeEnsinoCursoFacade().carregarDados(getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO(), getUsuarioLogado());
				getMatriculaPeriodoVO().setTranferenciaEntrada(transferenciaEntradaVO.getCodigo());
				getMatriculaPeriodoVO().setNovoObj(true);
				setRealizandoNovaMatriculaAluno(true);
				setRealizandoNovaMatriculaAlunoPartindoTransferenciaInterna(true);
				getMatriculaVO().getTransferenciaEntrada().setCarregarDisciplinasAproveitadas(true);
				novaMatriculaAluno(true);
				if(!getListaSelectItemProcessoMatricula().isEmpty() && getListaSelectItemProcessoMatricula().size() > 1) {
					for(SelectItem item: (List<SelectItem>) getListaSelectItemProcessoMatricula()) {
						if(!item.getValue().equals(0)) {
							getMatriculaPeriodoVO().setProcessoMatricula((Integer)item.getValue());
							selecionarProcessoMatricula();
							break;
						}
					}
					if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getGradeCurricular())) {
						executarDefinicaoPeriodoLetivoNovaMatriculaAluno();
					}
					selecionarPeriodoLetivo();
				}
				processarDadosPertinentesTurmaSelecionada(false);
				verificarPermissaoUsuarioInformarTipoMatriculaMatricula();
			} catch (Exception e) {
				setMensagemDetalhada(e.getMessage(), "msg_erro");
			}
			
		}
	}

	
//	
//	public void montarListasDescontosProgressivos() {
//		List<DescontoProgressivoVO> descontoProgressivoVOs = new ArrayList<DescontoProgressivoVO>(0);
//		try {
//			montarListaSelectItemDescontoProgressivoParceiro(descontoProgressivoVOs);
//			montarListaSelectItemDescontoProgressivoAluno(descontoProgressivoVOs);
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		} finally {
//			descontoProgressivoVOs = null;
//		}
//	}

//	public void montarListaSelectItemDescontoProgressivoParceiro(List<DescontoProgressivoVO> descontoProgressivoVOs) throws Exception {
//		descontoProgressivoVOs = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNomeAtivos("", false, getUsuarioLogado());
//		setListaSelectItemDescontoProgressivoParceiro(UtilSelectItem.getListaSelectItem(descontoProgressivoVOs, "codigo", "nome"));
//	}
//
//	public void montarListaSelectItemDescontoProgressivoAluno(List<DescontoProgressivoVO> descontoProgressivoVOs) throws Exception {
//		descontoProgressivoVOs = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNomeAtivos("", false, getUsuarioLogado());
//		setListaSelectItemDescontoProgressivoAluno(UtilSelectItem.getListaSelectItem(descontoProgressivoVOs, "codigo", "nome"));
//	}
//
//	public List getListaSelectItemDescontoProgressivoParceiro() {
//		if (listaSelectItemDescontoProgressivoParceiro == null) {
//			listaSelectItemDescontoProgressivoParceiro = new ArrayList(0);
//		}
//		return listaSelectItemDescontoProgressivoParceiro;
//	}
//
//	public void setListaSelectItemDescontoProgressivoParceiro(List listaSelectItemDescontoProgressivoParceiro) {
//		this.listaSelectItemDescontoProgressivoParceiro = listaSelectItemDescontoProgressivoParceiro;
//	}
//
//	public List getListaSelectItemDescontoProgressivoAluno() {
//		if (listaSelectItemDescontoProgressivoAluno == null) {
//			listaSelectItemDescontoProgressivoAluno = new ArrayList(0);
//		}
//		return listaSelectItemDescontoProgressivoAluno;
//	}
//
//	public void setListaSelectItemDescontoProgressivoAluno(List listaSelectItemDescontoProgressivoAluno) {
//		this.listaSelectItemDescontoProgressivoAluno = listaSelectItemDescontoProgressivoAluno;
//	}


	public List getListaSelectItemCentroReceita() {
		if (listaSelectItemCentroReceita == null) {
			listaSelectItemCentroReceita = new ArrayList(0);
		}
		return listaSelectItemCentroReceita;
	}

	/**
	 * @param listaSelectItemCentroReceita
	 *            the listaSelectItemCentroReceita to set
	 */
	public void setListaSelectItemCentroReceita(List listaSelectItemCentroReceita) {
		this.listaSelectItemCentroReceita = listaSelectItemCentroReceita;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>FormaPagamento</code>.
	 */
	

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	

	/**
	 * @return the listaSelectItemFormaPagamento
	 */
	public List getListaSelectItemFormaPagamento() {
		if (listaSelectItemFormaPagamento == null) {
			listaSelectItemFormaPagamento = new ArrayList<>();
		}
		return listaSelectItemFormaPagamento;
	}

	/**
	 * @param listaSelectItemFormaPagamento
	 *            the listaSelectItemFormaPagamento to set
	 */
	public void setListaSelectItemFormaPagamento(List listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo
	 * <code>tipoBolsaCusteadaParceiroParcela</code>
	 */
	public List getListaSelectItemTipoBolsaCusteadaParceiroParcelaConvenio() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoValorConvenios = (Hashtable) Dominios.getTipoValorConvenio();
		Enumeration keys = tipoValorConvenios.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoValorConvenios.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>cobertura</code>
	 */
	public List getListaSelectItemCoberturaConvenio() throws Exception {
		List objs = new ArrayList(0);
		objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoCoberturaConvenio.class);
		return objs;
	}

	public List<SelectItem> getListaSelectItemTipoFinanciamentoEstudantil() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(FinanciamentoEstudantil.class, "valor", "descricao", false);
	}

	
	/**
	 * @return the listaConsultaCentroDespesa
	 */
	public List getListaConsultaCentroDespesa() {
		if (listaConsultaCentroDespesa == null) {
			listaConsultaCentroDespesa = new ArrayList(0);
		}
		return listaConsultaCentroDespesa;
	}

	/**
	 * @param listaConsultaCentroDespesa
	 *            the listaConsultaCentroDespesa to set
	 */
	public void setListaConsultaCentroDespesa(List listaConsultaCentroDespesa) {
		this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
	}

	/**
	 * @return the valorConsultaCentroDespesa
	 */
	public String getValorConsultaCentroDespesa() {
		if (valorConsultaCentroDespesa == null) {
			valorConsultaCentroDespesa = "";
		}
		return valorConsultaCentroDespesa;
	}

	/**
	 * @param valorConsultaCentroDespesa
	 *            the valorConsultaCentroDespesa to set
	 */
	public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
		this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
	}

	/**
	 * @return the campoConsultaCentroDespesa
	 */
	public String getCampoConsultaCentroDespesa() {
		if (campoConsultaCentroDespesa == null) {
			campoConsultaCentroDespesa = "";
		}
		return campoConsultaCentroDespesa;
	}

	/**
	 * @param campoConsultaCentroDespesa
	 *            the campoConsultaCentroDespesa to set
	 */
	public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
		this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
	}

	

	public List getTipoConsultaComboCentroDespesa() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
		return itens;
	}

	
	public String getManterModalConvenioAberto() {
		if (manterModalConvenioAberto == null) {
			manterModalConvenioAberto = "";
		}
		return manterModalConvenioAberto;
	}

	public void setManterModalConvenioAberto(String manterModalConvenioAberto) {
		this.manterModalConvenioAberto = manterModalConvenioAberto;
	}

	public void realizarImpressaoContratoFichaAluno(MatriculaPeriodoVO matriculaPeriodoVO) {
		try {
			setMatriculaVO(matriculaPeriodoVO.getMatriculaVO());
			getMatriculaPeriodoVO().setCodigo(matriculaPeriodoVO.getCodigo());
			imprimirContrato("MA");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove(RenovarMatriculaControle.class.getSimpleName());
		}
	}
	
	public void realizarImpressaoComprovanteMatriculaFichaAluno(MatriculaPeriodoVO matriculaPeriodoVO, Integer tipoLayout) {
		try {
			setMatriculaVO(matriculaPeriodoVO.getMatriculaVO());
			getMatriculaPeriodoVO().setCodigo(matriculaPeriodoVO.getCodigo());
			setTipoLayoutComprovanteMatricula(tipoLayout);
			imprimirComprovanteMatricula();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void realizarMatriculaVindoDaTelaResultadoProcessoSeletivo() throws Exception {
		if (context().getExternalContext().getSessionMap().get("resultadoProcessoSeletivoMatricula") != null && context().getExternalContext().getSessionMap().get("resultadoProcessoSeletivoMatriculaPeriodo") != null) {
			setMatriculaVO((MatriculaVO) context().getExternalContext().getSessionMap().get("resultadoProcessoSeletivoMatricula"));
			setMatriculaPeriodoVO((MatriculaPeriodoVO) context().getExternalContext().getSessionMap().get("resultadoProcessoSeletivoMatriculaPeriodo"));
			context().getExternalContext().getSessionMap().remove("resultadoProcessoSeletivoMatricula");
			context().getExternalContext().getSessionMap().remove("resultadoProcessoSeletivoMatriculaPeriodo");
			setRealizandoNovaMatriculaAluno(true);
			montarListaSelectItemGradeCurricular();
			getMatriculaVO().setMatriculaOnlineProcSeletivo(Boolean.TRUE);
			if (getMatriculaPeriodoVO().getGradeCurricular().getCodigo() > 0) {
				inicializarMatriculaComHistoricoAluno(true);
			}
			getFacadeFactory().getMatriculaPeriodoFacade().obterListaPeriodosLetivosValidosParaRenovacaoMatriculaInicializandoPeriodoLetivoPadrao(getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaVO().getCurso().getConfiguracaoAcademico(), matriculaVO.getMatriculaComHistoricoAlunoVO(), getUsuarioLogado());
			novaMatriculaAluno(false);
		}
	}
	
	public Boolean getLiberarMatriculaTurmaSemVagaSemAula() {
		if(liberarMatriculaTurmaSemVagaSemAula == null){
			liberarMatriculaTurmaSemVagaSemAula = false;
		}
		return liberarMatriculaTurmaSemVagaSemAula;
	}

	public void setLiberarMatriculaTurmaSemVagaSemAula(Boolean liberarMatriculaTurmaSemVagaSemAula) {
		this.liberarMatriculaTurmaSemVagaSemAula = liberarMatriculaTurmaSemVagaSemAula;
	}	
	
	public Boolean getRealizarAdiamentoSuspensaoMatricula() {
		if (realizarAdiamentoSuspensaoMatricula == null) {
			realizarAdiamentoSuspensaoMatricula = false;
		}
		return realizarAdiamentoSuspensaoMatricula;
	}

	public void setRealizarAdiamentoSuspensaoMatricula(Boolean realizarAdiamentoSuspensaoMatricula) {
		this.realizarAdiamentoSuspensaoMatricula = realizarAdiamentoSuspensaoMatricula;
	}
	
	public void inicializarDadosMatriculaCancelamentoBloqueioMatricula() {
		try {
			matriculaPeriodoVO = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
			if (Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
				setMatriculaVO(matriculaPeriodoVO.getMatriculaVO());
			}
			setMatriculaVO(getFacadeFactory().getMatriculaFacade().verificarBloqueioMatricula(matriculaPeriodoVO.getMatriculaVO(), getUsuarioLogado(), null));
			setDataAdiamentoSuspensaoMatricula(UteisData.adicionarDiasEmData(getMatriculaVO().getDataAdiamentoSuspensaoMatricula(), getMatriculaVO().getQtdDiasAdiarBloqueio()));
			getMatriculaVO().setResponsavelCancelamentoSuspensaoMatricula(getUsuarioLogadoClone());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}		
	}
	
	public String getOncompleteCancelamentoModalBloqueioMatricula() {
		if (getSituacaoCancelamentoBloqueioMatricula().equals("CANCELAMENTO_REALIZADO_COM_SUCESSO") || getSituacaoCancelamentoBloqueioMatricula().equals("ADIAMENTO_REALIZADO_COM_SUCESSO")) {
			return "PF('panelCancelarBloquearMatricula').hide()";
		}
		return "";
	}
	
	public String getSituacaoCancelamentoBloqueioMatricula() {
		if (situacaoCancelamentoBloqueioMatricula == null) {
			situacaoCancelamentoBloqueioMatricula = "";
		}
		return situacaoCancelamentoBloqueioMatricula;
	}

	public void setSituacaoCancelamentoBloqueioMatricula(String situacaoCancelamentoBloqueioMatricula) {
		this.situacaoCancelamentoBloqueioMatricula = situacaoCancelamentoBloqueioMatricula;
	}
	
	public boolean isApresentarDataBaseGeracao() {
		if(!getMatriculaVO().getCurso().getIntegral()){
			if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getDataBaseGeracaoParcelas())){
				getMatriculaPeriodoVO().setDataBaseGeracaoParcelas(null);	
			}
			return false;
		}
		return  true;
	}
	
	
	
	public void realizarAdiamentoBloqueioMatricula() {
		try {
			getFacadeFactory().getMatriculaFacade().realizarAdiamentoBloqueioMatricula(getMatriculaVO(), getUsuarioLogado());
			setSituacaoCancelamentoBloqueioMatricula("ADIAMENTO_REALIZADO_COM_SUCESSO");
			setMensagemDetalhada("Adiamento de suspensão de matrícula realizada com sucesso!");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setSituacaoCancelamentoBloqueioMatricula("");
		}
	}

	public Date getDataAdiamentoSuspensaoMatricula() {
		if (dataAdiamentoSuspensaoMatricula == null) {
			dataAdiamentoSuspensaoMatricula = new Date();
		}
		return dataAdiamentoSuspensaoMatricula;
	}

	public void setDataAdiamentoSuspensaoMatricula(Date dataAdiamentoSuspensaoMatricula) {
		this.dataAdiamentoSuspensaoMatricula = dataAdiamentoSuspensaoMatricula;
	}

	public List getListaSelectItemCategoriaPlanoFinanceiroCurso() {
		if(listaSelectItemCategoriaPlanoFinanceiroCurso == null) {
			listaSelectItemCategoriaPlanoFinanceiroCurso = new ArrayList<>();
		}
		return listaSelectItemCategoriaPlanoFinanceiroCurso;
	}

	public void setListaSelectItemCategoriaPlanoFinanceiroCurso(List listaSelectItemCategoriaPlanoFinanceiroCurso) {
		this.listaSelectItemCategoriaPlanoFinanceiroCurso = listaSelectItemCategoriaPlanoFinanceiroCurso;
	}

	
	/**
	 * Vai no FACADE da condicao pagamento do plano financeiro curso e consulta as categorias do Plano Financeiro Curso
	 * 
	 * @return
	 * @throws Exception
	 */
//	public List<String> consultarCategoriaDasCondicoesDePagamentoDoPlanoFinanceiroCurso() throws Exception {
//		return getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarCategoriaDasCondicoesDePagamentoDoPlanoFinanceiroCurso(getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo(), false, getUsuarioLogado());
//	}
//	
	/**
	 * Monta a lista com as categorias do Plano Financeiro do Curso
	 * @throws Exception
	 */
//	public void montarListaSelectItemCategoriaCondicoesPagamentoPlanoFinanceiroCurso() throws Exception {
//		setListaSelectItemCategoriaPlanoFinanceiroCurso(consultarCategoriaDasCondicoesDePagamentoDoPlanoFinanceiroCurso());
//	}

	public List getListaSelectItemCondicoesDePagamentoDoPlanoFinanceiroCurso() {
		if(listaSelectItemCondicoesDePagamentoDoPlanoFinanceiroCurso == null) {
			listaSelectItemCondicoesDePagamentoDoPlanoFinanceiroCurso = new ArrayList<>();
		}
		return listaSelectItemCondicoesDePagamentoDoPlanoFinanceiroCurso;
	}

	public void setListaSelectItemCondicoesDePagamentoDoPlanoFinanceiroCurso(
			List listaSelectItemCondicoesDePagamentoDoPlanoFinanceiroCurso) {
		this.listaSelectItemCondicoesDePagamentoDoPlanoFinanceiroCurso = listaSelectItemCondicoesDePagamentoDoPlanoFinanceiroCurso;
	}

	public Boolean getLiberarAlteracaoCategoriaPlanoFinanceiro() {
		if(liberarAlteracaoCategoriaPlanoFinanceiro == null) {
			liberarAlteracaoCategoriaPlanoFinanceiro = false;
		}
		return liberarAlteracaoCategoriaPlanoFinanceiro;
	}

	public void setLiberarAlteracaoCategoriaPlanoFinanceiro(Boolean liberarAlteracaoCategoriaPlanoFinanceiro) {
		this.liberarAlteracaoCategoriaPlanoFinanceiro = liberarAlteracaoCategoriaPlanoFinanceiro;
	}

	public void setPermiteAlterarPlanoCondicaoFinanceiraCurso(Boolean permiteAlterarPlanoCondicaoFinanceiraCurso) {
		this.permiteAlterarPlanoCondicaoFinanceiraCurso = permiteAlterarPlanoCondicaoFinanceiraCurso;
	}
	
	public void selecionarMapaEquivalenciaDisciplinaCursandoParaVisualizacao() {
		HistoricoVO obj = (HistoricoVO) context().getExternalContext().getRequestMap().get("disciplinasCursandoItens");
		
		Integer numeroAgrupamentoEquivalenciaDisciplina = 0;
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			try {
				numeroAgrupamentoEquivalenciaDisciplina = getFacadeFactory().getHistoricoFacade().consultarNumeroAgrupamentoEquivalenciaDisciplinaPorCodigo(obj.getCodigo());
			} catch (Exception e) {

			}
		}
		if (!obj.getMapaEquivalenciaDisciplina().getCodigo().equals(0)) {
			try {
				obj.setMapaEquivalenciaDisciplina(getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(obj.getMapaEquivalenciaDisciplina().getCodigo(), NivelMontarDados.TODOS));
				MapaEquivalenciaDisciplinaVO mapaVisualizar = obj.getMapaEquivalenciaDisciplina();
				prepararMapaEquivalenciaParaVisualizacao(mapaVisualizar, numeroAgrupamentoEquivalenciaDisciplina);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		
	}	
	
	private Boolean utilizaIntegracaoFinanceira;
	
	

	public void setUtilizaIntegracaoFinanceira(Boolean utilizaIntegracaoFinanceira) {
		this.utilizaIntegracaoFinanceira = utilizaIntegracaoFinanceira;
	}

	
	
	
	
	public void adicionarRegistroFolowUpAlunoAceitouTermoAceite() {
		try {
			adicionarRegistroFolowUpAlunoAceitouTermoAceiteLancandoExcecao();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private Boolean apresentarBotaoLiberarRenovacaoSemAceiteTermo;
	private Boolean permitirRenovacaoSemAceiteTemo;
	private Boolean alunoJaAceitouTermoAceite;
	
	public Boolean getAlunoJaAceitouTermoAceite() {
		return alunoJaAceitouTermoAceite;
	}
	
	public void setAlunoJaAceitouTermoAceite(Boolean alunoJaAceitouTermoAceite) {
		this.alunoJaAceitouTermoAceite = alunoJaAceitouTermoAceite;
	}	

	public Boolean getApresentarBotaoLiberarRenovacaoSemAceiteTermo() {
		if (apresentarBotaoLiberarRenovacaoSemAceiteTermo == null) { 
			apresentarBotaoLiberarRenovacaoSemAceiteTermo = Boolean.FALSE;
		}
		return apresentarBotaoLiberarRenovacaoSemAceiteTermo;
	}

	public Boolean getPermitirRenovacaoSemAceiteTemo() {
		if (permitirRenovacaoSemAceiteTemo == null) { 
			permitirRenovacaoSemAceiteTemo = Boolean.FALSE;
		}
		return permitirRenovacaoSemAceiteTemo;
	}

	public void setPermitirRenovacaoSemAceiteTemo(Boolean permitirRenovacaoSemAceiteTemo) {
		this.permitirRenovacaoSemAceiteTemo = permitirRenovacaoSemAceiteTemo;
	}

	public void setApresentarBotaoLiberarRenovacaoSemAceiteTermo(Boolean apresentarBotaoLiberarRenovacaoSemAceiteTermo) {
		this.apresentarBotaoLiberarRenovacaoSemAceiteTermo = apresentarBotaoLiberarRenovacaoSemAceiteTermo;
	}

	public Boolean verificarAlunoJaConfirmouTermoAceiteVisaoAluno() throws Exception {
		if (alunoJaAceitouTermoAceite == null) {
			alunoJaAceitouTermoAceite = Boolean.FALSE;
			
			
        	String identificadorOrigem = getMatriculaPeriodoVO().getIdentificadorRenovacaoFollowUp();
        	Integer codigoEntidadeOrigem = getMatriculaPeriodoVO().getCodigo();
//        	int prospects = Uteis.isAtributoPreenchido(followUpVO.getProspect().getCodigo()) ? followUpVO.getProspect().getCodigo() : -1;
//        	alunoJaAceitouTermoAceite = getFacadeFactory().getFollowUpFacade().verificarExistenciaInteracaoWorkflowPorProspectsMatricula(followUpVO.getCodigo(), 0, 0, 0, TipoOrigemInteracaoEnum.RENOVACAO_MATRICULA, getMatriculaPeriodoVO().getIdentificadorRenovacaoFollowUp(), getMatriculaPeriodoVO().getCodigo(), prospects, getMatriculaVO().getMatricula());
        }
		return alunoJaAceitouTermoAceite.booleanValue();
	}
	
	
	public void prerararLiberarObrigatoriedadeAluonAceitarTermoAceita() {
		setTipoOperacaoFuncionalidadeLiberar("Liberar Obrigatoriedade Aluno Aceitar TERMO ACEITE DE RENOVAÇÃO DE MATRÍCULA no Portal do Aluno");
	}
	
	public void verificarAlunoAceitouTermoAceiteRenovacaoMatricula() throws Exception {
		if (getMatriculaVO().getCurso().getConfiguracaoAcademico().getObrigarAceiteAlunoTermoParaEditarRenovacao().booleanValue() && !getMatriculaPeriodoVO().getCalouro()) {
			if ((getPermitirRenovacaoSemAceiteTemo()) || (!getMatriculaPeriodoVO().getCodigo().equals(0))) {
				// se entrar aqui é por que o usuario ja informou sua senha e
				// liberou a renovacao. Caso tambem seja uma edicao de uma matriculaperiodo, tambem, 
				// nao temos mais que bloquear, pois a renovacao já foi feita.
				return;
			}
			if (!verificarAlunoJaConfirmouTermoAceiteVisaoAluno()) {
				setApresentarBotaoLiberarRenovacaoSemAceiteTermo(Boolean.TRUE);
				setTipoOperacaoFuncionalidadeLiberar("Liberar Obrigatoriedade Aluno Aceitar TERMO ACEITE DE RENOVAÇÃO DE MATRÍCULA no Portal do Aluno");
				setMensagemDetalhada("msg_erro", "Para renovar / editar renovação o aluno deverá primeiramente aceitar o TERMO DE ACEITE DA RENOVAÇÃO DE MATRÍCULA no portal do aluno.");
				throw new Exception("Para renovar / editar renovação o aluno deverá primeiramente aceitar o TERMO DE ACEITE DA RENOVAÇÃO DE MATRÍCULA no portal do aluno.");
			}
		} else {
			setPermitirRenovacaoSemAceiteTemo(Boolean.TRUE);
			setApresentarBotaoLiberarRenovacaoSemAceiteTermo(Boolean.FALSE);
		}
	}
	
	
	public void selecionarProcessoMatricula() {
		try {
			recuperarDadosProcessoMatriculaGradeCurricularPeriodoLetivoTurma();
			limparMensagem();
			setOncompleteModal("");
			getMatriculaPeriodoVO().setMensagemErro("");			
			if ((matriculaPeriodoVO.getProcessoMatricula() == null) || (matriculaPeriodoVO.getProcessoMatricula().equals(0))) {
				setMensagemID("msg_entre_dados");
				return;
			}
			if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO())) {
				String anoSemestre = getFacadeFactory().getProcessoMatriculaFacade().consultarAnoSemestrePorProcessoMatriculaUnidadeEnsinoCurso(getMatriculaPeriodoVO().getProcessoMatricula(), getMatriculaPeriodoVO().getUnidadeEnsinoCurso());
				if(anoSemestre != null && !(getMatriculaPeriodoVO().getAnoSemestreOriginal()).equals(anoSemestre)) {
					getMatriculaPeriodoVO().setMensagemErro(UteisJSF.internacionalizar("msg_MatriculaPeriodo_trocaAnoSemestre").replace("{0}", anoSemestre).replace("{1}", (getMatriculaPeriodoVO().getAnoSemestreOriginal())).replace("{2}", (getMatriculaPeriodoVO().getAnoSemestreOriginal())));
					setOncompleteModal("PF('panelAnoSemestreDiferenteMatriculaPeriodo').show()");
				}else {
					atualizarSituacaoMatriculaPeriodoPartindoProceMatricula();
				}
			}else {
				atualizarSituacaoMatriculaPeriodoPartindoProceMatricula();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public void cancelarTrocaProcessoMatricula() {
		getMatriculaPeriodoVO().setProcessoMatricula(getMatriculaPeriodoVO().getProcessoMatriculaOriginal().getCodigo());
		getMatriculaPeriodoVO().setProcessoMatriculaVO(getMatriculaPeriodoVO().getProcessoMatriculaOriginal());
		atualizarSituacaoMatriculaPeriodoPartindoProceMatricula();		
	}

	public void realizarDesistenciaEquivalencia() {
		try {
			getFacadeFactory().getHistoricoFacade().realizarDesistenciaEquivalencia(getMatriculaVO(), getMatriculaPeriodoVO(), getMapaEquivalenciaDisciplinaVisualizar(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void verificarPermissaoUsuarioLiberarAlterarCategoriaCondicaoPagamentoPlanoFinanceiroCurso() {
		if (getPermiteAlterarPlanoCondicaoFinanceiraCurso()) {
			return;
		}
		Boolean liberar = false;
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_LiberarAlterarCategoriaCondicaoPagamentoPlanoFinanceiroCurso", getUsuarioLogado());
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		setPermiteAlterarPlanoCondicaoFinanceiraCurso(liberar);
	}
	
	public String confirmarManterContasAReceberEditadasManualmente() throws Exception {
		this.getMatriculaPeriodoVO().setRegerarContaReceberComEdicaoManualAtiva(Boolean.FALSE);
		this.getMatriculaPeriodoVO().setUsuarioDefiniuAcaoSobreContasEdicaoManualAtiva(Boolean.TRUE);
		return gravar();
	}
	
	public String confirmarRegerarContasAReceberEditadasManualmente() throws Exception {
		this.getMatriculaPeriodoVO().setRegerarContaReceberComEdicaoManualAtiva(Boolean.TRUE);
		this.getMatriculaPeriodoVO().setUsuarioDefiniuAcaoSobreContasEdicaoManualAtiva(Boolean.TRUE);
		return gravar();
	}
	
//	public Boolean getRegerarFinanceiro() {
//		return getMatriculaPeriodoVO().getCodigo().equals(0) || (!getMatriculaPeriodoVO().getCodigo().equals(0) && getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getRegerarFinanceiro());
//	}
//	
//	public Boolean getApresentarBotaoLiberarRegerarFinanceiro() {
//		return !getMatriculaPeriodoVO().getCodigo().equals(0) && !getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getRegerarFinanceiro(); 
//	}

	public String getUserNamePermitirRegerarFinanceiro() {
		if (userNamePermitirRegerarFinanceiro == null) {
			userNamePermitirRegerarFinanceiro = "";
		}
		return userNamePermitirRegerarFinanceiro;
	}

	public void setUserNamePermitirRegerarFinanceiro(String userNamePermitirRegerarFinanceiro) {
		this.userNamePermitirRegerarFinanceiro = userNamePermitirRegerarFinanceiro;
	}

	public String getSenhaPermitirRegerarFinanceiro() {
		if (senhaPermitirRegerarFinanceiro == null) {
			senhaPermitirRegerarFinanceiro = "";
		}
		return senhaPermitirRegerarFinanceiro;
	}

	public void setSenhaPermitirRegerarFinanceiro(String senhaPermitirRegerarFinanceiro) {
		this.senhaPermitirRegerarFinanceiro = senhaPermitirRegerarFinanceiro;
	}
	
//	public void verificarPermissaoUsuarioRegerarFinanceiro() {
//		try {
//			UsuarioVO usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.getUserNamePermitirRegerarFinanceiro(), this.getSenhaPermitirRegerarFinanceiro(), true, Uteis.NIVELMONTARDADOS_TODOS);
//			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_PermitirRegerarFinanceiro", usuarioVerif);
//			getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_PERMITIR_REGERAR_FINANCEIRO, usuarioVerif, ""));
//			setPermitirRegerarFinanceiro(Boolean.TRUE);
//			getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setRegerarFinanceiro(true);
//			verificarVisuaizarAbaDescontos();
//			setMensagemID("msg_PermissaoUsuarioRealizarMatriculaDisciplinaPreRequisito");
//		} catch (Exception e) {
//			setPermitirRegerarFinanceiro(Boolean.FALSE);
//			getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setRegerarFinanceiro(false);
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
//	}

	public Boolean getPermitirRegerarFinanceiro() {
		if (permitirRegerarFinanceiro == null) {
			permitirRegerarFinanceiro = false;
		}
		return permitirRegerarFinanceiro;
	}

	public void setPermitirRegerarFinanceiro(Boolean permitirRegerarFinanceiro) {
		this.permitirRegerarFinanceiro = permitirRegerarFinanceiro;
	}
	
	public void prepararParaLiberarRegerarFinanceiro() {
		userNamePermitirRegerarFinanceiro = "";
		senhaPermitirRegerarFinanceiro = "";
	}

	

	public void setPermiteAlterarDataBaseGeracaoParcelas(Boolean permiteAlterarDataBaseGeracaoParcelas) {
		this.permiteAlterarDataBaseGeracaoParcelas = permiteAlterarDataBaseGeracaoParcelas;
	}
	
	public Map<String, Integer> getDadosRecuperadosAlteracaoProcessoMatricula() {
		if (dadosRecuperadosAlteracaoProcessoMatricula == null) {
			dadosRecuperadosAlteracaoProcessoMatricula = new HashMap<String, Integer>(0);
		}
		return dadosRecuperadosAlteracaoProcessoMatricula;
	}

	public void setDadosRecuperadosAlteracaoProcessoMatricula(Map<String, Integer> dadosRecuperadosAlteracaoProcessoMatricula) {
		this.dadosRecuperadosAlteracaoProcessoMatricula = dadosRecuperadosAlteracaoProcessoMatricula;
	}

	public void recuperarDadosProcessoMatriculaGradeCurricularPeriodoLetivoTurma() {
		setDadosRecuperadosAlteracaoProcessoMatricula(new HashMap<String, Integer>(0));
		getDadosRecuperadosAlteracaoProcessoMatricula().put("Curso", getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getCurso().getCodigo());
		getDadosRecuperadosAlteracaoProcessoMatricula().put("ProcessoMatricula", getMatriculaPeriodoVO().getProcessoMatricula());
		getDadosRecuperadosAlteracaoProcessoMatricula().put("GradeCurricular",	getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
		getDadosRecuperadosAlteracaoProcessoMatricula().put("PeriodoLetivo", getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo());
		getDadosRecuperadosAlteracaoProcessoMatricula().put("Turma", getMatriculaPeriodoVO().getTurma().getCodigo());
	}
	
	
	
	
	
	
	public boolean isObrigatorioContratoPorTurma() {
		return obrigatorioContratoPorTurma;
	}

	public void setObrigatorioContratoPorTurma(boolean obrigatorioContratoPorTurma) {
		this.obrigatorioContratoPorTurma = obrigatorioContratoPorTurma;
	}
	
	public void getCarregarPermissaoObrigatorioContratoPorTurma() {
		try {
			if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getTurma())) {				
				List<TurmaContratoVO> turmaContratoVOs = verificarPermissaoObrigatorioContratoPorTurma();
				if(isObrigatorioContratoPorTurma()) {
					Optional<TurmaContratoVO> tcOptional = turmaContratoVOs.stream().filter(p-> p.getPadrao()).findFirst();
					if(tcOptional.isPresent() && tcOptional.get().getPadrao()) {
						getMatriculaPeriodoVO().getContratoMatricula().setCodigo(tcOptional.get().getTextoPadraoVO().getCodigo());
					}else if(!Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getContratoMatricula()) && turmaContratoVOs.size() >= 1) {
						getMatriculaPeriodoVO().getContratoMatricula().setCodigo(turmaContratoVOs.get(0).getTextoPadraoVO().getCodigo());	
					}	
				}			
			}else {
				setObrigatorioContratoPorTurma(false);
				getMatriculaPeriodoVO().setContratoMatricula(new TextoPadraoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<TurmaContratoVO> verificarPermissaoObrigatorioContratoPorTurma() {
		getListaSelectItemContrato().clear();
		setObrigatorioContratoPorTurma(false);
		List<TurmaContratoVO> turmaContratoVOs = new ArrayList<>();
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_INFORMA_CONTRATO_MATRICULA_VINCULADO_TURMA, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getTurma())) {
				turmaContratoVOs = getFacadeFactory().getTurmaContratoFacade().consultarTurmaTipoContratoMatricula(getMatriculaPeriodoVO().getTurma().getCodigo(), TipoContratoMatriculaEnum.getEnumPorValor(getMatriculaVO().getTipoMatricula()), false, getUsuarioLogado());
				turmaContratoVOs.stream().forEach(p->{ getListaSelectItemContrato().add(new SelectItem(p.getTextoPadraoVO().getCodigo(),p.getTextoPadraoVO().getDescricao()));}); 
				 
			}
			setObrigatorioContratoPorTurma(true);
		} catch (Exception e) {
			setObrigatorioContratoPorTurma(false);
		}
		return turmaContratoVOs;
	}

	private Boolean abrirModalSelecaoContrato;
	private List<SelectItem> listaSelectItemContrato;
	private TipoContratoMatriculaEnum tipoContratoMatricula;
	private Boolean permitirAlterarContratoMatricula;

	public Boolean getAbrirModalSelecaoContrato() {
		if(abrirModalSelecaoContrato == null){
			abrirModalSelecaoContrato = false;
		}
		return abrirModalSelecaoContrato;
	}

	public void setAbrirModalSelecaoContrato(Boolean abrirModalSelecaoContrato) {
		this.abrirModalSelecaoContrato = abrirModalSelecaoContrato;
	}

	public List<SelectItem> getListaSelectItemContrato() {
		if(listaSelectItemContrato == null){
			listaSelectItemContrato = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemContrato;
	}

	public void setListaSelectItemContrato(List<SelectItem> listaSelectItemContrato) {
		this.listaSelectItemContrato = listaSelectItemContrato;
	}

	public TipoContratoMatriculaEnum getTipoContratoMatricula() {
		if(tipoContratoMatricula == null){
			tipoContratoMatricula = TipoContratoMatriculaEnum.NORMAL;
		}
		return tipoContratoMatricula;
	}

	public void setTipoContratoMatricula(TipoContratoMatriculaEnum tipoContratoMatricula) {
		this.tipoContratoMatricula = tipoContratoMatricula;
	}

	private void realizarCarregamentoPossiveisContratos() throws Exception {
		getListaSelectItemContrato().clear();
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			List<TurmaContratoVO> turmaContratoVOs = getFacadeFactory().getTurmaContratoFacade()
					.consultarTurmaTipoContratoMatricula(matriculaPeriodoVO.getTurma().getCodigo(),
							getTipoContratoMatricula(), false, getUsuarioLogado());
			boolean existeContratoMP = false;
			for (TurmaContratoVO turmaContratoVO : turmaContratoVOs) {
				getListaSelectItemContrato().add(new SelectItem(turmaContratoVO.getTextoPadraoVO().getCodigo(),
						turmaContratoVO.getTextoPadraoVO().getDescricao()));
				if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.NORMAL)
						&& Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoMatricula())
						&& matriculaPeriodoVO.getContratoMatricula().getCodigo()
								.equals(turmaContratoVO.getTextoPadraoVO().getCodigo())) {
					existeContratoMP = true;
				} else if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.EXTENSAO)
						&& Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoExtensao())
						&& matriculaPeriodoVO.getContratoExtensao().getCodigo()
								.equals(turmaContratoVO.getTextoPadraoVO().getCodigo())) {
					existeContratoMP = true;
				} else if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.FIADOR)
						&& Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoFiador())
						&& matriculaPeriodoVO.getContratoFiador().getCodigo()
								.equals(turmaContratoVO.getTextoPadraoVO().getCodigo())) {
					existeContratoMP = true;
				}
			}
			if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.NORMAL)
					&& Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoMatricula())) {
				getListaSelectItemContrato().add(new SelectItem(matriculaPeriodoVO.getContratoMatricula().getCodigo(),
						matriculaPeriodoVO.getContratoMatricula().getDescricao()));
			} else if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.EXTENSAO)
					&& Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoExtensao())) {
				getListaSelectItemContrato().add(new SelectItem(matriculaPeriodoVO.getContratoExtensao().getCodigo(),
						matriculaPeriodoVO.getContratoExtensao().getDescricao()));
			} else if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.FIADOR)
					&& Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoFiador())) {
				getListaSelectItemContrato().add(new SelectItem(matriculaPeriodoVO.getContratoFiador().getCodigo(),
						matriculaPeriodoVO.getContratoFiador().getDescricao()));
			}
			setAbrirModalSelecaoContrato(getListaSelectItemContrato().size() > 1);
		}
	}
	
	

	public Boolean getPermitirAlterarContratoMatricula() {
		if(permitirAlterarContratoMatricula == null){
			permitirAlterarContratoMatricula = false;
		}
		return permitirAlterarContratoMatricula;
	}

	public void setPermitirAlterarContratoMatricula(Boolean permitirAlterarContratoMatricula) {
		this.permitirAlterarContratoMatricula = permitirAlterarContratoMatricula;
	}

	public void selecionarContratoParaImpressao() {		
		try {	
			if(getPermitirAlterarContratoMatricula()) {
				if(getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.NORMAL)) {
					getMatriculaPeriodoVO().setGravarContratoMatricula(true);
				}else if(getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.EXTENSAO)) {
					getMatriculaPeriodoVO().setGravarContratoExtensao(true);
				}else if(getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.FIADOR)) {
					getMatriculaPeriodoVO().setGravarContratoFiador(true);
				}				
			}
			setImprimirContrato(true);
			String tipoContrato = "MA";
			if(!getTipoContratoMatricula().isContratoNormal()) {
				tipoContrato = getTipoContratoMatricula().getValor();
			}
			imprimirContrato(tipoContrato);
			setAbrirModalSelecaoContrato(false);
			setPermitirAlterarContratoMatricula(false);
		} catch (Exception e) {
			setImprimirContrato(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	
	public void verificarValidacaoDadosEnadeCenso() throws Exception{
		try {
			removerControleMemoriaFlashTela("AlunoControle");
			getMatriculaVO().getAluno().setAluno(true);
			context().getExternalContext().getSessionMap().put("aluno", getMatriculaVO().getAluno());
			context().getExternalContext().getSessionMap().put("validaCamposEnadeCenso", true);
			setOncompleteOperacaoFuncionalidade("PF('panelAvisoVerificarDadosEnadeCenso').hide()");				
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
	}

	public Boolean getPermiteLiberarValidacaoDadosEnadeCenso() {
		if(permiteLiberarValidacaoDadosEnadeCenso == null) {
			permiteLiberarValidacaoDadosEnadeCenso = false;
		}
		return permiteLiberarValidacaoDadosEnadeCenso;
	}

	public void setPermiteLiberarValidacaoDadosEnadeCenso(Boolean permiteLiberarValidacaoDadosEnadeCenso) {
		this.permiteLiberarValidacaoDadosEnadeCenso = permiteLiberarValidacaoDadosEnadeCenso;
	}
	
	private Boolean alunoPossuiPendenciaEnadeCenso;
	
	
	
	public Boolean getAlunoPossuiPendenciaEnadeCenso() {
		return alunoPossuiPendenciaEnadeCenso;
	}

	public void setAlunoPossuiPendenciaEnadeCenso(Boolean alunoPossuiPendenciaEnadeCenso) {
		this.alunoPossuiPendenciaEnadeCenso = alunoPossuiPendenciaEnadeCenso;
	}

	public void realizarValidacaoDadosAlunoEnadeCenso() {
		setAlunoPossuiPendenciaEnadeCenso(false);
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa() && getMatriculaVO().getCurso().getNivelEducacionalGraduacao() 
				&& getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarDadosEnadeCensoMatricularAluno() 
				&& !getPermiteLiberarValidacaoDadosEnadeCenso()) {
			try {					
				
				//removerControleMemoriaFlashTela("AlunoControle");
				getFacadeFactory().getPessoaFacade().carregarDados(getMatriculaVO().getAluno(), getUsuarioLogado());
				listCamposPreencherEnadeCenso.clear();
				msgCamposPreencherEnadeCenso();
				PessoaVO.validarDados(getMatriculaVO().getAluno(), true, true, false);
				

			}catch (Exception e) {
				setAlunoPossuiPendenciaEnadeCenso(true);
			}
		}
	}
	
	public String realizarNavegacaoPagina() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			if(getGuiaAba().equals("DisciplinaMatriculado")) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaConfirmadoForm.xhtml");
			}else if(getGuiaAba().equals("Documentacao")) {
					return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaDocumentacaoForm.xhtml");
			}else {
				return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
			}
		}
		return "";
	}
	

    public Boolean getApresentarBotaoLiberarBloqueio() {
    	return this.getMatriculaPeriodoVO().getApresentarBotaoLiberarBloqueioFechamentoMes();
    }
    
	public void alterarDataReferencia() {
//		getMatriculaPeriodoVO().reiniciarControleBloqueioCompetencia();
	}       
	
	public Boolean getControlaAprovacaoDocEntregue() {
		try {
			return getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getMatriculaVO().getUnidadeEnsino().getCodigo()).getControlaAprovacaoDocEntregue();
		} catch (Exception e) {
			return false;
		}
	}

	
	public void setTipoCartao(TipoCartaoOperadoraCartaoEnum tipoCartao) {
		this.tipoCartao = tipoCartao;
	}
	
	

	
	public List<String> getListCamposPreencherEnadeCenso() {
		if (listCamposPreencherEnadeCenso == null) {
			listCamposPreencherEnadeCenso = new ArrayList<String>(0);
		}
		return listCamposPreencherEnadeCenso;
	}

	public void setListCamposPreencherEnadeCenso(List<String> listCamposPreencherEnadeCenso) {
		this.listCamposPreencherEnadeCenso = listCamposPreencherEnadeCenso;
	}

	public void msgCamposPreencherEnadeCenso() {		
		
		if(getMatriculaVO().getAluno().getNome().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - Nome do aluno ");
		}
		if(getMatriculaVO().getAluno().getCPF().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - CPF ");
		}
		if(getMatriculaVO().getAluno().getRG().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - RG ");
		}
		if(getMatriculaVO().getAluno().getOrgaoEmissor().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - Orgão Expedidor RG ");
		}
		if(getMatriculaVO().getAluno().getEndereco().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - Endereço ");
		}
		if(getMatriculaVO().getAluno().getSetor().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - Bairro ");
		}
		if(getMatriculaVO().getAluno().getCidade().getNome().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - Cidade ");
		}
		if(getMatriculaVO().getAluno().getEmail().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - Email ");
		}
		if(getMatriculaVO().getAluno().getTelefoneComer().trim().equals("") && getMatriculaVO().getAluno().getTelefoneRes().trim().equals("") && getMatriculaVO().getAluno().getTelefoneRecado().trim().equals("") && getMatriculaVO().getAluno().getCelular().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - Telefone ");
		}
		if(getMatriculaVO().getAluno().getCEP().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - CEP ");
		}
		if(getMatriculaVO().getAluno().getSexo().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - Sexo ");
		}
		if(getMatriculaVO().getAluno().getCorRaca().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - Cor/Raça ");
		}
		if(getMatriculaVO().getAluno().getIdAlunoInep().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - Id Aluno INEP ");
		}
		if(getMatriculaVO().getAluno().getDataNasc() == null) {
			listCamposPreencherEnadeCenso.add(" - Data Nascimento ");
		}
		if(getMatriculaVO().getAluno().getNacionalidade().getCodigo() == null || getMatriculaVO().getAluno().getNacionalidade().getCodigo() == 0) {
			listCamposPreencherEnadeCenso.add(" - Nacionalidade ");
		}
		if(getMatriculaVO().getAluno().getNaturalidade().getNome().trim().equals("")) {
			listCamposPreencherEnadeCenso.add(" - Naturalidade ");
		}
		
		validarFormacaoEnsinoMedioObrigatorio();
		validarFiliacaoMaeObrigatorio();

		
	}
	
	public void validarFormacaoEnsinoMedioObrigatorio(){
		int cont = 0;
		if(!getMatriculaVO().getAluno().getFormacaoAcademicaVOs().isEmpty()) {
			Iterator itens = getMatriculaVO().getAluno().getFormacaoAcademicaVOs().iterator();
			while(itens.hasNext()) {
				FormacaoAcademicaVO objFormacao = (FormacaoAcademicaVO) itens.next();
				if (objFormacao.getEscolaridade().equals("EM")) {
					cont++;
				}
			}
			if (cont == 0) {
				listCamposPreencherEnadeCenso.add(" - Ano Conclusão Ensino Médio ");
				listCamposPreencherEnadeCenso.add(" - Colégio Conclusão Ensino Médio ");
			}
		}
		else {
			listCamposPreencherEnadeCenso.add(" - Ano Conclusão Ensino Médio ");
			listCamposPreencherEnadeCenso.add(" - Colégio Conclusão Ensino Médio ");
		}
	}
	
	public void validarFiliacaoMaeObrigatorio(){
		int cont = 0;
        if (!getMatriculaVO().getAluno().getFiliacaoVOs().isEmpty()) {
            Iterator itens = getMatriculaVO().getAluno().getFiliacaoVOs().iterator();
            while (itens.hasNext()) {
                FiliacaoVO objFiliacao = (FiliacaoVO) itens.next();
                if (objFiliacao.getTipo().equals("MA")) {
                	cont++;
                }
            }
            if(cont == 0) {
            	listCamposPreencherEnadeCenso.add(" - Nome Mãe ");
            }
        }
        else {
        	listCamposPreencherEnadeCenso.add(" - Nome Mãe ");
        }
    }

	/**
	 * @return the permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso
	 */
	public Boolean getPermitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso() {
		if (permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso == null) {
			permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso = false;
		}
		return permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso;
	}

	/**
	 * @param permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso the permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso to set
	 */
	public void setPermitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso(Boolean permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso) {
		this.permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso = permitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso;
	}
	
	/*public Boolean getSolicitarLiberacaoFinanceira() {
		if(solicitarLiberacaoFinanceira == null) {
			solicitarLiberacaoFinanceira = Boolean.FALSE;
		}
		return solicitarLiberacaoFinanceira;
	}

	public void setSolicitarLiberacaoFinanceira(Boolean solicitarLiberacaoFinanceira) {
		this.solicitarLiberacaoFinanceira = solicitarLiberacaoFinanceira;
	}

	public Boolean getSolicitarLiberacaoMatriculaAposInicioXModulos() {
		if(solicitarLiberacaoMatriculaAposInicioXModulos == null) {
			solicitarLiberacaoMatriculaAposInicioXModulos = Boolean.FALSE;
		}
		return solicitarLiberacaoMatriculaAposInicioXModulos;
	}

	public void setSolicitarLiberacaoMatriculaAposInicioXModulos(Boolean solicitarLiberacaoMatriculaAposInicioXModulos) {
		this.solicitarLiberacaoMatriculaAposInicioXModulos = solicitarLiberacaoMatriculaAposInicioXModulos;
	}
	*/

	public void executarSolicitarLiberacao(String motivoLiberacao) {		
		if(motivoLiberacao.equals("liberacaoFinanceira")) {
			setLiberarRenovacaoComDebitosFinanceiros(Boolean.TRUE);
			//setSolicitarLiberacaoFinanceira(Boolean.TRUE);
			matriculaVO.setBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira(Boolean.TRUE);
		}
		if(motivoLiberacao.equals("liberarMatriculaAposInicioXModulos")) {
			setAutorizacaoMatriculaAcimaRealizada(Boolean.TRUE);
			//setSolicitarLiberacaoMatriculaAposInicioXModulos(Boolean.TRUE);		
			matriculaVO.setBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica(Boolean.TRUE);
			setMensagemID("msg_ConfirmacaoLiberacaoMatriculaAcimaNrAulas");
		}
				
	}

	public Boolean getPermitirSolicitarAprovacaoLiberacaoFinanceira() {
		if(permitirSolicitarAprovacaoLiberacaoFinanceira == null) {
			permitirSolicitarAprovacaoLiberacaoFinanceira = Boolean.FALSE;
		}
		return permitirSolicitarAprovacaoLiberacaoFinanceira;
	}

	public void setPermitirSolicitarAprovacaoLiberacaoFinanceira(Boolean permitirSolicitarAprovacaoLiberacaoFinanceira) {
		this.permitirSolicitarAprovacaoLiberacaoFinanceira = permitirSolicitarAprovacaoLiberacaoFinanceira;
	}

	public Boolean getPermitirSolicitarLiberacaoMatriculaAposInicioXModulos() {
		if(permitirSolicitarLiberacaoMatriculaAposInicioXModulos == null) {
			permitirSolicitarLiberacaoMatriculaAposInicioXModulos = Boolean.FALSE;
		}
		return permitirSolicitarLiberacaoMatriculaAposInicioXModulos;
	}

	public void setPermitirSolicitarLiberacaoMatriculaAposInicioXModulos(
			Boolean permitirSolicitarLiberacaoMatriculaAposInicioXModulos) {
		this.permitirSolicitarLiberacaoMatriculaAposInicioXModulos = permitirSolicitarLiberacaoMatriculaAposInicioXModulos;
	}

	public void verificarPermissaoSolicitarAprovacaoLiberacaoFinanceira() {
		try {
			 if(getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("Matricula_PermitirSolicitarAprovacaoLiberacaoFinanceira", getUsuarioLogado())) {
				 setPermitirSolicitarAprovacaoLiberacaoFinanceira(Boolean.TRUE);
			 }
		} catch (Exception e) {
			setPermitirSolicitarAprovacaoLiberacaoFinanceira(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoSolicitarLiberacaoMatriculaAposInicioXModulos() {
		try {
			 if(getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("Matricula_PermitirSolicitarLiberacaoMatriculaAposInicioXModulos", getUsuarioLogado())) {
				 setPermitirSolicitarLiberacaoMatriculaAposInicioXModulos(Boolean.TRUE);
			 }
		} catch (Exception e) {
			setPermitirSolicitarLiberacaoMatriculaAposInicioXModulos(Boolean.FALSE);
		}
	}
	
	public void inicializarDadosNavegacaoTelaMapaSuspensaoMatricula() throws Exception{
		try {
			removerControleMemoriaFlashTela("MapaSuspensaoMatriculaControle");
			MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
			context().getExternalContext().getSessionMap().put("matricula", obj.getMatriculaVO());			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
	}
	
	
	
	public Boolean getPermitirReconsideracaoSolicitacao() {
		if(permitirReconsideracaoSolicitacao == null) {
			permitirReconsideracaoSolicitacao = Boolean.FALSE;
		}
		return permitirReconsideracaoSolicitacao;
	}

	public void setPermitirReconsideracaoSolicitacao(Boolean permitirReconsideracaoSolicitacao) {
		this.permitirReconsideracaoSolicitacao = permitirReconsideracaoSolicitacao;
	}

	public void verificarPermissaoReconsiderarSolicitacao() {
		try {
			 if(getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("Matricula_PermitirReconsideracaoSolicitacao", getUsuarioLogado())) {
				 setPermitirReconsideracaoSolicitacao(Boolean.TRUE);
			 }
		} catch (Exception e) {
			setPermitirReconsideracaoSolicitacao(Boolean.FALSE);
		}
	}
	
	public void solicitarReconsideracaoSolicitacao() {	 

		try {	
			CancelamentoVO cancelamentoVO = new CancelamentoVO();
			cancelamentoVO = getFacadeFactory().getCancelamentoFacade().consultarPorMatricula(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			cancelamentoVO.setResponsavelEstorno(getUsuarioLogadoClone());
			cancelamentoVO.setDataEstorno(new Date());
			cancelamentoVO.setSituacao("ES");
			getFacadeFactory().getCancelamentoFacade().executarEstorno(cancelamentoVO, getUsuarioLogado());
			getFacadeFactory().getPendenciaLiberacaoMatriculaInterfaceFacade().alterarSituacaoLiberacaoPendenciaMatricula(getMatriculaVO().getMatricula(), getUsuarioLogado());
			consultar();
			setMensagemID("msg_dados_reconsideracaoSoliciatao");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}
	
	public void inicializarDadosMatriculaSolicitarReconsideracao() {
			matriculaPeriodoVO = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
			if (Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
				setMatriculaVO(matriculaPeriodoVO.getMatriculaVO());
			}		
	}
	
	public String getCaminhoPreviewCertificado() {
		if (caminhoPreviewCertificado == null) {
			caminhoPreviewCertificado = "";
		}
		return caminhoPreviewCertificado;
	}

	public void setCaminhoPreviewCertificado(String caminhoPreviewCertificado) {
		this.caminhoPreviewCertificado = caminhoPreviewCertificado;
	}
//	
//	private void validarApresentacaoTermoAceitePorDesignTextoPadrao() throws Exception {
////		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "validarApresentacaoTermoAceitePorDesignTextoPadrao"+ getMatriculaVO().getMatricula());
//		TextoPadraoVO textoPadraoVO = getMatriculaPeriodoVO().getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline();
//		String contratoPronto = textoPadraoVO.substituirTagsTextoPadraoContratoMatricula(null, getMatriculaVO(),
////		new ArrayList<>(ContaReceberVO), getMatriculaPeriodoVO(), new PlanoFinanceiroAlunoVO(), new ArrayList<>(0), new DadosComerciaisVO(), getUsuarioLogado());
////		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "validarApresentacaoTermoAceitePorDesignTextoPadrao1"+ getMatriculaVO().getMatricula());
//		String contrato = textoPadraoVO.removerHtmlBotaoImprimirTextoPadraoContratoMatricula(contratoPronto);
//		contrato = getFacadeFactory().getTextoPadraoDeclaracaoFacade().removerBordaDaPagina(contrato);
//		contrato = getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarStyleFormatoPaginaTextoPadrao(contrato, textoPadraoVO.getOrientacaoDaPagina());
//		if (textoPadraoVO.getTipoDesigneTextoEnum().isPdf()) {
//			ImpressaoContratoVO impressaoContratoVO =  new ImpressaoContratoVO();
//			impressaoContratoVO.setMatriculaVO(getMatriculaVO());
//			impressaoContratoVO.setMatriculaPeriodoVO(getMatriculaPeriodoVO());			
//			setCaminhoPreviewCertificado(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" 
//					+ getFacadeFactory().getImpressaoContratoFacade().executarValidacaoTipoImpressaoContrato(textoPadraoVO, contratoPronto, impressaoContratoVO, getConfiguracaoGeralPadraoSistema(),false, getUsuarioLogado()) + "?embedded=true");
//		} else {
//			getMatriculaPeriodoVO().getProcessoMatriculaVO().setTermoAceite(contrato);
//		}
////		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "validarApresentacaoTermoAceitePorDesignTextoPadrao2"+ getMatriculaVO().getMatricula());
//	}			

	public Boolean permitirAlteracaoSubturma;

	public Boolean getPermitirAlteracaoSubturma() {
		if(permitirAlteracaoSubturma == null) {
			try {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirAlunoRegularAlterarSubturmaMatricula", getUsuarioLogado());
				permitirAlteracaoSubturma = true;
			}catch (Exception e) {
				permitirAlteracaoSubturma = false; 
			}
		}
		return (permitirAlteracaoSubturma && getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular()) 				
				|| (!getMatriculaPeriodoVO().getAlunoRegularMatrizCurricular());
	}
	
	
	public Boolean getPermiteAlunoAlterarTurmaBaseSugerida() {
		if(permiteAlunoAlterarTurmaBaseSugerida == null) {
			permiteAlunoAlterarTurmaBaseSugerida = Boolean.FALSE;
		}
		return permiteAlunoAlterarTurmaBaseSugerida;
	}

	public void setPermiteAlunoAlterarTurmaBaseSugerida(Boolean permiteAlunoAlterarTurmaBaseSugerida) {
		this.permiteAlunoAlterarTurmaBaseSugerida = permiteAlunoAlterarTurmaBaseSugerida;
	}

	public void verificarPermissaoAlunoAlterarTrumaBaseSugerida() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirAlunoAlterarTurmaBaseSugerida", getUsuarioLogado());
			setPermiteAlunoAlterarTurmaBaseSugerida(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteAlunoAlterarTurmaBaseSugerida(Boolean.FALSE);
		}
	}
	
	
	
	public ConfiguracaoBibliotecaVO getConfiguracaoBibliotecaVO() {
		if(configuracaoBibliotecaVO == null) {
			configuracaoBibliotecaVO = new ConfiguracaoBibliotecaVO();
		}
		return configuracaoBibliotecaVO;
	}

	public void setConfiguracaoBibliotecaVO(ConfiguracaoBibliotecaVO configuracaoBibliotecaVO) {
		this.configuracaoBibliotecaVO = configuracaoBibliotecaVO;
	}

	public void validarPermissaoRenovarMatriculaDoAlunoQuandoPossuirBloqueioBiblioteca() throws Exception {
		if(getPermiteLiberarValidacaoAlunoBloqueioBiblioteca()) {
			return;
		}
		
		setConfiguracaoBibliotecaVO(getFacadeFactory().getConfiguracaoBibliotecaFacade().
				consultarConfiguracaoBibliotecaPorUnidadeEnsinoENivelEducacional(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		
//		if(!getConfiguracaoBibliotecaVO().getPermiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca() && 
//				getFacadeFactory().getBloqueioBibliotecaFacade().verificarBloqueioExistente(getMatriculaVO().getAluno().getCodigo())) {
//			setApresentarBotaoLiberarValidacaoAlunoBloqueioBiblioteca(Boolean.TRUE);
//			setTipoOperacaoFuncionalidadeLiberar("Permitir Liberar Valida\u00e7\u00e3o Aluno Com Bloqueio na Biblioteca");
//			throw new Exception("Foi verificado um Bloqueio na Biblioteca procure o responsável pelo departamento e depois realize a renovação da matrícula.");
//		}
		
	}

	public Boolean getApresentarBotaoLiberarValidacaoAlunoBloqueioBiblioteca() {
		if(apresentarBotaoLiberarValidacaoAlunoBloqueioBiblioteca == null) {
			apresentarBotaoLiberarValidacaoAlunoBloqueioBiblioteca = false;
		}
		return apresentarBotaoLiberarValidacaoAlunoBloqueioBiblioteca;
	}

	public void setApresentarBotaoLiberarValidacaoAlunoBloqueioBiblioteca(Boolean apresentarBotaoLiberarValidacaoAlunoBloqueioBiblioteca) {
		this.apresentarBotaoLiberarValidacaoAlunoBloqueioBiblioteca = apresentarBotaoLiberarValidacaoAlunoBloqueioBiblioteca;
	}
	
	public Boolean getPermiteLiberarValidacaoAlunoBloqueioBiblioteca() {
		if(permiteLiberarValidacaoAlunoBloqueioBiblioteca == null) {
			permiteLiberarValidacaoAlunoBloqueioBiblioteca = false;
		}
		return permiteLiberarValidacaoAlunoBloqueioBiblioteca;
	}

	public void setPermiteLiberarValidacaoAlunoBloqueioBiblioteca(Boolean permiteLiberarValidacaoAlunoBloqueioBiblioteca) {
		this.permiteLiberarValidacaoAlunoBloqueioBiblioteca = permiteLiberarValidacaoAlunoBloqueioBiblioteca;
	}

	public void prerararLiberarValidacaoAlunoBloqueioBiblioteca() {
		// TODO
	}	
	
	public Boolean getPermitirEmitirBoletoMatriculaVisaoAluno() {
		if (permitirEmitirBoletoMatriculaVisaoAluno == null ) {
			permitirEmitirBoletoMatriculaVisaoAluno = false;
		}
		return permitirEmitirBoletoMatriculaVisaoAluno;
	}

	public void setPermitirEmitirBoletoMatriculaVisaoAluno(Boolean permitirEmitirBoletoMatriculaVisaoAluno) {
		this.permitirEmitirBoletoMatriculaVisaoAluno = permitirEmitirBoletoMatriculaVisaoAluno;
	}

	private void verificarPermissaoEmitirBoletoMatriculaVisaoAluno() throws Exception {
		setPermitirEmitirBoletoMatriculaVisaoAluno(false);
//		if (getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoReferenteMatricula() != null) {
//			ContaReceberVO contaReceberMatricula = getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoReferenteMatricula().getContaReceber();
//			if (Uteis.isAtributoPreenchido(contaReceberMatricula) && contaReceberMatricula.getContaCorrenteVO().getBloquearEmitirBoletoSemRegistroRemessa()) {
//				setPermitirEmitirBoletoMatriculaVisaoAluno(!getFacadeFactory().getControleRemessaContaReceberFacade().verificaContaReceberRegistrada(contaReceberMatricula));
//			} else if (Uteis.isAtributoPreenchido(contaReceberMatricula) && !contaReceberMatricula.getContaCorrenteVO().getBloquearEmitirBoletoSemRegistroRemessa()) {
//				setPermitirEmitirBoletoMatriculaVisaoAluno(!getMatriculaPeriodoVO().getMatriculaVO().getBloquearEmissaoBoletoMatMenVisaoAluno());
//			}
//		}
	}
	
	
	 /*liberado para fazer  inclusao e exclusao  de dsicplina apenas 1 vez  na renovação 
	 ja fez a renovação pelo menos 1 vez   
	 so chega aqui se  o booleano de permissao estiver marcado 
	 somente se esta com a matricula paga 
	 so cai aqui se o booleano de permissao estiver desmarcado  sendo assim o usuario podera incluir e excluir disciplina*/
	public Boolean getPermitirIncluirExcluirDisciplinaNaVisaoAlunoAposPagamentoMatricula() {
 		if (permitirIncluirExcluirDisciplinaNaVisaoAlunoAposPagamentoMatricula == null) {
			   permitirIncluirExcluirDisciplinaNaVisaoAlunoAposPagamentoMatricula = Boolean.TRUE;			
			try {
				if (!getMatriculaPeriodoVO().isNovoObj()) {
					getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
							"Matricula_PermitirAposConfirmacaoRenovacaoMatriculaPermitirInclusaoExclusaoDisciplinaAposPagamentoMatricula",
							getUsuarioLogado());
					if (!matriculaPeriodoVO.getSituacaoAtiva()) {					 
						   permitirIncluirExcluirDisciplinaNaVisaoAlunoAposPagamentoMatricula = Boolean.FALSE;
					}
							
				}			
			} catch (Exception e) {				
				permitirIncluirExcluirDisciplinaNaVisaoAlunoAposPagamentoMatricula = Boolean.TRUE;			
			}
		}

		return permitirIncluirExcluirDisciplinaNaVisaoAlunoAposPagamentoMatricula;
	}
	
	
	public void setPermitirIncluirExcluirDisciplinaNaVisaoAlunoAposPagamentoMatricula(Boolean permitirIncluirExcluirDisciplinaNaVisaoAlunoAposPagamentoMatricula) {
		this.permitirIncluirExcluirDisciplinaNaVisaoAlunoAposPagamentoMatricula = permitirIncluirExcluirDisciplinaNaVisaoAlunoAposPagamentoMatricula;
	}
	
	public Boolean getApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular() {
		if (apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular == null) {
			apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular = false;
		}
		return apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular;
	}

	public void setApresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular(
			Boolean apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular) {
		this.apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular = apresentarBotaoLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular;
	}
	
	
	public void consultarCursoTurnoRenovacaoMatricula() {
		
		try {
		  
		  if (realizandoNovaMatriculaAluno) {
			setListaConsultaCurso(new ArrayList(0));
		  }else {
		
			List objs = new ArrayList(0);
			objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(getMatriculaVO().getCurso().getCodigo(), getMatriculaVO().getUnidadeEnsino().getCodigo(),"AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			setListaConsultaCurso(objs);
		  }
		} catch (Exception e) {
		setListaConsultaCurso(new ArrayList(0));
		setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String getAbrirModalInclusaoArquivoVerso() {
		if (abrirModalInclusaoArquivoVerso == null) {
			abrirModalInclusaoArquivoVerso = "";
		}
		return abrirModalInclusaoArquivoVerso;
	}

	public void setAbrirModalInclusaoArquivoVerso(String abrirModalInclusaoArquivoVerso) {
		this.abrirModalInclusaoArquivoVerso = abrirModalInclusaoArquivoVerso;
	}
	
	public DocumetacaoMatriculaVO getDocumetacaoMatriculaVOAux() {
		return documetacaoMatriculaVOAux;
	}

	public void setDocumetacaoMatriculaVOAux(DocumetacaoMatriculaVO documetacaoMatriculaVOAux) {
		this.documetacaoMatriculaVOAux = documetacaoMatriculaVOAux;
	}

	public void limparDocumentacaoMatriculaVO() {
		try {
			int index = 0;
			if (getDocumetacaoMatriculaVO().getCodigo().equals(0) && getDocumetacaoMatriculaVOAux() != null) {
				Iterator<DocumetacaoMatriculaVO> i = getMatriculaVO().getDocumetacaoMatriculaVOs().iterator();
				while (i.hasNext()) {
					DocumetacaoMatriculaVO documetacaoMatriculaVO = i.next();
					if (documetacaoMatriculaVO == getDocumetacaoMatriculaVO()) {
						getDocumetacaoMatriculaVOAux().setArquivoVO(new ArquivoVO());
						getDocumetacaoMatriculaVOAux().setArquivoVOVerso(new ArquivoVO());
						getMatriculaVO().getDocumetacaoMatriculaVOs().set(index, getDocumetacaoMatriculaVOAux());
						break;
					}
					index++;
				}
				setDocumetacaoMatriculaVOAux(null);
			} else {
				Iterator<DocumetacaoMatriculaVO> i = getMatriculaVO().getDocumetacaoMatriculaVOs().iterator();
				while (i.hasNext()) {
					DocumetacaoMatriculaVO documetacaoMatriculaVO = i.next();
					if (documetacaoMatriculaVO.getCodigo().equals(getDocumetacaoMatriculaVO().getCodigo())) {
						getMatriculaVO().getDocumetacaoMatriculaVOs().set(index,
								getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorChavePrimaria(getDocumetacaoMatriculaVO().getCodigo(), 
										Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
						break;
					}
					index++;
				}
			}
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	public void scrollerListener() throws Exception {
		
		
		this.consultar();
	}

	public Boolean getPermissaoAlterarDadosFinanceiros() {
		if (permissaoAlterarDadosFinanceiros == null) {
			try {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), PerfilAcessoPermissaoAcademicoEnum.MATRICULA_ALTERAR_DADOS_FINANCEIROS.getValor());
				permissaoAlterarDadosFinanceiros = true;
			} catch (Exception e) {
				permissaoAlterarDadosFinanceiros = false;
			}
		}
		return permissaoAlterarDadosFinanceiros;
	}

	
	public void adicionarDisciplinaRegimeEspecial() {
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaEmRegimeEspecial(true);
		adicionarMatriculaPeriodoTurmaDisciplinaPorInclusao();
		setApresentarPanelIncluirDisciplinaRegimeEspecial(false);
	}
	
	public void naoAdicionarDisciplinaRegimeEspecial() {
		setApresentarPanelIncluirDisciplinaRegimeEspecial(false);
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaEmRegimeEspecial(false);
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurmaPratica(null);
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurmaTeorica(null);
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurma(null);
	}
	

	public Boolean getApresentarPanelIncluirDisciplinaRegimeEspecial() {
		if (apresentarPanelIncluirDisciplinaRegimeEspecial == null) {
			apresentarPanelIncluirDisciplinaRegimeEspecial = false;
		}
		return apresentarPanelIncluirDisciplinaRegimeEspecial;
	}

	public void setApresentarPanelIncluirDisciplinaRegimeEspecial(Boolean apresentarPanelIncluirDisciplinaRegimeEspecial) {
		this.apresentarPanelIncluirDisciplinaRegimeEspecial = apresentarPanelIncluirDisciplinaRegimeEspecial;
	}
	
	private void adicionarRegistroFolowUpAlunoAceitouTermoAceiteLancandoExcecao() throws Exception {
		/*setInteracaoWorkflowVO(new InteracaoWorkflowVO());
		getInteracaoWorkflowVO().setResponsavel(getUsuarioLogadoClone());
		getInteracaoWorkflowVO().setDataInicio(new Date());
		getInteracaoWorkflowVO().setHoraInicio(Uteis.getHoraAtual());
		getInteracaoWorkflowVO().setDataTermino(new Date());
		getInteracaoWorkflowVO().setHoraTermino(Uteis.getHoraAtual());
		getInteracaoWorkflowVO().setTipoOrigemInteracao(TipoOrigemInteracaoEnum.RENOVACAO_MATRICULA);
		getInteracaoWorkflowVO().setIdentificadorOrigem(getMatriculaPeriodoVO().getIdentificadorRenovacaoFollowUp());
		getInteracaoWorkflowVO().setCodigoEntidadeOrigem(getMatriculaPeriodoVO().getCodigo());
		ProspectsVO prospect = getFacadeFactory().getProspectsFacade().consultarPorCodigoPessoa(getMatriculaVO().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		this.getMatriculaVO().getAluno().setCodProspect(prospect.getCodigo());
		Integer codigoProspect = prospect.getCodigo();
		if ((codigoProspect == null) || (codigoProspect == 0)) {
			prospect.setTipoProspect(TipoProspectEnum.FISICO);
			prospect.setCpf(getMatriculaVO().getAluno().getCPF());
			prospect = getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorPessoa(getMatriculaVO().getAluno().getCPF(), prospect, getUsuarioLogado());
			if (!prospect.getEmailPrincipal().equals("")) {
				getFacadeFactory().getProspectsFacade().incluirSemValidarDados(prospect, false, getUsuarioLogado(), null);
			}
		}
		getInteracaoWorkflowVO().setProspect(prospect);
		getInteracaoWorkflowVO().setObservacao("Aluno aceitou Termo de Aceite para renovação de sua matrícula, período letivo: " + getMatriculaPeriodoVO().getIdentificadorRenovacaoFollowUp());
		getInteracaoWorkflowVO().setTipoInteracao(TipoInteracaoEnum.PORTALALUNO);
		getInteracaoWorkflowVO().getMatriculaVO().setMatricula(getMatriculaVO().getMatricula());
		getFacadeFactory().getInteracaoWorkflowFacade().incluirSemValidarDados(getInteracaoWorkflowVO(), getUsuarioLogado());*/
	}
	public String novoRenovacao() throws Exception {
		novo();
		setGuiaAba("Inicio");
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
	}
	
	public List<SelectItem> getListaSelectItemSituacaoMatriculaPeriodoEnum() {
		return SituacaoMatriculaPeriodoEnum.getListaSelectItemSituacaoMatriculaPeriodo();
	}
	
	public List<SelectItem> getListaSelectItemSituacaoVinculoMatriculaEnum() {
		return SituacaoVinculoMatricula.getListaSelectItemSituacaoVinculoMatricula(false);
	}
	
	public void inicializarMatriculaPeriodoAlteracaoSituacaoManual() {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
		setMatriculaPeriodoAlteracaoSituacaoManualVO(obj);
	}
	
	public void realizarAlteracaoSituacaoMatriculaManual() {
		try {
			getFacadeFactory().getMatriculaFacade().realizarAlteracaoSituacaoManual(getMatriculaPeriodoAlteracaoSituacaoManualVO(), getAlterarSituacaoManualMatricula(), getAlterarSituacaoManualMatriculaPeriodo(), getSituacaoVinculoMatricula(), getSituacaoMatriculaPeriodo(), getUsuarioLogado());
			setMensagemID("msg_dados_situacaoAlteradaComSucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public MatriculaPeriodoVO getMatriculaPeriodoAlteracaoSituacaoManualVO() {
		if (matriculaPeriodoAlteracaoSituacaoManualVO == null) {
			matriculaPeriodoAlteracaoSituacaoManualVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoAlteracaoSituacaoManualVO;
	}

	public void setMatriculaPeriodoAlteracaoSituacaoManualVO(MatriculaPeriodoVO matriculaPeriodoAlteracaoSituacaoManualVO) {
		this.matriculaPeriodoAlteracaoSituacaoManualVO = matriculaPeriodoAlteracaoSituacaoManualVO;
	}

	public SituacaoVinculoMatricula getSituacaoVinculoMatricula() {
		if (situacaoVinculoMatricula == null) {
			situacaoVinculoMatricula = SituacaoVinculoMatricula.ATIVA;
		}
		return situacaoVinculoMatricula;
	}

	public void setSituacaoVinculoMatricula(SituacaoVinculoMatricula situacaoVinculoMatricula) {
		this.situacaoVinculoMatricula = situacaoVinculoMatricula;
	}

	public SituacaoMatriculaPeriodoEnum getSituacaoMatriculaPeriodo() {
		if (situacaoMatriculaPeriodo == null) {
			situacaoMatriculaPeriodo = SituacaoMatriculaPeriodoEnum.ATIVA;
		}
		return situacaoMatriculaPeriodo;
	}

	public void setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum situacaoMatriculaPeriodo) {
		this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
	}

	public Boolean getAlterarSituacaoManualMatricula() {
		if (alterarSituacaoManualMatricula == null) {
			alterarSituacaoManualMatricula = false;
		}
		return alterarSituacaoManualMatricula;
	}

	public void setAlterarSituacaoManualMatricula(Boolean alterarSituacaoManualMatricula) {
		this.alterarSituacaoManualMatricula = alterarSituacaoManualMatricula;
	}

	public Boolean getAlterarSituacaoManualMatriculaPeriodo() {
		if (alterarSituacaoManualMatriculaPeriodo == null) {
			alterarSituacaoManualMatriculaPeriodo = false;
		}
		return alterarSituacaoManualMatriculaPeriodo;
	}

	public void setAlterarSituacaoManualMatriculaPeriodo(Boolean alterarSituacaoManualMatriculaPeriodo) {
		this.alterarSituacaoManualMatriculaPeriodo = alterarSituacaoManualMatriculaPeriodo;
	}
	
	public void verificarPermissaoAlterarSituacaoMatriculaManual() {
		try {
			 if(getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("Matricula_PermitirAlterarSituacaoMatriculaManual", getUsuarioLogado())) {
				 setPermitirAlterarSituacaoMatriculaManual(Boolean.TRUE);
			 }
		} catch (Exception e) {
			setPermitirAlterarSituacaoMatriculaManual(Boolean.FALSE);
		}
	}

	public Boolean getPermitirAlterarSituacaoMatriculaManual() {
		if (permitirAlterarSituacaoMatriculaManual == null) {
			permitirAlterarSituacaoMatriculaManual = false;
		}
		return permitirAlterarSituacaoMatriculaManual;
	}

	public void setPermitirAlterarSituacaoMatriculaManual(Boolean permitirAlterarSituacaoMatriculaManual) {
		this.permitirAlterarSituacaoMatriculaManual = permitirAlterarSituacaoMatriculaManual;
	}
	
	private void montarDadosMatriculaPeriodoTurmaDisciplinaPeriodoLetivoGradeCurricularGrupoOptativaDisciplina() {
		montarDadosMatriculaPeriodoTurmaDisciplinaPeriodoLetivo(mptd -> Uteis.isAtributoPreenchido(mptd.getGradeCurricularGrupoOptativaDisciplinaVO()),	this::montarDadosPeriodoLetivoGradeCurricularGrupoOptativaDisciplina);
	}

	private void montarDadosPeriodoLetivoGradeCurricularGrupoOptativaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		try {
			matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().setPeriodoLetivoDisciplinaReferenciada(getFacadeFactory().getPeriodoLetivoFacade()
				.consultarPeriodoLetivoPorGradeCurricularGrupoOptativaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarDadosMatriculaPeriodoTurmaDisciplinaPeriodoLetivoGradeDisciplinaComposta() {
		montarDadosMatriculaPeriodoTurmaDisciplinaPeriodoLetivo(mptd -> Uteis.isAtributoPreenchido(mptd.getGradeDisciplinaCompostaVO().getPeriodoLetivo()),	this::montarDadosPeriodoLetivoGradeDisciplinaComposta);
	}

	private void montarDadosPeriodoLetivoGradeDisciplinaComposta(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		try {
			matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaCompostaVO().setPeriodoLetivoVO(getFacadeFactory().getPeriodoLetivoFacade()
					.consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaCompostaVO().getPeriodoLetivo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarDadosMatriculaPeriodoTurmaDisciplinaPeriodoLetivo(Predicate<MatriculaPeriodoTurmaDisciplinaVO> filtro, Consumer<MatriculaPeriodoTurmaDisciplinaVO> acao) {
		getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(filtro).forEach(acao);
	}
	
	public void cancelarConfirmacaoMatriculaPlanoFinanceiroEContratoIntegracaoFinanceira() {
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			getMatriculaPeriodoVO().setApresentarConfirmacaoRenovacaoESolicitarCondicaoPagto("NAO_DEFINIDO");
			setListaSelectItemPlanoFinanceiroCurso(new ArrayList<>());
		}
	}

	public Boolean getHabilitadoAssinarEletronicamente() {
		if (habilitadoAssinarEletronicamente == null) {
			habilitadoAssinarEletronicamente = false;
		}
		return habilitadoAssinarEletronicamente;
	}

	public void setHabilitadoAssinarEletronicamente(Boolean habilitadoAssinarEletronicamente) {
		this.habilitadoAssinarEletronicamente = habilitadoAssinarEletronicamente;
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoExcluir() {
		if (documentoAssinadoExcluir == null) {
			documentoAssinadoExcluir = new DocumentoAssinadoVO();
		}
		return documentoAssinadoExcluir;
	}

	public void setDocumentoAssinadoExcluir(DocumentoAssinadoVO documentoAssinadoExcluir) {
		this.documentoAssinadoExcluir = documentoAssinadoExcluir;
	}

	public Integer getQuantidadeDocumentoAssinadoMatriculaPeriodo() {
		if (quantidadeDocumentoAssinadoMatriculaPeriodo == null) {
			quantidadeDocumentoAssinadoMatriculaPeriodo = 0;
		}
		return quantidadeDocumentoAssinadoMatriculaPeriodo;
	}

	public void setQuantidadeDocumentoAssinadoMatriculaPeriodo(Integer quantidadeDocumentoAssinadoMatriculaPeriodo) {
		this.quantidadeDocumentoAssinadoMatriculaPeriodo = quantidadeDocumentoAssinadoMatriculaPeriodo;
	}

	public Boolean getRejeitarContratosPendentesEmitidos() {
		if (rejeitarContratosPendentesEmitidos == null) {
			rejeitarContratosPendentesEmitidos = true;
		}
		return rejeitarContratosPendentesEmitidos;
	}

	public void setRejeitarContratosPendentesEmitidos(Boolean rejeitarContratosPendentesEmitidos) {
		this.rejeitarContratosPendentesEmitidos = rejeitarContratosPendentesEmitidos;
	}

	public List<DocumentoAssinadoVO> getDocumentoAssinadoMatriculaPeriodo() {
		if (documentoAssinadoMatriculaPeriodo == null) {
			documentoAssinadoMatriculaPeriodo = new ArrayList<DocumentoAssinadoVO>();
		}
		return documentoAssinadoMatriculaPeriodo;
	}

	public void setDocumentoAssinadoMatriculaPeriodo(List<DocumentoAssinadoVO> documentoAssinadoMatriculaPeriodo) {
		this.documentoAssinadoMatriculaPeriodo = documentoAssinadoMatriculaPeriodo;
	}
	
	public String getCaminhoPreviewContrato() {
		if (caminhoPreviewContrato == null) {
			caminhoPreviewContrato = "";
		}
		return caminhoPreviewContrato;
	}


	public void setCaminhoPreviewContrato(String caminhoPreviewContrato) {
		this.caminhoPreviewContrato = caminhoPreviewContrato;
	}
	public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if (documentoAssinadoVO == null) {
			documentoAssinadoVO = new DocumentoAssinadoVO();;
		}
		return documentoAssinadoVO;
	}
	
	public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}
	
	public String getMotivoRejeicao() {
		if (motivoRejeicao == null) {
			motivoRejeicao = "";
		}
		return motivoRejeicao;
	}

	public void setMotivoRejeicao(String motivoRejeicao) {
		this.motivoRejeicao = motivoRejeicao;
	}

	public Boolean getDocumentoRejeitado() {
		if (documentoRejeitado == null) {
			documentoRejeitado = Boolean.FALSE;
		}
		return documentoRejeitado;
	}

	public void setDocumentoRejeitado(Boolean documentoRejeitado) {
		this.documentoRejeitado = documentoRejeitado;
	}

	public void realizarDownloadContrato() throws CloneNotSupportedException {
		try {
		DocumentoAssinadoVO documentoAssinado = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("documentoAssinadoItens");
		ArquivoVO arquivoVO;
			arquivoVO = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(documentoAssinado.getArquivo().getCodigo(), 0, getUsuario());
		if(!arquivoVO.getPastaBaseArquivo().startsWith(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo())) {
			ArquivoVO cloneArquivo = (ArquivoVO) arquivoVO.clone();
			if (cloneArquivo.getPastaBaseArquivo().endsWith("TMP")) {
				cloneArquivo.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+cloneArquivo.getPastaBaseArquivo());
					
			}else {
			cloneArquivo.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+cloneArquivo.getPastaBaseArquivo()+File.separator);
			} context().getExternalContext().getSessionMap().put("arquivoVO", cloneArquivo);		
		}else {
			context().getExternalContext().getSessionMap().put("arquivoVO", arquivoVO);
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void excluirDocumentoAssinado() {
		try {
			getFacadeFactory().getControleAcessoFacade().excluir("MapaDocumentoAssinadoPessoa", true, getUsuarioLogado());
			getFacadeFactory().getDocumentoAssinadoFacade().excluir(getDocumentoAssinadoExcluir(), false, getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getDocumentoAssinadoExcluir().getUnidadeEnsinoVO().getCodigo()));
			getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().clear();
			getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().addAll(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().size() == 0) {
				setOncompleteModal("PF('panelDocumentoAssinado').hide();");
			}
//			setMensagemID("msg_Documento_Assinado_Excluido");
			setMensagemID("msg_Documento_Assinado_Excluido", Uteis.SUCESSO, Boolean.TRUE);
//			setMensagemDetalhada(MSG_TELA.msg_dados_excluidos.name(), UteisJSF.internacionalizar("msg_Documento_Assinado_Excluido"), true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
		}
	}
	
	public void documentoAssinadoExcluir() {
		try {
			DocumentoAssinadoVO obj = (DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItens");
			setDocumentoAssinadoExcluir(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
		}
	}
	
	 
	 public void notificacaoPendenciaContratoAssinado() {
//			try {
//				DocumentoAssinadoPessoaVO obj = (DocumentoAssinadoPessoaVO)getRequestMap().get("listaDocumentoPessoa");
////				PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.NOTIFICACAO_PENDENCIA_ASSINATURA_CONTRATO, false, null,getUsuarioLogado());
//				getFacadeFactory().getImpressaoContratoFacade().realizarNotificacaoPendenciaAssinaturaContrato(obj, mensagemTemplate != null ? mensagemTemplate : new PersonalizacaoMensagemAutomaticaVO(), getUsuarioLogado(), getConfiguracaoGeralSistemaVO(), false);
//				setMensagemID("msg_Notificacao_Pendencia_Enviada");
//				setMensagemDetalhada(MSG_TELA.msg_dados_Enviados.name(), UteisJSF.internacionalizar("msg_Notificacao_Pendencia_Enviada"), true);
//			} catch (Exception e) {
//				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
//			}
		}
	 
	 
	 public void imprimirContratoAssinaturaDigital(Boolean gerarNovoArquivoAssinado) {
		 try {
			 setImprimirContrato(false);
			 setFazerDownload(false);
			 getMatriculaPeriodoVO().setRegerarDocumentoAssinado(gerarNovoArquivoAssinado);			 
		 	if (getQuantidadeDocumentoAssinadoMatriculaPeriodo() > 0 && gerarNovoArquivoAssinado) {
			 	setOncompleteOperacaoFuncionalidade("PF('panelAlertaDocumentoAssinado').show();PF('panelAssinarDocumento').hide();");
			}else {
				imprimirContrato("MA");
				setOncompleteOperacaoFuncionalidade("PF('panelAssinarDocumento').hide();");
			}
		 	setHabilitadoAssinarEletronicamente(false);
//		 	getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().clear();
//			getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().addAll(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//			if(gerarNovoArquivoAssinado && !getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().isEmpty() && getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
//				 if(getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().get(0).getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
//					 realizarDownloadArquivoProvedorCertisin(getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().get(0));
//				 }
//			 }
		 } catch (Exception e) {
			 setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		 }
	 }
	 
	 
	 public void rejeitarContratosAssinaturaDigital(Boolean gerarNovoArquivoAssinado) {
		 try {
			 setMensagemID("msg_dados_consultados");
			 if (gerarNovoArquivoAssinado) {
				getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().stream().filter(doc -> doc.getProvedorDeAssinaturaEnum().isProvedorSei()).map(DocumentoAssinadoVO::getListaDocumentoAssinadoPessoa).flatMap(x -> x.stream())
					.filter(documentoPessoaPendendePessoa -> documentoPessoaPendendePessoa.getSituacaoDocumentoAssinadoPessoaEnum().isPendente())
					.forEach(documentoPessoaPendenteRejeitar -> getFacadeFactory().getDocumentoAssinadoPessoaFacade().rejeitarContratoAssinadoPendenteAutomaticamente(documentoPessoaPendenteRejeitar, getUsuarioLogado()));
				
				setMensagemDetalhada(MSG_TELA.msg_dados_gravados.name(), UteisJSF.internacionalizar("msg_Documento_Assinado_Rejeitado"), true);
			 }
			 imprimirContrato("MA");			 
			 getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().clear();
			 getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().addAll(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));			 
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	 }
	 
	 private void atualizarDadosRejeitado(DocumentoAssinadoPessoaVO documentoAssinadoPessoaAluno) {
	 		try {
				getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosRejeicao(documentoAssinadoPessoaAluno);
			} catch (Exception e) {
				try {
//					getFacadeFactory().getRegistroExecucaoJobFacade().incluir(new RegistroExecucaoJobVO("registrarIndeferimentoContratoPorAluno_visaoAluno", getMatriculaPeriodoVO().getMatricula().concat("-").concat(e.getMessage()) ,new Date()));
					setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
				} catch (Exception e1) {
					setMensagemDetalhada(MSG_TELA.msg_erro.name(), e1.getMessage());	
				}
			}
	 	}
	 
	 public void registrarIndeferimentoContratoPorAluno() {
			try {
				if (Uteis.isAtributoPreenchido(getMotivoRejeicao())) {
					getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().filter(
							documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO))
					.forEach(documentoAssinadoPessoaAluno -> {
						documentoAssinadoPessoaAluno.setDataRejeicao(new Date());
						documentoAssinadoPessoaAluno.setMotivoRejeicao(getMotivoRejeicao());
						documentoAssinadoPessoaAluno.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
						atualizarDadosRejeitado(documentoAssinadoPessoaAluno);
					});
					setDocumentoRejeitado(Boolean.TRUE);
					setMensagemID("msg_dados_gravados");
 				} else {
 					throw new Exception("O MOTIVO REJEIÇÃO dever ser informado");
 				}
			} catch (Exception e) {
					setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		
	 
	 
		
		public void registrarAssinaturaContratoPorAluno() {
			try {
				getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream()
				.filter(documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) || documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO) || documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO) || (documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_LEGAL) ))
				.forEach(documentoAssinadoPessoaAluno -> {
								documentoAssinadoPessoaAluno.setAssinarDocumento(Boolean.TRUE);
								documentoAssinadoPessoaAluno.setLongitude(getLongitude());
								documentoAssinadoPessoaAluno.setLatitude(getLatitude());
					});
				String arquivoTemp = getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getLocalUploadArquivoFixo() + File.separator + getDocumentoAssinadoVO().getArquivo().getPastaBaseArquivo() + File.separator + getDocumentoAssinadoVO().getArquivo().getNome(), getDocumentoAssinadoVO().getArquivo().getNome());
				File fileAssinar = new File(arquivoTemp);
				getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarContrato(getDocumentoAssinadoVO(), fileAssinar, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
				setDocumentoRejeitado(Boolean.FALSE);
				setMensagemID("msg_dados_gravados");
			} catch (Exception e) {
				try {
//					getFacadeFactory().getRegistroExecucaoJobFacade().incluir(new RegistroExecucaoJobVO("registrarAssinaturaContratoPorAluno_visaoAluno", getMatriculaPeriodoVO().getMatricula().concat("-").concat(e.getMessage()) ,new Date()));
					setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
				} catch (Exception e1) {
					setMensagemDetalhada(MSG_TELA.msg_erro.name(), e1.getMessage());
				}
			}
		}
		
		public void realizarDownloadArquivoProvedorCertisin()  {
			try {
				DocumentoAssinadoVO obj = (DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItens");
				realizarDownloadArquivoProvedorCertisin(obj);			
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}

	public void realizarDownloadArquivoProvedorTechCert()  {
		try {
			DocumentoAssinadoVO obj = (DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItens");
			realizarDownloadArquivoProvedorTechCert(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
		
		public void realizarDownloadArquivoProvedorCertisin(DocumentoAssinadoVO obj) throws Exception  {							
				getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(obj, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
				realizarDownloadArquivo(obj.getArquivo());			
		}

	public void realizarDownloadArquivoProvedorTechCert(DocumentoAssinadoVO obj) throws Exception  {
		getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(obj, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
		realizarDownloadArquivo(obj.getArquivo());
	}
		
		public String getUrlDonloadSV() {
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				return "location.href='../../DownloadSV'";
			}else {
				return "location.href='../DownloadSV'";
			}
		}
		
		private Boolean versaoAntiga;
		private Boolean versaoNova;

		public Boolean getVersaoAntiga() {
			if (versaoAntiga == null) {
				versaoAntiga = false;
			}
			return versaoAntiga;
		}
		
		public void setVersaoAntiga(Boolean versaoAntiga) {
			this.versaoAntiga = versaoAntiga;
		}
		
		public Boolean getVersaoNova() {
			if (versaoNova == null) {
				versaoNova = false;
			}
			return versaoNova;
		}
		
		public void setVersaoNova(Boolean versaoNova) {
			this.versaoNova = versaoNova;
		}
		
		public void mudarLayoutConsulta() {
			setVersaoAntiga(true);
			setVersaoNova(false);
			try {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), Matricula.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", getUsuarioLogado());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void mudarLayoutConsulta2() {
			setVersaoAntiga(false);
			setVersaoNova(true);
			try {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), Matricula.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", getUsuarioLogado());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private Boolean permitirAlterarDataMatricula;
		
		public Boolean getPermitirAlterarDataMatricula() {
			if (permitirAlterarDataMatricula == null) {
				try {
					permitirAlterarDataMatricula = getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("PermitirAlterarDataMatricula", getUsuarioLogado());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return permitirAlterarDataMatricula;
		}
		
		public void setPermitirAlterarDataMatricula(Boolean permitirAlterarDataMatricula) {
			this.permitirAlterarDataMatricula = permitirAlterarDataMatricula;
		}
		
	
	public void limparDocumentacaoMatriculaVerso() {
		try {
			if (!Uteis.isAtributoPreenchido(getDocumetacaoMatriculaVO().getArquivoVOVerso().getCodigo()) && Uteis.isAtributoPreenchido(getDocumetacaoMatriculaVO().getArquivoVOVerso().getNome())) {
				String arquivoExterno = "";
				arquivoExterno = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getDocumetacaoMatriculaVO().getArquivoVOVerso().getPastaBaseArquivo() + File.separator + (getDocumetacaoMatriculaVO().getArquivoVOVerso().getNome());
				File arquivo = new File(arquivoExterno);
				if (arquivo.exists()) {
					arquivo.delete();
				}
			}
			getDocumetacaoMatriculaVO().setArquivoVOVerso(new ArquivoVO());
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Boolean getPermitirGerarContratoMatrila() {
		if (permitirGerarContratoMatrila == null) {
			permitirGerarContratoMatrila = true;
		}
		return permitirGerarContratoMatrila;
	}
	
	public void setPermitirGerarContratoMatrila(Boolean permitirGerarContratoMatrila) {
		this.permitirGerarContratoMatrila = permitirGerarContratoMatrila;
	}
	
	public Boolean getDocumentoPendente() {
 		if (Uteis.isAtributoPreenchido(getDocumentoAssinadoVO())) {
 			if (getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))) {
 				return Boolean.TRUE;
 			} else {
 				return Boolean.FALSE;
 			}
 		}
 		return Boolean.FALSE;
 	}

	public Boolean getRealizandoNovaMatriculaAlunoPartindoTransferenciaInterna() {
		if(realizandoNovaMatriculaAlunoPartindoTransferenciaInterna==null) {
			realizandoNovaMatriculaAlunoPartindoTransferenciaInterna =Boolean.FALSE;
		}
		return realizandoNovaMatriculaAlunoPartindoTransferenciaInterna;
	}

	public void setRealizandoNovaMatriculaAlunoPartindoTransferenciaInterna(
			Boolean realizandoNovaMatriculaAlunoPartindoTransferenciaInterna) {
		this.realizandoNovaMatriculaAlunoPartindoTransferenciaInterna = realizandoNovaMatriculaAlunoPartindoTransferenciaInterna;
	}
	
}