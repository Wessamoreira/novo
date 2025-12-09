package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.academico.RenovarMatriculaControle;
import controle.arquitetura.DataModelo;
import jakarta.faces.model.SelectItem;
import negocio.comuns.academico.ColacaoGrauVO;
//import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.HistoricoVO;
//import negocio.comuns.academico.LogMatriculaVO;
import negocio.comuns.academico.MatriculaControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.MatriculaIntegralizacaoCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ObservacaoComplementarHistoricoAlunoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
//import relatorio.negocio.comuns.academico.ControleLivroRegistroDiplomaRelVO;
import webservice.servicos.MatriculaRSVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface MatriculaInterfaceFacade {
	
	List<SelectItem> inicializarDadosListaSelectItemMesAnoBaseadoComunicacaoInterna(Integer aluno, UsuarioVO usuarioVO);	
	
	void montarDadosUnidadeEnsino(MatriculaVO obj, int nivelMontarDados) throws Exception;


	public Boolean validarUnicidade(String novaMatricula);
	
	public void carregarDados(MatriculaVO valueObj, UsuarioVO usuario) throws Exception;

	public void carregarDados(MatriculaVO valueObj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarPorChavePrimaria(String matriculaPrm, Integer unidadeEnsino, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatricula(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaVO> consultaRapidaPorMatricula(String matricula, Integer unidadeEnsino, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaVO> consultaRapidaPorMatricula(String nomePessoa, Integer unidadeEnsino, Integer parceiro, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, Integer unidadeEnsino, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, Integer unidadeEnsino, Integer parceiro, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, String situacao, String nivelEducacional, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomeCurso(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomeCurso(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorCPF(String cpfPessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public MatriculaVO novo() throws Exception;

	public void incluir(MatriculaVO obj, ProcessoMatriculaCalendarioVO processoMatriculaCalendario,  UsuarioVO usuario) throws Exception;

	public void alterar(MatriculaVO ob, ProcessoMatriculaCalendarioVO processoMatriculaCalendario,   UsuarioVO usuario) throws Exception;

	public void alterarSituacaoMatriculaFormadaAtualizacao(final MatriculaVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(MatriculaVO obj,  UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarPorObjetoMatriculaAtivaOuTrancada(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultarPorNomePessoaAtivaOuTrancada(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultarPorNomeCursoAtivoTrancado(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorCPF(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorPessoaSituacao(Integer pessoa, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;	

	public MatriculaVO consultarPorChavePrimaria(String matricula, Integer unidadeEnsino, int nivelMontarDados,  UsuarioVO usuario) throws Exception;
	
	public MatriculaVO consultarAlunoPorMatricula(String prm, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;


	public List consultarPorMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorTurma_gradeCurricular(Integer turma, Integer periodoLetivo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorTurma_periodoLetivo(Integer turma, Integer periodoLetivo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

//	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestrePeriodoLetivoGradeCurricularBoletim(TurmaVO turma, Integer curso, String ano, String semestre, Integer gradeCurricular, Integer periodoLetivo, Integer unidadeEnsino, UsuarioVO usuario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota) throws Exception;

//	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestrePeriodoLetivoBoletim(List<TurmaAgrupadaVO> listaTurmaAgrupadas, Integer periodoLetivo, String ano, String semestre, Integer unidadeEnsino, UsuarioVO usuario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota) throws Exception;

	public MatriculaVO consultarPorObjetoMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;
	
	public MatriculaVO consultarPorObjetoMatricula(String valorConsulta, Integer unidadeEnsino, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public MatriculaVO consultarPorObjetoMatricula(String valorConsulta, Integer unidadeEnsino, Integer parceiro, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultarPorNomePessoa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarPorInscricao(Integer inscricao, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultarPorNomeCurso(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorSituacaoFinanceira(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultarMatriculaPorCodigoPessoa(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultarPorColacaoGrau(Integer Colacao, String ColouGrau, Integer curso, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public void liberarMatricula(MatriculaVO obj, UsuarioVO usuarioResponsavel) throws Exception;

	public void emitirBoletoMatricula(MatriculaVO obj, UsuarioVO usuarioResponsavel) throws Exception;

	public void setIdEntidade(String aIdEntidade);

//	public void inicializarPlanoFinanceiroMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

//	public PlanoFinanceiroCursoVO obterPlanoFinanceiroCursoMatriculaVO(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception;

	public List consultarPorRequerimentoTipoRequerimento(Integer codigoRequerimento, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public void inicializarDadosRenovacaoMatricula(MatriculaVO matriculaVO,  MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuario) throws Exception;

	public void executarDefinarProximoPeriodoLetivoCursar(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuario) throws Exception;

	public void inicializarDocumentacaoMatriculaCurso(MatriculaVO matricula, UsuarioVO usuario) throws Exception;

	public void incializarDadosAPartirTransferenciaEntrada(MatriculaVO matricula, TransferenciaEntradaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

//	public void excluirMatriculaDePendenciaFinanceira(MatriculaVO matricula, UsuarioVO usuario) throws Exception;

	public void verificaAlunoJaMatriculado(MatriculaVO obj, boolean permiteInformarTipMatricula,  UsuarioVO usuario , boolean validarAlunoMatriculadoWebServiceMatriculaCrm, boolean validarUnidadeEnsino) throws Exception;

	public void alterarSituacaoMatriculaVOParaAtivada(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

	public List consultarPorTurnoTipoRequerimentoSemProgramacaoFormatura(Integer turno, String tipoRequerimento, Integer unidadeEnsino, boolean semRequerimento, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorCursoTipoRequerimentoSemProgramacaoFormatura(Integer curso, String tipoRequerimento, Integer unidadeEnsino, boolean semRequerimento, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultaRapidaPorCursoTipoRequerimentoSemProgramacaoFormatura(Integer curso, String tipoRequerimento, Integer unidadeEnsino, String nivelEducacional, boolean semRequerimento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorMatriculaTipoRequerimentoSemProgramacaoFormatura(String matricula, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorNomePessoaTipoRequerimentoSemProgramacaoFormatura(String nomePessoa, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorNomeCursoTipoRequerimentoSemProgramacaoFormatura(String nomeCurso, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public void validarConsultarRenovacaoMatriculaVisaoAluno(UsuarioVO usuario) throws Exception;

	public void incluir(MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendario,   ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean transferencia, UsuarioVO usuario) throws Exception;

	public void alterar(final MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendario,   ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;

	public void alterar(final MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendario,   ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario, boolean controleAcesso) throws Exception;

	public void atualizarListaDocumentosEntregarMatricula(MatriculaVO matricula, UsuarioVO usuario) throws Exception;

	public void validarConsultarMeusHorarios(UsuarioVO usuario) throws Exception;

	public void validarConsultarDocumentoEntregueAluno(UsuarioVO usuario) throws Exception;

	public void alterarSituacaoMatricula(String matricula, String string, UsuarioVO usuarioVO) throws Exception;

	public void alterarSituacaoMatriculaParaIndicarAbandonoCurso(String matricula, Boolean situacaoAbandono) throws Exception;

	public List consultarPorCursoTurmaAnoSemestre(Integer curso, Integer turma, String ano, String semestre, String tipoMatricula, boolean controlarAcesso, int nivelMontarDados,  String nivelEducacional, UsuarioVO usuario) throws Exception;

	public void alterarEnade(List<MatriculaVO> lista) throws Exception;

	public boolean isMatriculaIntegralizada(MatriculaVO matricula, Integer gradeCurricular, UsuarioVO usuario, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO) throws Exception;

	public MatriculaVO verificarControleDisciplinaReprovacao(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception;

	public void verificarMatriculaAtivaPorCodigoPessoa(Integer codPessoa,  UsuarioVO usuario) throws Exception;

	public MatriculaVO getUtimaMatricula(List<MatriculaVO> matriculaVOs);

	public boolean executarValidarMatriculaAptaAvancarPeriodoLetivo(MatriculaVO matricula, Integer ultimaMatriculaPeriodo, PeriodoLetivoVO periodoLetivoEstaAvancado, UsuarioVO usuario) throws Exception;

	public void alterarSituacaoFinanceiraMatricula(String matricula, String string) throws Exception;

//	public MapaLancamentoFuturoVO popularMapaLancamentoFuturo(MatriculaVO matriculaVO, ProcessoMatriculaCalendarioVO processoCalendarioMatriculaVO) throws Exception;

	public String getNumeroMatriculaPorPessoa(PessoaVO pessoa,  UsuarioVO usuario) throws Exception;

	public ObservacaoComplementarHistoricoAlunoVO consultarObservacaoComplementarMatricula(MatriculaVO obj, Integer gradecurricular);

	public void alterarObservacaoComplementar(final MatriculaVO obj, final Integer gradecurricular, final String observacaoComplementar ,UsuarioVO usuarioLogado) throws Exception;

	public void alterarDadosAlteracoesCadastraisMatricula(final MatriculaVO matricula, UsuarioVO usuarioVO) throws Exception;

	public List<MatriculaVO> consultaRapidaPorCodigoPessoa(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultaRapidaPorCodigoPessoaNaoCancelada(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultaRapidaCompletaPorCodigoPessoa(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultaRapidaCompletaPorCodigoPessoaNaoCancelada(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultaRapidaPorCodigoPessoaCurso(Integer pessoa, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void gerenciarEntregaDocumentoMatricula(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception;

	public void validarMatriculaPodeSerRenovada(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;

	public MatriculaVO consultaRapidaFichaAlunoPorMatricula(MatriculaVO obj, String matricula, UsuarioVO usuario) throws Exception;

	public List consultarPorMatriculaNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorNomePessoaNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacinoal, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorNomeCursoNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List executarConsultaPorTurmaCurso(Integer turma, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public Object consultarPorMatriculaCodigoCurso(String valorConsultaAluno, Integer codigo, int nivelmontardadosDadosbasicos,  UsuarioVO usuario) throws Exception;

	public List consultarPorNomePessoaCodigoCurso(String valorConsultaAluno, Integer codigo, int nivelmontardadosDadosbasicos,  UsuarioVO usuario) throws Exception;

	public List consultarPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public void executarAlteracaoGradeCurricular(List listaMatriculaPeriodoAlunos, List listaDisciplinaEquivalente, GradeCurricularVO gradeOrigem, GradeCurricularVO gradeMigrar,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomeCurso(String nomePessoa, Integer unidadeEnsino, Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarPorChavePrimaria(String matriculaPrm, Integer unidadeEnsino, Integer curso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, Integer unidadeEnsino, Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void processarRotinaSetarTurmaMatricula(UsuarioVO usuario) throws Exception;

	public boolean executarVerificacaoPermissaoAlterarOrdemDesconto();

//	public void regerarBoletos(MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorTurma(Integer turma, String parcela, String ano, String semestre, Optional<Date> dataInicio, Optional<Date> dataFim, UsuarioVO usuarioVO, String tipoAluno,String situacaoContratoDigital) throws Exception;

	public List executarConsultaPorTurmaCursoGradeCurricularAnoSemestre(Integer turma, Integer curso, String ano, String semestre, Integer gradeCurriculcar, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorTurmaCursoGradeCurricularAnoSemestre(Integer turma, Integer curso, String ano, String semestre, Integer gradeCurricular, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestre(List<TurmaAgrupadaVO> listaTurmaAgrupadas, String ano, String semestre, Integer unidadeEnsino,  UsuarioVO usuario) throws Exception;

	public MatriculaVO consultaRapidaPorMatriculaUnica(String matricula, Integer codigo, boolean b, UsuarioVO usuario) throws Exception;

	public MatriculaVO consultaRapidaPorMatriculaUnica(String matricula, Integer unidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception;

	public List consultarPorCursoTurmaAnoSemestreSituacao(Integer codigo, Integer codigo2, String ano, String semestre, String valor, String tipoMatricula, boolean b, int nivelmontardadosDadosminimos,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestrePeriodoLetivo(List<TurmaAgrupadaVO> listaTurmaAgrupadas, Integer periodoLetivo, String ano, String semestre, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestrePeriodoLetivoGradeCurricular(Integer turma, Integer curso, String ano, String semestre, Integer gradeCurricular, Integer periodoLetivo, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	public void calcularTotalDesconto(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List ordemDesconto, UsuarioVO usuario ) throws Exception;

//	public void inicializarTextoContratoPlanoFinanceiroAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, boolean forcaTrocarContrato, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorUnicaMatricula(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorTurmaCursoAnoSemestre(Integer turma, CursoVO curso, String ano, String semestre, Integer unidadeEnsino, String situacaoMatricula) throws Exception;

	public void atualizarTurnoMatricula(String matricula, Integer codigoTurno) throws Exception;

	public MatriculaVO consultarPorChavePrimariaSituacaoDadosCompletos(String matricula, Integer codUnidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarPorObjetoMatriculaDigitadasCompleta(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public Boolean verificarAlunoEstudouSomenteNoPrimeiroPeriodo(String matricula) throws Exception;

	public MatriculaVO consultarPorObjetoMatriculaParaCancelamentoQuandoSituacaoMatriculaPeriodoAtiva(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorDescontoMatriculaUnidadeEnsino(String tipoDesconto, Integer codigoPlanoDesconto, Integer codigoDescontoConvenio, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public MatriculaVO consultaRapidaPorMatriculaUnicaParaExpedicaoDiploma(String matricula, Integer unidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarPorObjetoMatriculaSituacaoMatricula(String matricula, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorNomePessoaSituacaoMatricula(String valorConsulta, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultarPorNomeCursoSituacaoMatricula(String valorConsulta, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public void alterarSituacaoMatriculaEMatriculaPeriodoTransferenciaInterna(MatriculaVO matricula, OrigemFechamentoMatriculaPeriodoEnum origemFechamentoMatriculaPeriodoEnum, Integer codigoOrigemFechamentoMatriculaPeriodo, Date dataFechamentoMatriculaPeriodo,  UsuarioVO usuario) throws Exception;

	void verificarAlunoPosGraduacaoFormadoExpedicaoDiploma(MatriculaVO obj) throws Exception;

	public MatriculaVO consultarPorAutorizacaoCursoMatricula(Integer autorizacaoCurso, String matricula, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaAutorizacaoCursoMatricula(Integer autorizacaoCurso,  UsuarioVO usuario) throws Exception;
	
	public void excluirMatriculaERegistrosRelacionados(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, String motivoExclusao,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * Para consultar somente por nomePessoa passar no atributo filtro
	 * NOMEPESSOA, filtrar somente por matrícula passar no atributo filtro
	 * MATRICULA, para usar os dois passar no atributo filtro
	 * NOMEPESSOAMATRICULA ou para não usar nenhum filtro, passar em branco.
	 */
	public List<MatriculaVO> consultaRapidaPorNomePessoaMatriculaCursoTurmaTurno(String matricula, String nomePessoa, String filtro, Integer curso, Integer turma, Integer turno, Integer unidadeEnsino,  UsuarioVO usuario) throws Exception;

	public void incluirLogExclusaoMatricula(final MatriculaVO obj, final String motivo,  final UsuarioVO usuario) throws Exception;
	

	public void alterarConsultorResponsavelMatricula(String matricula, Integer consultorSubstituido, FuncionarioVO consultorSubstituto, UsuarioVO usuario) throws Exception;

	public void validarDadosAlteracaoConsultorMatricula(String matricula) throws Exception;

	public List<MatriculaVO> consultaRapidaPorTurma(Integer turma, String ano, String semestre, Integer unidadeEnsino, Boolean curriculaIntegralizacao, String situacaoRegistroFormatura, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaVO> consultaRapidaPorCurso(Integer curso, String ano, String semestre, Integer unidadeEnsino, Boolean curriculaIntegralizacao, String situacaoRegistroFormatura, UsuarioVO usuario, String tipoAluno ,String situacaoContratoDigital) throws Exception;

	public Boolean consultarPorAutorizacaoCurso(Integer autorizacaoCurso);

	public void incluir(final MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendario,    ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean transferencia, boolean controleAcesso,boolean validarAlunoMatriculadoWebServiceMatriculaCrm,Boolean criarHistoricoTransferenciaEntrada,  UsuarioVO usuario) throws Exception;

	public List consultarPorCursoTurmaAnoSemestreSituacaoReposicaoInclusao(Integer curso, Integer turma, String ano, String semestre, String situacao, String tipoMatricula, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarPorMatriculaReceitaDW(String matriculaPrm, Integer unidadeEnsino, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List consultaRapidaCompletaPorCodigoPessoaNaoCancelada(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarPorChavePrimariaCursoTurma(String matriculaPrm, Integer unidadeEnsino, Integer curso, Integer turma, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomePessoaCursoTurma(String nomePessoa, Integer unidadeEnsino, Integer curso, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatriculaCursoTurma(String matricula, Integer unidadeEnsino, Integer curso, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaBasicaPorCodigoPessoaNaoCancelada(Integer pessoa, Boolean visaoPais, boolean validarBloqueioMatricula, boolean validarSuspensaoMatricula, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception;

	public MatriculaVO consultaRapidaPorMatriculaSituacoes(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorPessoaESituacoes(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorCursoESituacoes(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorSituacao(String situacao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorSituacaoFinanceira(String situacaoFinanceira, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomeResponsavel(String nomeResponsavel, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void alterarMatriculaSuspensaoDocumentacaoMatricula(final MatriculaVO obj) throws Exception;

	public void alterarMatriculaSuspensao(final MatriculaVO obj) throws Exception;

//	public void alterarMatriculaSerasa(final MatriculaVO obj, final UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatriculaUnicaParaHistoricoAluno(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<MatriculaVO> consultaRapidaBasicaPorCodigoResponsavelFinanceiro(Integer codigoResponsavelFinanceiro, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultarMatriculaPorCodigoPessoaParaComboBox(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void excluirPreMatriculaERegistrosRelacionados(MatriculaPeriodoVO matriculaPeriodoVO, String motivoExclusao,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioLogado) throws Exception;

	public Boolean consultarMatriculaAtivaPorRegistroAula(Integer registroAula, UsuarioVO usuarioVO);

	void alterarNaoEnviarMensagemCobranca(String matricula, Boolean naoEnviarMensagemCobranca) throws Exception;

//	public List<LogMatriculaVO> consultaLogMatriculaPorMatricula(String matricula, UsuarioVO usuario) throws Exception;

	public void alterarDataLiberacaoPendenciaFinanceira(String matricula, Date dataLiberacao, Integer usuarioResp, UsuarioVO usuario) throws Exception;

	public void alterarAlunoConcluiuDisciplinasRegulares(String matricula, Boolean alunoConcluiuDisciplinasRegulares) throws Exception;

	void realizarAtualizarAutomaticaAlunoConcluiuDisciplinasRegulares();

	void atualizarDataEnvioNotificacaoPendenciaDocumento(Set<String> matriculas) throws Exception;

	public void registrarAdiarBloqueio(MatriculaVO matricula) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatriculaUnidadeEnsinoBiblioteca(String matricula, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public MatriculaVO consultaRapidaPorMatriculaUnicaUnidadeEnsinoBiblioteca(String matricula, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception;

	List<MatriculaVO> consultarMatriculaProspectApresentarCompromisso(Integer prospect, Date dataCompromisso, UsuarioVO usuarioVO) throws Exception;

	void incluirLogMatricula(MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodo, UsuarioVO usuario) throws Exception;

	Map<String, List<? extends SuperVO>> consultarDadosAlteracaoCadastral(String matricula, Integer unidadeEnsino, Integer curso, Integer turma, String ano, String semestre, ColacaoGrauVO colacaoGrauVO, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, UsuarioVO usuarioVO) throws Exception;

	void alterarInformacoesCadastrais(List<MatriculaVO> matriculaVOs, List<MatriculaControleLivroRegistroDiplomaVO> matriculaControleLivroRegistroDiplomaVOs, UsuarioVO usuarioVO) throws Exception;

	public void alterarLiberarBloqueioAlunoInadimplente(final MatriculaVO matricula);

//	public List<MatriculaVO> consultaRapidaPorTurmaCursoGradeCurricularAnoSemestreTipoAluno( TurmaVO turma, Integer curso, String ano, String semestre, Integer gradeCurricular, Integer unidadeEnsino, String tipoAluno, UsuarioVO usuario) throws Exception;

//	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestreTipoAlunoEntregaDocumento( List<TurmaAgrupadaVO> listaTurmaAgrupadas, String ano, String semestre, Integer unidadeEnsino,  String tipoAluno, UsuarioVO usuario) throws Exception;

	public void incluirLogPreMatricula(final MatriculaVO obj, final MatriculaPeriodoVO matriculaPeriodo, final String acao, final UsuarioVO usuario) throws Exception;

	public void alterarDataNotificacaoAlunoInadimplente(String matricula) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatriculaSemProgramacaoFormatura(String matricula, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, List<TurnoVO> turnoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomeCursoSemProgramacaoFormatura(String nomeCurso, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, List<TurnoVO> turnoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomePessoaSemProgramacaoFormatura(String nomePessoa, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, List<TurnoVO> turnoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorTurmaTipoRequerimentoSemProgramacaoFormatura(Integer turma, String tipoRequerimento, Integer unidadeEnsino, String nivelEducacional,boolean semRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorTurnoTipoRequerimentoSemProgramacaoFormatura(Integer turno, String tipoRequerimento, Integer unidadeEnsino, String nivelEducacional, boolean semRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatriculaTipoRequerimentoSemProgramacaoFormatura(String matricula, String tipoRequerimento, Integer unidadeEnsino, String nivelEducacional,boolean semRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorRequerimentoTipoRequerimentoSemProgramacaoFormatura(Integer codigoRequerimento, String tipoRequerimento, Integer unidadeEnsino, String nivelEducacional, boolean semRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	public void alterarDataPrimeiraNotificacaoAlunoInadimplente(String matricula) throws Exception;

	public void alterarDataSegundaNotificacaoAlunoInadimplente(String matricula) throws Exception;

	public void alterarDataTerceiraNotificacaoAlunoInadimplente(String matricula) throws Exception;

	public void incluirLogMatriculaSerasa(final MatriculaVO obj, final String acao, final UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatriculaPorCoordenador(String matricula, Integer codigoCoordenador, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomePessoaPorCoordenador(String nomePessoa, Integer codigoCoordenador, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomeCursoPorCoordenador(String nomePessoa, Integer codigoCoordenador, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarPorChavePrimariaPorCoordenador(String matriculaPrm, Integer codigoCoordenador, Integer unidadeEnsino, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	void realizarVerificacaoELiberacaoSuspensaoMatricula(String matricula) throws Exception;

	public List<MatriculaVO> consultaRapidaPorUnidadeEnsinoCursoESituacaoMatricula(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, String situacaoMatricula, Integer ano, UsuarioVO usuarioVO) throws Exception; 

	Boolean validarMatriculaAlunoPosGraduacao(Integer codigoPessoa, Integer codigoUnidadeEnsino) throws Exception;

	public Integer consultaQuantidadeAlunoPorUnidadeEnsinoCursoESituacaoMatricula(Integer unidadeEnsino, Integer curso, String situacaoMatricula, UsuarioVO usuarioVO);

	void alterarDataQuartaNotificacaoAlunoInadimplente(String matricula) throws Exception;

	Boolean consultarAlunoPendenciaEnadePorMatricula(Integer pessoa, String matricula) throws Exception;
	
    public void verificarDocumentaoImpediRenovacaoEstaPendente(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception;
        
	public List consultarAlunosPossuemParceiroDadosComboBox(List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, Integer codigoParceiro, UsuarioVO usuario) throws Exception;

	MatriculaVO consultaRapidaPorCodigoPessoaUnicaMatricula(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public MatriculaVO consultarPorObjetoMatriculaListaUnidadeEnsinoVOs(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;
	
	public List<MatriculaVO> consultaRapidaPorNomePessoaListaUnidadeEnsino(String nomePessoa, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaVO> consultaRapidaPorNomeCursoListaUnidadeEnsinoVOs(String nomePessoa, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception;

	Boolean consultarAlunoSerasa(Integer codigoPessoa) throws Exception;
	
	String consultarUltimaMatriculaAtivaPorContaReceber(Integer contaReceber) throws Exception;
	
	String consultarUltimaMatriculaAtivaPorCodigoPessoa(Integer codigoPessoa) throws Exception;
	
	MatriculaVO consultarMatriculaMaiorNivelEducacionalMaiorDataPorCodigoPessoa(Integer pessoa, UsuarioVO usuario) throws Exception;

	Date consultarDataInicioTurmaAgrupadaPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<MatriculaVO> consultarMatriculaPorCodigoProspect(Integer codigoProspect, Integer unidadeEnsinoLogada, UsuarioVO usuarioLogado) throws Exception;

	void alterarObservacaoDiplomaMatricula(String matricula, String observacao, UsuarioVO usuario) throws Exception;

	void alterarDataConclusaoCurso(String matricula, Date data, UsuarioVO usuario) throws Exception;
	
	void executarAlteracaoNumeroMatriculaManualmente(MatriculaVO matricula , String novaMatricula, OperacaoFuncionalidadeVO operacao, UsuarioVO usuario) throws Exception;

	void alterarGradeCurricularAtual(String matricula, Integer codigoGradeCurricularAtual) throws Exception;
	
	public void alterarTituloNotaMonogradia(String matricula, String titulo, Double nota, UsuarioVO usuarioVO) throws Exception;
			
	public MatriculaVO consultarPorChavePrimariaSituacaoDadosCompletosDocumentacaoPendente(String matricula, Integer codUnidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception;

	MatriculaVO consultaRapidaPorMatriculaAnoSemestrePeriodoLetivoGradeCurricularBoletim(String matricula, String ano, String semestre, Integer gradeCurricular, Integer periodoLetivo, Integer unidadeEnsino, UsuarioVO usuario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota) throws Exception;
	
	void alterarTransferenciaEntrada(String matricula, Integer transferenciaEntrada, UsuarioVO usuario) throws Exception;

	public void alterarLocalArmazenamentoDocumentosMatricula(String localArmazenamentoDocumentoMatricula, String matricula, UsuarioVO usuarioVO) throws Exception;

	void inicializarMatriculaComHistoricoAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, Boolean forcarNovoCarregamentoDados, UsuarioVO usuarioVO) throws Exception;

	MatriculaPeriodoVO realizarGeracaoNovaMatriculaPeriodoParaRenovacao(MatriculaVO matriculaVO,  UsuarioVO usuarioVO) throws Exception;

	

	void executarDefinicaoPeriodoLetivoNovaMatriculaAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, Integer matrizCurricular, Boolean realizandoNovaMatricula, UsuarioVO usuarioVO) throws Exception;

	void realizarDefinicaoDocumentacaoMatriculaAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean simulandoRenovacao, UsuarioVO usuarioVO) throws Exception;

	void realizarDefinicaoDisciplinaMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean renovandoMatricula, boolean liberadoInclusaoTurmaOutroUnidadeEnsino, boolean liberadoInclusaoTurmaOutroCurso, boolean liberadoInclusaoTurmaOutroMatrizCurricular, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception;

//	void realizarCalculoValoresMatriculaMensalidadePeriodoAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuarioVO) throws Exception;

//	void realizarDefinicaoDadosFinanceiroMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  Boolean permitirRealizarMatriculaDisciplinaPreRequisito, UsuarioVO usuarioVO) throws Exception;

	void persistir(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO) throws Exception;

	public void alterarDataColacaoGrauPorMatricula(final MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception;

	public void alterarDataInicioEDataConclusaoCursoPorMatricula(final MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 04/05/2015
	 * @param matricula
	 * @param canceladoFinanceiro
	 * @throws Exception
	 */
	void alterarMatriculaCanceladoFinanceiro(String matricula, boolean canceladoFinanceiro) throws Exception;
	
	MatriculaVO consultarPorCodigoFinanceiroMatricula(String codigoFinanceiroMatricula, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 21/05/2015
	 * @param pessoaAntigo
	 * @param pessoaNova
	 * @throws Exception
	 */
	void alterarPessoaMatriculaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception;
	
//	public List<MatriculaVO> consultarPorMatriculaConfiguracaoGabaritoProvaPresencial( String situacao, UsuarioVO usuarioVO);
	
	public Boolean consultarExistenciaMatriculaPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception;
	
	public Boolean consultarExistenciaMatriculaPorCodigoPessoaPorUnidadeEnsinoDiferenteMatricula(Integer pessoa, Integer unidadeEnsino, String matricula, UsuarioVO usuarioVO) throws Exception;
	
	public MatriculaVO consultaRapidaPorMatriculaUnicaRemovendoCaracterEspecialMatricula(String matricula, String caracterEspecial, Integer unidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception;

	List<MatriculaVO> consultarMatriculasAlunosFormadosParaCorrecaoDataConclusaoCurso() throws Exception;
	
	void executarCorrecaoDataConclusaoCursoUniRV(UsuarioVO usuario) throws Exception;

	void adicionarObjMatriculaPeriodoVOs(MatriculaPeriodoVO obj, MatriculaVO matriculaVO) throws Exception;
	
	List<MatriculaVO> consultaRapidaPorNomePessoaCursoTurma(String nomePessoa, List<UnidadeEnsinoVO> unidades, List<CursoVO> cursos, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, List<UnidadeEnsinoVO> unidades, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, List<UnidadeEnsinoVO> unidades, List<CursoVO> cursos, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	List<MatriculaVO> consultaRapidaPorAlteracaoDataBaseTurma(Integer turma, Integer unidadeEnsino, Boolean curriculaIntegralizacao, Boolean utilizarControleGeracaoTurma, Boolean validarDataBaseGeracaoParcelaMatricula, UsuarioVO usuario) throws Exception;
	
	void alterarSituacaoMatriculaEstornoCancelamento(final String matricula, final UsuarioVO usuarioVO) throws Exception;
	
	void alterarSituacaoMatriculaEstornoTrancamento(final String matricula, final UsuarioVO usuarioVO) throws Exception;
	
	/** 
	 * @author Wellington - 3 de mar de 2016 
	 * @param turmaVO
	 * @throws Exception 
	 */
	void alterarGradeCurricularAtualPorTurma(TurmaVO turmaVO, String situacaoMatricula,  String situacaoMatriculaPeriodo, List<MatriculaPeriodoVO> listaMatriculaPeriodoVO, UsuarioVO usuarioVO, Boolean realizandoTransferenciaMatrizCurricularPelaTurma) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatriculaTurmaAnoSemestreDataVencimentoContaReceber(String tipoConsulta, Integer codUnidadeEnsino, String matricula, TurmaVO turma, String ano, String semestre, Date dataVencimentoInicio, 
			Date dataVencimentoFinal, DiaSemana diaSemana, Boolean consultaContaVencida, String parcela,  UsuarioVO usuarioVO) throws Exception;        
	
	public Boolean consultarExistenciaCodigoFinanceiroMatricula(String codigoFinanceiroMatricula,String matricula) throws Exception;
	
	/**
	 * @author Jean Pierre - 27/06/2016
	 * @param matricula
	 * @param usuarioVO
	 * @throws Exception
	 */
	void alterarAtivarMatriculaAlunoFormado(final MatriculaVO matriculaVO, final UsuarioVO usuarioVO) throws Exception ;
	
	public MatriculaVO consultaRapidaBasicaPorMatriculaNaoCancelada(String matricula, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 26/10/2016
	 * @param obj
	 * @param usuario
	 * @throws Exception
	 */
	void incluirObservacaoComplementarHistoricoAluno(ObservacaoComplementarHistoricoAlunoVO obj, UsuarioVO usuario) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 27/10/2016
	 * @param matricula
	 * @param gradeCurricular
	 * @param usuario
	 * @throws Exception
	 */
	void excluirObservacaoComplementarHistoricoAluno(String matricula, Integer gradeCurricular, UsuarioVO usuario) throws Exception;	
	
	void alterarFormaIngressoAluno(final String matricula, final String formaIngresso) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
	 * @param pessoa
	 * @param controlarAcesso
	 * @param usuario
	 * @param conf
	 * @return
	 * @throws Exception 
	 */
	List<MatriculaVO> consultaRapidaBasicaPorCodigoPessoaAtivasMatriculasFilhoFuncionario(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception;
	
	List<MatriculaVO> consultarPorCodigoAlunoDadosFichaAluno(Integer aluno, String filtroMatricula ,ConfiguracaoGeralSistemaVO configuracaoGeralVO, UsuarioVO usuarioVO) throws Exception;

//	List<MatriculaVO> inicializarDadosContaReceberAbaFinanceira(Integer aluno, List<MatriculaVO> listaMatriculaVOs, TipoOrigemContaReceber tipoOrigemContaReceber, SituacaoContaReceber situacaoContaReceber, String mesAno, UsuarioVO usuarioVO) throws Exception;

//	List<SelectItem> inicializarDadosListaSelectItemMesAnoBaseadoContaReceber(Integer aluno, UsuarioVO usuarioVO);

	List<MatriculaVO> inicializarDadosRequerimentoFichaAluno(Integer aluno, List<MatriculaVO> listaMatriculaVOs, Integer tipoRequerimento, String situacao, String mesAno, UsuarioVO usuarioVO) throws Exception;

//	List<SelectItem> inicializarDadosListaSelectItemMesAnoBaseadoRequerimento(Integer aluno, UsuarioVO usuarioVO);
	
	Boolean consultarAlunoInadimplentePossuiBloqueioMatricula(String matricula, UsuarioVO usuarioVO);

	Boolean consultarAlunoPossuiDocumentacaoPendente(String matricula, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, ConfiguracaoGeralSistemaVO confEmail) throws Exception;

	void validarDadosPendenciaLiberacaoMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarBloqueioMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception;

	void realizarCancelamentoBloqueioMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception;

	void realizarAdiamentoBloqueioMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception;

	void realizarLiberacaoBloqueioAlunoInadimplente(MatriculaVO matriculaVO, UsuarioVO usuarioVO);
	
	List<MatriculaVO> inicializarDadosBibliotecaFichaAluno(Integer aluno, List<MatriculaVO> listaMatriculaVOs, String situacao, String mesAno, UsuarioVO usuarioVO) throws Exception;

	List<SelectItem> inicializarDadosListaSelectItemMesAnoBaseadoItemEmprestimo(Integer aluno, UsuarioVO usuarioVO);
//
//	List<InteracaoWorkflowHistoricoVO> inicializarDadosCRMFichaAluno(Integer aluno, Integer responsavel, String mesAno, UsuarioVO usuarioVO) throws Exception;
//
//	List<SelectItem> inicializarDadosListaSelectItemMesAnoBaseadoInteracaoWorkflow(Integer aluno, UsuarioVO usuarioVO);
//

	
	void alterarDataEmissaoHistorico(final String matricula, final Date data, UsuarioVO usuario, Boolean controlarAcesso) throws Exception;
	
	public MatriculaVO consultarDataAdiamentoSuspensaoMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception;
	
	public List<MatriculaVO> obterStatusMatricula(Integer unidadeEnsino) throws Exception;
	
	MatriculaVO verificarBloqueioMatricula(MatriculaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception;
	
//	public void alterarPlanoFinanceiroAlunoConformeDadosTurma(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  String categoria,    UsuarioVO usuarioVO) throws Exception;

	void cancelarIndiceReajustePreco(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception;
	
	public MatriculaVO consultarPorChavePrimaria(String matricula) throws Exception;
	
	public SqlRowSet consultarPessoaLoginToken(String token);
	
	public String consultarTokenPessoaPorMatricula(String matricula);
	
	public String consultarTokenPessoaPorCodigo(Integer codPessoa);

	List<MatriculaVO> consultaRapidaPorCodigoPessoaESituacao(Integer pessoa, String situacoesSqlIn, String matriculaIgnorar, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	String consultarGruposLdapPorPessoa(Integer pessoa) throws Exception;

//	void alterarDadosIngressoAlteracoesCadastrais(List<MatriculaVO> listaMatriculaVOs, MatriculaVO matricula, UsuarioVO usuarioVO);

	List<String> consultarMatriculasPorCodigoPessoa(Integer pessoa) throws Exception;
	
	
	public String consultarPossuiFormacaoAcademicaVinculadaMatricula(Integer codigoFormacaoAcademica, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void removerVinculoAutorizacaoCursoMatricula(Integer autorizacaoCurso, UsuarioVO usuarioVO, boolean controlarAcesso) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomePessoa(DataModelo dataModeloAluno, boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception;

	public Integer consultaRapidaTotalPorNomePessoa(DataModelo dataModeloAluno, boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception;

	public void alterarDadosMatriculaDeferimentoIndeferimento(MatriculaVO matriculaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<MatriculaVO> consultaRapidaPorDataModeloUnidadeEnsinoBiblioteca(DataModelo dataModelo,
			List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, String situacao, UsuarioVO usuario)
			throws Exception;

	Integer consultaTotalRapidaPorDataModeloUnidadeEnsinoBiblioteca(DataModelo dataModelo,
			List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, String situacao, UsuarioVO usuario)
			throws Exception;

	MatriculaVO consultaRapidaPorCPFUnidadeEnsinoBiblioteca(String matricula,
			List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso, String situacao,
			UsuarioVO usuario) throws Exception;

	List<MatriculaVO> consultarAlunoParticipaQuestaoOnline(Integer questao, UsuarioVO usuarioVO);
	
public List<MatriculaVO> consultaRapidaPorTurmaIntegralUnidadeEnsino(TurmaVO turmaVO, boolean controlarAcesso,  UsuarioVO usuario) throws Exception;
	
	public void alterarUnidadeEnsinoTurmaBase (TurmaVO turmaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	

	void consultarMatriculas(DataModelo dataModelo, UsuarioVO usuario) throws Exception;
	
	Integer consultarQuantidadeMatriculaPorSituacaoMatrizCurricular(String situacao, Integer gradeCurricularAtual, UsuarioVO usuarioVO);

	List<MatriculaVO> consultarMatriculaPorSituacaoMatrizCurricular(String situacao, Integer gradeCurricularAtual, UsuarioVO usuarioVO);
	
	void realizarAlteracaoSituacaoManual(MatriculaPeriodoVO matriculaPeriodoVO, Boolean alterarSituacaoManualMatricula, Boolean alterarSituacaoManualMatriculaPeriodo, SituacaoVinculoMatricula situacaoMatriculaAlterar, SituacaoMatriculaPeriodoEnum situacaoMatriculaPeriodoAlterar, UsuarioVO usuarioVO) throws Exception;
	
	public List<MatriculaVO> consultaRapidaPorNomePessoaCursoTurmaUnidadeEnsinoFinanceira(String nomePessoa, Integer unidadeEnsinoFinanceira, Integer curso, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaVO> consultaRapidaPorMatriculaCursoTurmaUnidadeEnsinoFinanceira(String matricula, Integer unidadeEnsinoFinanceira, Integer curso, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public Boolean consultarEntregaTccAluno(Integer pessoa, String matricula) throws Exception;

    List<MatriculaVO> consultarPorUnidadeEnsinoCursoTurmaDisciplina(String campoConsulta, String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, Integer turma, Integer disciplina, UsuarioVO usuarioVO) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatriculaAutoComplete(String valorConsulta, Integer codigoUnidadeEnsino, int limit, boolean contrarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaVO> consultaRapidaPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	


	public List consultarPorRegistroAcademicoMatriculaAutoComplete(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public List<MatriculaVO> consultaRapidaPorRegistroAcademico(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	public List<MatriculaVO> consultaRapidaPorRegistroAcademico(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, String situacao, String nivelEducacional, UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarMatriculaPorRegistroAcademico(String registroAcademico, Integer unidadeEnsino, Integer curso,int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorRegistroAcademicoSemProgramacaoFormatura(String matricula, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, List<TurnoVO> turnoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorRegistroAcademicoAlunoPorDataModelo(String valorConsulta ,  DataModelo dataModeloAluno, 	boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatriculaAlunoPorDataModelo(String valorConsulta, DataModelo dataModeloAluno, boolean controlarAcesso,	String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception;

	List<MatriculaVO> consultarPorRegistroAcademicoPessoaAtivaOuTrancada(String valorConsulta, Integer unidadeEnsino,boolean controlarAcesso, int nivelMontarDados, 
			UsuarioVO usuario) throws Exception;

	List<MatriculaVO> consultarPorRegistroAcademicoNivelEducacional(String valorConsulta, Integer unidadeEnsino,String nivelEducacinoal, boolean controlarAcesso, int nivelMontarDados,	 UsuarioVO usuario) throws Exception;
	public void realizarOperacaoInclusaoDadosBancarioAlunoMatriculaExterna(PessoaVO aluno, String bancoAluno,	String agenciaAluno, String contaCorrenteAluno) throws Exception;

	public void executarCriacaoRegistroAcademicoAluno(MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO , UsuarioVO usuario,ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
	public void executarProcessamentoRegistroLdapValidandoRegraParaVeterano(MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO , UsuarioVO usuario,ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public Boolean verificarExisteMultiplasMatriculasAtivasDominioLDAP(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception;

	void realizarAlteracaoSituacaoMatriculaIntegralizada(UsuarioVO usuarioVO) throws Exception;

	void realizarCriacaoMatriculaPorTransferenciaInternaOrigemRequerimento(TransferenciaEntradaVO transferenciaEntradaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean apresentarVisaoAluno, ProcessoMatriculaVO processoMatriculaVO, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaVO> consultaCompletaSemProgramacaoFormatura(Integer programacaoFormaturaAtual, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, List<TurnoVO> turnoVOs, List<CursoVO> cursoVOs, Integer turma, String matricula, Integer codigoRequerimento, String nivelEducacional, Boolean enade, Boolean TCC, boolean semRequerimento, String tipoRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void alterarDataColacaoGrauMatriculaPorColacaoGrau(Integer colacaoGrau, Date dataColacaoGrau, UsuarioVO usuarioVO) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomePessoa(DataModelo dataModeloAluno, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaVO> consultaRapidaPorMatriculaAlunoPorDataModelo(String valorConsulta, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, DataModelo dataModeloAluno, boolean controlarAcesso,	String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorRegistroAcademicoAlunoPorDataModelo(String valorConsulta, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, DataModelo dataModeloAluno, boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarPorChavePrimariaEUnidadesSelecionadas(String matriculaPrm, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, Integer curso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarMatriculaPorRegistroAcademico(String registroAcademico, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, Integer curso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public MatriculaVO consultarPorChavePrimariaEUnidadesEnsinos(String matriculaPrm, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultarPorProgramacaoFormatura(Integer codigoProgramacao, String ColouGrau, Integer curso, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelmontardados,  UsuarioVO usuario) throws Exception;

	public List consultaRapidaResumidaSemProgramacaoFormatura(ControleConsulta controleConsulta, DataModelo controleConsultaOtimizado, List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao, List<TurnoVO> listaTurnos, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Date obterDataColacaoGrauMatricula(String matricula);

	public Boolean verificarBloqueioPorSolicitacaoLiberacaoMatricula(String matricula);

	public List<MatriculaVO> consultarMatriculasAtivaOuTrancadaOutroCursoPermitiOutraMatriculaMesmoAluno(Integer curso, Integer aluno,Integer inscricao ,  Boolean controlarAcesso,  UsuarioVO usuarioVO) throws Exception;

	
	void verificarExistenciaPreencherDadosPertinentesMatriculaOnlineRealizadaPendendeAssinaturaContratoEletronicoEntregaDocumento(
			MatriculaRSVO matriculaRSVO, Boolean somenteGravarSessao, String actionNavegador, UsuarioVO usuarioVO,
			boolean gerarContratoValidandoDocObrigatorio, ConfiguracaoGeralSistemaVO configuracaoGeralSistema,
			 RenovarMatriculaControle renovarMatriculaControle) throws Exception;

	void realizarValidacaoControleDeVagasMatriculaPorEixoCursoUnidadeEnsino(MatriculaVO matriculaVO,UsuarioVO usuarioVO) throws Exception;

	void realizarRenovacaoMatriculaAutomaticaAtravesTransferenciaInterna(TransferenciaEntradaVO transferenciaEntradaVO, PeriodoLetivoVO periodoLetivoCursar , 
			Boolean apresentarVisaoAluno, 
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO)
			throws Exception;

	void realizarDefinicaoGradeDisciplinaHistoricoAproveitarEspecificoTransferenciaInterna(
			TransferenciaEntradaVO transferenciaEntradaVO, UsuarioVO usuario, MatriculaVO novaMatricula,
			MatriculaPeriodoVO novaMatriculaPeriodo, List<HistoricoVO> historicos) throws Exception;
	
	List<HistoricoVO>  realizarCriacaoHistoricoApartirDisciplinasAprovadasMesmaGradeCurricularMatriculaOnline(String matricula ,
			Integer aluno , Integer gradeCurricularAtual  ,TurmaVO turmaVO   , NivelMontarDados nivelMontarDados ,UsuarioVO usuario) throws Exception;

	List<MatriculaVO> consultarMatriculasAlunoDiferenteMatriculaAtualPorCodigoPessoaDadosMinimos(String matricula ,Integer pessoa, boolean controlarAcesso,int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	boolean realizarVerificacaoAlunoPossuiMatriculaAtivaOUPreMatriculaPorInscricao(Integer inscricao, boolean controlarAcesso,UsuarioVO usuario) throws Exception;

	public void realizarMontagemDadosProcSeletivoMatricula(MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

	String consultarCPFalunoPreencherDocumentacaoMatriculaParaMatriculaOnline(String matricula);

	StringBuilder getWhereMatriculaLiberadasVisaoAlunoPais(boolean validarSituacaoMatricula, boolean validarSuspensao,
			boolean validarCurso);

	Boolean verificarPossuiMatriculaAtivaPessoa(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario)
			throws Exception;
		
	
	public void validarExtensaoArquivo(String fileName, DocumetacaoMatriculaVO documetacaoMatriculaVO, Boolean arquivoGED) throws Exception;

//	void inicializarPlanoFinanceiroMatriculaPeriodoSemTurmaInicializada(MatriculaVO matriculaVO,
//			MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

	
	void realizarCriacaoNovaMatriculaProcessoSeletivo(MatriculaRSVO matriculaRSVO , ConfiguracaoGeralSistemaVO configuracaoGeralSistema ,UsuarioVO usuarioVO) throws Exception;
	
	
	MatriculaIntegralizacaoCurricularVO consultarPercentuaisIntegralizacaoCurricularMatricula(String matricula);

	Boolean verificarExisteMultiplasMatriculasAluno(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception;

	Date consultarDataConclusaoCursoPorMatricula(String matricula);

	boolean verificarMatriculaGeradaDaTransferenciaPossuiDisciplinaNaGradeCurricularParaAproveitamento(String matricula,
			Integer disciplina, UsuarioVO usuarioVO) throws Exception;


	MatriculaVO consultarMatriculaTransferidaDestinoPorMatriculaTransferidaOrigem(String matricula,
			int nivelMontarDados,  UsuarioVO usuari) throws Exception;

	
	

	void realizarMontagenDadosDisciplinasMatriculaOnline(MatriculaRSVO matriculaRSVO ,List<MatriculaPeriodoTurmaDisciplinaVO> disciplinasAluno , UsuarioVO usuarioVO) throws Exception;


	void realizarMontagemDadosMatriculaParaRealizacaoMatriculaOnlineProcessoSeletivo( MatriculaRSVO matriculaRSVO,MatriculaVO matriculaVO,MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuarioVO) throws Exception;

	void consultarDadosParaRealizarMatriculaOnline(Integer codigoInscricao, Integer cursoAprovado, String navegador,MatriculaRSVO matriculaRSVO, UsuarioVO usuarioVO) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatriculaPorUnidadeEnsino(String matricula, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomePessoaPorUnidadeEnsino(String nomePessoa, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorNomeCursoPorUnidadeEnsino(String nomePessoa, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaVO> consultaRapidaPorMatriculaAutoComplete(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, int limit, boolean contrarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void alterarDataNotificacaoConfirmacaoMatricula(String matricula, UsuarioVO usuario) throws Exception;

	Boolean validarSeMatriculaEstaAptaEnviarMensagemConfirmacaoMatricula(String matricula, UsuarioVO usuario) throws Exception;

	void realizarEnvioMensagemConfirmacaoMatriculaValidandoRegrasEntregaDocumentacao(MatriculaVO obj, UsuarioVO usuario) throws Exception;

	public void consultarMatriculaParaGeracaoRelatorioLivroRegistro(ControleLivroFolhaReciboVO obj,   UsuarioVO usuario) throws Exception;

	public void atualizarDataEnvioNotificacaoMensagemAtivacaoPreMatricula(List<MatriculaVO> listaAlunos, UsuarioVO usuarioVO)throws Exception;

	public List<MatriculaVO> consultarMatriculasAtivasAptasRealizarNotificacaoMensagemAtivacaoPreMatriculaPorChamadaProcessoSeletivo()throws Exception;

//	void realizarMontagemDadosMatriculaOnlineRealizada(MatriculaRSVO matriculaRSVO, MatriculaVO matriculaVO,
//			MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema,
//			 Boolean gerarContratoValidandoDocObrigatorio, UsuarioVO usuarioVO) throws Exception;

	public boolean verificarSeMatriculaEstaAptaReceberNotificacaoAtivacaoPreMatricula(String matricula,UsuarioVO usuarioVO)throws Exception;
	
	List<MatriculaVO> consultaRapidaPorNomeFacilitador(String nomePessoa, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	MatriculaVO consultarPorObjetoMatriculaFacilitador(String valorConsulta, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public void consultaControleConsultaOtimizadoMatricula(DataModelo dataModelo, Integer unidadeEnsino, boolean controlarAcesso) throws Exception;
	
	public void excluirBolqueioMatricula(MatriculaVO obj, UsuarioVO usuarioVO) throws Exception;
	
	public void incluirBloqueioMatricula(MatriculaVO obj, UsuarioVO usuarioVO) throws Exception;
	
	public MatriculaVO consultarBloqueioMatriculaPorMatricula(MatriculaVO matricula) throws Exception;
	
	public MatriculaVO montarDadosBloqueioMatricula(SqlRowSet tabelaResultado) throws Exception;
	
	public String consultarSituacaoMatriculaPorMatricula(MatriculaVO matricula) throws Exception;
	
	public MatriculaVO consultarMatriculaNrMatriculaCancelada(String matricula, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
}