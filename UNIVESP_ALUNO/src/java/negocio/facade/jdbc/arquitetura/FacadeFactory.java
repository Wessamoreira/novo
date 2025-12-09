
package negocio.facade.jdbc.arquitetura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import integracoes.mestregr.ProcessamentoFilaThreadsStrategy;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirArquivoVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirFuncionarioVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirPerfilAcessoVO;
import negocio.comuns.blackboard.HistoricoNotaBlackboardVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.formula.interpreters.SQLInterpreter;
import negocio.interfaces.academico.*;
import negocio.interfaces.administrativo.AgrupamentoUnidadeEnsinoInterfaceFacade;
import negocio.interfaces.administrativo.AgrupamentoUnidadeEnsinoItemInterfaceFacade;
import negocio.interfaces.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade;
import negocio.interfaces.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade;
import negocio.interfaces.administrativo.AtendimentoInterfaceFacade;

import negocio.interfaces.administrativo.CampanhaMarketingInterfaceFacade;
import negocio.interfaces.administrativo.CampanhaMarketingMidiaInterfaceFacade;
import negocio.interfaces.administrativo.CampanhaMidiaInterfaceFacade;
import negocio.interfaces.administrativo.CampanhaRSLogInterfaceFacade;
import negocio.interfaces.administrativo.CargoInterfaceFacade;
import negocio.interfaces.administrativo.CategoriaGEDInterfaceFacade;
import negocio.interfaces.administrativo.ComunicacaoInternaArquivoInterfaceFacade;
import negocio.interfaces.administrativo.ComunicacaoInternaInterfaceFacade;
import negocio.interfaces.administrativo.ComunicadoInternoDestinatarioInterfaceFacade;
import negocio.interfaces.administrativo.ComunicadoInternoRegistroLeituraInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoAparenciaSistemaInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoAtendimentoFuncionarioInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoAtendimentoInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoAtualizacaoCadastralInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoCandidatoProcessoSeletivoInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoGeralSistemaInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoLdapInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoMobileInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoSeiBlackboardDominioInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoSeiBlackboardInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoSeiGsuiteInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoSeiGsuiteUnidadeEnsinoInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoTCCArtefatoInterfaceFacade;
import negocio.interfaces.administrativo.ConfiguracaoTCCInterfaceFacade;
import negocio.interfaces.administrativo.DepartamentoInterfaceFacade;
import negocio.interfaces.administrativo.FiltroPainelGestorAcademicoInterfaceFacade;
import negocio.interfaces.administrativo.FiltroPersonalizadoInterfaceFacade;
import negocio.interfaces.administrativo.FiltroPersonalizadoOpcaoInterfaceFacade;

import negocio.interfaces.administrativo.FormacaoAcademicaInterfaceFacade;
import negocio.interfaces.administrativo.FormacaoExtraCurricularInterfaceFacade;
import negocio.interfaces.administrativo.FraseInspiracaoInterfaceFacade;

import negocio.interfaces.administrativo.FuncionarioGrupoDestinatariosInterfaceFacade;
import negocio.interfaces.administrativo.FuncionarioInterfaceFacade;
import negocio.interfaces.administrativo.GestaoEnvioMensagemAutomaticaInterfaceFacade;
import negocio.interfaces.administrativo.IntegracaoSymplictyInterfaceFacade;

import negocio.interfaces.administrativo.LayoutRelatorioSeiDecidirCampoInterface;
import negocio.interfaces.administrativo.LogFuncionarioInterfaceFacade;
import negocio.interfaces.administrativo.OcorrenciaLGPDInterfaceFacade;
import negocio.interfaces.administrativo.PainelGestorInterfaceFacade;
import negocio.interfaces.administrativo.PersonalizacaoMensagemAutomaticaInterfaceFacade;
import negocio.interfaces.administrativo.PersonalizacaoMensagemAutomaticaUnidadeEnsinoInterfaceFacade;
import negocio.interfaces.administrativo.PessoaPreInscricaoCursoInterfaceFacade;
import negocio.interfaces.administrativo.PreferenciaSistemaUsuarioInterfaceFacade;
import negocio.interfaces.administrativo.TipagemOuvidoriaInterfaceFacade;
import negocio.interfaces.administrativo.TipoMidiaCaptacaoInterfaceFacade;
import negocio.interfaces.administrativo.UnidadeEnsinoCursoCentroResultadoInterfaceFacade;
import negocio.interfaces.administrativo.UnidadeEnsinoCursoInterfaceFacade;
import negocio.interfaces.administrativo.UnidadeEnsinoInterfaceFacade;
import negocio.interfaces.administrativo.UnidadeEnsinoNivelEducacionalCentroResultadoInterfaceFacade;
import negocio.interfaces.administrativo.UnidadeEnsinoTipoRequerimentoCentroResultadoInterfaceFacade;
import negocio.interfaces.administrativo.VisaoInterfaceFacade;
import negocio.interfaces.arquitetura.ControleAcessoInterfaceFacade;
import negocio.interfaces.arquitetura.DashboardInterfaceFacade;
import negocio.interfaces.arquitetura.EmailFacadeInterface;
import negocio.interfaces.arquitetura.FavoritoInterfaceFacade;
import negocio.interfaces.arquitetura.FuncaoBloqueadaInterfaceFacade;
import negocio.interfaces.arquitetura.LdapInterfaceFacade;
import negocio.interfaces.arquitetura.LogAlteracaoSenhaInterfaceFacade;
import negocio.interfaces.arquitetura.LogTriggerInterfaceFacade;
import negocio.interfaces.arquitetura.NovidadeSeiInterfaceFacade;
import negocio.interfaces.arquitetura.OperacaoFuncionalidadeInterfaceFacade;
import negocio.interfaces.arquitetura.PerfilAcessoInterfaceFacade;
import negocio.interfaces.arquitetura.PermissaoInterfaceFacade;
import negocio.interfaces.arquitetura.PesquisaPadraoAlunoInterfaceFacade;
import negocio.interfaces.arquitetura.RegistroWebserviceInterfaceFacade;
import negocio.interfaces.arquitetura.SMSFacadeInterface;
import negocio.interfaces.arquitetura.SimularAcessoAlunoInterfaceFacade;
import negocio.interfaces.arquitetura.SolicitarAlterarSenhaInterfaceFacede;
import negocio.interfaces.arquitetura.TratamentoErroInterfaceFacade;
import negocio.interfaces.arquitetura.UsuarioInterfaceFacade;
import negocio.interfaces.arquitetura.UsuarioPerfilAcessoInterfaceFacade;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalCursoInterfaceFacade;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalInterfaceFacade;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalPessoaAvaliadaInterfaceFacade;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalPresencialItemRespostaInterfaceFacade;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalPresencialRespostaInterfaceFacade;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalRespondenteInterfaceFacade;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalUnidadeEnsinoInterfaceFacade;
import negocio.interfaces.avaliacaoinst.RespostaAvaliacaoInstitucionalDWInterfaceFacade;
import negocio.interfaces.avaliacaoinst.RespostaAvaliacaoInstitucionalParcialInterfaceFacade;

import negocio.interfaces.basico.ArtefatoAjudaInterfaceFacade;
import negocio.interfaces.basico.BairroInterfaceFacade;

import negocio.interfaces.basico.CidadeInterfaceFacade;
import negocio.interfaces.basico.ConfiguracaoGEDInterfaceFacade;
import negocio.interfaces.basico.ConfiguracaoGedOrigemInterfaceFacade;
import negocio.interfaces.basico.ConfiguracoesInterfaceFacade;

import negocio.interfaces.basico.DataComemorativaInterfaceFacade;
import negocio.interfaces.basico.DocumetacaoPessoaInterfaceFacade;
import negocio.interfaces.basico.EnderecoInterfaceFacade;
import negocio.interfaces.basico.EstadoInterfaceFacade;
import negocio.interfaces.basico.FeriadoInterfaceFacade;
import negocio.interfaces.basico.LayoutEtiquetaInterfaceFacade;
import negocio.interfaces.basico.LayoutEtiquetaTagInterfaceFacade;
import negocio.interfaces.basico.LinksUteisInterfaceFacade;
import negocio.interfaces.basico.PaizInterfaceFacade;
import negocio.interfaces.basico.PessoaEmailInstitucionalInterfaceFacade;
import negocio.interfaces.basico.PessoaInterfaceFacade;
import negocio.interfaces.basico.QuestaoTCCInterfaceFacade;
import negocio.interfaces.basico.ScriptExecutadoInterfaceFacade;
import negocio.interfaces.basico.UsuarioLinksUteisInterfaceFacade;

import negocio.interfaces.biblioteca.BibliotecaExternaInterfaceFacade;
import negocio.interfaces.biblioteca.BibliotecaInterfaceFacade;

//import negocio.interfaces.biblioteca.CatalogoInterfaceFacade;

import negocio.interfaces.biblioteca.ConfiguracaoBibliotecaInterfaceFacade;
import negocio.interfaces.biblioteca.ConfiguracaoBibliotecaNivelEducacionalInterface;

import negocio.interfaces.biblioteca.ImpressoraInterfaceFacade;

import negocio.interfaces.biblioteca.LogTransferenciaBibliotecaExemplarInterfaceFacade;

import negocio.interfaces.biblioteca.PoolImpressaoInterfaceFacade;

import negocio.interfaces.biblioteca.UnidadeEnsinoBibliotecaInterfaceFacade;
import negocio.interfaces.blackboard.BlackboardFechamentoNotaOperacaoInterfaceFacade;
import negocio.interfaces.blackboard.HistoricoNotaBlackboardInterfaceFacade;
import negocio.interfaces.blackboard.LogOperacaoEnsalamentoBlackboardInterfaceFacade;
import negocio.interfaces.blackboard.SalaAulaBlackboardInterfaceFacade;
import negocio.interfaces.blackboard.SalaAulaBlackboardOperacaoInterfaceFacade;
import negocio.interfaces.blackboard.SalaAulaBlackboardPessoaInterfaceFacade;
import negocio.interfaces.ead.AnotacaoDisciplinaInterfaceFacade;
import negocio.interfaces.ead.AtividadeDiscursivaInteracaoInterfaceFacade;
import negocio.interfaces.ead.AtividadeDiscursivaInterfaceFacade;
import negocio.interfaces.ead.AtividadeDiscursivaRespostaAlunoInterfaceFacade;
import negocio.interfaces.ead.AvaliacaoOnlineInterfaceFacade;
import negocio.interfaces.ead.AvaliacaoOnlineMatriculaInterfaceFacade;
import negocio.interfaces.ead.AvaliacaoOnlineMatriculaQuestaoInterfaceFacade;
import negocio.interfaces.ead.AvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade;
import negocio.interfaces.ead.AvaliacaoOnlineQuestaoInterfaceFacade;
import negocio.interfaces.ead.AvaliacaoOnlineTemaAssuntoInterfaceFacade;
import negocio.interfaces.ead.ConfiguracaoEADInterfaceFacade;
import negocio.interfaces.ead.ConteudoRegistroAcessoInterfaceFacade;
import negocio.interfaces.ead.DuvidaProfessorInteracaoInterfaceFacade;
import negocio.interfaces.ead.DuvidaProfessorInterfaceFacade;
import negocio.interfaces.ead.GestaoEventoConteudoTurmaAvaliacaoPBLInterfaceFacade;
import negocio.interfaces.ead.GestaoEventoConteudoTurmaInteracaoAtaInterfaceFacade;
import negocio.interfaces.ead.GestaoEventoConteudoTurmaInterfaceFacade;
import negocio.interfaces.ead.GestaoEventoConteudoTurmaResponsavelAtaInterfaceFacade;
import negocio.interfaces.ead.GraficoAproveitamentoAvaliacaoInterfaceFacade;
import negocio.interfaces.ead.ItemParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade;
import negocio.interfaces.ead.ListaExercicioInterfaceFacade;
import negocio.interfaces.ead.MonitorConhecimentoInterfaceFacade;
import negocio.interfaces.ead.MonitorConhecimentoPBLInterfaceFacade;
import negocio.interfaces.ead.MonitoramentoAlunosEADInterfaceFacade;
import negocio.interfaces.ead.NotaConceitoAvaliacaoPBLInterfaceFacade;
import negocio.interfaces.ead.OpcaoRespostaQuestaoInterfaceFacade;
import negocio.interfaces.ead.ParametrosGraficoComparativoAvaliacoesOnlinesInterfaceFacade;
import negocio.interfaces.ead.ParametrosGraficoComparativoMeusColegasInterfaceFacade;
import negocio.interfaces.ead.ParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade;
import negocio.interfaces.ead.ProgramacaoTutoriaOnlineInterfaceFacade;
import negocio.interfaces.ead.ProgramacaoTutoriaOnlineProfessorInterfaceFacade;
import negocio.interfaces.ead.QuestaoAssuntoInterfaceFacade;
import negocio.interfaces.ead.QuestaoConteudoFacade;
import negocio.interfaces.ead.QuestaoInterfaceFacade;
import negocio.interfaces.ead.QuestaoListaExercicioInterfaceFacade;
import negocio.interfaces.ead.TurmaDisciplinaConteudoFacade;
import negocio.interfaces.estagio.ConcedenteInterfaceFacade;
import negocio.interfaces.estagio.ConfiguracaoEstagioObrigatorioFuncionarioInterfaceFacade;
import negocio.interfaces.estagio.ConfiguracaoEstagioObrigatorioInterfaceFacade;
import negocio.interfaces.estagio.GrupoPessoaInterfaceFacade;
import negocio.interfaces.estagio.GrupoPessoaItemInterfaceFacade;
import negocio.interfaces.estagio.MotivosPadroesEstagioInterfaceFacade;
import negocio.interfaces.estagio.TipoConcedenteInterfaceFacade;
import negocio.interfaces.financeiro.AlteracaoPlanoFinanceiroAlunoInterfaceFacade;
import negocio.interfaces.financeiro.TextoPadraoInterfaceFacade;
import negocio.interfaces.financeiro.TextoPadraoTagInterfaceFacade;
import negocio.interfaces.gsuite.PessoaGsuiteInterfaceFacade;
import negocio.interfaces.moodle.OperacaoMoodleInterfaceFacade;
import negocio.interfaces.processosel.InscricaoInterfaceFacade;

import negocio.interfaces.processosel.PerguntaInterfaceFacade;
import negocio.interfaces.processosel.PerguntaItemInterfaceFacade;
import negocio.interfaces.processosel.PerguntaQuestionarioInterfaceFacade;
import negocio.interfaces.processosel.ProcSeletivoInterfaceFacade;
import negocio.interfaces.processosel.QuestionarioAlunoInterfaceFacade;
import negocio.interfaces.processosel.QuestionarioInterfaceFacade;
import negocio.interfaces.processosel.RespostaPerguntaAlunoInterfaceFacade;
import negocio.interfaces.processosel.RespostaPerguntaInterfaceFacade;
import negocio.interfaces.protocolo.AlterarResponsavelRequerimentoInterfaceFacade;
import negocio.interfaces.protocolo.CidTipoRequerimentoInterfaceFacade;
import negocio.interfaces.protocolo.ControleCorrespondenciaInterfaceFacade;
import negocio.interfaces.protocolo.RequerimentoCidTipoRequerimentoIntefaceFacade;
import negocio.interfaces.protocolo.RequerimentoDisciplinaInterfaceFacade;
import negocio.interfaces.protocolo.RequerimentoDisciplinasAproveitadasInterfaceFacade;
import negocio.interfaces.protocolo.RequerimentoHistoricoInterfaceFacade;
import negocio.interfaces.protocolo.RequerimentoInterfaceFacade;
import negocio.interfaces.protocolo.SituacaoRequerimentoDepartamentoInterfaceFacade;
import negocio.interfaces.protocolo.TipoRequerimentoCursoInterfaceFacade;
import negocio.interfaces.protocolo.TipoRequerimentoCursoTransferenciaInterfaceFacade;
import negocio.interfaces.protocolo.TipoRequerimentoDepartamentoFuncionarioInterfaceFacade;
import negocio.interfaces.protocolo.TipoRequerimentoDepartamentoInterfaceFacade;
import negocio.interfaces.protocolo.TipoRequerimentoInterfaceFacade;
import negocio.interfaces.protocolo.TipoRequerimentoSituacaoDepartamentoInterfaceFacade;
import negocio.interfaces.protocolo.TipoRequerimentoTurmaInterfaceFacade;
import negocio.interfaces.protocolo.TipoRequerimentoUnidadeEnsinoInterfaceFacade;
import negocio.interfaces.secretaria.AtividadeComplementarMatriculaInterfaceFacade;
import negocio.interfaces.secretaria.CalendarioAgrupamentoDisciplinaInterfaceFacade;
import negocio.interfaces.secretaria.CalendarioAgrupamentoTccInterfaceFacade;

import negocio.interfaces.sentry.ServicoIntegracaoSentryInterfaceFacade;
import relatorio.negocio.interfaces.academico.AtividadeComplementarRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.CertificadoCursoExtensaoRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.ComprovanteRenovacaoMatriculaRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.ComunicadoDebitoDocumentosAlunoRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.DadosMatriculaRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.DebitoDocumentosAlunoRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.DisciplinaGradeRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.DisciplinaRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.DownloadRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.GradeCurricularAlunoRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.HistoricoAlunoRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.ParametroRelatorioInterfaceFacade;
import relatorio.negocio.interfaces.academico.PlanoDisciplinaRelInterfaceFacade;
import relatorio.negocio.interfaces.academico.RequerimentoRelInterfaceFacade;
import relatorio.negocio.interfaces.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelInterfaceFacade;
import relatorio.negocio.interfaces.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade;
import relatorio.negocio.interfaces.avaliacaoInst.QuestionarioRelInterfaceFacade;

@Service
@Scope("singleton")
@Lazy
public class FacadeFactory {

	@Autowired
	private TipagemOuvidoriaInterfaceFacade tipagemOuvidoriaFacade;
	
	
	
	
	private AtendimentoInterfaceFacade atendimentoFacade;
	
	
	
	
	
	private AtendimentoInteracaoDepartamentoInterfaceFacade atendimentoInteracaoDepartamentoFacade;
	
	@Autowired
	private AtendimentoInteracaoSolicitanteInterfaceFacade atendimentoInteracaoSolicitanteFacade;
	@Autowired
	private ConfiguracaoAtendimentoInterfaceFacade configuracaoAtendimentoFacade;
	@Autowired
	private ConfiguracaoAtendimentoFuncionarioInterfaceFacade configuracaoAtendimentoFuncionarioFacade;
	@Autowired
	private ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade configuracaoAtendimentoUnidadeEnsinoFacade;
	@Autowired
	private PainelGestorInterfaceFacade painelGestorFacade;
	@Autowired
	private PaizInterfaceFacade paizFacade;
	@Autowired
	private TurnoHorarioInterfaceFacade turnoHorarioFacade;
	@Autowired
	private LinksUteisInterfaceFacade linksUteisFacade;
	@Autowired
	private UsuarioLinksUteisInterfaceFacade usuarioLinksUteisFacade;
	@Autowired
	private PessoaInterfaceFacade pessoaFacade;
	@Autowired
	private MatriculaInterfaceFacade matriculaFacade;

	@Autowired
	private MatriculaPeriodoInterfaceFacade matriculaPeriodoFacade;

	@Autowired
	private TrabalhoConclusaoCursoInteracaoInterfaceFacade trabalhoConclusaoCursoInteracaoFacade;

	public TrabalhoConclusaoCursoInteracaoInterfaceFacade getTrabalhoConclusaoCursoInteracaoFacade() {
		return trabalhoConclusaoCursoInteracaoFacade;
	}

	@Autowired
	private FuncionarioInterfaceFacade funcionarioFacade;

	@Autowired
	private UnidadeEnsinoInterfaceFacade unidadeEnsinoFacade;

	@Autowired
	private CancelamentoInterfaceFacade cancelamentoFacade;
	@Autowired
	private TrancamentoInterfaceFacade trancamentoFacade;
//

	@Autowired
	private UnidadeEnsinoCursoCentroResultadoInterfaceFacade unidadeEnsinoCursoCentroResultadoFacade;

	@Autowired
	private UnidadeEnsinoNivelEducacionalCentroResultadoInterfaceFacade unidadeEnsinoNivelEducacionalCentroResultadoFacade;

	@Autowired
	private UnidadeEnsinoTipoRequerimentoCentroResultadoInterfaceFacade unidadeEnsinoTipoRequerimentoCentroResultadoFacade;

	@Autowired
	private RequerimentoInterfaceFacade requerimentoFacade;
	@Autowired
	private RequerimentoDisciplinasAproveitadasInterfaceFacade requerimentoDisciplinasAproveitadasFacade;

	@Autowired
	private EmailFacadeInterface emailFacade;
	@Autowired
	private SMSFacadeInterface smsFacade;
	@Autowired
	private UnidadeEnsinoCursoInterfaceFacade unidadeEnsinoCursoFacade;

	@Autowired
	private CidadeInterfaceFacade cidadeFacade;

	@Autowired
	private ConfiguracoesInterfaceFacade configuracoesFacade;

	@Autowired
	private FormacaoAcademicaInterfaceFacade formacaoAcademicaFacade;
	@Autowired
	private PessoaPreInscricaoCursoInterfaceFacade pessoaPreInscricaoCursoFacade;
	@Autowired
	private DisciplinasInteresseInterfaceFacade disciplinasInteresseFacade;

	@Autowired
	private FiliacaoInterfaceFacade filiacaoFacade;
	@Autowired
	private MatriculaPeriodoTurmaDisciplinaInterfaceFacade matriculaPeriodoTurmaDisciplinaFacade;
	@Autowired
	private PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade preMatriculaPeriodoTurmaDisciplinaFacade;

	@Autowired
	private DocumetacaoMatriculaInterfaceFacade documetacaoMatriculaFacade;
	@Autowired
	private DocumetacaoPessoaInterfaceFacade documetacaoPessoaFacade;

	@Autowired
	private TipoCategoriaInterfaceFacade tipoCategoriaFacade;
	@Autowired
	private CategoriaTurmaInterfaceFacade categoriaTurmaFacade;

	@Autowired
	private UsuarioInterfaceFacade usuarioFacade;
	@Autowired
	private FavoritoInterfaceFacade favoritoFacade;
	@Autowired
	private ConfiguracaoGeralSistemaInterfaceFacade configuracaoGeralSistemaFacade;

	@Autowired
	private TipoMidiaCaptacaoInterfaceFacade tipoMidiaCaptacaoFacade;
	@Autowired
	private TurnoInterfaceFacade turnoFacade;
	@Autowired
	private CursoInterfaceFacade cursoFacade;
	@Autowired
	private CursoTurnoInterfaceFacade cursoTurnoFacade;

	@Autowired
	private ArquivoInterfaceFacade arquivoFacade;
	@Autowired
	private DocumentoAssinadoInterfaceFacade documentoAssinadoFacade;
	@Autowired
	private DocumentoAssinadoPessoaInterfaceFacade documentoAssinadoPessoaFacade;
	@Autowired
	private DownloadInterfaceFacade downloadFacade;
	@Autowired
	private HistoricoInterfaceFacade historicoFacade;

	@Autowired
	private EstadoInterfaceFacade estadoFacade;
	@Autowired
	private ConfiguracaoAcademicoInterfaceFacade configuracaoAcademicoFacade;

	@Autowired
	private ArquivoHelper arquivoHelper;

	@Autowired
	private TurmaInterfaceFacade turmaFacade;

	@Autowired
	private GradeCurricularInterfaceFacade gradeCurricularFacade;
	@Autowired
	private PeriodoLetivoInterfaceFacade periodoLetivoFacade;

	@Autowired
	private DepartamentoInterfaceFacade departamentoFacade;

	@Autowired
	private DisciplinaInterfaceFacade disciplinaFacade;

	@Autowired
	private EixoCursoInterfaceFacade eixoCursoFacade;

	@Autowired
	private TipoDocumentoInterfaceFacade tipoDeDocumentoFacade;
	@Autowired
	private TipoDocumentoEquivalenteInterfaceFacade tipoDocumentoEquivalenteFacade;

	@Autowired
	private PerfilAcessoInterfaceFacade perfilAcessoFacade;
	@Autowired
	private AvaliacaoInstitucionalInterfaceFacade avaliacaoInstitucionalFacade;

	@Autowired
	private TransferenciaEntradaInterfaceFacade transferenciaEntradaFacade;
	@Autowired
	private TransferenciaSaidaInterfaceFacade transferenciaSaidaFacade;

	@Autowired
	private CargoInterfaceFacade cargoFacade;
	@Autowired
	private ConfiguracaoAtualizacaoCadastralInterfaceFacade configuracaoAtualizacaoCadastralFacade;

	private FraseInspiracaoInterfaceFacade fraseInspiracaoFacade;
	@Autowired
	private EnderecoInterfaceFacade enderecoFacade;
	@Autowired
	private VisaoInterfaceFacade visaoFacade;

	@Autowired
	private TextoPadraoInterfaceFacade textoPadraoFacade;
	@Autowired
	private TextoPadraoDeclaracaoInterfaceFacade textoPadraoDeclaracaoFacade;

	@Autowired
	private ColacaoGrauInterfaceFacade colacaoGrauFacade;

	@Autowired
	private MatriculaPeriodoHistoricoInterfaceFacade matriculaPeriodoHistoricoFacade;

	@Autowired
	private GradeDisciplinaInterfaceFacade gradeDisciplinaFacade;

	private ProgramacaoFormaturaInterfaceFacade programacaoFormaturaFacade;
	@Autowired
	private DisciplinaPreRequisitoInterfaceFacade disciplinaPreRequisitoFacade;

	private ProgramacaoFormaturaAlunoInterfaceFacade programacaoFormaturaAlunoFacade;

	private ProcessoMatriculaLogInterfaceFacade processoMatriculaLogFacade;
	@Autowired
	private ProcessoMatriculaInterfaceFacade processoMatriculaFacade;

	@Autowired
	private IndiceElasticInterfaceFacade indiceElasticFacade;

	@Autowired
	private TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade transferenciaEntradaDisciplinasAproveitadasFacade;

	@Autowired
	private ComprovanteRenovacaoMatriculaRelInterfaceFacade comprovanteRenovacaoMatriculaRelFacade;
	@Autowired
	private TurmaDisciplinaInterfaceFacade turmaDisciplinaFacade;

	private RegistroAulaInterfaceFacade registroAulaFacade;

	@Autowired
	private ConfiguracaoBibliotecaInterfaceFacade configuracaoBibliotecaFacade;

	@Autowired
	private TipoConcedenteInterfaceFacade tipoConcedenteFacade;
	@Autowired
	private ConcedenteInterfaceFacade concedenteFacade;
	@Autowired
	private ConfiguracaoEstagioObrigatorioInterfaceFacade configuracaoEstagioObrigatorioFacade;
	@Autowired
	private ConfiguracaoEstagioObrigatorioFuncionarioInterfaceFacade configuracaoEstagioObrigatorioFuncionarioFacade;
	@Autowired
	private MotivosPadroesEstagioInterfaceFacade motivosPadroesEstagioFacade;
	@Autowired
	private GradeCurricularEstagioInterfaceFacade gradeCurricularEstagioFacade;
	@Autowired
	private GrupoPessoaInterfaceFacade grupoPessoaFacade;
	@Autowired
	private GrupoPessoaItemInterfaceFacade grupoPessoaItemFacade;

	@Autowired
	private BibliotecaInterfaceFacade bibliotecaFacade;

	@Autowired
	private PlanoCursoInterfaceFacade planoCursoFacade;

	@Autowired
	private TipoRequerimentoInterfaceFacade tipoRequerimentoFacade;

	private ReativacaoMatriculaInterfaceFacade reativacaoMatriculaFacade;
	@Autowired
	private ComunicacaoInternaInterfaceFacade comunicacaoInternaFacade;

	@Autowired
	private PeriodoLetivoAtivoUnidadeEnsinoCursoInterfaceFacade periodoLetivoAtivoUnidadeEnsinoCursoFacade;
	@Autowired
	private PeriodoLetivoAtivoUnidadeEnsinoCursoLogInterfaceFacade periodoLetivoAtivoUnidadeEnsinoCursoLogFacade;

	@Autowired
	private AbonoFaltaInterfaceFacade abonoFaltaFacade;
	@Autowired
	private ConteudoPlanejamentoInterfaceFacade conteudoPlanejamentoFacade;

	@Autowired
	private DisciplinaEquivalenteInterfaceFacade disciplinaEquivalenteFacade;

	@Autowired
	private DocumentacaoCursoInterfaceFacade documentacaoCursoFacade;
	@Autowired
	private MaterialCursoInterfaceFacade materialCursoFacade;
	@Autowired
	private MaterialRequerimentoInterfaceFacade materialRequerimentoFacade;
	@Autowired
	private MaterialAlunoInterfaceFacade materialAlunoFacade;

	@Autowired
	private MaterialUnidadeEnsinoInterfaceFacade materialUnidadeEnsinoFacade;

	@Autowired
	private NumeroMatriculaInterfaceFacade numeroMatriculaFacade;

	
	private PeriodoDisciplinaInterfaceFacade periodoDisciplinaFacade;

	@Autowired
	private ReferenciaBibliograficaInterfaceFacade referenciaBibliograficaFacade;
	@Autowired
	private TurmaAgrupadaInterfaceFacade turmaAgrupadaFacade;

	private TurmaAberturaInterfaceFacade turmaAberturaFacade;

	private CampanhaMarketingInterfaceFacade campanhaMarketingFacade;
	private CampanhaMarketingMidiaInterfaceFacade campanhaMarketingMidiaFacade;
	@Autowired
	private ComunicadoInternoDestinatarioInterfaceFacade comunicadoInternoDestinatarioFacade;
	@Autowired
	private ComunicadoInternoRegistroLeituraInterfaceFacade comunicadoInternoRegistroLeituraFacade;
	@Autowired
	private PermissaoInterfaceFacade permissaoFacade;
	@Autowired
	private UsuarioPerfilAcessoInterfaceFacade usuarioPerfilAcessoFacade;
	@Autowired
	private RespostaAvaliacaoInstitucionalDWInterfaceFacade respostaAvaliacaoInstitucionalDWFacade;
	@Autowired
	private BairroInterfaceFacade bairroFacade;

	@Autowired
	private TextoPadraoTagInterfaceFacade textoPadraoTagFacade;

	private ControleCorrespondenciaInterfaceFacade controleCorrespondenciaFacade;

	private ComunicadoDebitoDocumentosAlunoRelInterfaceFacade comunicadoDebitoDocumentosAlunoRelFacade;
	@Autowired
	private DadosMatriculaRelInterfaceFacade dadosMatriculaRelFacade;

	private DebitoDocumentosAlunoRelInterfaceFacade debitoDocumentosAlunoRelFacade;

	@Autowired
	private DisciplinaRelInterfaceFacade disciplinaRelFacade;
	@Autowired
	private DownloadRelInterfaceFacade downloadRelFacade;

	private GradeCurricularAlunoRelInterfaceFacade gradeCurricularAlunoRelFacade;
	@Autowired
	private HistoricoAlunoRelInterfaceFacade historicoAlunoRelFacade;

	@Autowired
	private AvaliacaoInstitucionalRelInterfaceFacade avaliacaoInstitucionalRelFacade;

	@Autowired
	private AproveitamentoDisciplinaInterfaceFacade aproveitamentoDisciplinaFacade;
	@Autowired
	private DisciplinasAproveitadasInterfaceFacade disciplinaAproveitadasFacade;

	@Autowired
	private FeriadoInterfaceFacade feriadoFacade;

	private InclusaoExclusaoDisciplinaInterfaceFacade inclusaoExclusaoDisciplina;

	@Autowired
	private CursoCoordenadorInterfaceFacade cursoCoordenadorFacade;

	private LogFechamentoInterfaceFacade logFechamentoFacade;

	@Autowired
	private LayoutPadraoInterfaceFacade layoutPadraoFacade;

	@Autowired
	private CertificadoCursoExtensaoRelInterfaceFacade certificadoCursoExtensaoRelFacade;
	@Autowired
	private DisciplinaGradeRelInterfaceFacade disciplinaGradeRelFacade;

	@Autowired
	private ArtefatoAjudaInterfaceFacade artefatoAjudaFacade;

	@Autowired
	private AutorizacaoCursoInterfaceFacade autorizacaoCursoFacade;

	@Autowired
	private IdentificacaoEstudantilInterfaceFacade identificacaoEstudantilFacade;

	private RegistroAulaNotaInterfaceFacade registroAulaNotaFacade;

	@Autowired
	private UnidadeEnsinoBibliotecaInterfaceFacade unidadeEnsinoBibliotecaFacade;

	private FuncionarioGrupoDestinatariosInterfaceFacade funcionarioGrupoDestinatariosFacade;

	
	private LogRegistroAulaInterfaceFacade logRegistroAulaFacade;

	@Autowired
	private LogLancamentoNotaInterfaceFacade logLancamentoNotaFacade;

	@Autowired
	private UploadArquivosComunsInterfaceFacade uploadArquivosComunsFacade;

	@Autowired
	private ImpressaoContratoInterfaceFacade impressaoContratoFacade;

	private TurmaAtualizacaoDisciplinaLogInterfaceFacade turmaAtualizacaoDisciplinaLogFacade;

	@Autowired
	private ArquivoLogInterfaceFacade arquivoLogFacade;
	@Autowired
	private BibliotecaExternaInterfaceFacade bibliotecaExternaFacade;
	@Autowired
	private RequerimentoRelInterfaceFacade requerimentoRelFacade;

	@Autowired
	private AvaliacaoInstitucionalPresencialRespostaInterfaceFacade avaliacaoInstitucionalPresencialRespostaFacade;
	@Autowired
	private AvaliacaoInstitucionalPresencialItemRespostaInterfaceFacade avaliacaoInstitucionalPresencialItemRespostaFacade;
	@Autowired
	private ObservacaoComplementarInterfaceFacade observacaoComplementarFacade;

	@Autowired
	private MotivoCancelamentoTrancamentoInterfaceFacade motivoCancelamentoTrancamentoFacade;

	@Autowired
	private FormacaoExtraCurricularInterfaceFacade formacaoExtraCurricularFacade;

	private CampanhaMidiaInterfaceFacade campanhaMidiaFacade;

	private TitulacaoCursoInterfaceFacade titulacaoCursoFacade;

	private ItemTitulacaoCursoInterfaceFacade itemTitulacaoCursoFacade;

	@Autowired
	private DisciplinaCompostaInterfaceFacade disciplinaCompostaFacade;
	@Autowired
	private AtividadeComplementarInterfaceFacade atividadeComplementarFacade;
	@Autowired
	private AtividadeComplementarMatriculaInterfaceFacade atividadeComplementarMatriculaFacade;

	private LogFuncionarioInterfaceFacade logFuncionarioFacade;

	@Autowired
	private PlanoDisciplinaRelInterfaceFacade planoDisciplinaRelFacade;

	@Autowired
	private QuestionarioRelInterfaceFacade questionarioRelFacade;

	@Autowired
	private TipoRequerimentoDepartamentoInterfaceFacade tipoRequerimentoDepartamentoFacade;
	@Autowired
	private RequerimentoHistoricoInterfaceFacade requerimentoHistoricoFacade;

	private InclusaoDisciplinaForaGradeInterfaceFacade inclusaoDisciplinaForaGradeFacade;

	
	private DisciplinaForaGradeInterfaceFacade disciplinaForaGradeFacade;

	@Autowired
	private TransferenciaEntradaRegistroAulaFrequenciaInterfaceFacade transferenciaEntradaRegistroAulaFrequenciaFacade;

	private DisciplinaAproveitadaAlteradaMatriculaInterfaceFacade disciplinaAproveitadaAlteradaMatriculaFacade;
	@Autowired
	private TipoAdvertenciaInterfaceFacade tipoAdvertenciaFacade;
	@Autowired
	private AdvertenciaInterfaceFacade advertenciaFacade;

	private HistoricoGradeAnteriorAlteradaInterfaceFacade historicoGradeAnteriorAlteradaFacade;

	@Autowired
	private TipoAtividadeComplementarInterfaceFacade tipoAtividadeComplementarFacade;
	@Autowired
	private GradeCurricularTipoAtividadeComplementarInterfaceFacade gradeCurricularTipoAtividadeComplementarFacade;
	@Autowired
	private RegistroAtividadeComplementarInterfaceFacade registroAtividadeComplementarFacade;
	@Autowired
	private RegistroAtividadeComplementarMatriculaInterfaceFacade registroAtividadeComplementarMatriculaFacade;
	@Autowired
	private AcompanhamentoAtividadeComplementarInterfaceFacade acompanhamentoAtividadeComplementarFacade;

	@Autowired
	private ParametroRelatorioInterfaceFacade parametroRelatorioFacade;

	@Autowired
	private LogTriggerInterfaceFacade logTriggerInterfaceFacade;
	@Autowired
	private TurmaDisciplinaInclusaoSugeridaInterfaceFacade turmaDisciplinaInclusaoSugeridaInterfaceFacade;

	@Autowired
	private TipoRequerimentoUnidadeEnsinoInterfaceFacade tipoRequerimentoUnidadeEnsinoFacade;
	@Autowired
	private PoliticaDivulgacaoMatriculaOnlineInterfaceFacade politicaDivulgacaoMatriculaOnlineInterfaceFacade;
	@Autowired
	private PoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade politicaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade;

	private RenovacaoMatriculaTurmaMatriculaPeriodoInterfaceFacade renovacaoMatriculaTurmaMatriculaPeriodoFacade;

	private RenovacaoMatriculaTurmaInterfaceFacade renovacaoMatriculaTurmaFacade;

	@Autowired
	private AnotacaoDisciplinaInterfaceFacade anotacaoDisciplinaInterfaceFacade;

	@Autowired
	private AtividadeDiscursivaInterfaceFacade atividadeDiscursivaInterfaceFacade;
	@Autowired
	private AtividadeDiscursivaRespostaAlunoInterfaceFacade atividadeDiscursivaRespostaAlunoInterfaceFacade;
	@Autowired
	private AtividadeDiscursivaInteracaoInterfaceFacade atividadeDiscursivaInteracaoInterfaceFacade;

	@Autowired
	private AvaliacaoOnlineInterfaceFacade avaliacaoOnlineInterfaceFacade;
	@Autowired
	private AvaliacaoOnlineQuestaoInterfaceFacade avaliacaoOnlineQuestaoInterfaceFacade;
	@Autowired
	private AvaliacaoOnlineMatriculaInterfaceFacade avaliacaoOnlineMatriculaInterfaceFacade;
	@Autowired
	private AvaliacaoOnlineMatriculaQuestaoInterfaceFacade avaliacaoOnlineMatriculaQuestaoInterfaceFacade;
	@Autowired
	private AvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade avaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade;

	@Autowired
	private ProgramacaoTutoriaOnlineInterfaceFacade programacaoTutoriaOnlineInterfaceFacade;
	@Autowired
	private ProgramacaoTutoriaOnlineProfessorInterfaceFacade programacaoTutoriaOnlineProfessorInterfaceFacade;

	private LayoutRelatorioSeiDecidirCampoInterface layoutRelatorioSEIDecidirCampoInterfaceFacade;
	@Autowired
	private ConfiguracaoBibliotecaNivelEducacionalInterface configuracaoBibliotecaNivelEducacionalFacade;

	@Autowired
	private ConfiguracaoAcademicaHistoricoInterface configuracaoAcademicaHistoricoInterface;
	@Autowired
	private QuestaoConteudoFacade questaoConteudoFacade;
	@Autowired
	private TurmaDisciplinaConteudoFacade turmaDisciplinaConteudoFacade;

	@Autowired
	private SolicitarAlterarSenhaInterfaceFacede solicitarAlterarSenhaInterfaceFacede;
	@Autowired
	private TemaAssuntoInterfaceFacade temaAssuntoFacade;
	@Autowired
	private TemaAssuntoDisciplinaInterfaceFacade temaAssuntoDisciplinaFacade;
	@Autowired
	private QuestaoAssuntoInterfaceFacade questaoAssuntoFacade;

	@Autowired
	private ParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade parametrosMonitoramentoAvaliacaoOnlineFacade;
	@Autowired
	private ItemParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade itemParametrosMonitoramentoAvaliacaoOnlineFacade;
	@Autowired
	private GraficoAproveitamentoAvaliacaoInterfaceFacade graficoAproveitamentoAvaliacaoFacade;
	@Autowired
	private ParametrosGraficoComparativoAvaliacoesOnlinesInterfaceFacade graficoComparativoAvaliacoesOnlinesFacade;

	@Autowired
	private MonitorConhecimentoInterfaceFacade monitorConhecimentoFacade;
	@Autowired
	private ParametrosGraficoComparativoMeusColegasInterfaceFacade parametrosGraficoComparativoMeusColegasFacade;
	@Autowired
	private MonitoramentoAlunosEADInterfaceFacade monitoramentoAlunosEADFacade;

	@Autowired
	private TipoRequerimentoCursoInterfaceFacade tipoRequerimentoCursoFacade;

	@Autowired
	private AlterarResponsavelRequerimentoInterfaceFacade alterarResponsavelRequerimentoFacade;

	@Autowired
	private SimularAcessoAlunoInterfaceFacade simularAcessoAlunoFacade;

	@Autowired
	private NotaConceitoAvaliacaoPBLInterfaceFacade notaConceitoAvaliacaoPBLFacade;
	@Autowired
	private GestaoEventoConteudoTurmaInterfaceFacade gestaoEventoConteudoTurmaFacade;
	@Autowired
	private GestaoEventoConteudoTurmaAvaliacaoPBLInterfaceFacade gestaoEventoConteudoTurmaAvaliacaoPBLFacade;
	@Autowired
	private GestaoEventoConteudoTurmaResponsavelAtaInterfaceFacade gestaoEventoConteudoTurmaResponsavelAtaFacade;
	@Autowired
	private GestaoEventoConteudoTurmaInteracaoAtaInterfaceFacade gestaoEventoConteudoTurmaInteracaoAtaFacade;

	@Autowired
	private CalendarioAberturaRequerimentoInterfaceFacade calendarioAberturaRequerimentoFacade;

	@Autowired
	private TipoRequerimentoTurmaInterfaceFacade tipoRequerimentoTurmaFacade;

	private ConfiguracaoMobileInterfaceFacade configuracaoMobileFacade;

	private ConfiguracaoSeiGsuiteInterfaceFacade configuracaoSeiGsuiteFacade;

	private ConfiguracaoSeiGsuiteUnidadeEnsinoInterfaceFacade configuracaoSeiGsuiteUnidadeEnsinoFacade;

	@Autowired
	private PessoaEmailInstitucionalInterfaceFacade pessoaEmailInstitucionalFacade;

	@Autowired
	private ConfiguracaoSeiBlackboardInterfaceFacade configuracaoSeiBlackboardFacade;

	@Autowired
	private ConfiguracaoSeiBlackboardDominioInterfaceFacade configuracaoSeiBlackboardDominioFacade;

	@Autowired
	private MonitorConhecimentoPBLInterfaceFacade monitorConhecimentoPBLFacade;

	@Autowired
	private AvaliacaoInstitucionalPessoaAvaliadaInterfaceFacade avaliacaoInstitucionalPessoaAvaliadaFacade;

	private CampanhaRSLogInterfaceFacade campanhaRSLogFacade;

	@Autowired
	private ConfiguracaoCandidatoProcessoSeletivoInterfaceFacade configuracaoCandidatoProcessoSeletivoInterfaceFacade;

	@Autowired
	private CategoriaGEDInterfaceFacade categoriaGEDInterfaceFacade;

	@Autowired
	private DocumentacaoGEDInterfaceFacade documentacaoGEDInterfaceFacade;

	@Autowired
	private TipoDocumentoGEDInterfaceFacade tipoDocumentoGEDInterfaceFacade;

	@Autowired
	private InteracaoRequerimentoHistoricoInterfaceFacade interacaoRequerimentoHistoricoFacade;

	private PendenciaLiberacaoMatriculaInterfaceFacade pendenciaLiberacaoMatriculaInterfaceFacade;

	@Autowired
	private LdapInterfaceFacade ldapFacade;

	@Autowired
	private PesquisaPadraoAlunoInterfaceFacade pesquisaPadraoAlunoFacade;

	private ScriptExecutadoInterfaceFacade scriptExecutadoInterfaceFacade;

	@Autowired
	@Lazy
	private SQLInterpreter sqlInterpreter;

	@Autowired
	private AvaliacaoInstitucionalRespondenteInterfaceFacade avaliacaoInstitucionalRespondenteInterfaceFacade;

	@Autowired
	private ConfiguracaoGEDInterfaceFacade configuracaoGEDFacade;

	@Autowired
	private AssinarDocumentoEntregueInterfaceFacade assinarDocumentoEntregueInterfaceFacade;

	@Autowired
	private TurmaDisciplinaNotaParcialInterfaceFacade turmaDisciplinaNotaParcialInterfaceFacade;

	@Autowired
	private HistoricoNotaParcialInterfaceFacade historicoNotaParcialInterfaceFacade;

	@Autowired
	private PendenciaTipoDocumentoTipoRequerimentoInterfaceFacade pendenciaTipoDocumentoTipoRequerimentoInterfaceFacade;

	@Autowired
	private SalaAulaBlackboardInterfaceFacade salaAulaBlackboardFacade;

	@Autowired
	private SalaAulaBlackboardPessoaInterfaceFacade salaAulaBlackboardPessoaFacade;

	@Autowired
	private LogRegistroOperacoesInterfaceFacade logRegistroOperacoesFacade;

	@Autowired
	private ConfiguracaoLdapInterfaceFacade configuracaoLdapInterfaceFacade;

	@Autowired
	private OcorrenciaLGPDInterfaceFacade ocorrenciaLGPDInterfaceFacade;

	@Autowired
	private BlackboardFechamentoNotaOperacaoInterfaceFacade blackboardFechamentoNotaOperacaoInterfaceFacade;

	@Qualifier("expedicaoDiplomaDigital_1_05")
	private ExpedicaoDiplomaDigitalInterfaceFacade expedicaoDiplomaDigital_1_05_interfaceFacade;

	@Autowired
	private IntegracaoMestreGRInterfaceFacade integracaoMestreGRInterfaceFacade;

	@Autowired
	private ProcessamentoFilaThreadsStrategy processamentoFilaThreadsStrategy;

	@Autowired
	private CalendarioRelatorioFinalFacilitadorInterfaceFacade calendarioRelatorioFinalFacilitadorInterfaceFacade;

	@Autowired
	private RelatorioFinalFacilitadorInterfaceFacade relatorioFinalFacilitadorInterfaceFacade;

	private IntegracaoSymplictyInterfaceFacade integracaoSymplictyInterfaceFacade;

	@Autowired
	private ServicoIntegracaoSentryInterfaceFacade servicoIntegracaoSentryInterfaceFacade;

	@Autowired
	private RespostaPerguntaInterfaceFacade respostaPerguntaFacade;

	@Autowired
	private PerguntaItemInterfaceFacade perguntaItemInterfaceFacade;

	@Autowired
	private PerguntaQuestionarioInterfaceFacade perguntaQuestionarioFacade;

	@Autowired
	private QuestionarioInterfaceFacade questionarioFacade;

	@Autowired
	private QuestionarioAlunoInterfaceFacade questionarioAlunoFacade;

	@Autowired
	private ProcSeletivoInterfaceFacade procSeletivoFacade;

	@Autowired
	private PerguntaInterfaceFacade perguntaFacade;

	private RespostaPerguntaAlunoInterfaceFacade respostaPerguntaAlunoFacade;

	@Autowired
	private AtividadeComplementarRelInterfaceFacade atividadeComplementarRelFacade;

	private InscricaoInterfaceFacade inscricaoFacade;

	@Autowired
	private HistoricoNotaBlackboardInterfaceFacade<HistoricoNotaBlackboardVO> historicoNotaBlackboardFacade;

	@Autowired
	private OperacaoMoodleInterfaceFacade operacaoMoodleInterfaceFacade;

	@Autowired
	@Qualifier("controleAcesso")
	private ControleAcessoInterfaceFacade controleAcessoFacade;

	private PessoaGsuiteInterfaceFacade pessoaGsuiteFacade;

	@Autowired
	private AgrupamentoUnidadeEnsinoItemInterfaceFacade agrupamentoUnidadeEnsinoItemFacade;

	@Autowired
	private PersonalizacaoMensagemAutomaticaInterfaceFacade personalizacaoMensagemAutomaticaFacade;

	@Autowired
	private GestaoEnvioMensagemAutomaticaInterfaceFacade gestaoEnvioMensagemAutomaticaFacade;

	
	private ProcessoMatriculaCalendarioInterfaceFacade processoMatriculaCalendarioFacade;

	public ProcessoMatriculaCalendarioInterfaceFacade getProcessoMatriculaCalendarioFacade() {
		return processoMatriculaCalendarioFacade;
	}

	public void setProcessoMatriculaCalendarioFacade(
			ProcessoMatriculaCalendarioInterfaceFacade processoMatriculaCalendarioFacade) {
		this.processoMatriculaCalendarioFacade = processoMatriculaCalendarioFacade;
	}

	public PersonalizacaoMensagemAutomaticaInterfaceFacade getPersonalizacaoMensagemAutomaticaFacade() {
		return personalizacaoMensagemAutomaticaFacade;
	}

	public void setPersonalizacaoMensagemAutomaticaFacade(
			PersonalizacaoMensagemAutomaticaInterfaceFacade personalizacaoMensagemAutomaticaFacade) {
		this.personalizacaoMensagemAutomaticaFacade = personalizacaoMensagemAutomaticaFacade;
	}

	public GestaoEnvioMensagemAutomaticaInterfaceFacade getGestaoEnvioMensagemAutomaticaFacade() {
		return gestaoEnvioMensagemAutomaticaFacade;
	}

	public void setGestaoEnvioMensagemAutomaticaFacade(
			GestaoEnvioMensagemAutomaticaInterfaceFacade gestaoEnvioMensagemAutomaticaFacade) {
		this.gestaoEnvioMensagemAutomaticaFacade = gestaoEnvioMensagemAutomaticaFacade;
	}

	public PessoaGsuiteInterfaceFacade getPessoaGsuiteFacade() {
		return pessoaGsuiteFacade;
	}

	public ControleAcessoInterfaceFacade getControleAcessoFacade() {
		return controleAcessoFacade;
	}

	public OperacaoMoodleInterfaceFacade getOperacaoMoodleInterfaceFacade() {
		return operacaoMoodleInterfaceFacade;
	}

	public HistoricoNotaBlackboardInterfaceFacade<HistoricoNotaBlackboardVO> getHistoricoNotaBlackboardFacade() {
		return historicoNotaBlackboardFacade;
	}

	public AtividadeComplementarRelInterfaceFacade getAtividadeComplementarRelFacade() {
		return atividadeComplementarRelFacade;
	}

	public InscricaoInterfaceFacade getInscricaoFacade() {
		return inscricaoFacade;
	}

	public RespostaPerguntaAlunoInterfaceFacade getRespostaPerguntaAlunoFacade() {
		return respostaPerguntaAlunoFacade;
	}

	public AvaliacaoInstitucionalPessoaAvaliadaInterfaceFacade getAvaliacaoInstitucionalPessoaAvaliadaFacade() {
		return avaliacaoInstitucionalPessoaAvaliadaFacade;
	}

	public PerguntaInterfaceFacade getPerguntaFacade() {
		return perguntaFacade;
	}

	public ProcSeletivoInterfaceFacade getProcSeletivoFacade() {
		return procSeletivoFacade;
	}

	public QuestionarioAlunoInterfaceFacade getQuestionarioAlunoFacade() {
		return questionarioAlunoFacade;
	}

	public QuestionarioInterfaceFacade getQuestionarioFacade() {
		return questionarioFacade;
	}

	public PerguntaQuestionarioInterfaceFacade getPerguntaQuestionarioFacade() {
		return perguntaQuestionarioFacade;
	}

	public PerguntaItemInterfaceFacade getPerguntaItemInterfaceFacade() {
		return perguntaItemInterfaceFacade;
	}

	public RespostaPerguntaInterfaceFacade getRespostaPerguntaFacade() {
		return respostaPerguntaFacade;
	}

	public void setAvaliacaoInstitucionalCurso(
			AvaliacaoInstitucionalCursoInterfaceFacade avaliacaoInstitucionalCursoInterfaceFacade) {
		this.avaliacaoInstitucionalCurso = avaliacaoInstitucionalCursoInterfaceFacade;
	}

	public void setAvaliacaoInstitucionalPessoaAvaliadaFacade(
			AvaliacaoInstitucionalPessoaAvaliadaInterfaceFacade avaliacaoInstitucionalPessoaAvaliadaFacade) {
		this.avaliacaoInstitucionalPessoaAvaliadaFacade = avaliacaoInstitucionalPessoaAvaliadaFacade;
	}

	public MonitorConhecimentoPBLInterfaceFacade getMonitorConhecimentoPBLFacade() {
		return monitorConhecimentoPBLFacade;
	}

	public void setMonitorConhecimentoPBLFacade(MonitorConhecimentoPBLInterfaceFacade monitorConhecimentoPBLFacade) {
		this.monitorConhecimentoPBLFacade = monitorConhecimentoPBLFacade;
	}

	public TipagemOuvidoriaInterfaceFacade getTipagemOuvidoriaFacade() {
		return tipagemOuvidoriaFacade;
	}

	public void setTipagemOuvidoriaFacade(TipagemOuvidoriaInterfaceFacade tipagemOuvidoriaFacade) {
		this.tipagemOuvidoriaFacade = tipagemOuvidoriaFacade;
	}

	public AtendimentoInterfaceFacade getAtendimentoFacade() {
		return atendimentoFacade;
	}

	public void setAtendimentoFacade(AtendimentoInterfaceFacade atendimentoFacade) {
		this.atendimentoFacade = atendimentoFacade;
	}

	public AtendimentoInteracaoDepartamentoInterfaceFacade getAtendimentoInteracaoDepartamentoFacade() {
		return atendimentoInteracaoDepartamentoFacade;
	}

	public void setAtendimentoInteracaoDepartamentoFacade(
			AtendimentoInteracaoDepartamentoInterfaceFacade atendimentoInteracaoDepartamentoFacade) {
		this.atendimentoInteracaoDepartamentoFacade = atendimentoInteracaoDepartamentoFacade;
	}

	public AtendimentoInteracaoSolicitanteInterfaceFacade getAtendimentoInteracaoSolicitanteFacade() {
		return atendimentoInteracaoSolicitanteFacade;
	}

	public void setAtendimentoInteracaoSolicitanteFacade(
			AtendimentoInteracaoSolicitanteInterfaceFacade atendimentoInteracaoSolicitanteFacade) {
		this.atendimentoInteracaoSolicitanteFacade = atendimentoInteracaoSolicitanteFacade;
	}

	public ConfiguracaoAtendimentoInterfaceFacade getConfiguracaoAtendimentoFacade() {
		return configuracaoAtendimentoFacade;
	}

	public void setConfiguracaoAtendimentoFacade(ConfiguracaoAtendimentoInterfaceFacade configuracaoAtendimentoFacade) {
		this.configuracaoAtendimentoFacade = configuracaoAtendimentoFacade;
	}

	public ConfiguracaoAtendimentoFuncionarioInterfaceFacade getConfiguracaoAtendimentoFuncionarioFacade() {
		return configuracaoAtendimentoFuncionarioFacade;
	}

	public void setConfiguracaoAtendimentoFuncionarioFacade(
			ConfiguracaoAtendimentoFuncionarioInterfaceFacade configuracaoAtendimentoFuncionarioFacade) {
		this.configuracaoAtendimentoFuncionarioFacade = configuracaoAtendimentoFuncionarioFacade;
	}

	public ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade getConfiguracaoAtendimentoUnidadeEnsinoFacade() {
		return configuracaoAtendimentoUnidadeEnsinoFacade;
	}

	public void setConfiguracaoAtendimentoUnidadeEnsinoFacade(
			ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade configuracaoAtendimentoUnidadeEnsinoFacade) {
		this.configuracaoAtendimentoUnidadeEnsinoFacade = configuracaoAtendimentoUnidadeEnsinoFacade;
	}

	public IdentificacaoEstudantilInterfaceFacade getIdentificacaoEstudantilFacade() {
		return identificacaoEstudantilFacade;
	}

	public void setIdentificacaoEstudantilFacade(IdentificacaoEstudantilInterfaceFacade identificacaoEstudantilFacade) {
		this.identificacaoEstudantilFacade = identificacaoEstudantilFacade;
	}

	public FeriadoInterfaceFacade getFeriadoFacade() {
		return feriadoFacade;
	}

	public void setFeriadoFacade(FeriadoInterfaceFacade feriadoFacade) {
		this.feriadoFacade = feriadoFacade;
	}

	public void setPaizFacade(PaizInterfaceFacade paizFacade) {
		this.paizFacade = paizFacade;
	}

	public PaizInterfaceFacade getPaizFacade() {
		return paizFacade;
	}

	public PessoaInterfaceFacade getPessoaFacade() {
		return pessoaFacade;
	}

	public void setPessoaFacade(PessoaInterfaceFacade pessoaFacade) {
		this.pessoaFacade = pessoaFacade;
	}

	public MatriculaInterfaceFacade getMatriculaFacade() {
		return matriculaFacade;
	}

	public void setMatriculaFacade(MatriculaInterfaceFacade matriculaFacade) {
		this.matriculaFacade = matriculaFacade;
	}

	public FuncionarioInterfaceFacade getFuncionarioFacade() {
		return funcionarioFacade;
	}

	public void setFuncionarioFacade(FuncionarioInterfaceFacade funcionarioFacade) {
		this.funcionarioFacade = funcionarioFacade;
	}

	public UnidadeEnsinoInterfaceFacade getUnidadeEnsinoFacade() {
		return unidadeEnsinoFacade;
	}

	public void setUnidadeEnsinoFacade(UnidadeEnsinoInterfaceFacade unidadeEnsinoFacade) {
		this.unidadeEnsinoFacade = unidadeEnsinoFacade;
	}

	public CancelamentoInterfaceFacade getCancelamentoFacade() {
		return cancelamentoFacade;
	}

	public void setCancelamentoFacade(CancelamentoInterfaceFacade cancelamentoFacade) {
		this.cancelamentoFacade = cancelamentoFacade;
	}

	public TrancamentoInterfaceFacade getTrancamentoFacade() {
		return trancamentoFacade;
	}

	public void setTrancamentoFacade(TrancamentoInterfaceFacade trancamentoFacade) {
		this.trancamentoFacade = trancamentoFacade;
	}

	public UnidadeEnsinoCursoCentroResultadoInterfaceFacade getUnidadeEnsinoCursoCentroResultadoFacade() {
		return unidadeEnsinoCursoCentroResultadoFacade;
	}

	public void setUnidadeEnsinoCursoCentroResultadoFacade(
			UnidadeEnsinoCursoCentroResultadoInterfaceFacade unidadeEnsinoCentroResultadoFacade) {
		this.unidadeEnsinoCursoCentroResultadoFacade = unidadeEnsinoCentroResultadoFacade;
	}

	public UnidadeEnsinoNivelEducacionalCentroResultadoInterfaceFacade getUnidadeEnsinoNivelEducacionalCentroResultadoFacade() {
		return unidadeEnsinoNivelEducacionalCentroResultadoFacade;
	}

	public void setUnidadeEnsinoNivelEducacionalCentroResultadoFacade(
			UnidadeEnsinoNivelEducacionalCentroResultadoInterfaceFacade unidadeEnsinoNivelEducacionalCentroResultadoFacade) {
		this.unidadeEnsinoNivelEducacionalCentroResultadoFacade = unidadeEnsinoNivelEducacionalCentroResultadoFacade;
	}

	public UnidadeEnsinoTipoRequerimentoCentroResultadoInterfaceFacade getUnidadeEnsinoTipoRequerimentoCentroResultadoFacade() {
		return unidadeEnsinoTipoRequerimentoCentroResultadoFacade;
	}

	public void setUnidadeEnsinoTipoRequerimentoCentroResultadoFacade(
			UnidadeEnsinoTipoRequerimentoCentroResultadoInterfaceFacade unidadeEnsinoTipoRequerimentoCentroResultadoFacade) {
		this.unidadeEnsinoTipoRequerimentoCentroResultadoFacade = unidadeEnsinoTipoRequerimentoCentroResultadoFacade;
	}

	public RequerimentoInterfaceFacade getRequerimentoFacade() {
		return requerimentoFacade;
	}

	public void setRequerimentoFacade(RequerimentoInterfaceFacade requerimentoFacade) {
		this.requerimentoFacade = requerimentoFacade;
	}

	public RequerimentoDisciplinasAproveitadasInterfaceFacade getRequerimentoDisciplinasAproveitadasFacade() {
		return requerimentoDisciplinasAproveitadasFacade;
	}

	public void setRequerimentoDisciplinasAproveitadasFacade(
			RequerimentoDisciplinasAproveitadasInterfaceFacade requerimentoDisciplinasAproveitadasFacade) {
		this.requerimentoDisciplinasAproveitadasFacade = requerimentoDisciplinasAproveitadasFacade;
	}

	public UnidadeEnsinoCursoInterfaceFacade getUnidadeEnsinoCursoFacade() {
		return unidadeEnsinoCursoFacade;
	}

	public void setUnidadeEnsinoCursoFacade(UnidadeEnsinoCursoInterfaceFacade unidadeEnsinoCursoFacade) {
		this.unidadeEnsinoCursoFacade = unidadeEnsinoCursoFacade;
	}

	public AgrupamentoUnidadeEnsinoItemInterfaceFacade getAgrupamentoUnidadeEnsinoItemFacade() {
		return agrupamentoUnidadeEnsinoItemFacade;
	}

	public void setAgrupamentoUnidadeEnsinoItemFacade(
			AgrupamentoUnidadeEnsinoItemInterfaceFacade agrupamentoUnidadeEnsinoItemFacade) {
		this.agrupamentoUnidadeEnsinoItemFacade = agrupamentoUnidadeEnsinoItemFacade;
	}

	public CidadeInterfaceFacade getCidadeFacade() {
		return cidadeFacade;
	}

	public void setCidadeFacade(CidadeInterfaceFacade cidadeFacade) {
		this.cidadeFacade = cidadeFacade;
	}

	public ConfiguracoesInterfaceFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesInterfaceFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public FormacaoAcademicaInterfaceFacade getFormacaoAcademicaFacade() {
		return formacaoAcademicaFacade;
	}

	public void setFormacaoAcademicaFacade(FormacaoAcademicaInterfaceFacade formacaoAcademicaFacade) {
		this.formacaoAcademicaFacade = formacaoAcademicaFacade;
	}

	public DisciplinasInteresseInterfaceFacade getDisciplinasInteresseFacade() {
		return disciplinasInteresseFacade;
	}

	public void setDisciplinasInteresseFacade(DisciplinasInteresseInterfaceFacade disciplinasInteresseFacade) {
		this.disciplinasInteresseFacade = disciplinasInteresseFacade;
	}

	public FiliacaoInterfaceFacade getFiliacaoFacade() {
		return filiacaoFacade;
	}

	public void setFiliacaoFacade(FiliacaoInterfaceFacade filiacaoFacade) {
		this.filiacaoFacade = filiacaoFacade;
	}

	public MatriculaPeriodoTurmaDisciplinaInterfaceFacade getMatriculaPeriodoTurmaDisciplinaFacade() {
		return matriculaPeriodoTurmaDisciplinaFacade;
	}

	public void setMatriculaPeriodoTurmaDisciplinaFacade(
			MatriculaPeriodoTurmaDisciplinaInterfaceFacade matriculaPeriodoTurmaDisciplinaFacade) {
		this.matriculaPeriodoTurmaDisciplinaFacade = matriculaPeriodoTurmaDisciplinaFacade;
	}

	public PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade getPreMatriculaPeriodoTurmaDisciplinaFacade() {
		return preMatriculaPeriodoTurmaDisciplinaFacade;
	}

	public void setPreMatriculaPeriodoTurmaDisciplinaFacade(
			PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade preMatriculaPeriodoTurmaDisciplinaFacade) {
		this.preMatriculaPeriodoTurmaDisciplinaFacade = preMatriculaPeriodoTurmaDisciplinaFacade;
	}

	public DocumetacaoMatriculaInterfaceFacade getDocumetacaoMatriculaFacade() {
		return documetacaoMatriculaFacade;
	}

	public void setDocumetacaoMatriculaFacade(DocumetacaoMatriculaInterfaceFacade documetacaoMatriculaFacade) {
		this.documetacaoMatriculaFacade = documetacaoMatriculaFacade;
	}

	public DocumetacaoPessoaInterfaceFacade getDocumetacaoPessoaFacade() {
		return documetacaoPessoaFacade;
	}

	public void setDocumetacaoPessoaFacade(DocumetacaoPessoaInterfaceFacade documetacaoPessoaFacade) {
		this.documetacaoPessoaFacade = documetacaoPessoaFacade;
	}

	public TipoCategoriaInterfaceFacade getTipoCategoriaFacade() {
		return tipoCategoriaFacade;
	}

	public void setTipoCategoriaFacade(TipoCategoriaInterfaceFacade tipoCategoriaFacade) {
		this.tipoCategoriaFacade = tipoCategoriaFacade;
	}

	public CategoriaTurmaInterfaceFacade getCategoriaTurmaFacade() {
		return categoriaTurmaFacade;
	}

	public void setCategoriaTurmaFacade(CategoriaTurmaInterfaceFacade categoriaTurmaFacade) {
		this.categoriaTurmaFacade = categoriaTurmaFacade;
	}

	public UsuarioInterfaceFacade getUsuarioFacade() {
		return usuarioFacade;
	}

	public void setUsuarioFacade(UsuarioInterfaceFacade usuarioFacade) {
		this.usuarioFacade = usuarioFacade;
	}

	public FavoritoInterfaceFacade getFavoritoFacade() {
		return favoritoFacade;
	}

	public void setFavoritoFacade(FavoritoInterfaceFacade favoritoFacade) {
		this.favoritoFacade = favoritoFacade;
	}

	public ConfiguracaoGeralSistemaInterfaceFacade getConfiguracaoGeralSistemaFacade() {
		return configuracaoGeralSistemaFacade;
	}

	public void setConfiguracaoGeralSistemaFacade(
			ConfiguracaoGeralSistemaInterfaceFacade configuracaoGeralSistemaFacade) {
		this.configuracaoGeralSistemaFacade = configuracaoGeralSistemaFacade;
	}

	public TipoMidiaCaptacaoInterfaceFacade getTipoMidiaCaptacaoFacade() {
		return tipoMidiaCaptacaoFacade;
	}

	public void setTipoMidiaCaptacaoFacade(TipoMidiaCaptacaoInterfaceFacade tipoMidiaCaptacaoFacade) {
		this.tipoMidiaCaptacaoFacade = tipoMidiaCaptacaoFacade;
	}

	public TurnoInterfaceFacade getTurnoFacade() {
		return turnoFacade;
	}

	public void setTurnoFacade(TurnoInterfaceFacade turnoFacade) {
		this.turnoFacade = turnoFacade;
	}

	public CursoInterfaceFacade getCursoFacade() {
		return cursoFacade;
	}

	public void setCursoFacade(CursoInterfaceFacade cursoFacade) {
		this.cursoFacade = cursoFacade;
	}

	public CursoTurnoInterfaceFacade getCursoTurnoFacade() {
		return cursoTurnoFacade;
	}

	public void setCursoTurnoFacade(CursoTurnoInterfaceFacade cursoTurnoFacade) {
		this.cursoTurnoFacade = cursoTurnoFacade;
	}

	public ArquivoInterfaceFacade getArquivoFacade() {
		return arquivoFacade;
	}

	public void setArquivoFacade(ArquivoInterfaceFacade arquivoFacade) {
		this.arquivoFacade = arquivoFacade;
	}

	public DocumentoAssinadoInterfaceFacade getDocumentoAssinadoFacade() {
		return documentoAssinadoFacade;
	}

	public void setDocumentoAssinadoFacade(DocumentoAssinadoInterfaceFacade documentoAssinadoFacade) {
		this.documentoAssinadoFacade = documentoAssinadoFacade;
	}

	public DocumentoAssinadoPessoaInterfaceFacade getDocumentoAssinadoPessoaFacade() {
		return documentoAssinadoPessoaFacade;
	}

	public void setDocumentoAssinadoPessoaFacade(DocumentoAssinadoPessoaInterfaceFacade documentoAssinadoPessoaFacade) {
		this.documentoAssinadoPessoaFacade = documentoAssinadoPessoaFacade;
	}

	public DownloadInterfaceFacade getDownloadFacade() {
		return downloadFacade;
	}

	public void setDownloadFacade(DownloadInterfaceFacade downloadFacade) {
		this.downloadFacade = downloadFacade;
	}

	public HistoricoInterfaceFacade getHistoricoFacade() {
		return historicoFacade;
	}

	public void setHistoricoFacade(HistoricoInterfaceFacade historicoFacade) {
		this.historicoFacade = historicoFacade;
	}

	public EstadoInterfaceFacade getEstadoFacade() {
		return estadoFacade;
	}

	public void setEstadoFacade(EstadoInterfaceFacade estadoFacade) {
		this.estadoFacade = estadoFacade;
	}

	public ConfiguracaoAcademicoInterfaceFacade getConfiguracaoAcademicoFacade() {
		return configuracaoAcademicoFacade;
	}

	public void setConfiguracaoAcademicoFacade(ConfiguracaoAcademicoInterfaceFacade configuracaoAcademicoFacade) {
		this.configuracaoAcademicoFacade = configuracaoAcademicoFacade;
	}

	public TurmaInterfaceFacade getTurmaFacade() {
		return turmaFacade;
	}

	public void setMatriculaPeriodoFacade(MatriculaPeriodoInterfaceFacade matriculaPeriodoFacade) {
		this.matriculaPeriodoFacade = matriculaPeriodoFacade;
	}

	public void setTurmaFacade(TurmaInterfaceFacade turmaFacade) {
		this.turmaFacade = turmaFacade;
	}

	public GradeCurricularInterfaceFacade getGradeCurricularFacade() {
		return gradeCurricularFacade;
	}

	public void setGradeCurricularFacade(GradeCurricularInterfaceFacade gradeCurricularFacade) {
		this.gradeCurricularFacade = gradeCurricularFacade;
	}

	public PeriodoLetivoInterfaceFacade getPeriodoLetivoFacade() {
		return periodoLetivoFacade;
	}

	public void setPeriodoLetivoFacade(PeriodoLetivoInterfaceFacade periodoLetivoFacade) {
		this.periodoLetivoFacade = periodoLetivoFacade;
	}

	public DepartamentoInterfaceFacade getDepartamentoFacade() {
		return departamentoFacade;
	}

	public void setDepartamentoFacade(DepartamentoInterfaceFacade departamentoFacade) {
		this.departamentoFacade = departamentoFacade;
	}

	public DisciplinaInterfaceFacade getDisciplinaFacade() {
		return disciplinaFacade;
	}

	public void setDisciplinaFacade(DisciplinaInterfaceFacade disciplinaFacade) {
		this.disciplinaFacade = disciplinaFacade;
	}

	public TipoDocumentoEquivalenteInterfaceFacade getTipoDocumentoEquivalenteFacade() {
		return tipoDocumentoEquivalenteFacade;
	}

	public void setTipoDocumentoEquivalenteFacade(
			TipoDocumentoEquivalenteInterfaceFacade tipoDocumentoEquivalenteFacade) {
		this.tipoDocumentoEquivalenteFacade = tipoDocumentoEquivalenteFacade;
	}

	public TipoDocumentoInterfaceFacade getTipoDeDocumentoFacade() {
		return tipoDeDocumentoFacade;
	}

	public void setTipoDeDocumentoFacade(TipoDocumentoInterfaceFacade tipoDeDocumentoFacade) {
		this.tipoDeDocumentoFacade = tipoDeDocumentoFacade;
	}

	public PerfilAcessoInterfaceFacade getPerfilAcessoFacade() {
		return perfilAcessoFacade;
	}

	public void setPerfilAcessoFacade(PerfilAcessoInterfaceFacade perfilAcessoFacade) {
		this.perfilAcessoFacade = perfilAcessoFacade;
	}

	public AvaliacaoInstitucionalInterfaceFacade getAvaliacaoInstitucionalFacade() {
		return avaliacaoInstitucionalFacade;
	}

	public void setAvaliacaoInstitucionalFacade(AvaliacaoInstitucionalInterfaceFacade avaliacaoInstitucionalFacade) {
		this.avaliacaoInstitucionalFacade = avaliacaoInstitucionalFacade;
	}

	public TransferenciaEntradaInterfaceFacade getTransferenciaEntradaFacade() {
		return transferenciaEntradaFacade;
	}

	public void setTransferenciaEntradaFacade(TransferenciaEntradaInterfaceFacade transferenciaEntradaFacade) {
		this.transferenciaEntradaFacade = transferenciaEntradaFacade;
	}

	public TransferenciaSaidaInterfaceFacade getTransferenciaSaidaFacade() {
		return transferenciaSaidaFacade;
	}

	public void setTransferenciaSaidaFacade(TransferenciaSaidaInterfaceFacade transferenciaSaidaFacade) {
		this.transferenciaSaidaFacade = transferenciaSaidaFacade;
	}

	public CargoInterfaceFacade getCargoFacade() {
		return cargoFacade;
	}

	public void setCargoFacade(CargoInterfaceFacade cargoFacade) {
		this.cargoFacade = cargoFacade;
	}

	public FraseInspiracaoInterfaceFacade getFraseInspiracaoFacade() {
		return fraseInspiracaoFacade;
	}

	public void setFraseInspiracaoFacade(FraseInspiracaoInterfaceFacade fraseInspiracaoFacade) {
		this.fraseInspiracaoFacade = fraseInspiracaoFacade;
	}

	public EnderecoInterfaceFacade getEnderecoFacade() {
		return enderecoFacade;
	}

	public void setEnderecoFacade(EnderecoInterfaceFacade enderecoFacade) {
		this.enderecoFacade = enderecoFacade;
	}

	public VisaoInterfaceFacade getVisaoFacade() {
		return visaoFacade;
	}

	public void setVisaoFacade(VisaoInterfaceFacade visaoFacade) {
		this.visaoFacade = visaoFacade;
	}

	public TextoPadraoInterfaceFacade getTextoPadraoFacade() {
		return textoPadraoFacade;
	}

	public void setTextoPadraoFacade(TextoPadraoInterfaceFacade textoPadraoFacade) {
		this.textoPadraoFacade = textoPadraoFacade;
	}

	public ColacaoGrauInterfaceFacade getColacaoGrauFacade() {
		return colacaoGrauFacade;
	}

	public void setColacaoGrauFacade(ColacaoGrauInterfaceFacade colacaoGrauFacade) {
		this.colacaoGrauFacade = colacaoGrauFacade;
	}

	public MatriculaPeriodoHistoricoInterfaceFacade getMatriculaPeriodoHistoricoFacade() {
		return matriculaPeriodoHistoricoFacade;
	}

	public void setMatriculaPeriodoHistoricoFacade(
			MatriculaPeriodoHistoricoInterfaceFacade matriculaPeriodoHistoricoFacade) {
		this.matriculaPeriodoHistoricoFacade = matriculaPeriodoHistoricoFacade;
	}

	public GradeDisciplinaInterfaceFacade getGradeDisciplinaFacade() {
		return gradeDisciplinaFacade;
	}

	public void setGradeDisciplinaFacade(GradeDisciplinaInterfaceFacade gradeDisciplinaFacade) {
		this.gradeDisciplinaFacade = gradeDisciplinaFacade;
	}

	public ProgramacaoFormaturaInterfaceFacade getProgramacaoFormaturaFacade() {
		return programacaoFormaturaFacade;
	}

	public void setProgramacaoFormaturaFacade(ProgramacaoFormaturaInterfaceFacade programacaoFormaturaFacade) {
		this.programacaoFormaturaFacade = programacaoFormaturaFacade;
	}

	public DisciplinaPreRequisitoInterfaceFacade getDisciplinaPreRequisitoFacade() {
		return disciplinaPreRequisitoFacade;
	}

	public void setDisciplinaPreRequisitoFacade(DisciplinaPreRequisitoInterfaceFacade disciplinaPreRequisitoFacade) {
		this.disciplinaPreRequisitoFacade = disciplinaPreRequisitoFacade;
	}

	public ProgramacaoFormaturaAlunoInterfaceFacade getProgramacaoFormaturaAlunoFacade() {
		return programacaoFormaturaAlunoFacade;
	}

	public void setProgramacaoFormaturaAlunoFacade(
			ProgramacaoFormaturaAlunoInterfaceFacade programacaoFormaturaAlunoFacade) {
		this.programacaoFormaturaAlunoFacade = programacaoFormaturaAlunoFacade;
	}

	public ProcessoMatriculaInterfaceFacade getProcessoMatriculaFacade() {
		return processoMatriculaFacade;
	}

	public void setProcessoMatriculaFacade(ProcessoMatriculaInterfaceFacade processoMatriculaFacade) {
		this.processoMatriculaFacade = processoMatriculaFacade;
	}

	public TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade getTransferenciaEntradaDisciplinasAproveitadasFacade() {
		return transferenciaEntradaDisciplinasAproveitadasFacade;
	}

	public void setTransferenciaEntradaDisciplinasAproveitadasFacade(
			TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade transferenciaEntradaDisciplinasAproveitadasFacade) {
		this.transferenciaEntradaDisciplinasAproveitadasFacade = transferenciaEntradaDisciplinasAproveitadasFacade;
	}

	public ComprovanteRenovacaoMatriculaRelInterfaceFacade getComprovanteRenovacaoMatriculaRelFacade() {
		return comprovanteRenovacaoMatriculaRelFacade;
	}

	public void setComprovanteRenovacaoMatriculaRelFacade(
			ComprovanteRenovacaoMatriculaRelInterfaceFacade comprovanteRenovacaoMatriculaRelFacade) {
		this.comprovanteRenovacaoMatriculaRelFacade = comprovanteRenovacaoMatriculaRelFacade;
	}

	public TurmaDisciplinaInterfaceFacade getTurmaDisciplinaFacade() {
		return turmaDisciplinaFacade;
	}

	public void setTurmaDisciplinaFacade(TurmaDisciplinaInterfaceFacade turmaDisciplinaFacade) {
		this.turmaDisciplinaFacade = turmaDisciplinaFacade;
	}

	public RegistroAulaInterfaceFacade getRegistroAulaFacade() {
		return registroAulaFacade;
	}

	public void setRegistroAulaFacade(RegistroAulaInterfaceFacade registroAulaFacade) {
		this.registroAulaFacade = registroAulaFacade;
	}

	public ConfiguracaoBibliotecaInterfaceFacade getConfiguracaoBibliotecaFacade() {
		return configuracaoBibliotecaFacade;
	}

	public void setConfiguracaoBibliotecaFacade(ConfiguracaoBibliotecaInterfaceFacade configuracaoBibliotecaFacade) {
		this.configuracaoBibliotecaFacade = configuracaoBibliotecaFacade;
	}

	public BibliotecaInterfaceFacade getBibliotecaFacade() {
		return bibliotecaFacade;
	}

	public void setBibliotecaFacade(BibliotecaInterfaceFacade bibliotecaFacade) {
		this.bibliotecaFacade = bibliotecaFacade;
	}

	public PlanoCursoInterfaceFacade getPlanoCursoFacade() {
		return planoCursoFacade;
	}

	public void setPlanoCursoFacade(PlanoCursoInterfaceFacade planoCursoFacade) {
		this.planoCursoFacade = planoCursoFacade;
	}

	public TipoRequerimentoInterfaceFacade getTipoRequerimentoFacade() {
		return tipoRequerimentoFacade;
	}

	public void setTipoRequerimentoFacade(TipoRequerimentoInterfaceFacade tipoRequerimentoFacade) {
		this.tipoRequerimentoFacade = tipoRequerimentoFacade;
	}

	public ReativacaoMatriculaInterfaceFacade getReativacaoMatriculaFacade() {
		return reativacaoMatriculaFacade;
	}

	public void setReativacaoMatriculaFacade(ReativacaoMatriculaInterfaceFacade reativacaoMatriculaFacade) {
		this.reativacaoMatriculaFacade = reativacaoMatriculaFacade;
	}

	public ComunicacaoInternaInterfaceFacade getComunicacaoInternaFacade() {
		return comunicacaoInternaFacade;
	}

	public void setComunicacaoInternaFacade(ComunicacaoInternaInterfaceFacade comunicacaoInternaFacade) {
		this.comunicacaoInternaFacade = comunicacaoInternaFacade;
	}

	public PeriodoLetivoAtivoUnidadeEnsinoCursoInterfaceFacade getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade() {
		return periodoLetivoAtivoUnidadeEnsinoCursoFacade;
	}

	public void setPeriodoLetivoAtivoUnidadeEnsinoCursoFacade(
			PeriodoLetivoAtivoUnidadeEnsinoCursoInterfaceFacade periodoLetivoAtivoUnidadeEnsinoCursoFacade) {
		this.periodoLetivoAtivoUnidadeEnsinoCursoFacade = periodoLetivoAtivoUnidadeEnsinoCursoFacade;
	}

	public AbonoFaltaInterfaceFacade getAbonoFaltaFacade() {
		return abonoFaltaFacade;
	}

	public void setAbonoFaltaFacade(AbonoFaltaInterfaceFacade abonoFaltaFacade) {
		this.abonoFaltaFacade = abonoFaltaFacade;
	}

	public ConteudoPlanejamentoInterfaceFacade getConteudoPlanejamentoFacade() {
		return conteudoPlanejamentoFacade;
	}

	public void setConteudoPlanejamentoFacade(ConteudoPlanejamentoInterfaceFacade conteudoPlanejamentoFacade) {
		this.conteudoPlanejamentoFacade = conteudoPlanejamentoFacade;
	}

	public DisciplinaEquivalenteInterfaceFacade getDisciplinaEquivalenteFacade() {
		return disciplinaEquivalenteFacade;
	}

	public void setDisciplinaEquivalenteFacade(DisciplinaEquivalenteInterfaceFacade disciplinaEquivalenteFacade) {
		this.disciplinaEquivalenteFacade = disciplinaEquivalenteFacade;
	}

	public DocumentacaoCursoInterfaceFacade getDocumentacaoCursoFacade() {
		return documentacaoCursoFacade;
	}

	public void setDocumentacaoCursoFacade(DocumentacaoCursoInterfaceFacade documentacaoCursoFacade) {
		this.documentacaoCursoFacade = documentacaoCursoFacade;
	}

	public NumeroMatriculaInterfaceFacade getNumeroMatriculaFacade() {
		return numeroMatriculaFacade;
	}

	public void setNumeroMatriculaFacade(NumeroMatriculaInterfaceFacade numeroMatriculaFacade) {
		this.numeroMatriculaFacade = numeroMatriculaFacade;
	}

	public PeriodoDisciplinaInterfaceFacade getPeriodoDisciplinaFacade() {
		return periodoDisciplinaFacade;
	}

	public void setPeriodoDisciplinaFacade(PeriodoDisciplinaInterfaceFacade periodoDisciplinaFacade) {
		this.periodoDisciplinaFacade = periodoDisciplinaFacade;
	}

	public ReferenciaBibliograficaInterfaceFacade getReferenciaBibliograficaFacade() {
		return referenciaBibliograficaFacade;
	}

	public void setReferenciaBibliograficaFacade(ReferenciaBibliograficaInterfaceFacade referenciaBibliograficaFacade) {
		this.referenciaBibliograficaFacade = referenciaBibliograficaFacade;
	}

	public TurmaAgrupadaInterfaceFacade getTurmaAgrupadaFacade() {
		return turmaAgrupadaFacade;
	}

	public void setTurmaAgrupadaFacade(TurmaAgrupadaInterfaceFacade turmaAgrupadaFacade) {
		this.turmaAgrupadaFacade = turmaAgrupadaFacade;
	}

	public CampanhaMarketingInterfaceFacade getCampanhaMarketingFacade() {
		return campanhaMarketingFacade;
	}

	public void setCampanhaMarketingFacade(CampanhaMarketingInterfaceFacade campanhaMarketingFacade) {
		this.campanhaMarketingFacade = campanhaMarketingFacade;
	}

	public CampanhaMarketingMidiaInterfaceFacade getCampanhaMarketingMidiaFacade() {
		return campanhaMarketingMidiaFacade;
	}

	public void setCampanhaMarketingMidiaFacade(CampanhaMarketingMidiaInterfaceFacade campanhaMarketingMidiaFacade) {
		this.campanhaMarketingMidiaFacade = campanhaMarketingMidiaFacade;
	}

	public ComunicadoInternoDestinatarioInterfaceFacade getComunicadoInternoDestinatarioFacade() {
		return comunicadoInternoDestinatarioFacade;
	}

	public void setComunicadoInternoDestinatarioFacade(
			ComunicadoInternoDestinatarioInterfaceFacade comunicadoInternoDestinatarioFacade) {
		this.comunicadoInternoDestinatarioFacade = comunicadoInternoDestinatarioFacade;
	}

	public PermissaoInterfaceFacade getPermissaoFacade() {
		return permissaoFacade;
	}

	public void setPermissaoFacade(PermissaoInterfaceFacade permissaoFacade) {
		this.permissaoFacade = permissaoFacade;
	}

	public UsuarioPerfilAcessoInterfaceFacade getUsuarioPerfilAcessoFacade() {
		return usuarioPerfilAcessoFacade;
	}

	public void setUsuarioPerfilAcessoFacade(UsuarioPerfilAcessoInterfaceFacade usuarioPerfilAcessoFacade) {
		this.usuarioPerfilAcessoFacade = usuarioPerfilAcessoFacade;
	}

	public RespostaAvaliacaoInstitucionalDWInterfaceFacade getRespostaAvaliacaoInstitucionalDWFacade() {
		return respostaAvaliacaoInstitucionalDWFacade;
	}

	public void setRespostaAvaliacaoInstitucionalDWFacade(
			RespostaAvaliacaoInstitucionalDWInterfaceFacade respostaAvaliacaoInstitucionalDWFacade) {
		this.respostaAvaliacaoInstitucionalDWFacade = respostaAvaliacaoInstitucionalDWFacade;
	}

	public BairroInterfaceFacade getBairroFacade() {
		return bairroFacade;
	}

	public void setBairroFacade(BairroInterfaceFacade bairroFacade) {
		this.bairroFacade = bairroFacade;
	}

	public TextoPadraoTagInterfaceFacade getTextoPadraoTagFacade() {
		return textoPadraoTagFacade;
	}

	public void setTextoPadraoTagFacade(TextoPadraoTagInterfaceFacade textoPadraoTagFacade) {
		this.textoPadraoTagFacade = textoPadraoTagFacade;
	}

	public ControleCorrespondenciaInterfaceFacade getControleCorrespondenciaFacade() {
		return controleCorrespondenciaFacade;
	}

	public void setControleCorrespondenciaFacade(ControleCorrespondenciaInterfaceFacade controleCorrespondenciaFacade) {
		this.controleCorrespondenciaFacade = controleCorrespondenciaFacade;
	}

	public ComunicadoDebitoDocumentosAlunoRelInterfaceFacade getComunicadoDebitoDocumentosAlunoRelFacade() {
		return comunicadoDebitoDocumentosAlunoRelFacade;
	}

	public void setComunicadoDebitoDocumentosAlunoRelFacade(
			ComunicadoDebitoDocumentosAlunoRelInterfaceFacade comunicadoDebitoDocumentosAlunoRelFacade) {
		this.comunicadoDebitoDocumentosAlunoRelFacade = comunicadoDebitoDocumentosAlunoRelFacade;
	}

	public DadosMatriculaRelInterfaceFacade getDadosMatriculaRelFacade() {
		return dadosMatriculaRelFacade;
	}

	public void setDadosMatriculaRelFacade(DadosMatriculaRelInterfaceFacade dadosMatriculaRelFacade) {
		this.dadosMatriculaRelFacade = dadosMatriculaRelFacade;
	}

	public DebitoDocumentosAlunoRelInterfaceFacade getDebitoDocumentosAlunoRelFacade() {
		return debitoDocumentosAlunoRelFacade;
	}

	public void setDebitoDocumentosAlunoRelFacade(
			DebitoDocumentosAlunoRelInterfaceFacade debitoDocumentosAlunoRelFacade) {
		this.debitoDocumentosAlunoRelFacade = debitoDocumentosAlunoRelFacade;
	}

	public DisciplinaRelInterfaceFacade getDisciplinaRelFacade() {
		return disciplinaRelFacade;
	}

	public void setDisciplinaRelFacade(DisciplinaRelInterfaceFacade disciplinaRelFacade) {
		this.disciplinaRelFacade = disciplinaRelFacade;
	}

	public DownloadRelInterfaceFacade getDownloadRelFacade() {
		return downloadRelFacade;
	}

	public void setDownloadRelFacade(DownloadRelInterfaceFacade downloadRelFacade) {
		this.downloadRelFacade = downloadRelFacade;
	}

	public GradeCurricularAlunoRelInterfaceFacade getGradeCurricularAlunoRelFacade() {
		return gradeCurricularAlunoRelFacade;
	}

	public void setGradeCurricularAlunoRelFacade(GradeCurricularAlunoRelInterfaceFacade gradeCurricularAlunoRelFacade) {
		this.gradeCurricularAlunoRelFacade = gradeCurricularAlunoRelFacade;
	}

	public HistoricoAlunoRelInterfaceFacade getHistoricoAlunoRelFacade() {
		return historicoAlunoRelFacade;
	}

	public void setHistoricoAlunoRelFacade(HistoricoAlunoRelInterfaceFacade historicoAlunoRelFacade) {
		this.historicoAlunoRelFacade = historicoAlunoRelFacade;
	}

	public AvaliacaoInstitucionalRelInterfaceFacade getAvaliacaoInstitucionalRelFacade() {
		return avaliacaoInstitucionalRelFacade;
	}

	public void setAvaliacaoInstitucionalRelFacade(
			AvaliacaoInstitucionalRelInterfaceFacade avaliacaoInstitucionalRelFacade) {
		this.avaliacaoInstitucionalRelFacade = avaliacaoInstitucionalRelFacade;
	}

	public ComunicadoInternoRegistroLeituraInterfaceFacade getComunicadoInternoRegistroLeituraFacade() {
		return comunicadoInternoRegistroLeituraFacade;
	}

	public void setComunicadoInternoRegistroLeituraFacade(
			ComunicadoInternoRegistroLeituraInterfaceFacade comunicadoInternoRegistroLeituraFacade) {
		this.comunicadoInternoRegistroLeituraFacade = comunicadoInternoRegistroLeituraFacade;
	}

	/**
	 * @return the aproveitamentoDisciplinaFacade
	 */
	public AproveitamentoDisciplinaInterfaceFacade getAproveitamentoDisciplinaFacade() {
		return aproveitamentoDisciplinaFacade;
	}

	/**
	 * @param aproveitamentoDisciplinaFacade the aproveitamentoDisciplinaFacade to
	 *                                       set
	 */
	public void setAproveitamentoDisciplinaFacade(
			AproveitamentoDisciplinaInterfaceFacade aproveitamentoDisciplinaFacade) {
		this.aproveitamentoDisciplinaFacade = aproveitamentoDisciplinaFacade;
	}

	/**
	 * @return the disciplinaAproveitadasFacade
	 */
	public DisciplinasAproveitadasInterfaceFacade getDisciplinaAproveitadasFacade() {
		return disciplinaAproveitadasFacade;
	}

	/**
	 * @param disciplinaAproveitadasFacade the disciplinaAproveitadasFacade to set
	 */
	public void setDisciplinaAproveitadasFacade(DisciplinasAproveitadasInterfaceFacade disciplinaAproveitadasFacade) {
		this.disciplinaAproveitadasFacade = disciplinaAproveitadasFacade;
	}

	public EmailFacadeInterface getEmailFacade() {
		return emailFacade;
	}

	public void setEmailFacade(EmailFacadeInterface emailFacade) {
		this.emailFacade = emailFacade;
	}

	public InclusaoExclusaoDisciplinaInterfaceFacade getInclusaoExclusaoDisciplina() {
		return inclusaoExclusaoDisciplina;
	}

	public void setInclusaoExclusaoDisciplina(InclusaoExclusaoDisciplinaInterfaceFacade inclusaoExclusaoDisciplina) {
		this.inclusaoExclusaoDisciplina = inclusaoExclusaoDisciplina;
	}

	public CursoCoordenadorInterfaceFacade getCursoCoordenadorFacade() {
		return cursoCoordenadorFacade;
	}

	public void setCursoCoordenadorFacade(CursoCoordenadorInterfaceFacade cursoCoordenadorFacade) {
		this.cursoCoordenadorFacade = cursoCoordenadorFacade;
	}

	public void setLogFechamentoFacade(LogFechamentoInterfaceFacade logFechamentoFacade) {
		this.logFechamentoFacade = logFechamentoFacade;
	}

	public LogFechamentoInterfaceFacade getLogFechamentoFacade() {
		return logFechamentoFacade;
	}

	public TurnoHorarioInterfaceFacade getTurnoHorarioFacade() {
		return turnoHorarioFacade;
	}

	public void setTurnoHorarioFacade(TurnoHorarioInterfaceFacade turnoHorarioFacade) {
		this.turnoHorarioFacade = turnoHorarioFacade;
	}

	public ArquivoHelper getArquivoHelper() {
		return arquivoHelper;
	}

	public void setArquivoHelper(ArquivoHelper arquivoHelper) {
		this.arquivoHelper = arquivoHelper;
	}

	public PainelGestorInterfaceFacade getPainelGestorFacade() {
		return painelGestorFacade;
	}

	public void setPainelGestorFacade(PainelGestorInterfaceFacade painelGestorFacade) {
		this.painelGestorFacade = painelGestorFacade;
	}

	public void setLayoutPadraoFacade(LayoutPadraoInterfaceFacade layoutPadraoFacade) {
		this.layoutPadraoFacade = layoutPadraoFacade;
	}

	public LayoutPadraoInterfaceFacade getLayoutPadraoFacade() {
		return layoutPadraoFacade;
	}

	public CertificadoCursoExtensaoRelInterfaceFacade getCertificadoCursoExtensaoRelFacade() {
		return certificadoCursoExtensaoRelFacade;
	}

	public void setCertificadoCursoExtensaoRelFacade(
			CertificadoCursoExtensaoRelInterfaceFacade certificadoCursoExtensaoRelFacade) {
		this.certificadoCursoExtensaoRelFacade = certificadoCursoExtensaoRelFacade;
	}

	public DisciplinaGradeRelInterfaceFacade getDisciplinaGradeRelFacade() {
		return disciplinaGradeRelFacade;
	}

	public void setDisciplinaGradeRelFacade(DisciplinaGradeRelInterfaceFacade disciplinaGradeRelFacade) {
		this.disciplinaGradeRelFacade = disciplinaGradeRelFacade;
	}

	public ArtefatoAjudaInterfaceFacade getArtefatoAjudaFacade() {
		return artefatoAjudaFacade;
	}

	public void setArtefatoAjudaFacade(ArtefatoAjudaInterfaceFacade artefatoAjudaFacade) {
		this.artefatoAjudaFacade = artefatoAjudaFacade;
	}

	public AutorizacaoCursoInterfaceFacade getAutorizacaoCursoFacade() {
		return autorizacaoCursoFacade;
	}

	public void setAutorizacaoCursoFacade(AutorizacaoCursoInterfaceFacade autorizacaoCursoFacade) {
		this.autorizacaoCursoFacade = autorizacaoCursoFacade;
	}

	public MatriculaPeriodoInterfaceFacade getMatriculaPeriodoFacade() {
		return matriculaPeriodoFacade;
	}

	public void setRegistroAulaNotaFacade(RegistroAulaNotaInterfaceFacade registroAulaNotaFacade) {
		this.registroAulaNotaFacade = registroAulaNotaFacade;
	}

	public RegistroAulaNotaInterfaceFacade getRegistroAulaNotaFacade() {
		return registroAulaNotaFacade;
	}

	/**
	 * @param horarioProfessorDiaItemFacade the horarioProfessorDiaItemFacade to set
	 */

	public void setFuncionarioGrupoDestinatariosFacade(
			FuncionarioGrupoDestinatariosInterfaceFacade funcionarioGrupoDestinatariosFacade) {
		this.funcionarioGrupoDestinatariosFacade = funcionarioGrupoDestinatariosFacade;
	}

	public FuncionarioGrupoDestinatariosInterfaceFacade getFuncionarioGrupoDestinatariosFacade() {
		return funcionarioGrupoDestinatariosFacade;
	}

	public void setLogRegistroAulaFacade(LogRegistroAulaInterfaceFacade logRegistroAulaFacade) {
		this.logRegistroAulaFacade = logRegistroAulaFacade;
	}

	public LogRegistroAulaInterfaceFacade getLogRegistroAulaFacade() {
		return logRegistroAulaFacade;
	}

	public void setLogLancamentoNotaFacade(LogLancamentoNotaInterfaceFacade logLancamentoNotaFacade) {
		this.logLancamentoNotaFacade = logLancamentoNotaFacade;
	}

	public LogLancamentoNotaInterfaceFacade getLogLancamentoNotaFacade() {
		return logLancamentoNotaFacade;
	}

	public UploadArquivosComunsInterfaceFacade getUploadArquivosComunsFacade() {
		return uploadArquivosComunsFacade;
	}

	public void setUploadArquivosComunsFacade(UploadArquivosComunsInterfaceFacade uploadArquivosComunsFacade) {
		this.uploadArquivosComunsFacade = uploadArquivosComunsFacade;
	}

	public ImpressaoContratoInterfaceFacade getImpressaoContratoFacade() {
		return impressaoContratoFacade;
	}

	public void setImpressaoContratoFacade(ImpressaoContratoInterfaceFacade impressaoContratoFacade) {
		this.impressaoContratoFacade = impressaoContratoFacade;
	}

	public TurmaAtualizacaoDisciplinaLogInterfaceFacade getTurmaAtualizacaoDisciplinaLogFacade() {
		return turmaAtualizacaoDisciplinaLogFacade;
	}

	public void setTurmaAtualizacaoDisciplinaLogFacade(
			TurmaAtualizacaoDisciplinaLogInterfaceFacade turmaAtualizacaoDisciplinaLogFacade) {
		this.turmaAtualizacaoDisciplinaLogFacade = turmaAtualizacaoDisciplinaLogFacade;
	}

	/**
	 * @return the turmaAberturaFacade
	 */
	public TurmaAberturaInterfaceFacade getTurmaAberturaFacade() {
		return turmaAberturaFacade;
	}

	/**
	 * @param turmaAberturaFacade the turmaAberturaFacade to set
	 */
	public void setTurmaAberturaFacade(TurmaAberturaInterfaceFacade turmaAberturaFacade) {
		this.turmaAberturaFacade = turmaAberturaFacade;
	}

	/**
	 * @return the contaPagarPorTurmaRelFacade
	 */

	/**
	 * @param contaPagarPorTurmaRelFacade the contaPagarPorTurmaRelFacade to set
	 */

	public BibliotecaExternaInterfaceFacade getBibliotecaExternaFacade() {
		return bibliotecaExternaFacade;
	}

	public void setBibliotecaExternaFacade(BibliotecaExternaInterfaceFacade bibliotecaExternaFacade) {
		this.bibliotecaExternaFacade = bibliotecaExternaFacade;
	}

	public ArquivoLogInterfaceFacade getArquivoLogFacade() {
		return arquivoLogFacade;
	}

	public void setArquivoLogFacade(ArquivoLogInterfaceFacade arquivoLogFacade) {
		this.arquivoLogFacade = arquivoLogFacade;
	}

	public RequerimentoRelInterfaceFacade getRequerimentoRelFacade() {
		return requerimentoRelFacade;
	}

	public void setRequerimentoRelFacade(RequerimentoRelInterfaceFacade requerimentoRelFacade) {
		this.requerimentoRelFacade = requerimentoRelFacade;
	}

	/**
	 * @return the solicitacaoOrcamentoPlanoOrcamentarioFacade
	 */

	/**
	 * @param solicitacaoOrcamentoPlanoOrcamentarioFacade the
	 *                                                    solicitacaoOrcamentoPlanoOrcamentarioFacade
	 *                                                    to set
	 */

	/**
	 * @return the itemSolicitacaoOrcamentoPlanoOrcamentarioFacade
	 */

	/**
	 * @param itemSolicitacaoOrcamentoPlanoOrcamentarioFacade the
	 *                                                        itemSolicitacaoOrcamentoPlanoOrcamentarioFacade
	 *                                                        to set
	 */

	public AvaliacaoInstitucionalPresencialRespostaInterfaceFacade getAvaliacaoInstitucionalPresencialRespostaFacade() {
		return avaliacaoInstitucionalPresencialRespostaFacade;
	}

	public void setAvaliacaoInstitucionalPresencialRespostaFacade(
			AvaliacaoInstitucionalPresencialRespostaInterfaceFacade avaliacaoInstitucionalPresencialRespostaFacade) {
		this.avaliacaoInstitucionalPresencialRespostaFacade = avaliacaoInstitucionalPresencialRespostaFacade;
	}

	public AvaliacaoInstitucionalPresencialItemRespostaInterfaceFacade getAvaliacaoInstitucionalPresencialItemRespostaFacade() {
		return avaliacaoInstitucionalPresencialItemRespostaFacade;
	}

	public void setAvaliacaoInstitucionalPresencialItemRespostaFacade(
			AvaliacaoInstitucionalPresencialItemRespostaInterfaceFacade avaliacaoInstitucionalPresencialItemRespostaFacade) {
		this.avaliacaoInstitucionalPresencialItemRespostaFacade = avaliacaoInstitucionalPresencialItemRespostaFacade;
	}

	public ObservacaoComplementarInterfaceFacade getObservacaoComplementarFacade() {
		return observacaoComplementarFacade;
	}

	public void setObservacaoComplementarFacade(ObservacaoComplementarInterfaceFacade observacaoComplementarFacade) {
		this.observacaoComplementarFacade = observacaoComplementarFacade;
	}

	public FormacaoExtraCurricularInterfaceFacade getFormacaoExtraCurricularFacade() {
		return formacaoExtraCurricularFacade;
	}

	public void setFormacaoExtraCurricularFacade(FormacaoExtraCurricularInterfaceFacade formacaoExtraCurricularFacade) {
		this.formacaoExtraCurricularFacade = formacaoExtraCurricularFacade;
	}

	public CampanhaMidiaInterfaceFacade getCampanhaMidiaFacade() {
		return campanhaMidiaFacade;
	}

	public void setCampanhaMidiaFacade(CampanhaMidiaInterfaceFacade campanhaMidiaFacade) {
		this.campanhaMidiaFacade = campanhaMidiaFacade;
	}

	public TitulacaoCursoInterfaceFacade getTitulacaoCursoFacade() {
		return titulacaoCursoFacade;
	}

	public void setTitulacaoCursoFacade(TitulacaoCursoInterfaceFacade titulacaoCursoFacade) {
		this.titulacaoCursoFacade = titulacaoCursoFacade;
	}

	public ItemTitulacaoCursoInterfaceFacade getItemTitulacaoCursoFacade() {
		return itemTitulacaoCursoFacade;
	}

	public void setItemTitulacaoCursoFacade(ItemTitulacaoCursoInterfaceFacade itemTitulacaoCursoFacade) {
		this.itemTitulacaoCursoFacade = itemTitulacaoCursoFacade;
	}

	public MotivoCancelamentoTrancamentoInterfaceFacade getMotivoCancelamentoTrancamentoFacade() {
		return motivoCancelamentoTrancamentoFacade;
	}

	public void setMotivoCancelamentoTrancamentoFacade(
			MotivoCancelamentoTrancamentoInterfaceFacade motivoCancelamentoTrancamentoFacade) {
		this.motivoCancelamentoTrancamentoFacade = motivoCancelamentoTrancamentoFacade;
	}

	/**
	 * @return the pessoPreInscricaoCursoFacade
	 */
	public PessoaPreInscricaoCursoInterfaceFacade getPessoaPreInscricaoCursoFacade() {
		return pessoaPreInscricaoCursoFacade;
	}

	/**
	 * @param pessoPreInscricaoCursoFacade the pessoPreInscricaoCursoFacade to set
	 */
	public void setPessoaPreInscricaoCursoFacade(PessoaPreInscricaoCursoInterfaceFacade pessoaPreInscricaoCursoFacade) {
		this.pessoaPreInscricaoCursoFacade = pessoaPreInscricaoCursoFacade;
	}

	public DisciplinaCompostaInterfaceFacade getDisciplinaCompostaFacade() {
		return disciplinaCompostaFacade;
	}

	public void setDisciplinaCompostaFacade(DisciplinaCompostaInterfaceFacade disciplinaCompostaFacade) {
		this.disciplinaCompostaFacade = disciplinaCompostaFacade;
	}

	public TextoPadraoDeclaracaoInterfaceFacade getTextoPadraoDeclaracaoFacade() {
		return textoPadraoDeclaracaoFacade;
	}

	public void setTextoPadraoDeclaracaoFacade(TextoPadraoDeclaracaoInterfaceFacade textoPadraoDeclaracaoFacade) {
		this.textoPadraoDeclaracaoFacade = textoPadraoDeclaracaoFacade;
	}

	public AtividadeComplementarInterfaceFacade getAtividadeComplementarFacade() {
		return atividadeComplementarFacade;
	}

	public void setAtividadeComplementarFacade(AtividadeComplementarInterfaceFacade atividadeComplementarFacade) {
		this.atividadeComplementarFacade = atividadeComplementarFacade;
	}

	/**
	 * @return the cartaoRespostaRelFacade
	 */

	/**
	 * @param cartaoRespostaRelFacade the cartaoRespostaRelFacade to set
	 */

	public AtividadeComplementarMatriculaInterfaceFacade getAtividadeComplementarMatriculaFacade() {
		return atividadeComplementarMatriculaFacade;
	}

	public void setAtividadeComplementarMatriculaFacade(
			AtividadeComplementarMatriculaInterfaceFacade atividadeComplementarMatriculaFacade) {
		this.atividadeComplementarMatriculaFacade = atividadeComplementarMatriculaFacade;
	}

	public LogFuncionarioInterfaceFacade getLogFuncionarioFacade() {
		return logFuncionarioFacade;
	}

	public void setLogFuncionarioFacade(LogFuncionarioInterfaceFacade logFuncionarioFacade) {
		this.logFuncionarioFacade = logFuncionarioFacade;
	}

	
	private DataComemorativaInterfaceFacade dataComemorativaFacade;

	public DataComemorativaInterfaceFacade getDataComemorativaFacade() {
		return dataComemorativaFacade;
	}

	public void setDataComemorativaFacade(DataComemorativaInterfaceFacade dataComemorativaFacade) {
		this.dataComemorativaFacade = dataComemorativaFacade;
	}

	@Autowired
	private FiltroPainelGestorAcademicoInterfaceFacade filtroPainelGestorAcademicoFacade;

	public FiltroPainelGestorAcademicoInterfaceFacade getFiltroPainelGestorAcademicoFacade() {
		return filtroPainelGestorAcademicoFacade;
	}

	public void setFiltroPainelGestorAcademicoFacade(
			FiltroPainelGestorAcademicoInterfaceFacade filtroPainelGestorAcademicoFacade) {
		this.filtroPainelGestorAcademicoFacade = filtroPainelGestorAcademicoFacade;
	}

	@Autowired
	private ConteudoInterfaceFacade conteudoFacade;
	@Autowired
	private UnidadeConteudoInterfaceFacade unidadeConteudoFacade;

	public UnidadeConteudoInterfaceFacade getUnidadeConteudoFacade() {
		return unidadeConteudoFacade;
	}

	public void setUnidadeConteudoFacade(UnidadeConteudoInterfaceFacade unidadeConteudoFacade) {
		this.unidadeConteudoFacade = unidadeConteudoFacade;
	}

	public ConteudoInterfaceFacade getConteudoFacade() {
		return conteudoFacade;
	}

	public void setConteudoFacade(ConteudoInterfaceFacade conteudoFacade) {
		this.conteudoFacade = conteudoFacade;
	}

	@Autowired
	private RecursoEducacionalInterfaceFacade recursoEducacionalFacade;
	@Autowired
	private ConteudoUnidadePaginaInterfaceFacade conteudoUnidadePaginaFacade;
	@Autowired
	private ConteudoUnidadePaginaRecursoEducacionalInterfaceFacade conteudoUnidadePaginaRecursoEducacionalFacade;

	public TipoConcedenteInterfaceFacade getTipoConcedenteFacade() {
		return tipoConcedenteFacade;
	}

	public void setTipoConcedenteFacade(TipoConcedenteInterfaceFacade tipoConcedenteFacade) {
		this.tipoConcedenteFacade = tipoConcedenteFacade;
	}

	public ConcedenteInterfaceFacade getConcedenteFacade() {
		return concedenteFacade;
	}

	public void setConcedenteFacade(ConcedenteInterfaceFacade concedenteFacade) {
		this.concedenteFacade = concedenteFacade;
	}

	public ConfiguracaoEstagioObrigatorioInterfaceFacade getConfiguracaoEstagioObrigatorioFacade() {
		return configuracaoEstagioObrigatorioFacade;
	}

	public void setConfiguracaoEstagioObrigatorioFacade(
			ConfiguracaoEstagioObrigatorioInterfaceFacade configuracaoEstagioObrigatorioFacade) {
		this.configuracaoEstagioObrigatorioFacade = configuracaoEstagioObrigatorioFacade;
	}

	public ConfiguracaoEstagioObrigatorioFuncionarioInterfaceFacade getConfiguracaoEstagioObrigatorioFuncionarioFacade() {
		return configuracaoEstagioObrigatorioFuncionarioFacade;
	}

	public void setConfiguracaoEstagioObrigatorioFuncionarioFacade(
			ConfiguracaoEstagioObrigatorioFuncionarioInterfaceFacade configuracaoEstagioObrigatorioFuncionarioFacade) {
		this.configuracaoEstagioObrigatorioFuncionarioFacade = configuracaoEstagioObrigatorioFuncionarioFacade;
	}

	public MotivosPadroesEstagioInterfaceFacade getMotivosPadroesEstagioFacade() {
		return motivosPadroesEstagioFacade;
	}

	public void setMotivosPadroesEstagioFacade(MotivosPadroesEstagioInterfaceFacade motivosPadroesEstagioFacade) {
		this.motivosPadroesEstagioFacade = motivosPadroesEstagioFacade;
	}

	public GradeCurricularEstagioInterfaceFacade getGradeCurricularEstagioFacade() {
		return gradeCurricularEstagioFacade;
	}

	public void setGradeCurricularEstagioFacade(GradeCurricularEstagioInterfaceFacade gradeCurricularEstagioFacade) {
		this.gradeCurricularEstagioFacade = gradeCurricularEstagioFacade;
	}

	public GrupoPessoaInterfaceFacade getGrupoPessoaFacade() {
		return grupoPessoaFacade;
	}

	public void setGrupoPessoaFacade(GrupoPessoaInterfaceFacade grupoPessoaFacade) {
		this.grupoPessoaFacade = grupoPessoaFacade;
	}

	public GrupoPessoaItemInterfaceFacade getGrupoPessoaItemFacade() {
		return grupoPessoaItemFacade;
	}

	public void setGrupoPessoaItemFacade(GrupoPessoaItemInterfaceFacade grupoPessoaItemFacade) {
		this.grupoPessoaItemFacade = grupoPessoaItemFacade;
	}

	public RecursoEducacionalInterfaceFacade getRecursoEducacionalFacade() {
		return recursoEducacionalFacade;
	}

	public void setRecursoEducacionalFacade(RecursoEducacionalInterfaceFacade recursoEducacionalFacade) {
		this.recursoEducacionalFacade = recursoEducacionalFacade;
	}

	public ConteudoUnidadePaginaInterfaceFacade getConteudoUnidadePaginaFacade() {
		return conteudoUnidadePaginaFacade;
	}

	public void setConteudoUnidadePaginaFacade(ConteudoUnidadePaginaInterfaceFacade conteudoUnidadePaginaFacade) {
		this.conteudoUnidadePaginaFacade = conteudoUnidadePaginaFacade;
	}

	public ConteudoUnidadePaginaRecursoEducacionalInterfaceFacade getConteudoUnidadePaginaRecursoEducacionalFacade() {
		return conteudoUnidadePaginaRecursoEducacionalFacade;
	}

	public void setConteudoUnidadePaginaRecursoEducacionalFacade(
			ConteudoUnidadePaginaRecursoEducacionalInterfaceFacade conteudoUnidadePaginaRecursoEducacionalFacade) {
		this.conteudoUnidadePaginaRecursoEducacionalFacade = conteudoUnidadePaginaRecursoEducacionalFacade;
	}

	public ProcessoMatriculaLogInterfaceFacade getProcessoMatriculaLogFacade() {
		return processoMatriculaLogFacade;
	}

	public void setProcessoMatriculaLogFacade(ProcessoMatriculaLogInterfaceFacade processoMatriculaLogFacade) {
		this.processoMatriculaLogFacade = processoMatriculaLogFacade;
	}

	public PeriodoLetivoAtivoUnidadeEnsinoCursoLogInterfaceFacade getPeriodoLetivoAtivoUnidadeEnsinoCursoLogFacade() {
		return periodoLetivoAtivoUnidadeEnsinoCursoLogFacade;
	}

	public void setPeriodoLetivoAtivoUnidadeEnsinoCursoLogFacade(
			PeriodoLetivoAtivoUnidadeEnsinoCursoLogInterfaceFacade periodoLetivoAtivoUnidadeEnsinoCursoLogFacade) {
		this.periodoLetivoAtivoUnidadeEnsinoCursoLogFacade = periodoLetivoAtivoUnidadeEnsinoCursoLogFacade;
	}

	@Autowired
	private IconeInterfaceFacade iconeFacade;

	public IconeInterfaceFacade getIconeFacade() {
		return iconeFacade;
	}

	public void setIconeFacade(IconeInterfaceFacade iconeFacade) {
		this.iconeFacade = iconeFacade;
	}

	@Autowired
	private ForumInterfaceFacade forumFacade;
	@Autowired
	private ForumInteracaoInterfaceFacade forumInteracaoFacade;

	@Autowired
	private ForumPessoaInterfaceFacade forumPessoaFacade;
	@Autowired
	private ForumRegistrarNotaInterfaceFacade forumRegistrarNotaFacade;

	@Autowired
	private ForumInteracaoGostadoInterfaceFacade forumInteracaoGostadoFacade;
	@Autowired
	private ForumAcessoInterfaceFacade forumAcessoFacade;

	public ForumInterfaceFacade getForumFacade() {
		return forumFacade;
	}

	public void setForumFacade(ForumInterfaceFacade forumFacade) {
		this.forumFacade = forumFacade;
	}

	public ForumInteracaoInterfaceFacade getForumInteracaoFacade() {
		return forumInteracaoFacade;
	}

	public void setForumInteracaoFacade(ForumInteracaoInterfaceFacade forumInteracaoFacade) {
		this.forumInteracaoFacade = forumInteracaoFacade;
	}

	public ForumPessoaInterfaceFacade getForumPessoaFacade() {
		return forumPessoaFacade;
	}

	public void setForumPessoaFacade(ForumPessoaInterfaceFacade forumPessoaFacade) {
		this.forumPessoaFacade = forumPessoaFacade;
	}

	public ForumRegistrarNotaInterfaceFacade getForumRegistrarNotaFacade() {
		return forumRegistrarNotaFacade;
	}

	public void setForumRegistrarNotaFacade(ForumRegistrarNotaInterfaceFacade forumRegistrarNotaInterfaceFacade) {
		this.forumRegistrarNotaFacade = forumRegistrarNotaInterfaceFacade;
	}

	public ForumInteracaoGostadoInterfaceFacade getForumInteracaoGostadoFacade() {
		return forumInteracaoGostadoFacade;
	}

	public void setForumInteracaoGostadoFacade(ForumInteracaoGostadoInterfaceFacade forumInteracaoGostadoFacade) {
		this.forumInteracaoGostadoFacade = forumInteracaoGostadoFacade;
	}

	public ForumAcessoInterfaceFacade getForumAcessoFacade() {
		return forumAcessoFacade;
	}

	public void setForumAcessoFacade(ForumAcessoInterfaceFacade forumAcessoFacade) {
		this.forumAcessoFacade = forumAcessoFacade;
	}

	@Autowired
	private ConfiguracaoEADInterfaceFacade configuracaoEADFacade;

	public ConfiguracaoEADInterfaceFacade getConfiguracaoEADFacade() {
		return configuracaoEADFacade;
	}

	public void setConfiguracaoEADFacade(ConfiguracaoEADInterfaceFacade configuracaoEADFacade) {
		this.configuracaoEADFacade = configuracaoEADFacade;
	}

	@Autowired
	private NovidadeSeiInterfaceFacade novidadeSeiFacade;

	public NovidadeSeiInterfaceFacade getNovidadeSeiFacade() {
		return novidadeSeiFacade;
	}

	public void setNovidadeSeiFacade(NovidadeSeiInterfaceFacade novidadeSeiFacade) {
		this.novidadeSeiFacade = novidadeSeiFacade;
	}

	@Autowired
	private QuestaoInterfaceFacade questaoFacade;
	@Autowired
	private OpcaoRespostaQuestaoInterfaceFacade opcaoRespostaQuestaoFacade;

	public QuestaoInterfaceFacade getQuestaoFacade() {
		return questaoFacade;
	}

	public void setQuestaoFacade(QuestaoInterfaceFacade questaoFacade) {
		this.questaoFacade = questaoFacade;
	}

	public OpcaoRespostaQuestaoInterfaceFacade getOpcaoRespostaQuestaoFacade() {
		return opcaoRespostaQuestaoFacade;
	}

	public void setOpcaoRespostaQuestaoFacade(OpcaoRespostaQuestaoInterfaceFacade opcaoRespostaQuestaoFacade) {
		this.opcaoRespostaQuestaoFacade = opcaoRespostaQuestaoFacade;
	}

	@Autowired
	private ListaExercicioInterfaceFacade listaExercicioFacade;
	@Autowired
	private QuestaoListaExercicioInterfaceFacade questaoListaExercicioFacade;

	public ListaExercicioInterfaceFacade getListaExercicioFacade() {
		return listaExercicioFacade;
	}

	public void setListaExercicioFacade(ListaExercicioInterfaceFacade listaExercicioFacade) {
		this.listaExercicioFacade = listaExercicioFacade;
	}

	public QuestaoListaExercicioInterfaceFacade getQuestaoListaExercicioFacade() {
		return questaoListaExercicioFacade;
	}

	public void setQuestaoListaExercicioFacade(QuestaoListaExercicioInterfaceFacade questaoListaExercicioFacade) {
		this.questaoListaExercicioFacade = questaoListaExercicioFacade;
	}

	public MaterialCursoInterfaceFacade getMaterialCursoFacade() {
		return materialCursoFacade;
	}

	public void setMaterialCursoFacade(MaterialCursoInterfaceFacade materialCursoFacade) {
		this.materialCursoFacade = materialCursoFacade;
	}

	public MaterialRequerimentoInterfaceFacade getMaterialRequerimentoFacade() {
		return materialRequerimentoFacade;
	}

	public void setMaterialRequerimentoFacade(MaterialRequerimentoInterfaceFacade materialRequerimentoFacade) {
		this.materialRequerimentoFacade = materialRequerimentoFacade;
	}

	public MaterialAlunoInterfaceFacade getMaterialAlunoFacade() {
		return materialAlunoFacade;
	}

	public void setMaterialAlunoFacade(MaterialAlunoInterfaceFacade materialAlunoFacade) {
		this.materialAlunoFacade = materialAlunoFacade;
	}

	public MaterialUnidadeEnsinoInterfaceFacade getMaterialUnidadeEnsinoFacade() {
		return materialUnidadeEnsinoFacade;
	}

	public void setMaterialUnidadeEnsinoFacade(MaterialUnidadeEnsinoInterfaceFacade materialUnidadeEnsinoFacade) {
		this.materialUnidadeEnsinoFacade = materialUnidadeEnsinoFacade;
	}

	@Autowired
	private ConfiguracaoAcademicoNotaConceitoInterfaceFacade configuracaoAcademicoNotaConceitoFacade;

	public ConfiguracaoAcademicoNotaConceitoInterfaceFacade getConfiguracaoAcademicoNotaConceitoFacade() {
		return configuracaoAcademicoNotaConceitoFacade;
	}

	public void setConfiguracaoAcademicoNotaConceitoFacade(
			ConfiguracaoAcademicoNotaConceitoInterfaceFacade configuracaoAcademicoNotaConceitoFacade) {
		this.configuracaoAcademicoNotaConceitoFacade = configuracaoAcademicoNotaConceitoFacade;
	}

	@Autowired
	private ConteudoRegistroAcessoInterfaceFacade conteudoRegistroAcessoFacade;

	public ConteudoRegistroAcessoInterfaceFacade getConteudoRegistroAcessoFacade() {
		return conteudoRegistroAcessoFacade;
	}

	public void setConteudoRegistroAcessoFacade(ConteudoRegistroAcessoInterfaceFacade conteudoRegistroAcessoFacade) {
		this.conteudoRegistroAcessoFacade = conteudoRegistroAcessoFacade;
	}

	@Autowired
	private DuvidaProfessorInterfaceFacade duvidaProfessorFacade;
	@Autowired
	private DuvidaProfessorInteracaoInterfaceFacade duvidaProfessorInteracaoFacade;

	public DuvidaProfessorInterfaceFacade getDuvidaProfessorFacade() {
		return duvidaProfessorFacade;
	}

	public void setDuvidaProfessorFacade(DuvidaProfessorInterfaceFacade duvidaProfessorFacade) {
		this.duvidaProfessorFacade = duvidaProfessorFacade;
	}

	public DuvidaProfessorInteracaoInterfaceFacade getDuvidaProfessorInteracaoFacade() {
		return duvidaProfessorInteracaoFacade;
	}

	public void setDuvidaProfessorInteracaoFacade(
			DuvidaProfessorInteracaoInterfaceFacade duvidaProfessorInteracaoFacade) {
		this.duvidaProfessorInteracaoFacade = duvidaProfessorInteracaoFacade;
	}

	@Autowired
	private CalendarioLancamentoNotaInterfaceFacade calendarioLancamentoNotaFacade;

	public CalendarioLancamentoNotaInterfaceFacade getCalendarioLancamentoNotaFacade() {
		return calendarioLancamentoNotaFacade;
	}

	public void setCalendarioLancamentoNotaFacade(
			CalendarioLancamentoNotaInterfaceFacade calendarioLancamentoNotaFacade) {
		this.calendarioLancamentoNotaFacade = calendarioLancamentoNotaFacade;
	}

	
	private LayoutEtiquetaTagInterfaceFacade layoutEtiquetaTagFacade;
	
	private LayoutEtiquetaInterfaceFacade layoutEtiquetaFacade;

	public LayoutEtiquetaTagInterfaceFacade getLayoutEtiquetaTagFacade() {
		return layoutEtiquetaTagFacade;
	}

	public void setLayoutEtiquetaTagFacade(LayoutEtiquetaTagInterfaceFacade layoutEtiquetaTagFacade) {
		this.layoutEtiquetaTagFacade = layoutEtiquetaTagFacade;
	}

	public LayoutEtiquetaInterfaceFacade getLayoutEtiquetaFacade() {
		return layoutEtiquetaFacade;
	}

	public void setLayoutEtiquetaFacade(LayoutEtiquetaInterfaceFacade layoutEtiquetaFacade) {
		this.layoutEtiquetaFacade = layoutEtiquetaFacade;
	}

	
	private SolicitacaoAberturaTurmaDisciplinaInterfaceFacade solicitacaoAberturaTurmaDisciplinaFacade;
	
	
	private SolicitacaoAberturaTurmaInterfaceFacade solicitacaoAberturaTurmaFacade;

	public SolicitacaoAberturaTurmaDisciplinaInterfaceFacade getSolicitacaoAberturaTurmaDisciplinaFacade() {
		return solicitacaoAberturaTurmaDisciplinaFacade;
	}

	public void setSolicitacaoAberturaTurmaDisciplinaFacade(
			SolicitacaoAberturaTurmaDisciplinaInterfaceFacade solicitacaoAberturaTurmaDisciplinaFacade) {
		this.solicitacaoAberturaTurmaDisciplinaFacade = solicitacaoAberturaTurmaDisciplinaFacade;
	}

	public SolicitacaoAberturaTurmaInterfaceFacade getSolicitacaoAberturaTurmaFacade() {
		return solicitacaoAberturaTurmaFacade;
	}

	public void setSolicitacaoAberturaTurmaFacade(
			SolicitacaoAberturaTurmaInterfaceFacade solicitacaoAberturaTurmaFacade) {
		this.solicitacaoAberturaTurmaFacade = solicitacaoAberturaTurmaFacade;
	}

	public PlanoDisciplinaRelInterfaceFacade getPlanoDisciplinaRelFacade() {
		return planoDisciplinaRelFacade;
	}

	public void setPlanoDisciplinaRelFacade(PlanoDisciplinaRelInterfaceFacade planoDisciplinaRelFacade) {
		this.planoDisciplinaRelFacade = planoDisciplinaRelFacade;
	}

	public UnidadeEnsinoBibliotecaInterfaceFacade getUnidadeEnsinoBibliotecaFacade() {
		return unidadeEnsinoBibliotecaFacade;
	}

	public void setUnidadeEnsinoBibliotecaFacade(UnidadeEnsinoBibliotecaInterfaceFacade unidadeEnsinoBibliotecaFacade) {
		this.unidadeEnsinoBibliotecaFacade = unidadeEnsinoBibliotecaFacade;
	}

	
	private SalaLocalAulaInterfaceFacade salaLocalAulaFacade;
//	@Autowired
//	private LocalAulaInterfaceFacade localAulaFacade;

	public SalaLocalAulaInterfaceFacade getSalaLocalAulaFacade() {
		return salaLocalAulaFacade;
	}

	public void setSalaLocalAulaFacade(SalaLocalAulaInterfaceFacade salaLocalAulaFacade) {
		this.salaLocalAulaFacade = salaLocalAulaFacade;
	}

//	public LocalAulaInterfaceFacade getLocalAulaFacade() {
//		return localAulaFacade;
//	}
//
//	public void setLocalAulaFacade(LocalAulaInterfaceFacade localAulaFacade) {
//		this.localAulaFacade = localAulaFacade;
//	}

	public QuestionarioRelInterfaceFacade getQuestionarioRelFacade() {
		return questionarioRelFacade;
	}

	public void setQuestionarioRelFacade(QuestionarioRelInterfaceFacade questionarioRelFacade) {
		this.questionarioRelFacade = questionarioRelFacade;
	}

	/**
	 * @return the tipoRequerimentoDepartamentoFacade
	 */
	public TipoRequerimentoDepartamentoInterfaceFacade getTipoRequerimentoDepartamentoFacade() {
		return tipoRequerimentoDepartamentoFacade;
	}

	/**
	 * @param tipoRequerimentoDepartamentoFacade the
	 *                                           tipoRequerimentoDepartamentoFacade
	 *                                           to set
	 */
	public void setTipoRequerimentoDepartamentoFacade(
			TipoRequerimentoDepartamentoInterfaceFacade tipoRequerimentoDepartamentoFacade) {
		this.tipoRequerimentoDepartamentoFacade = tipoRequerimentoDepartamentoFacade;
	}

	/**
	 * @return the requerimentoHistoricoFacade
	 */
	public RequerimentoHistoricoInterfaceFacade getRequerimentoHistoricoFacade() {
		return requerimentoHistoricoFacade;
	}

	/**
	 * @param requerimentoHistoricoFacade the requerimentoHistoricoFacade to set
	 */
	public void setRequerimentoHistoricoFacade(RequerimentoHistoricoInterfaceFacade requerimentoHistoricoFacade) {
		this.requerimentoHistoricoFacade = requerimentoHistoricoFacade;
	}

	@Autowired
	private AvaliacaoInstitucionalAnaliticoRelInterfaceFacade avaliacaoInstitucionalAnaliticoRelFacade;

	public AvaliacaoInstitucionalAnaliticoRelInterfaceFacade getAvaliacaoInstitucionalAnaliticoRelFacade() {
		return avaliacaoInstitucionalAnaliticoRelFacade;
	}

	public void setAvaliacaoInstitucionalAnaliticoRelFacade(
			AvaliacaoInstitucionalAnaliticoRelInterfaceFacade avaliacaoInstitucionalAnaliticoRelFacade) {
		this.avaliacaoInstitucionalAnaliticoRelFacade = avaliacaoInstitucionalAnaliticoRelFacade;
	}

	private PlanoEnsinoInterfaceFacade planoEnsinoFacade;

	public PlanoEnsinoInterfaceFacade getPlanoEnsinoFacade() {
		return planoEnsinoFacade;
	}

	public void setPlanoEnsinoFacade(PlanoEnsinoInterfaceFacade planoEnsinoFacade) {
		this.planoEnsinoFacade = planoEnsinoFacade;
	}

	
	private CalendarioRegistroAulaInterfaceFacade calendarioRegistroAulaFacade;

	public CalendarioRegistroAulaInterfaceFacade getCalendarioRegistroAulaFacade() {
		return calendarioRegistroAulaFacade;
	}

	public void setCalendarioRegistroAulaFacade(CalendarioRegistroAulaInterfaceFacade calendarioRegistroAulaFacade) {
		this.calendarioRegistroAulaFacade = calendarioRegistroAulaFacade;
	}

	
	private ConfiguracaoTCCInterfaceFacade configuracaoTCCFacade;

	
	private ConfiguracaoTCCArtefatoInterfaceFacade configuracaoTCCArtefatoFacade;

	public ConfiguracaoTCCInterfaceFacade getConfiguracaoTCCFacade() {
		return configuracaoTCCFacade;
	}

	public void setConfiguracaoTCCFacade(ConfiguracaoTCCInterfaceFacade configuracaoTCCFacade) {
		this.configuracaoTCCFacade = configuracaoTCCFacade;
	}

	public ConfiguracaoTCCArtefatoInterfaceFacade getConfiguracaoTCCArtefatoFacade() {
		return configuracaoTCCArtefatoFacade;
	}

	public void setConfiguracaoTCCArtefatoFacade(ConfiguracaoTCCArtefatoInterfaceFacade configuracaoTCCArtefatoFacade) {
		this.configuracaoTCCArtefatoFacade = configuracaoTCCArtefatoFacade;
	}

	
	private TrabalhoConclusaoCursoArquivoInterfaceFacade trabalhoConclusaoCursoArquivoFacade;
	private TrabalhoConclusaoCursoArtefatoInterfaceFacade trabalhoConclusaoCursoArtefatoFacade;
	
	private TrabalhoConclusaoCursoMembroBancaInterfaceFacade trabalhoConclusaoCursoMembroBancaFacade;
	
	private TrabalhoConclusaoCursoInterfaceFacade trabalhoConclusaoCursoFacade;

	@Autowired
	private SituacaoComplementarHistoricoInterfaceFacade situacaoComplementarHistoricoFacade;
	@Autowired
	private CalendarioAgrupamentoTccInterfaceFacade calendarioAgrupamentoTccFacade;

	public CalendarioAgrupamentoTccInterfaceFacade getCalendarioAgrupamentoTccFacade() {
		return calendarioAgrupamentoTccFacade;
	}

	public void setCalendarioAgrupamentoTccFacade(
			CalendarioAgrupamentoTccInterfaceFacade calendarioAgrupamentoTccFacade) {
		this.calendarioAgrupamentoTccFacade = calendarioAgrupamentoTccFacade;
	}

	public TrabalhoConclusaoCursoArquivoInterfaceFacade getTrabalhoConclusaoCursoArquivoFacade() {
		return trabalhoConclusaoCursoArquivoFacade;
	}

	public void setTrabalhoConclusaoCursoArquivoFacade(
			TrabalhoConclusaoCursoArquivoInterfaceFacade trabalhoConclusaoCursoArquivoFacade) {
		this.trabalhoConclusaoCursoArquivoFacade = trabalhoConclusaoCursoArquivoFacade;
	}

	public TrabalhoConclusaoCursoArtefatoInterfaceFacade getTrabalhoConclusaoCursoArtefatoFacade() {
		return trabalhoConclusaoCursoArtefatoFacade;
	}

	public void setTrabalhoConclusaoCursoArtefatoFacade(
			TrabalhoConclusaoCursoArtefatoInterfaceFacade trabalhoConclusaoCursoArtefatoFacade) {
		this.trabalhoConclusaoCursoArtefatoFacade = trabalhoConclusaoCursoArtefatoFacade;
	}

	public TrabalhoConclusaoCursoInterfaceFacade getTrabalhoConclusaoCursoFacade() {
		return trabalhoConclusaoCursoFacade;
	}

	public void setTrabalhoConclusaoCursoFacade(TrabalhoConclusaoCursoInterfaceFacade trabalhoConclusaoCursoFacade) {
		this.trabalhoConclusaoCursoFacade = trabalhoConclusaoCursoFacade;
	}

	public SituacaoComplementarHistoricoInterfaceFacade getSituacaoComplementarHistoricoFacade() {
		return situacaoComplementarHistoricoFacade;
	}

	public void setSituacaoComplementarHistoricoFacade(
			SituacaoComplementarHistoricoInterfaceFacade situacaoComplementarHistoricoFacade) {
		this.situacaoComplementarHistoricoFacade = situacaoComplementarHistoricoFacade;
	}

	public InclusaoDisciplinaForaGradeInterfaceFacade getInclusaoDisciplinaForaGradeFacade() {
		return inclusaoDisciplinaForaGradeFacade;
	}

	public void setInclusaoDisciplinaForaGradeFacade(
			InclusaoDisciplinaForaGradeInterfaceFacade inclusaoDisciplinaForaGradeFacade) {
		this.inclusaoDisciplinaForaGradeFacade = inclusaoDisciplinaForaGradeFacade;
	}

	public DisciplinaForaGradeInterfaceFacade getDisciplinaForaGradeFacade() {
		return disciplinaForaGradeFacade;
	}

	public void setDisciplinaForaGradeFacade(DisciplinaForaGradeInterfaceFacade disciplinaForaGradeFacade) {
		this.disciplinaForaGradeFacade = disciplinaForaGradeFacade;
	}

	public TrabalhoConclusaoCursoMembroBancaInterfaceFacade getTrabalhoConclusaoCursoMembroBancaFacade() {
		return trabalhoConclusaoCursoMembroBancaFacade;
	}

	public void setTrabalhoConclusaoCursoMembroBancaFacade(
			TrabalhoConclusaoCursoMembroBancaInterfaceFacade trabalhoConclusaoCursoMembroBancaFacade) {
		this.trabalhoConclusaoCursoMembroBancaFacade = trabalhoConclusaoCursoMembroBancaFacade;
	}

	public TransferenciaEntradaRegistroAulaFrequenciaInterfaceFacade getTransferenciaEntradaRegistroAulaFrequenciaFacade() {
		return transferenciaEntradaRegistroAulaFrequenciaFacade;
	}

	public void setTransferenciaEntradaRegistroAulaFrequenciaFacade(
			TransferenciaEntradaRegistroAulaFrequenciaInterfaceFacade transferenciaEntradaRegistroAulaFrequenciaFacade) {
		this.transferenciaEntradaRegistroAulaFrequenciaFacade = transferenciaEntradaRegistroAulaFrequenciaFacade;
	}

	public DisciplinaAproveitadaAlteradaMatriculaInterfaceFacade getDisciplinaAproveitadaAlteradaMatriculaFacade() {
		return disciplinaAproveitadaAlteradaMatriculaFacade;
	}

	public void setDisciplinaAproveitadaAlteradaMatriculaFacade(
			DisciplinaAproveitadaAlteradaMatriculaInterfaceFacade disciplinaAproveitadaAlteradaMatriculaFacade) {
		this.disciplinaAproveitadaAlteradaMatriculaFacade = disciplinaAproveitadaAlteradaMatriculaFacade;
	}

	public TipoAdvertenciaInterfaceFacade getTipoAdvertenciaFacade() {
		return tipoAdvertenciaFacade;
	}

	public void setTipoAdvertenciaFacade(TipoAdvertenciaInterfaceFacade tipoAdvertenciaFacade) {
		this.tipoAdvertenciaFacade = tipoAdvertenciaFacade;
	}

	public AdvertenciaInterfaceFacade getAdvertenciaFacade() {
		return advertenciaFacade;
	}

	public void setAdvertenciaFacade(AdvertenciaInterfaceFacade advertenciaFacade) {
		this.advertenciaFacade = advertenciaFacade;
	}

	public HistoricoGradeAnteriorAlteradaInterfaceFacade getHistoricoGradeAnteriorAlteradaFacade() {
		return historicoGradeAnteriorAlteradaFacade;
	}

	public void setHistoricoGradeAnteriorAlteradaFacade(
			HistoricoGradeAnteriorAlteradaInterfaceFacade historicoGradeAnteriorAlteradaFacade) {
		this.historicoGradeAnteriorAlteradaFacade = historicoGradeAnteriorAlteradaFacade;
	}

	public TipoAtividadeComplementarInterfaceFacade getTipoAtividadeComplementarFacade() {
		return tipoAtividadeComplementarFacade;
	}

	public void setTipoAtividadeComplementarFacade(
			TipoAtividadeComplementarInterfaceFacade tipoAtividadeComplementarFacade) {
		this.tipoAtividadeComplementarFacade = tipoAtividadeComplementarFacade;
	}

	public GradeCurricularTipoAtividadeComplementarInterfaceFacade getGradeCurricularTipoAtividadeComplementarFacade() {
		return gradeCurricularTipoAtividadeComplementarFacade;
	}

	public void setGradeCurricularTipoAtividadeComplementarFacade(
			GradeCurricularTipoAtividadeComplementarInterfaceFacade gradeCurricularTipoAtividadeComplementarFacade) {
		this.gradeCurricularTipoAtividadeComplementarFacade = gradeCurricularTipoAtividadeComplementarFacade;
	}

	public RegistroAtividadeComplementarInterfaceFacade getRegistroAtividadeComplementarFacade() {
		return registroAtividadeComplementarFacade;
	}

	public void setRegistroAtividadeComplementarFacade(
			RegistroAtividadeComplementarInterfaceFacade registroAtividadeComplementarFacade) {
		this.registroAtividadeComplementarFacade = registroAtividadeComplementarFacade;
	}

	public RegistroAtividadeComplementarMatriculaInterfaceFacade getRegistroAtividadeComplementarMatriculaFacade() {
		return registroAtividadeComplementarMatriculaFacade;
	}

	public void setRegistroAtividadeComplementarMatriculaFacade(
			RegistroAtividadeComplementarMatriculaInterfaceFacade registroAtividadeComplementarMatriculaFacade) {
		this.registroAtividadeComplementarMatriculaFacade = registroAtividadeComplementarMatriculaFacade;
	}

	public AcompanhamentoAtividadeComplementarInterfaceFacade getAcompanhamentoAtividadeComplementarFacade() {
		return acompanhamentoAtividadeComplementarFacade;
	}

	public void setAcompanhamentoAtividadeComplementarFacade(
			AcompanhamentoAtividadeComplementarInterfaceFacade acompanhamentoAtividadeComplementarFacade) {
		this.acompanhamentoAtividadeComplementarFacade = acompanhamentoAtividadeComplementarFacade;
	}

//	public PessoaEADIPOGInterfaceFacade getPessoaEADIPOGFacade() {
//		return pessoaEADIPOGFacade;
//	}
//
//	public void setPessoaEADIPOGFacade(PessoaEADIPOGInterfaceFacade pessoaEADIPOGFacade) {
//		this.pessoaEADIPOGFacade = pessoaEADIPOGFacade;
//	}

	@Autowired
	private GradeCurricularGrupoOptativaInterfaceFacade gradeCurricularGrupoOptativaFacade;
	@Autowired
	private GradeCurricularGrupoOptativaDisciplinaInterfaceFacade gradeCurricularGrupoOptativaDisciplinaFacade;

	public GradeCurricularGrupoOptativaInterfaceFacade getGradeCurricularGrupoOptativaFacade() {
		return gradeCurricularGrupoOptativaFacade;
	}

	public void setGradeCurricularGrupoOptativaFacade(
			GradeCurricularGrupoOptativaInterfaceFacade gradeCurricularGrupoOptativaFacade) {
		this.gradeCurricularGrupoOptativaFacade = gradeCurricularGrupoOptativaFacade;
	}

	public GradeCurricularGrupoOptativaDisciplinaInterfaceFacade getGradeCurricularGrupoOptativaDisciplinaFacade() {
		return gradeCurricularGrupoOptativaDisciplinaFacade;
	}

	public void setGradeCurricularGrupoOptativaDisciplinaFacade(
			GradeCurricularGrupoOptativaDisciplinaInterfaceFacade gradeCurricularGrupoOptativaDisciplinaFacade) {
		this.gradeCurricularGrupoOptativaDisciplinaFacade = gradeCurricularGrupoOptativaDisciplinaFacade;
	}

	@Autowired
	private MapaEquivalenciaDisciplinaCursadaInterfaceFacade mapaEquivalenciaDisciplinaCursadaFacade;
	@Autowired
	private MapaEquivalenciaDisciplinaInterfaceFacade mapaEquivalenciaDisciplinaFacade;
	@Autowired
	private MapaEquivalenciaDisciplinaMatrizCurricularInterfaceFacade mapaEquivalenciaDisciplinaMatrizCurricularFacade;
	@Autowired
	private MapaEquivalenciaMatrizCurricularInterfaceFacade mapaEquivalenciaMatrizCurricularFacade;

	public MapaEquivalenciaDisciplinaCursadaInterfaceFacade getMapaEquivalenciaDisciplinaCursadaFacade() {
		return mapaEquivalenciaDisciplinaCursadaFacade;
	}

	public void setMapaEquivalenciaDisciplinaCursadaFacade(
			MapaEquivalenciaDisciplinaCursadaInterfaceFacade mapaEquivalenciaDisciplinaCursadaFacade) {
		this.mapaEquivalenciaDisciplinaCursadaFacade = mapaEquivalenciaDisciplinaCursadaFacade;
	}

	public MapaEquivalenciaDisciplinaInterfaceFacade getMapaEquivalenciaDisciplinaFacade() {
		return mapaEquivalenciaDisciplinaFacade;
	}

	public void setMapaEquivalenciaDisciplinaFacade(
			MapaEquivalenciaDisciplinaInterfaceFacade mapaEquivalenciaDisciplinaFacade) {
		this.mapaEquivalenciaDisciplinaFacade = mapaEquivalenciaDisciplinaFacade;
	}

	public MapaEquivalenciaDisciplinaMatrizCurricularInterfaceFacade getMapaEquivalenciaDisciplinaMatrizCurricularFacade() {
		return mapaEquivalenciaDisciplinaMatrizCurricularFacade;
	}

	public void setMapaEquivalenciaDisciplinaMatrizCurricularFacade(
			MapaEquivalenciaDisciplinaMatrizCurricularInterfaceFacade mapaEquivalenciaDisciplinaMatrizCurricularFacade) {
		this.mapaEquivalenciaDisciplinaMatrizCurricularFacade = mapaEquivalenciaDisciplinaMatrizCurricularFacade;
	}

	public MapaEquivalenciaMatrizCurricularInterfaceFacade getMapaEquivalenciaMatrizCurricularFacade() {
		return mapaEquivalenciaMatrizCurricularFacade;
	}

	public void setMapaEquivalenciaMatrizCurricularFacade(
			MapaEquivalenciaMatrizCurricularInterfaceFacade mapaEquivalenciaMatrizCurricularFacade) {
		this.mapaEquivalenciaMatrizCurricularFacade = mapaEquivalenciaMatrizCurricularFacade;
	}

	public ParametroRelatorioInterfaceFacade getParametroRelatorioFacade() {
		return parametroRelatorioFacade;
	}

	public void setParametroRelatorioFacade(ParametroRelatorioInterfaceFacade parametroRelatorioFacade) {
		this.parametroRelatorioFacade = parametroRelatorioFacade;
	}

	@Autowired
	private EstagioInterfaceFacade estagioFacade;

	public EstagioInterfaceFacade getEstagioFacade() {
		return estagioFacade;
	}

	public void setEstagioFacade(EstagioInterfaceFacade estagioFacade) {
		this.estagioFacade = estagioFacade;
	}

	@Autowired
	private GradeDisciplinaCompostaInterfaceFacade gradeDisciplinaCompostaFacade;

	public GradeDisciplinaCompostaInterfaceFacade getGradeDisciplinaCompostaFacade() {
		return gradeDisciplinaCompostaFacade;
	}

	public void setGradeDisciplinaCompostaFacade(GradeDisciplinaCompostaInterfaceFacade gradeDisciplinaCompostaFacade) {
		this.gradeDisciplinaCompostaFacade = gradeDisciplinaCompostaFacade;
	}

	
	private TextoEnadeInterfaceFacade textoEnadeFacade;

	public TextoEnadeInterfaceFacade getTextoEnadeFacade() {

		return textoEnadeFacade;
	}

	public void setTextoEnadeFacade(TextoEnadeInterfaceFacade textoEnadeFacade) {
		this.textoEnadeFacade = textoEnadeFacade;
	}

	public LogTriggerInterfaceFacade getLogTriggerInterfaceFacade() {
		return logTriggerInterfaceFacade;
	}

	public void setLogTriggerInterfaceFacade(LogTriggerInterfaceFacade logTriggerInterfaceFacade) {
		this.logTriggerInterfaceFacade = logTriggerInterfaceFacade;
	}

	public TurmaDisciplinaInclusaoSugeridaInterfaceFacade getTurmaDisciplinaInclusaoSugeridaInterfaceFacade() {
		return turmaDisciplinaInclusaoSugeridaInterfaceFacade;
	}

	public void setTurmaDisciplinaInclusaoSugeridaInterfaceFacade(
			TurmaDisciplinaInclusaoSugeridaInterfaceFacade turmaDisciplinaInclusaoSugeridaInterfaceFacade) {
		this.turmaDisciplinaInclusaoSugeridaInterfaceFacade = turmaDisciplinaInclusaoSugeridaInterfaceFacade;
	}

	
	private CriterioAvaliacaoPeriodoLetivoInterfaceFacade criterioAvaliacaoPeriodoLetivoFacade;

	public CriterioAvaliacaoPeriodoLetivoInterfaceFacade getCriterioAvaliacaoPeriodoLetivoFacade() {
		return criterioAvaliacaoPeriodoLetivoFacade;
	}

	public void setCriterioAvaliacaoPeriodoLetivoFacade(
			CriterioAvaliacaoPeriodoLetivoInterfaceFacade criterioAvaliacaoPeriodoLetivoFacade) {
		this.criterioAvaliacaoPeriodoLetivoFacade = criterioAvaliacaoPeriodoLetivoFacade;
	}

	public TipoRequerimentoUnidadeEnsinoInterfaceFacade getTipoRequerimentoUnidadeEnsinoFacade() {
		return tipoRequerimentoUnidadeEnsinoFacade;
	}

	public void setTipoRequerimentoUnidadeEnsinoFacade(
			TipoRequerimentoUnidadeEnsinoInterfaceFacade tipoRequerimentoUnidadeEnsinoFacade) {
		this.tipoRequerimentoUnidadeEnsinoFacade = tipoRequerimentoUnidadeEnsinoFacade;
	}

	public PoliticaDivulgacaoMatriculaOnlineInterfaceFacade getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade() {
		return politicaDivulgacaoMatriculaOnlineInterfaceFacade;
	}

	public void setPoliticaDivulgacaoMatriculaOnlineInterfaceFacade(
			PoliticaDivulgacaoMatriculaOnlineInterfaceFacade politicaDivulgacaoMatriculaOnlineInterfaceFacade) {
		this.politicaDivulgacaoMatriculaOnlineInterfaceFacade = politicaDivulgacaoMatriculaOnlineInterfaceFacade;
	}

	public PoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade() {
		return politicaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade;
	}

	public void setPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade(
			PoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade politicaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade) {
		this.politicaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade = politicaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade;
	}

	@Autowired
	private ConfiguracaoAcademicoNotaInterfaceFacade configuracaoAcademicoNotaFacade;

	public ConfiguracaoAcademicoNotaInterfaceFacade getConfiguracaoAcademicoNotaFacade() {
		return configuracaoAcademicoNotaFacade;
	}

	public void setConfiguracaoAcademicoNotaFacade(
			ConfiguracaoAcademicoNotaInterfaceFacade configuracaoAcademicoNotaFacade) {
		this.configuracaoAcademicoNotaFacade = configuracaoAcademicoNotaFacade;
	}

	public RenovacaoMatriculaTurmaMatriculaPeriodoInterfaceFacade getRenovacaoMatriculaTurmaMatriculaPeriodoFacade() {
		return renovacaoMatriculaTurmaMatriculaPeriodoFacade;
	}

	public void setRenovacaoMatriculaTurmaMatriculaPeriodoFacade(
			RenovacaoMatriculaTurmaMatriculaPeriodoInterfaceFacade renovacaoMatriculaTurmaMatriculaPeriodoFacade) {
		this.renovacaoMatriculaTurmaMatriculaPeriodoFacade = renovacaoMatriculaTurmaMatriculaPeriodoFacade;
	}

	public RenovacaoMatriculaTurmaInterfaceFacade getRenovacaoMatriculaTurmaFacade() {
		return renovacaoMatriculaTurmaFacade;
	}

	public void setRenovacaoMatriculaTurmaFacade(RenovacaoMatriculaTurmaInterfaceFacade renovacaoMatriculaTurmaFacade) {
		this.renovacaoMatriculaTurmaFacade = renovacaoMatriculaTurmaFacade;
	}

	
	private QuestaoTCCInterfaceFacade questaoTCCFacade;

	public QuestaoTCCInterfaceFacade getQuestaoTCCFacade() {
		return questaoTCCFacade;
	}

	public void setQuestaoTCCFacade(QuestaoTCCInterfaceFacade questaoTCCFacade) {
		this.questaoTCCFacade = questaoTCCFacade;
	}

	public AnotacaoDisciplinaInterfaceFacade getAnotacaoDisciplinaInterfaceFacade() {
		return anotacaoDisciplinaInterfaceFacade;
	}

	public void setAnotacaoDisciplinaInterfaceFacade(
			AnotacaoDisciplinaInterfaceFacade anotacaoDisciplinaInterfaceFacade) {
		this.anotacaoDisciplinaInterfaceFacade = anotacaoDisciplinaInterfaceFacade;
	}

	public AtividadeDiscursivaInterfaceFacade getAtividadeDiscursivaInterfaceFacade() {
		return atividadeDiscursivaInterfaceFacade;
	}

	public void setAtividadeDiscursivaInterfaceFacade(
			AtividadeDiscursivaInterfaceFacade atividadeDiscursivaInterfaceFacade) {
		this.atividadeDiscursivaInterfaceFacade = atividadeDiscursivaInterfaceFacade;
	}

	public AtividadeDiscursivaRespostaAlunoInterfaceFacade getAtividadeDiscursivaRespostaAlunoInterfaceFacade() {
		return atividadeDiscursivaRespostaAlunoInterfaceFacade;
	}

	public void setAtividadeDiscursivaRespostaAlunoInterfaceFacade(
			AtividadeDiscursivaRespostaAlunoInterfaceFacade atividadeDiscursivaRespostaAlunoInterfaceFacade) {
		this.atividadeDiscursivaRespostaAlunoInterfaceFacade = atividadeDiscursivaRespostaAlunoInterfaceFacade;
	}

	public AtividadeDiscursivaInteracaoInterfaceFacade getAtividadeDiscursivaInteracaoInterfaceFacade() {
		return atividadeDiscursivaInteracaoInterfaceFacade;
	}

	public void setAtividadeDiscursivaInteracaoInterfaceFacade(
			AtividadeDiscursivaInteracaoInterfaceFacade atividadeDiscursivaInteracaoInterfaceFacade) {
		this.atividadeDiscursivaInteracaoInterfaceFacade = atividadeDiscursivaInteracaoInterfaceFacade;
	}

	public AvaliacaoOnlineInterfaceFacade getAvaliacaoOnlineInterfaceFacade() {
		return avaliacaoOnlineInterfaceFacade;
	}

	public void setAvaliacaoOnlineInterfaceFacade(AvaliacaoOnlineInterfaceFacade avaliacaoOnlineInterfaceFacade) {
		this.avaliacaoOnlineInterfaceFacade = avaliacaoOnlineInterfaceFacade;
	}

	public AvaliacaoOnlineQuestaoInterfaceFacade getAvaliacaoOnlineQuestaoInterfaceFacade() {
		return avaliacaoOnlineQuestaoInterfaceFacade;
	}

	public void setAvaliacaoOnlineQuestaoInterfaceFacade(
			AvaliacaoOnlineQuestaoInterfaceFacade avaliacaoOnlineQuestaoInterfaceFacade) {
		this.avaliacaoOnlineQuestaoInterfaceFacade = avaliacaoOnlineQuestaoInterfaceFacade;
	}

	public AvaliacaoOnlineMatriculaInterfaceFacade getAvaliacaoOnlineMatriculaInterfaceFacade() {
		return avaliacaoOnlineMatriculaInterfaceFacade;
	}

	public void setAvaliacaoOnlineMatriculaInterfaceFacade(
			AvaliacaoOnlineMatriculaInterfaceFacade avaliacaoOnlineMatriculaInterfaceFacade) {
		this.avaliacaoOnlineMatriculaInterfaceFacade = avaliacaoOnlineMatriculaInterfaceFacade;
	}

	public AvaliacaoOnlineMatriculaQuestaoInterfaceFacade getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade() {
		return avaliacaoOnlineMatriculaQuestaoInterfaceFacade;
	}

	public void setAvaliacaoOnlineMatriculaQuestaoInterfaceFacade(
			AvaliacaoOnlineMatriculaQuestaoInterfaceFacade avaliacaoOnlineMatriculaQuestaoInterfaceFacade) {
		this.avaliacaoOnlineMatriculaQuestaoInterfaceFacade = avaliacaoOnlineMatriculaQuestaoInterfaceFacade;
	}

	public AvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade getAvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade() {
		return avaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade;
	}

	public void setAvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade(
			AvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade avaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade) {
		this.avaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade = avaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade;
	}

	@Autowired
	private HistoricoNotaInterfaceFacade historicoNotaFacade;

	public HistoricoNotaInterfaceFacade getHistoricoNotaFacade() {
		return historicoNotaFacade;
	}

	public void setHistoricoNotaFacade(HistoricoNotaInterfaceFacade historicoNotaFacade) {
		this.historicoNotaFacade = historicoNotaFacade;
	}

	public ProgramacaoTutoriaOnlineInterfaceFacade getProgramacaoTutoriaOnlineInterfaceFacade() {
		return programacaoTutoriaOnlineInterfaceFacade;
	}

	public void setProgramacaoTutoriaOnlineInterfaceFacade(
			ProgramacaoTutoriaOnlineInterfaceFacade programacaoTutoriaOnlineInterfaceFacade) {
		this.programacaoTutoriaOnlineInterfaceFacade = programacaoTutoriaOnlineInterfaceFacade;
	}

	public ConfiguracaoAcademicaHistoricoInterface getConfiguracaoAcademicaHistoricoInterface() {
		return configuracaoAcademicaHistoricoInterface;
	}

	public void setConfiguracaoAcademicaHistoricoInterface(
			ConfiguracaoAcademicaHistoricoInterface configuracaoAcademicaHistoricoInterface) {
		this.configuracaoAcademicaHistoricoInterface = configuracaoAcademicaHistoricoInterface;
	}

	public ProgramacaoTutoriaOnlineProfessorInterfaceFacade getProgramacaoTutoriaOnlineProfessorInterfaceFacade() {
		return programacaoTutoriaOnlineProfessorInterfaceFacade;
	}

	public void setProgramacaoTutoriaOnlineProfessorInterfaceFacade(
			ProgramacaoTutoriaOnlineProfessorInterfaceFacade programacaoTutoriaOnlineProfessorInterfaceFacade) {
		this.programacaoTutoriaOnlineProfessorInterfaceFacade = programacaoTutoriaOnlineProfessorInterfaceFacade;
	}

	public LayoutRelatorioSeiDecidirCampoInterface getLayoutRelatorioSEIDecidirCampoInterfaceFacade() {
		return layoutRelatorioSEIDecidirCampoInterfaceFacade;
	}

	public void setLayoutRelatorioSEIDecidirCampoInterfaceFacade(
			LayoutRelatorioSeiDecidirCampoInterface layoutRelatorioSEIDecidirCampoInterfaceFacade) {
		this.layoutRelatorioSEIDecidirCampoInterfaceFacade = layoutRelatorioSEIDecidirCampoInterfaceFacade;
	}

	public ConfiguracaoBibliotecaNivelEducacionalInterface getConfiguracaoBibliotecaNivelEducacionalFacade() {
		return configuracaoBibliotecaNivelEducacionalFacade;
	}

	public void setConfiguracaoBibliotecaNivelEducacionalFacade(
			ConfiguracaoBibliotecaNivelEducacionalInterface configuracaoBibliotecaNivelEducacionalFacade) {
		this.configuracaoBibliotecaNivelEducacionalFacade = configuracaoBibliotecaNivelEducacionalFacade;
	}

	public QuestaoConteudoFacade getQuestaoConteudoFacade() {
		return questaoConteudoFacade;
	}

	public void setQuestaoConteudoFacade(QuestaoConteudoFacade questaoConteudoFacade) {
		this.questaoConteudoFacade = questaoConteudoFacade;
	}

	public TurmaDisciplinaConteudoFacade getTurmaDisciplinaConteudoFacade() {
		return turmaDisciplinaConteudoFacade;
	}

	public void setTurmaDisciplinaConteudoFacade(TurmaDisciplinaConteudoFacade turmaDisciplinaConteudoFacade) {
		this.turmaDisciplinaConteudoFacade = turmaDisciplinaConteudoFacade;
	}

	public SolicitarAlterarSenhaInterfaceFacede getSolicitarAlterarSenhaInterfaceFacede() {
		return solicitarAlterarSenhaInterfaceFacede;
	}

	public void setSolicitarAlterarSenhaInterfaceFacede(
			SolicitarAlterarSenhaInterfaceFacede solicitarAlterarSenhaInterfaceFacede) {
		this.solicitarAlterarSenhaInterfaceFacede = solicitarAlterarSenhaInterfaceFacede;
	}

	public TemaAssuntoInterfaceFacade getTemaAssuntoFacade() {
		return temaAssuntoFacade;
	}

	public void setTemaAssuntoFacade(TemaAssuntoInterfaceFacade temaAssuntoFacade) {
		this.temaAssuntoFacade = temaAssuntoFacade;
	}

	public TemaAssuntoDisciplinaInterfaceFacade getTemaAssuntoDisciplinaFacade() {
		return temaAssuntoDisciplinaFacade;
	}

	public void setTemaAssuntoDisciplinaFacade(TemaAssuntoDisciplinaInterfaceFacade temaAssuntoDisciplinaFacade) {
		this.temaAssuntoDisciplinaFacade = temaAssuntoDisciplinaFacade;
	}

	public QuestaoAssuntoInterfaceFacade getQuestaoAssuntoFacade() {
		return questaoAssuntoFacade;
	}

	public void setQuestaoAssuntoFacade(QuestaoAssuntoInterfaceFacade questaoAssuntoFacade) {
		this.questaoAssuntoFacade = questaoAssuntoFacade;
	}

	public ParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade getParametrosMonitoramentoAvaliacaoOnlineFacade() {
		return parametrosMonitoramentoAvaliacaoOnlineFacade;
	}

	public void setParametrosMonitoramentoAvaliacaoOnlineFacade(
			ParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade parametrosMonitoramentoAvaliacaoOnlineFacade) {
		this.parametrosMonitoramentoAvaliacaoOnlineFacade = parametrosMonitoramentoAvaliacaoOnlineFacade;
	}

	public ItemParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade getItemParametrosMonitoramentoAvaliacaoOnlineFacade() {
		return itemParametrosMonitoramentoAvaliacaoOnlineFacade;
	}

	public void setItemParametrosMonitoramentoAvaliacaoOnlineFacade(
			ItemParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade itemParametrosMonitoramentoAvaliacaoOnlineFacade) {
		this.itemParametrosMonitoramentoAvaliacaoOnlineFacade = itemParametrosMonitoramentoAvaliacaoOnlineFacade;
	}

	public GraficoAproveitamentoAvaliacaoInterfaceFacade getGraficoAproveitamentoAvaliacaoFacade() {
		return graficoAproveitamentoAvaliacaoFacade;
	}

	public void setGraficoAproveitamentoAvaliacaoFacade(
			GraficoAproveitamentoAvaliacaoInterfaceFacade graficoAproveitamentoAvaliacaoFacade) {
		this.graficoAproveitamentoAvaliacaoFacade = graficoAproveitamentoAvaliacaoFacade;
	}

	public ParametrosGraficoComparativoAvaliacoesOnlinesInterfaceFacade getGraficoComparativoAvaliacoesOnlinesFacade() {
		return graficoComparativoAvaliacoesOnlinesFacade;
	}

	public void setGraficoComparativoAvaliacoesOnlinesFacade(
			ParametrosGraficoComparativoAvaliacoesOnlinesInterfaceFacade graficoComparativoAvaliacoesOnlinesFacade) {
		this.graficoComparativoAvaliacoesOnlinesFacade = graficoComparativoAvaliacoesOnlinesFacade;
	}

	public MonitorConhecimentoInterfaceFacade getMonitorConhecimentoFacade() {
		return monitorConhecimentoFacade;
	}

	public void setMonitorConhecimentoFacade(MonitorConhecimentoInterfaceFacade monitorConhecimentoFacade) {
		this.monitorConhecimentoFacade = monitorConhecimentoFacade;
	}

	public ParametrosGraficoComparativoMeusColegasInterfaceFacade getParametrosGraficoComparativoMeusColegasFacade() {
		return parametrosGraficoComparativoMeusColegasFacade;
	}

	public void setParametrosGraficoComparativoMeusColegasFacade(
			ParametrosGraficoComparativoMeusColegasInterfaceFacade parametrosGraficoComparativoMeusColegasFacade) {
		this.parametrosGraficoComparativoMeusColegasFacade = parametrosGraficoComparativoMeusColegasFacade;
	}

	public MonitoramentoAlunosEADInterfaceFacade getMonitoramentoAlunosEADFacade() {
		return monitoramentoAlunosEADFacade;
	}

	public void setMonitoramentoAlunosEADFacade(MonitoramentoAlunosEADInterfaceFacade monitoramentoAlunosEADFacade) {
		this.monitoramentoAlunosEADFacade = monitoramentoAlunosEADFacade;
	}

	public TipoRequerimentoCursoInterfaceFacade getTipoRequerimentoCursoFacade() {
		return tipoRequerimentoCursoFacade;
	}

	public void setTipoRequerimentoCursoFacade(TipoRequerimentoCursoInterfaceFacade tipoRequerimentoCursoFacade) {
		this.tipoRequerimentoCursoFacade = tipoRequerimentoCursoFacade;
	}

	public SMSFacadeInterface getSmsFacade() {
		return smsFacade;
	}

	public void setSmsFacade(SMSFacadeInterface smsFacade) {
		this.smsFacade = smsFacade;
	}

	
	private HorarioTurmaDiaItemInterfaceFacade horarioTurmaDiaItemFacade;

	/**
	 * @return the horarioTurmaDiaItemFacade
	 */
	public HorarioTurmaDiaItemInterfaceFacade getHorarioTurmaDiaItemFacade() {
		return horarioTurmaDiaItemFacade;
	}

	/**
	 * @param horarioTurmaDiaItemFacade the horarioTurmaDiaItemFacade to set
	 */
	public void setHorarioTurmaDiaItemFacade(HorarioTurmaDiaItemInterfaceFacade horarioTurmaDiaItemFacade) {
		this.horarioTurmaDiaItemFacade = horarioTurmaDiaItemFacade;
	}

	@Autowired
	private OperacaoFuncionalidadeInterfaceFacade operacaoFuncionalidadeFacade;

	public OperacaoFuncionalidadeInterfaceFacade getOperacaoFuncionalidadeFacade() {
		return operacaoFuncionalidadeFacade;
	}

	public void setOperacaoFuncionalidadeFacade(OperacaoFuncionalidadeInterfaceFacade operacaoFuncionalidadeFacade) {
		this.operacaoFuncionalidadeFacade = operacaoFuncionalidadeFacade;
	}

	/**
	 * @param operacaoFinanceiraCaixaRelFacade the operacaoFinanceiraCaixaRelFacade
	 *                                         to set
	 */

	private AlteracaoPlanoFinanceiroAlunoInterfaceFacade alteracaoPlanoFinanceiroAlunoFacade;

	public AlteracaoPlanoFinanceiroAlunoInterfaceFacade getAlteracaoPlanoFinanceiroAlunoFacade() {
		return alteracaoPlanoFinanceiroAlunoFacade;
	}

	public void setAlteracaoPlanoFinanceiroAlunoFacade(
			AlteracaoPlanoFinanceiroAlunoInterfaceFacade alteracaoPlanoFinanceiroAlunoFacade) {
		this.alteracaoPlanoFinanceiroAlunoFacade = alteracaoPlanoFinanceiroAlunoFacade;
	}

	public AlterarResponsavelRequerimentoInterfaceFacade getAlterarResponsavelRequerimentoFacade() {
		return alterarResponsavelRequerimentoFacade;
	}

	public void setAlterarResponsavelRequerimentoFacade(
			AlterarResponsavelRequerimentoInterfaceFacade alterarResponsavelRequerimentoFacade) {
		this.alterarResponsavelRequerimentoFacade = alterarResponsavelRequerimentoFacade;
	}

	@Autowired
	private TurmaDisciplinaCompostaInterfaceFacade turmaDisciplinaCompostaFacade;

	public TurmaDisciplinaCompostaInterfaceFacade getTurmaDisciplinaCompostaFacade() {
		return turmaDisciplinaCompostaFacade;
	}

	public void setTurmaDisciplinaCompostaFacade(TurmaDisciplinaCompostaInterfaceFacade turmaDisciplinaCompostaFacade) {
		this.turmaDisciplinaCompostaFacade = turmaDisciplinaCompostaFacade;
	}

	
	private RenovacaoMatriculaTurmaGradeDisciplinaCompostaInterfaceFacade renovacaoMatriculaTurmaGradeDisciplinaCompostaFacade;

	public RenovacaoMatriculaTurmaGradeDisciplinaCompostaInterfaceFacade getRenovacaoMatriculaTurmaGradeDisciplinaCompostaFacade() {
		return renovacaoMatriculaTurmaGradeDisciplinaCompostaFacade;
	}

	public void setRenovacaoMatriculaTurmaGradeDisciplinaCompostaFacade(
			RenovacaoMatriculaTurmaGradeDisciplinaCompostaInterfaceFacade renovacaoMatriculaTurmaGradeDisciplinaCompostaFacade) {
		this.renovacaoMatriculaTurmaGradeDisciplinaCompostaFacade = renovacaoMatriculaTurmaGradeDisciplinaCompostaFacade;
	}

	public SimularAcessoAlunoInterfaceFacade getSimularAcessoAlunoFacade() {
		return simularAcessoAlunoFacade;
	}

	public void setSimularAcessoAlunoFacade(SimularAcessoAlunoInterfaceFacade simularAcessoAlunoFacade) {
		this.simularAcessoAlunoFacade = simularAcessoAlunoFacade;
	}

	
	private InclusaoHistoricoAlunoInterfaceFacade inclusaoHistoricoAlunoFacade;

	public InclusaoHistoricoAlunoInterfaceFacade getInclusaoHistoricoAlunoFacade() {
		return inclusaoHistoricoAlunoFacade;
	}

	public void setInclusaoHistoricoAlunoFacade(InclusaoHistoricoAlunoInterfaceFacade inclusaoHistoricoAlunoFacade) {
		this.inclusaoHistoricoAlunoFacade = inclusaoHistoricoAlunoFacade;
	}

	
	private InclusaoHistoricoAlunoDisciplinaInterfaceFacade inclusaoHistoricoAlunoDisciplinaFacade;

	public InclusaoHistoricoAlunoDisciplinaInterfaceFacade getInclusaoHistoricoAlunoDisciplinaFacade() {
		return inclusaoHistoricoAlunoDisciplinaFacade;
	}

	public void setInclusaoHistoricoAlunoDisciplinaFacade(
			InclusaoHistoricoAlunoDisciplinaInterfaceFacade inclusaoHistoricoAlunoDisciplinaFacade) {
		this.inclusaoHistoricoAlunoDisciplinaFacade = inclusaoHistoricoAlunoDisciplinaFacade;
	}

	public GestaoEventoConteudoTurmaInterfaceFacade getGestaoEventoConteudoTurmaFacade() {
		return gestaoEventoConteudoTurmaFacade;
	}

	public void setGestaoEventoConteudoTurmaFacade(
			GestaoEventoConteudoTurmaInterfaceFacade gestaoEventoConteudoTurmaFacade) {
		this.gestaoEventoConteudoTurmaFacade = gestaoEventoConteudoTurmaFacade;
	}

	public NotaConceitoAvaliacaoPBLInterfaceFacade getNotaConceitoAvaliacaoPBLFacade() {
		return notaConceitoAvaliacaoPBLFacade;
	}

	public void setNotaConceitoAvaliacaoPBLFacade(
			NotaConceitoAvaliacaoPBLInterfaceFacade notaConceitoAvaliacaoPBLFacade) {
		this.notaConceitoAvaliacaoPBLFacade = notaConceitoAvaliacaoPBLFacade;
	}

	public GestaoEventoConteudoTurmaAvaliacaoPBLInterfaceFacade getGestaoEventoConteudoTurmaAvaliacaoPBLFacade() {
		return gestaoEventoConteudoTurmaAvaliacaoPBLFacade;
	}

	public void setGestaoEventoConteudoTurmaAvaliacaoPBLFacade(
			GestaoEventoConteudoTurmaAvaliacaoPBLInterfaceFacade gestaoEventoConteudoTurmaAvaliacaoPBLFacade) {
		this.gestaoEventoConteudoTurmaAvaliacaoPBLFacade = gestaoEventoConteudoTurmaAvaliacaoPBLFacade;
	}

	public GestaoEventoConteudoTurmaResponsavelAtaInterfaceFacade getGestaoEventoConteudoTurmaResponsavelAtaFacade() {
		return gestaoEventoConteudoTurmaResponsavelAtaFacade;
	}

	public void setGestaoEventoConteudoTurmaResponsavelAtaFacade(
			GestaoEventoConteudoTurmaResponsavelAtaInterfaceFacade gestaoEventoConteudoTurmaAtaFacade) {
		this.gestaoEventoConteudoTurmaResponsavelAtaFacade = gestaoEventoConteudoTurmaAtaFacade;
	}

	public GestaoEventoConteudoTurmaInteracaoAtaInterfaceFacade getGestaoEventoConteudoTurmaInteracaoAtaFacade() {
		return gestaoEventoConteudoTurmaInteracaoAtaFacade;
	}

	public void setGestaoEventoConteudoTurmaInteracaoAtaFacade(
			GestaoEventoConteudoTurmaInteracaoAtaInterfaceFacade gestaoEventoConteudoTurmaInteracaoAtaFacade) {
		this.gestaoEventoConteudoTurmaInteracaoAtaFacade = gestaoEventoConteudoTurmaInteracaoAtaFacade;
	}

	public CalendarioAberturaRequerimentoInterfaceFacade getCalendarioAberturaRequerimentoFacade() {
		return calendarioAberturaRequerimentoFacade;
	}

	public void setCalendarioAberturaRequerimentoFacade(
			CalendarioAberturaRequerimentoInterfaceFacade calendarioAberturaRequerimentoFacade) {
		this.calendarioAberturaRequerimentoFacade = calendarioAberturaRequerimentoFacade;
	}

	public TipoRequerimentoTurmaInterfaceFacade getTipoRequerimentoTurmaFacade() {
		return tipoRequerimentoTurmaFacade;
	}

	public void setTipoRequerimentoTurmaFacade(TipoRequerimentoTurmaInterfaceFacade tipoRequerimentoTurmaFacade) {
		this.tipoRequerimentoTurmaFacade = tipoRequerimentoTurmaFacade;
	}

	@Autowired
	private TipoJustificativaFaltaInterfaceFacade tipoJustificativaFaltaFacade;

	public TipoJustificativaFaltaInterfaceFacade getTipoJustificativaFaltaFacade() {
		return tipoJustificativaFaltaFacade;
	}

	public void setTipoJustificativaFaltaFacade(TipoJustificativaFaltaInterfaceFacade tipoJustificativaFaltaFacade) {
		this.tipoJustificativaFaltaFacade = tipoJustificativaFaltaFacade;
	}

	public ConfiguracaoMobileInterfaceFacade getConfiguracaoMobileFacade() {
		return configuracaoMobileFacade;
	}

	public void setConfiguracaoMobileFacade(ConfiguracaoMobileInterfaceFacade configuracaoMobileFacade) {
		this.configuracaoMobileFacade = configuracaoMobileFacade;
	}

	public ConfiguracaoSeiGsuiteInterfaceFacade getConfiguracaoSeiGsuiteFacade() {
		return configuracaoSeiGsuiteFacade;
	}

	public void setConfiguracaoSeiGsuiteFacade(ConfiguracaoSeiGsuiteInterfaceFacade configuracaoSeiGsuiteFacade) {
		this.configuracaoSeiGsuiteFacade = configuracaoSeiGsuiteFacade;
	}

	public ConfiguracaoSeiGsuiteUnidadeEnsinoInterfaceFacade getConfiguracaoSeiGsuiteUnidadeEnsinoFacade() {
		return configuracaoSeiGsuiteUnidadeEnsinoFacade;
	}

	public void setConfiguracaoSeiGsuiteUnidadeEnsinoFacade(
			ConfiguracaoSeiGsuiteUnidadeEnsinoInterfaceFacade configuracaoSeiGsuiteUnidadeEnsinoFacade) {
		this.configuracaoSeiGsuiteUnidadeEnsinoFacade = configuracaoSeiGsuiteUnidadeEnsinoFacade;
	}

	public PessoaEmailInstitucionalInterfaceFacade getPessoaEmailInstitucionalFacade() {
		return pessoaEmailInstitucionalFacade;
	}

	public void setPessoaEmailInstitucionalFacade(
			PessoaEmailInstitucionalInterfaceFacade pessoaEmailInstitucionalFacade) {
		this.pessoaEmailInstitucionalFacade = pessoaEmailInstitucionalFacade;
	}

	public ConfiguracaoSeiBlackboardInterfaceFacade getConfiguracaoSeiBlackboardFacade() {
		return configuracaoSeiBlackboardFacade;
	}

	public void setConfiguracaoSeiBlackboardFacade(
			ConfiguracaoSeiBlackboardInterfaceFacade configuracaoSeiBlackboardFacade) {
		this.configuracaoSeiBlackboardFacade = configuracaoSeiBlackboardFacade;
	}

	public ConfiguracaoSeiBlackboardDominioInterfaceFacade getConfiguracaoSeiBlackboardDominioFacade() {
		return configuracaoSeiBlackboardDominioFacade;
	}

	public void setConfiguracaoSeiBlackboardDominioFacade(
			ConfiguracaoSeiBlackboardDominioInterfaceFacade configuracaoSeiBlackboardDominioFacade) {
		this.configuracaoSeiBlackboardDominioFacade = configuracaoSeiBlackboardDominioFacade;
	}

	@Autowired
	private CalendarioAberturaTipoRequerimentoraPrazoInterfaceFacade calendarioAberturaTipoRequerimentoraPrazoFacade;

	public CalendarioAberturaTipoRequerimentoraPrazoInterfaceFacade getCalendarioAberturaTipoRequerimentoraPrazoFacade() {
		return calendarioAberturaTipoRequerimentoraPrazoFacade;
	}

	public void setCalendarioAberturaTipoRequerimentoraPrazoFacade(
			CalendarioAberturaTipoRequerimentoraPrazoInterfaceFacade calendarioAberturaTipoRequerimentoraPrazoFacade) {
		this.calendarioAberturaTipoRequerimentoraPrazoFacade = calendarioAberturaTipoRequerimentoraPrazoFacade;
	}

	@Autowired
	private FuncaoBloqueadaInterfaceFacade funcaoBloqueadaFacade;

	public FuncaoBloqueadaInterfaceFacade getFuncaoBloqueadaFacade() {
		return funcaoBloqueadaFacade;
	}

	public void setFuncaoBloqueadaFacade(FuncaoBloqueadaInterfaceFacade funcaoBloqueadaFacade) {
		this.funcaoBloqueadaFacade = funcaoBloqueadaFacade;
	}

	
	private LogTransferenciaBibliotecaExemplarInterfaceFacade logTransferenciaBibliotecaExemplarFacade;

	public LogTransferenciaBibliotecaExemplarInterfaceFacade getLogTransferenciaBibliotecaExemplarFacade() {
		return logTransferenciaBibliotecaExemplarFacade;
	}

	public void setLogTransferenciaBibliotecaExemplarFacade(
			LogTransferenciaBibliotecaExemplarInterfaceFacade logTransferenciaBibliotecaExemplarFacade) {
		this.logTransferenciaBibliotecaExemplarFacade = logTransferenciaBibliotecaExemplarFacade;
	}

	
	private ImpressoraInterfaceFacade impressoraFacade;
	
	private PoolImpressaoInterfaceFacade poolImpressaoFacade;

	public ImpressoraInterfaceFacade getImpressoraFacade() {
		return impressoraFacade;
	}

	public void setImpressoraFacade(ImpressoraInterfaceFacade impressoraFacade) {
		this.impressoraFacade = impressoraFacade;
	}

	public PoolImpressaoInterfaceFacade getPoolImpressaoFacade() {
		return poolImpressaoFacade;
	}

	public void setPoolImpressaoFacade(PoolImpressaoInterfaceFacade poolImpressaoFacade) {
		this.poolImpressaoFacade = poolImpressaoFacade;
	}

	public CampanhaRSLogInterfaceFacade getCampanhaRSLogFacade() {
		return campanhaRSLogFacade;
	}

	public void setCampanhaRSLogFacade(CampanhaRSLogInterfaceFacade campanhaRSLogFacade) {
		this.campanhaRSLogFacade = campanhaRSLogFacade;
	}

	public ConfiguracaoAtualizacaoCadastralInterfaceFacade getConfiguracaoAtualizacaoCadastralFacade() {
		return configuracaoAtualizacaoCadastralFacade;
	}

	public void setConfiguracaoAtualizacaoCadastralFacade(
			ConfiguracaoAtualizacaoCadastralInterfaceFacade configuracaoAtualizacaoCadastralFacade) {
		this.configuracaoAtualizacaoCadastralFacade = configuracaoAtualizacaoCadastralFacade;
	}

	public IndiceElasticInterfaceFacade getIndiceElasticFacade() {
		return indiceElasticFacade;
	}

	public void setIndiceElasticFacade(IndiceElasticInterfaceFacade indiceElasticFacade) {
		this.indiceElasticFacade = indiceElasticFacade;
	}

	@Autowired
	private RespostaAvaliacaoInstitucionalParcialInterfaceFacade respostaAvaliacaoInstitucionalParcialFacade;

	public RespostaAvaliacaoInstitucionalParcialInterfaceFacade getRespostaAvaliacaoInstitucionalParcialFacade() {
		return respostaAvaliacaoInstitucionalParcialFacade;
	}

	public void setRespostaAvaliacaoInstitucionalParcialFacade(
			RespostaAvaliacaoInstitucionalParcialInterfaceFacade respostaAvaliacaoInstitucionalParcialFacade) {
		this.respostaAvaliacaoInstitucionalParcialFacade = respostaAvaliacaoInstitucionalParcialFacade;
	}

	@Autowired
	private ComunicacaoInternaArquivoInterfaceFacade comunicacaoInternaArquivoFacade;

	public ComunicacaoInternaArquivoInterfaceFacade getComunicacaoInternaArquivoFacade() {
		return comunicacaoInternaArquivoFacade;
	}

	public void setComunicacaoInternaArquivoFacade(
			ComunicacaoInternaArquivoInterfaceFacade comunicacaoInternaArquivoFacade) {
		this.comunicacaoInternaArquivoFacade = comunicacaoInternaArquivoFacade;
	}

	@Autowired
	private LogAlteracaoSenhaInterfaceFacade logAlteracaoSenhaFacade;

	public LogAlteracaoSenhaInterfaceFacade getLogAlteracaoSenhaFacade() {
		return logAlteracaoSenhaFacade;
	}

	public void setLogAlteracaoSenhaFacade(LogAlteracaoSenhaInterfaceFacade logAlteracaoSenhaFacade) {
		this.logAlteracaoSenhaFacade = logAlteracaoSenhaFacade;
	}

	@Autowired
	private TurmaDisciplinaNotaTituloInterfaceFacade turmaDisciplinaNotaTituloFacade;

	public TurmaDisciplinaNotaTituloInterfaceFacade getTurmaDisciplinaNotaTituloFacade() {
		return turmaDisciplinaNotaTituloFacade;
	}

	public void setTurmaDisciplinaNotaTituloFacade(
			TurmaDisciplinaNotaTituloInterfaceFacade turmaDisciplinaNotaTituloFacade) {
		this.turmaDisciplinaNotaTituloFacade = turmaDisciplinaNotaTituloFacade;
	}

	@Autowired
	private PlanoEnsinoHorarioAulaInterfaceFacade planoEnsinoHorarioAulaFacade;

	public PlanoEnsinoHorarioAulaInterfaceFacade getPlanoEnsinoHorarioAulaFacade() {
		return planoEnsinoHorarioAulaFacade;
	}

	public void setPlanoEnsinoHorarioAulaFacade(PlanoEnsinoHorarioAulaInterfaceFacade planoEnsinoHorarioAulaFacade) {
		this.planoEnsinoHorarioAulaFacade = planoEnsinoHorarioAulaFacade;
	}

	public ConfiguracaoCandidatoProcessoSeletivoInterfaceFacade getConfiguracaoCandidatoProcessoSeletivoInterfaceFacade() {
		return configuracaoCandidatoProcessoSeletivoInterfaceFacade;
	}

	public void setConfiguracaoCandidatoProcessoSeletivoInterfaceFacade(
			ConfiguracaoCandidatoProcessoSeletivoInterfaceFacade configuracaoCandidatoProcessoSeletivoInterfaceFacade) {
		this.configuracaoCandidatoProcessoSeletivoInterfaceFacade = configuracaoCandidatoProcessoSeletivoInterfaceFacade;
	}

	
	private AlteracaoPlanoFinanceiroAlunoTurmaFacade alteracaoPlanoFinanceiroAlunoTurmaFacade;

	public AlteracaoPlanoFinanceiroAlunoTurmaFacade getAlteracaoPlanoFinanceiroAlunoTurmaFacade() {
		return alteracaoPlanoFinanceiroAlunoTurmaFacade;
	}

	public void setAlteracaoPlanoFinanceiroAlunoTurmaFacade(
			AlteracaoPlanoFinanceiroAlunoTurmaFacade alteracaoPlanoFinanceiroAlunoTurmaFacade) {
		this.alteracaoPlanoFinanceiroAlunoTurmaFacade = alteracaoPlanoFinanceiroAlunoTurmaFacade;
	}

	public CategoriaGEDInterfaceFacade getCategoriaGEDInterfaceFacade() {
		return categoriaGEDInterfaceFacade;
	}

	public void setCategoriaGEDInterfaceFacade(CategoriaGEDInterfaceFacade categoriaGEDInterfaceFacade) {
		this.categoriaGEDInterfaceFacade = categoriaGEDInterfaceFacade;
	}

	public DocumentacaoGEDInterfaceFacade getDocumentacaoGEDInterfaceFacade() {
		return documentacaoGEDInterfaceFacade;
	}

	public void setDocumentacaoGEDInterfaceFacade(DocumentacaoGEDInterfaceFacade documentacaoGEDInterfaceFacade) {
		this.documentacaoGEDInterfaceFacade = documentacaoGEDInterfaceFacade;
	}

	public TipoDocumentoGEDInterfaceFacade getTipoDocumentoGEDInterfaceFacade() {
		return tipoDocumentoGEDInterfaceFacade;
	}

	public void setTipoDocumentoGEDInterfaceFacade(TipoDocumentoGEDInterfaceFacade tipoDocumentoGEDInterfaceFacade) {
		this.tipoDocumentoGEDInterfaceFacade = tipoDocumentoGEDInterfaceFacade;
	}

	@Autowired
	private TipoRequerimentoDepartamentoFuncionarioInterfaceFacade tipoRequerimentoDepartamentoFuncionarioFacade;

	public TipoRequerimentoDepartamentoFuncionarioInterfaceFacade getTipoRequerimentoDepartamentoFuncionarioFacade() {
		return tipoRequerimentoDepartamentoFuncionarioFacade;
	}

	public void setTipoRequerimentoDepartamentoFuncionarioFacade(
			TipoRequerimentoDepartamentoFuncionarioInterfaceFacade tipoRequerimentoDepartamentoFuncionarioFacade) {
		this.tipoRequerimentoDepartamentoFuncionarioFacade = tipoRequerimentoDepartamentoFuncionarioFacade;
	}

	@Autowired
	private TipoRequerimentoSituacaoDepartamentoInterfaceFacade tipoRequerimentoSituacaoDepartamentoFacade;

	public TipoRequerimentoSituacaoDepartamentoInterfaceFacade getTipoRequerimentoSituacaoDepartamentoFacade() {
		return tipoRequerimentoSituacaoDepartamentoFacade;
	}

	public void setTipoRequerimentoSituacaoDepartamentoFacade(
			TipoRequerimentoSituacaoDepartamentoInterfaceFacade tipoRequerimentoSituacaoDepartamentoFacade) {
		this.tipoRequerimentoSituacaoDepartamentoFacade = tipoRequerimentoSituacaoDepartamentoFacade;
	}

	@Autowired
	private SituacaoRequerimentoDepartamentoInterfaceFacade situacaoRequerimentoDepartamentoFacade;

	public SituacaoRequerimentoDepartamentoInterfaceFacade getSituacaoRequerimentoDepartamentoFacade() {
		return situacaoRequerimentoDepartamentoFacade;
	}

	public void setSituacaoRequerimentoDepartamentoFacade(
			SituacaoRequerimentoDepartamentoInterfaceFacade situacaoRequerimentoDepartamentoFacade) {
		this.situacaoRequerimentoDepartamentoFacade = situacaoRequerimentoDepartamentoFacade;
	}

	@Autowired
	private TurmaContratoInterfaceFacade turmaContratoFacade;

	public TurmaContratoInterfaceFacade getTurmaContratoFacade() {
		return turmaContratoFacade;
	}

	public void setTurmaContratoFacade(TurmaContratoInterfaceFacade turmaContratoFacade) {
		this.turmaContratoFacade = turmaContratoFacade;
	}

	@Autowired
	public AvaliacaoInstitucionalCursoInterfaceFacade avaliacaoInstitucionalCurso;

	public AvaliacaoInstitucionalCursoInterfaceFacade getAvaliacaoInstitucionalCurso() {
		return avaliacaoInstitucionalCurso;
	}

	public InteracaoRequerimentoHistoricoInterfaceFacade getInteracaoRequerimentoHistoricoFacade() {
		return interacaoRequerimentoHistoricoFacade;
	}

	public void setInteracaoRequerimentoHistoricoFacade(
			InteracaoRequerimentoHistoricoInterfaceFacade interacaoRequerimentoHistoricoFacade) {
		this.interacaoRequerimentoHistoricoFacade = interacaoRequerimentoHistoricoFacade;
	}

	public PendenciaLiberacaoMatriculaInterfaceFacade getPendenciaLiberacaoMatriculaInterfaceFacade() {
		return pendenciaLiberacaoMatriculaInterfaceFacade;
	}

	public void setPendenciaLiberacaoMatriculaInterfaceFacade(
			PendenciaLiberacaoMatriculaInterfaceFacade pendenciaLiberacaoMatriculaInterfaceFacade) {
		this.pendenciaLiberacaoMatriculaInterfaceFacade = pendenciaLiberacaoMatriculaInterfaceFacade;
	}

	public LdapInterfaceFacade getLdapFacade() {
		return ldapFacade;
	}

	public void setLdapFacade(LdapInterfaceFacade ldapFacade) {
		this.ldapFacade = ldapFacade;
	}

	public PesquisaPadraoAlunoInterfaceFacade getPesquisaPadraoAlunoFacade() {
		return pesquisaPadraoAlunoFacade;
	}

	public void setPesquisaPadraoAlunoFacade(PesquisaPadraoAlunoInterfaceFacade pesquisaPadraoAlunoFacade) {
		this.pesquisaPadraoAlunoFacade = pesquisaPadraoAlunoFacade;
	}

	@Autowired
	private TratamentoErroInterfaceFacade tratamentoErroFacade;

	public TratamentoErroInterfaceFacade getTratamentoErroFacade() {
		return tratamentoErroFacade;
	}

	public void setTratamentoErroFacade(TratamentoErroInterfaceFacade tratamentoErroFacade) {
		this.tratamentoErroFacade = tratamentoErroFacade;
	}

	public ScriptExecutadoInterfaceFacade getScriptExecutadoInterfaceFacade() {
		return scriptExecutadoInterfaceFacade;
	}

	public void setScriptExecutadoInterfaceFacade(ScriptExecutadoInterfaceFacade scriptExecutadoInterfaceFacade) {
		this.scriptExecutadoInterfaceFacade = scriptExecutadoInterfaceFacade;
	}

	public SQLInterpreter getSqlInterpreter() {
		return sqlInterpreter;
	}

	public void setSqlInterpreter(SQLInterpreter sqlInterpreter) {
		this.sqlInterpreter = sqlInterpreter;
	}

	public TurmaDisciplinaNotaParcialInterfaceFacade getTurmaDisciplinaNotaParcialInterfaceFacade() {
		return turmaDisciplinaNotaParcialInterfaceFacade;
	}

	public void setTurmaDisciplinaNotaParcialInterfaceFacade(
			TurmaDisciplinaNotaParcialInterfaceFacade turmaDisciplinaNotaParcialInterfaceFacade) {
		this.turmaDisciplinaNotaParcialInterfaceFacade = turmaDisciplinaNotaParcialInterfaceFacade;
	}

	public HistoricoNotaParcialInterfaceFacade getHistoricoNotaParcialInterfaceFacade() {
		return historicoNotaParcialInterfaceFacade;
	}

	public void setHistoricoNotaParcialInterfaceFacade(
			HistoricoNotaParcialInterfaceFacade historicoNotaParcialInterfaceFacade) {
		this.historicoNotaParcialInterfaceFacade = historicoNotaParcialInterfaceFacade;
	}

	public PendenciaTipoDocumentoTipoRequerimentoInterfaceFacade getPendenciaTipoDocumentoTipoRequerimentoInterfaceFacade() {
		return pendenciaTipoDocumentoTipoRequerimentoInterfaceFacade;
	}

	public void setPendenciaTipoDocumentoTipoRequerimentoInterfaceFacade(
			PendenciaTipoDocumentoTipoRequerimentoInterfaceFacade pendenciaTipoDocumentoTipoRequerimentoInterfaceFacade) {
		this.pendenciaTipoDocumentoTipoRequerimentoInterfaceFacade = pendenciaTipoDocumentoTipoRequerimentoInterfaceFacade;
	}

	public AvaliacaoInstitucionalRespondenteInterfaceFacade getAvaliacaoInstitucionalRespondenteInterfaceFacade() {
		return avaliacaoInstitucionalRespondenteInterfaceFacade;
	}

	public void setAvaliacaoInstitucionalRespondenteInterfaceFacade(
			AvaliacaoInstitucionalRespondenteInterfaceFacade avaliacaoInstitucionalRespondenteInterfaceFacade) {
		this.avaliacaoInstitucionalRespondenteInterfaceFacade = avaliacaoInstitucionalRespondenteInterfaceFacade;
	}

	public ConfiguracaoGEDInterfaceFacade getConfiguracaoGEDFacade() {
		return configuracaoGEDFacade;
	}

	public void setConfiguracaoGEDFacade(ConfiguracaoGEDInterfaceFacade configuracaoGEDFacade) {
		this.configuracaoGEDFacade = configuracaoGEDFacade;
	}

	@Autowired
	private QuestionarioRespostaOrigemInterfaceFacade questionarioRespostaOrigemInterfaceFacade;

	public QuestionarioRespostaOrigemInterfaceFacade getQuestionarioRespostaOrigemInterfaceFacade() {
		return questionarioRespostaOrigemInterfaceFacade;
	}

	public void setQuestionarioRespostaOrigemInterfaceFacade(
			QuestionarioRespostaOrigemInterfaceFacade questionarioRespostaOrigemInterfaceFacade) {
		this.questionarioRespostaOrigemInterfaceFacade = questionarioRespostaOrigemInterfaceFacade;
	}

	@Autowired
	private PerguntaRespostaOrigemInterfaceFacade perguntaRespostaOrigemInterfaceFacade;

	public PerguntaRespostaOrigemInterfaceFacade getPerguntaRespostaOrigemInterfaceFacade() {
		return perguntaRespostaOrigemInterfaceFacade;
	}

	public void setPerguntaRespostaOrigemInterfaceFacade(
			PerguntaRespostaOrigemInterfaceFacade perguntaRespostaOrigemInterfaceFacade) {
		this.perguntaRespostaOrigemInterfaceFacade = perguntaRespostaOrigemInterfaceFacade;
	}

	@Autowired
	private QuestionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade questionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade;

	public QuestionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade getQuestionarioRespostaOrigemMotivosPadroesEstagioFacade() {
		return questionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade;
	}

	public void setQuestionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade(
			QuestionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade questionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade) {
		this.questionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade = questionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade;
	}

	@Autowired
	private PerguntaItemRespostaOrigemInterfaceFacade perguntaItemRespostaOrigemInterfaceFacade;

	public PerguntaItemRespostaOrigemInterfaceFacade getPerguntaItemRespostaOrigemInterfaceFacade() {
		return perguntaItemRespostaOrigemInterfaceFacade;
	}

	public void setPerguntaItemRespostaOrigemInterfaceFacade(
			PerguntaItemRespostaOrigemInterfaceFacade perguntaItemRespostaOrigemInterfaceFacade) {
		this.perguntaItemRespostaOrigemInterfaceFacade = perguntaItemRespostaOrigemInterfaceFacade;
	}

	@Autowired
	private RespostaPerguntaRespostaOrigemInterfaceFacade respostaPerguntaRespostaOrigemInterfaceFacade;

	public RespostaPerguntaRespostaOrigemInterfaceFacade getRespostaPerguntaRespostaOrigemInterfaceFacade() {
		return respostaPerguntaRespostaOrigemInterfaceFacade;
	}

	public void setRespostaPerguntaRespostaOrigemInterfaceFacade(
			RespostaPerguntaRespostaOrigemInterfaceFacade respostaPerguntaRespostaOrigemInterfaceFacade) {
		this.respostaPerguntaRespostaOrigemInterfaceFacade = respostaPerguntaRespostaOrigemInterfaceFacade;
	}

	@Autowired
	private PerguntaChecklistOrigemInterfaceFacade perguntaChecklistOrigemFacade;

	public PerguntaChecklistOrigemInterfaceFacade getPerguntaChecklistOrigemFacade() {
		return perguntaChecklistOrigemFacade;
	}

	public void setPerguntaChecklistOrigemFacade(PerguntaChecklistOrigemInterfaceFacade perguntaChecklistOrigemFacade) {
		this.perguntaChecklistOrigemFacade = perguntaChecklistOrigemFacade;
	}

	public SalaAulaBlackboardInterfaceFacade getSalaAulaBlackboardFacade() {
		return salaAulaBlackboardFacade;
	}

	public void setSalaAulaBlackboardFacade(SalaAulaBlackboardInterfaceFacade salaAulaBlackboardFacade) {
		this.salaAulaBlackboardFacade = salaAulaBlackboardFacade;
	}

	public SalaAulaBlackboardPessoaInterfaceFacade getSalaAulaBlackboardPessoaFacade() {
		return salaAulaBlackboardPessoaFacade;
	}

	public void setSalaAulaBlackboardPessoaFacade(
			SalaAulaBlackboardPessoaInterfaceFacade salaAulaBlackboardPessoaFacade) {
		this.salaAulaBlackboardPessoaFacade = salaAulaBlackboardPessoaFacade;
	}

	public AssinarDocumentoEntregueInterfaceFacade getAssinarDocumentoEntregueInterfaceFacade() {
		return assinarDocumentoEntregueInterfaceFacade;
	}

	public void setAssinarDocumentoEntregueInterfaceFacade(
			AssinarDocumentoEntregueInterfaceFacade assinarDocumentoEntregueInterfaceFacade) {
		this.assinarDocumentoEntregueInterfaceFacade = assinarDocumentoEntregueInterfaceFacade;
	}

	@Autowired
	private AvaliacaoOnlineTemaAssuntoInterfaceFacade avaliacaoOnlineTemaAssuntoInterfaceFacade;

	public AvaliacaoOnlineTemaAssuntoInterfaceFacade getAvaliacaoOnlineTemaAssuntoInterfaceFacade() {
		return avaliacaoOnlineTemaAssuntoInterfaceFacade;
	}

	public void setAvaliacaoOnlineTemaAssuntoInterfaceFacade(
			AvaliacaoOnlineTemaAssuntoInterfaceFacade avaliacaoOnlineTemaAssuntoInterfaceFacade) {
		this.avaliacaoOnlineTemaAssuntoInterfaceFacade = avaliacaoOnlineTemaAssuntoInterfaceFacade;
	}

	@Autowired
	private DashboardInterfaceFacade dashboardInterfaceFacade;

	public DashboardInterfaceFacade getDashboardInterfaceFacade() {
		return dashboardInterfaceFacade;
	}

	public void setDashboardInterfaceFacade(DashboardInterfaceFacade dashboardInterfaceFacade) {
		this.dashboardInterfaceFacade = dashboardInterfaceFacade;
	}

	@Autowired
	private ConfiguracaoAparenciaSistemaInterfaceFacade configuracaoAparenciaSistemaFacade;

	public ConfiguracaoAparenciaSistemaInterfaceFacade getConfiguracaoAparenciaSistemaFacade() {
		return configuracaoAparenciaSistemaFacade;
	}

	public void setConfiguracaoAparenciaSistemaFacade(
			ConfiguracaoAparenciaSistemaInterfaceFacade configuracaoAparenciaSistemaFacade) {
		this.configuracaoAparenciaSistemaFacade = configuracaoAparenciaSistemaFacade;
	}

//	public HorarioTurmaDisciplinaProgramadaInterfaceFacade getHorarioTurmaDisciplinaProgramadaFacade() {
//		return horarioTurmaDisciplinaProgramadaFacade;
//	}
//
//	public void setHorarioTurmaDisciplinaProgramadaFacade(
//			HorarioTurmaDisciplinaProgramadaInterfaceFacade horarioTurmaDisciplinaProgramadaFacade) {
//		this.horarioTurmaDisciplinaProgramadaFacade = horarioTurmaDisciplinaProgramadaFacade;
//	}

	@Autowired
	private PreferenciaSistemaUsuarioInterfaceFacade preferenciaSistemaUsuarioFacade;

	public PreferenciaSistemaUsuarioInterfaceFacade getPreferenciaSistemaUsuarioFacade() {
		return preferenciaSistemaUsuarioFacade;
	}

	public void setPreferenciaSistemaUsuarioFacade(
			PreferenciaSistemaUsuarioInterfaceFacade preferenciaSistemaUsuarioFacade) {
		this.preferenciaSistemaUsuarioFacade = preferenciaSistemaUsuarioFacade;
	}

	@Autowired
	private ConfiguracaoGedOrigemInterfaceFacade configuracaoGedOrigemInterfaceFacade;

	public ConfiguracaoGedOrigemInterfaceFacade getConfiguracaoGedOrigemInterfaceFacade() {
		return configuracaoGedOrigemInterfaceFacade;
	}

	public void setConfiguracaoGedOrigemInterfaceFacade(
			ConfiguracaoGedOrigemInterfaceFacade configuracaoGedOrigemInterfaceFacade) {
		this.configuracaoGedOrigemInterfaceFacade = configuracaoGedOrigemInterfaceFacade;
	}

	@Autowired
	private PersonalizacaoMensagemAutomaticaUnidadeEnsinoInterfaceFacade PersonalizacaoMensagemAutomaticaUnidadeEnsinoFacade;

	public PersonalizacaoMensagemAutomaticaUnidadeEnsinoInterfaceFacade getPersonalizacaoMensagemAutomaticaUnidadeEnsinoFacade() {
		return PersonalizacaoMensagemAutomaticaUnidadeEnsinoFacade;
	}

	public void setPersonalizacaoMensagemAutomaticaUnidadeEnsinoFacade(
			PersonalizacaoMensagemAutomaticaUnidadeEnsinoInterfaceFacade personalizacaoMensagemAutomaticaUnidadeEnsinoFacade) {
		PersonalizacaoMensagemAutomaticaUnidadeEnsinoFacade = personalizacaoMensagemAutomaticaUnidadeEnsinoFacade;
	}

	public LogRegistroOperacoesInterfaceFacade getLogRegistroOperacoesFacade() {
		return logRegistroOperacoesFacade;
	}

	public void setLogRegistroOperacoesFacade(LogRegistroOperacoesInterfaceFacade logRegistroOperacoesFacade) {
		this.logRegistroOperacoesFacade = logRegistroOperacoesFacade;
	}

	public ConfiguracaoLdapInterfaceFacade getConfiguracaoLdapInterfaceFacade() {
		return configuracaoLdapInterfaceFacade;
	}

	public void setConfiguracaoLdapInterfaceFacade(ConfiguracaoLdapInterfaceFacade configuracaoLdapInterfaceFacade) {
		this.configuracaoLdapInterfaceFacade = configuracaoLdapInterfaceFacade;
	}

	@Autowired
	private ConfiguracaoHistoricoInterfaceFacade configuracaoHistoricoFacade;
	@Autowired
	private ConfiguracaoLayoutHistoricoInterfaceFacade configuracaoLayoutHistoricoFacade;
	@Autowired
	private ConfiguracaoObservacaoHistoricoInterfaceFacade configuracaoObservacaoHistoricoFacade;

	public ConfiguracaoHistoricoInterfaceFacade getConfiguracaoHistoricoFacade() {
		return configuracaoHistoricoFacade;
	}

	public void setConfiguracaoHistoricoFacade(ConfiguracaoHistoricoInterfaceFacade configuracaoHistoricoFacade) {
		this.configuracaoHistoricoFacade = configuracaoHistoricoFacade;
	}

	public ConfiguracaoLayoutHistoricoInterfaceFacade getConfiguracaoLayoutHistoricoFacade() {
		return configuracaoLayoutHistoricoFacade;
	}

	public void setConfiguracaoLayoutHistoricoFacade(
			ConfiguracaoLayoutHistoricoInterfaceFacade configuracaoLayoutHistoricoFacade) {
		this.configuracaoLayoutHistoricoFacade = configuracaoLayoutHistoricoFacade;
	}

	public ConfiguracaoObservacaoHistoricoInterfaceFacade getConfiguracaoObservacaoHistoricoFacade() {
		return configuracaoObservacaoHistoricoFacade;
	}

	public void setConfiguracaoObservacaoHistoricoFacade(
			ConfiguracaoObservacaoHistoricoInterfaceFacade configuracaoObservacaoHistoricoFacade) {
		this.configuracaoObservacaoHistoricoFacade = configuracaoObservacaoHistoricoFacade;
	}

	@Autowired
	private SalaAulaBlackboardOperacaoInterfaceFacade salaAulaBlackboardOperacaoFacade;

	public SalaAulaBlackboardOperacaoInterfaceFacade getSalaAulaBlackboardOperacaoFacade() {
		return salaAulaBlackboardOperacaoFacade;
	}

	public void setSalaAulaBlackboardOperacaoFacade(
			SalaAulaBlackboardOperacaoInterfaceFacade salaAulaBlackboardOperacaoFacade) {
		this.salaAulaBlackboardOperacaoFacade = salaAulaBlackboardOperacaoFacade;
	}

	@Autowired
	private ProcessoMatriculaUnidadeEnsinoInterfaceFacade processoMatriculaUnidadeEnsinoFacade;

	public ProcessoMatriculaUnidadeEnsinoInterfaceFacade getProcessoMatriculaUnidadeEnsinoFacade() {
		return processoMatriculaUnidadeEnsinoFacade;
	}

	public void setProcessoMatriculaUnidadeEnsinoFacade(
			ProcessoMatriculaUnidadeEnsinoInterfaceFacade processoMatriculaUnidadeEnsinoFacade) {
		this.processoMatriculaUnidadeEnsinoFacade = processoMatriculaUnidadeEnsinoFacade;
	}

	private ProgramacaoFormaturaUnidadeEnsinoInterfaceFacade programacaoFormaturaUnidadeEnsinoInterfaceFacade;

	public ProgramacaoFormaturaUnidadeEnsinoInterfaceFacade getProgramacaoFormaturaUnidadeEnsinoInterfaceFacade() {
		return programacaoFormaturaUnidadeEnsinoInterfaceFacade;
	}

	public void setProgramacaoFormaturaUnidadeEnsinoInterfaceFacade(
			ProgramacaoFormaturaUnidadeEnsinoInterfaceFacade programacaoFormaturaUnidadeEnsinoInterfaceFacade) {
		this.programacaoFormaturaUnidadeEnsinoInterfaceFacade = programacaoFormaturaUnidadeEnsinoInterfaceFacade;
	}

	private ProgramacaoFormaturaCursoInterfaceFacade programacaoFormaturaCursoInterfaceFacade;

	public ProgramacaoFormaturaCursoInterfaceFacade getProgramacaoFormaturaCursoInterfaceFacade() {
		return programacaoFormaturaCursoInterfaceFacade;
	}

	public void setProgramacaoFormaturaCursoInterfaceFacade(
			ProgramacaoFormaturaCursoInterfaceFacade programacaoFormaturaCursoInterfaceFacade) {
		this.programacaoFormaturaCursoInterfaceFacade = programacaoFormaturaCursoInterfaceFacade;
	}

	@Autowired
	private RegistroWebserviceInterfaceFacade registroWebserviceFacade;

	public RegistroWebserviceInterfaceFacade getRegistroWebserviceFacade() {
		return registroWebserviceFacade;
	}

	public void setRegistroWebserviceFacade(RegistroWebserviceInterfaceFacade registroWebserviceFacade) {
		this.registroWebserviceFacade = registroWebserviceFacade;
	}

	@Autowired
	private TipoRequerimentoCursoTransferenciaInterfaceFacade tipoRequerimentoCursoTransferenciaFacade;

	public TipoRequerimentoCursoTransferenciaInterfaceFacade getTipoRequerimentoCursoTransferenciaFacade() {
		return tipoRequerimentoCursoTransferenciaFacade;
	}

	public void setTipoRequerimentoCursoTransferenciaFacade(
			TipoRequerimentoCursoTransferenciaInterfaceFacade tipoRequerimentoCursoTransferenciaFacade) {
		this.tipoRequerimentoCursoTransferenciaFacade = tipoRequerimentoCursoTransferenciaFacade;
	}

	public OcorrenciaLGPDInterfaceFacade getOcorrenciaLGPDInterfaceFacade() {
		return ocorrenciaLGPDInterfaceFacade;
	}

	public void setOcorrenciaLGPDInterfaceFacade(OcorrenciaLGPDInterfaceFacade ocorrenciaLGPDInterfaceFacade) {
		this.ocorrenciaLGPDInterfaceFacade = ocorrenciaLGPDInterfaceFacade;
	}

	@Autowired
	private RequerimentoDisciplinaInterfaceFacade requerimentoDisciplinaInterfaceFacade;

	public void setRequerimentoDisciplinaInterfaceFacade(
			RequerimentoDisciplinaInterfaceFacade requerimentoDisciplinaInterfaceFacade) {
		this.requerimentoDisciplinaInterfaceFacade = requerimentoDisciplinaInterfaceFacade;
	}

	public RequerimentoDisciplinaInterfaceFacade getRequerimentoDisciplinaInterfaceFacade() {
		return requerimentoDisciplinaInterfaceFacade;
	}

	public LinksUteisInterfaceFacade getLinksUteisFacade() {
		return linksUteisFacade;
	}

	public void setLinksUteisFacade(LinksUteisInterfaceFacade linksUteisFacade) {
		this.linksUteisFacade = linksUteisFacade;
	}

	public UsuarioLinksUteisInterfaceFacade getUsuarioLinksUteisFacade() {
		return usuarioLinksUteisFacade;
	}

	public void setUsuarioLinksUteisFacade(UsuarioLinksUteisInterfaceFacade usuarioLinksUteisFacade) {
		this.usuarioLinksUteisFacade = usuarioLinksUteisFacade;
	}

	
	private ConfiguracaoAtaResultadosFinaisInterfaceFacade configuracaoAtaResultadosFinaisFacade;
	
	private ConfiguracaoLayoutAtaResultadosFinaisInterfaceFacade configuracaoLayoutAtaResultadosFinaisFacade;

	public ConfiguracaoAtaResultadosFinaisInterfaceFacade getConfiguracaoAtaResultadosFinaisFacade() {
		return configuracaoAtaResultadosFinaisFacade;
	}

	public void setConfiguracaoAtaResultadosFinaisFacade(
			ConfiguracaoAtaResultadosFinaisInterfaceFacade configuracaoAtaResultadosFinaisFacade) {
		this.configuracaoAtaResultadosFinaisFacade = configuracaoAtaResultadosFinaisFacade;
	}

	public ConfiguracaoLayoutAtaResultadosFinaisInterfaceFacade getConfiguracaoLayoutAtaResultadosFinaisFacade() {
		return configuracaoLayoutAtaResultadosFinaisFacade;
	}

	public void setConfiguracaoLayoutAtaResultadosFinaisFacade(
			ConfiguracaoLayoutAtaResultadosFinaisInterfaceFacade configuracaoLayoutAtaResultadosFinaisFacade) {
		this.configuracaoLayoutAtaResultadosFinaisFacade = configuracaoLayoutAtaResultadosFinaisFacade;
	}

	public EixoCursoInterfaceFacade getEixoCursoFacade() {
		return eixoCursoFacade;
	}

	public void setEixoCursoFacade(EixoCursoInterfaceFacade eixoCursoFacade) {
		this.eixoCursoFacade = eixoCursoFacade;
	}

	@Autowired
	private CalendarioAgrupamentoDisciplinaInterfaceFacade calendarioAgrupamentoDisciplinaFacade;

	public CalendarioAgrupamentoDisciplinaInterfaceFacade getCalendarioAgrupamentoDisciplinaFacade() {
		return calendarioAgrupamentoDisciplinaFacade;
	}

	public void setCalendarioAgrupamentoDisciplinaFacade(
			CalendarioAgrupamentoDisciplinaInterfaceFacade calendarioAgrupamentoDisciplinaFacade) {
		this.calendarioAgrupamentoDisciplinaFacade = calendarioAgrupamentoDisciplinaFacade;
	}

	@Autowired
	private LogOperacaoEnsalamentoBlackboardInterfaceFacade logOperacaoEnsalamentoBlackboardFacade;

	public LogOperacaoEnsalamentoBlackboardInterfaceFacade getLogOperacaoEnsalamentoBlackboardFacade() {
		return logOperacaoEnsalamentoBlackboardFacade;
	}

	public void setLogOperacaoEnsalamentoBlackboardFacade(
			LogOperacaoEnsalamentoBlackboardInterfaceFacade logOperacaoEnsalamentoBlackboardFacade) {
		this.logOperacaoEnsalamentoBlackboardFacade = logOperacaoEnsalamentoBlackboardFacade;
	}

	
	private LogAuditHistoricoInterfaceFacade logAuditHistoricoFacade;

	public LogAuditHistoricoInterfaceFacade getLogAuditHistoricoFacade() {
		return logAuditHistoricoFacade;
	}

	public void setLogAuditHistoricoFacade(LogAuditHistoricoInterfaceFacade logAuditHistoricoFacade) {
		this.logAuditHistoricoFacade = logAuditHistoricoFacade;
	}

	@Autowired
	private MotivoIndeferimentoDocumentoAlunoInterfaceFacade motivoIndeferimentoDocumentoAlunoInterfaceFacade;

	public MotivoIndeferimentoDocumentoAlunoInterfaceFacade getMotivoIndeferimentoDocumentoAlunoInterfaceFacade() {
		return motivoIndeferimentoDocumentoAlunoInterfaceFacade;
	}

	public void setMotivoIndeferimentoDocumentoAlunoInterfaceFacade(
			MotivoIndeferimentoDocumentoAlunoInterfaceFacade motivoIndeferimentoDocumentoAlunoInterfaceFacade) {
		this.motivoIndeferimentoDocumentoAlunoInterfaceFacade = motivoIndeferimentoDocumentoAlunoInterfaceFacade;
	}

	@Autowired
	private OfertaDisciplinaInterfaceFacade ofertaDisciplinaFacade;

	public OfertaDisciplinaInterfaceFacade getOfertaDisciplinaFacade() {
		return ofertaDisciplinaFacade;
	}

	public void setOfertaDisciplinaFacade(OfertaDisciplinaInterfaceFacade ofertaDisciplinaFacade) {
		this.ofertaDisciplinaFacade = ofertaDisciplinaFacade;
	}

	@Autowired
	private FiltroPersonalizadoInterfaceFacade filtroPersonalizadoFacade;

	public FiltroPersonalizadoInterfaceFacade getFiltroPersonalizadoFacade() {
		return filtroPersonalizadoFacade;
	}

	public void setFiltroPersonalizadoFacade(FiltroPersonalizadoInterfaceFacade filtroPersonalizadoFacade) {
		this.filtroPersonalizadoFacade = filtroPersonalizadoFacade;
	}

	@Autowired
	private FiltroPersonalizadoOpcaoInterfaceFacade filtroPersonalizadoOpcaoInterfaceFacade;

	public FiltroPersonalizadoOpcaoInterfaceFacade getFiltroPersonalizadoOpcaoInterfaceFacade() {
		return filtroPersonalizadoOpcaoInterfaceFacade;
	}

	public void setFiltroPersonalizadoOpcaoInterfaceFacade(
			FiltroPersonalizadoOpcaoInterfaceFacade filtroPersonalizadoOpcaoInterfaceFacade) {
		this.filtroPersonalizadoOpcaoInterfaceFacade = filtroPersonalizadoOpcaoInterfaceFacade;
	}

	@Autowired
	private CidTipoRequerimentoInterfaceFacade cidTipoRequerimentoInterfaceFacade;

	public CidTipoRequerimentoInterfaceFacade getCidTipoRequerimentoInterfaceFacade() {
		return cidTipoRequerimentoInterfaceFacade;
	}

	public void setCidTipoRequerimentoInterfaceFacade(
			CidTipoRequerimentoInterfaceFacade cidTipoRequerimentoInterfaceFacade) {
		this.cidTipoRequerimentoInterfaceFacade = cidTipoRequerimentoInterfaceFacade;
	}

	@Autowired
	private RequerimentoCidTipoRequerimentoIntefaceFacade requerimentoCidTipoRequerimentoIntefaceFacade;

	public RequerimentoCidTipoRequerimentoIntefaceFacade getRequerimentoCidTipoRequerimentoIntefaceFacade() {
		return requerimentoCidTipoRequerimentoIntefaceFacade;
	}

	public void setRequerimentoCidTipoRequerimentoIntefaceFacade(
			RequerimentoCidTipoRequerimentoIntefaceFacade requerimentoCidTipoRequerimentoIntefaceFacade) {
		this.requerimentoCidTipoRequerimentoIntefaceFacade = requerimentoCidTipoRequerimentoIntefaceFacade;
	}

	public BlackboardFechamentoNotaOperacaoInterfaceFacade getBlackboardFechamentoNotaOperacaoInterfaceFacade() {
		return blackboardFechamentoNotaOperacaoInterfaceFacade;
	}

	public void setBlackboardFechamentoNotaOperacaoInterfaceFacade(
			BlackboardFechamentoNotaOperacaoInterfaceFacade blackboardFechamentoNotaOperacaoInterfaceFacade) {
		this.blackboardFechamentoNotaOperacaoInterfaceFacade = blackboardFechamentoNotaOperacaoInterfaceFacade;
	}

	public ExpedicaoDiplomaDigitalInterfaceFacade getExpedicaoDiplomaDigital_1_05_interfaceFacade() {
		return expedicaoDiplomaDigital_1_05_interfaceFacade;
	}

	public void setExpedicaoDiplomaDigital_1_05_interfaceFacade(
			ExpedicaoDiplomaDigitalInterfaceFacade expedicaoDiplomaDigital_1_05_interfaceFacade) {
		this.expedicaoDiplomaDigital_1_05_interfaceFacade = expedicaoDiplomaDigital_1_05_interfaceFacade;
	}

	public IntegracaoMestreGRInterfaceFacade getIntegracaoMestreGRInterfaceFacade() {
		return integracaoMestreGRInterfaceFacade;
	}

	public void setIntegracaoMestreGRInterfaceFacade(
			IntegracaoMestreGRInterfaceFacade integracaoMestreGRInterfaceFacade) {
		this.integracaoMestreGRInterfaceFacade = integracaoMestreGRInterfaceFacade;
	}

	public ProcessamentoFilaThreadsStrategy getProcessamentoFilaThreadsStrategy() {
		return processamentoFilaThreadsStrategy;
	}

	public void setProcessamentoFilaThreadsStrategy(ProcessamentoFilaThreadsStrategy processamentoFilaThreadsStrategy) {
		this.processamentoFilaThreadsStrategy = processamentoFilaThreadsStrategy;
	}

	public CalendarioRelatorioFinalFacilitadorInterfaceFacade getCalendarioRelatorioFinalFacilitadorInterfaceFacade() {
		return calendarioRelatorioFinalFacilitadorInterfaceFacade;
	}

	public void setCalendarioRelatorioFinalFacilitadorInterfaceFacade(
			CalendarioRelatorioFinalFacilitadorInterfaceFacade calendarioRelatorioFinalFacilitadorInterfaceFacade) {
		this.calendarioRelatorioFinalFacilitadorInterfaceFacade = calendarioRelatorioFinalFacilitadorInterfaceFacade;
	}

	public RelatorioFinalFacilitadorInterfaceFacade getRelatorioFinalFacilitadorInterfaceFacade() {
		return relatorioFinalFacilitadorInterfaceFacade;
	}

	public void setRelatorioFinalFacilitadorInterfaceFacade(
			RelatorioFinalFacilitadorInterfaceFacade relatorioFinalFacilitadorInterfaceFacade) {
		this.relatorioFinalFacilitadorInterfaceFacade = relatorioFinalFacilitadorInterfaceFacade;
	}

	public IntegracaoSymplictyInterfaceFacade getIntegracaoSymplictyInterfaceFacade() {
		return integracaoSymplictyInterfaceFacade;
	}

	public void setIntegracaoSymplictyInterfaceFacade(
			IntegracaoSymplictyInterfaceFacade integracaoSymplictyInterfaceFacade) {
		this.integracaoSymplictyInterfaceFacade = integracaoSymplictyInterfaceFacade;
	}

	@Autowired
	public AvaliacaoInstitucionalUnidadeEnsinoInterfaceFacade avaliacaoInstitucionalUnidadeEnsino;

	public AvaliacaoInstitucionalUnidadeEnsinoInterfaceFacade getAvaliacaoInstitucionalUnidadeEnsino() {
		return avaliacaoInstitucionalUnidadeEnsino;
	}

	public void setAvaliacaoInstitucionalUnidadeEnsino(
			AvaliacaoInstitucionalUnidadeEnsinoInterfaceFacade avaliacaoInstitucionalUnidadeEnsinoInterfaceFacade) {
		this.avaliacaoInstitucionalUnidadeEnsino = avaliacaoInstitucionalUnidadeEnsinoInterfaceFacade;
	}

	public ServicoIntegracaoSentryInterfaceFacade getServicoIntegracaoSentryInterfaceFacade() {
		return servicoIntegracaoSentryInterfaceFacade;
	}

	public void setServicoIntegracaoSentryInterfaceFacade(
			ServicoIntegracaoSentryInterfaceFacade servicoIntegracaoSentryInterfaceFacade) {
		this.servicoIntegracaoSentryInterfaceFacade = servicoIntegracaoSentryInterfaceFacade;
	}
}
