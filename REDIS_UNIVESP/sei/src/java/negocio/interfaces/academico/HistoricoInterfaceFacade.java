package negocio.interfaces.academico;

import java.util.*;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CalendarioLancamentoNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaAproveitadaAlteradaMatriculaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisciplinasAproveitadasVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoGradeAnteriorAlteradaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.TurmaDisciplinaEstatisticaAlunoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.BlackboardFechamentoNotaOperacaoVO;
import negocio.comuns.blackboard.SalaAulaBlackboardNotaVO;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.secretaria.enumeradores.FormaReplicarNotaOutraDisciplinaEnum;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.HistoricoAlunoDisciplinaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface HistoricoInterfaceFacade {

	public HistoricoVO novo() throws Exception;

	public void incluir(HistoricoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(HistoricoVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(HistoricoVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;

	public void incluirListaHistorico(final List<HistoricoVO> listaHistorico, String idEntidade, final UsuarioVO usuario, final String visao, final Boolean incluirNotaRecuperacao, final TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico) throws Exception;

	public void incluirListaHistorico(List<HistoricoVO> listaHistorico, UsuarioVO usuario, String visao, TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistoricoEnum) throws Exception;

	public List consultarPorMatricula_matriculaPeriodoTodos(String matricula, Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultarHistoricoParaAproveitamentoNaTransferenciaInterna(String matriculaAntiga, Integer cursoNovo, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public HistoricoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List consultarPorMatricula(String valorConsulta, int ordemDisciplinas, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void verificaAlunoReprovadoFalta(HistoricoVO obj, ConfiguracaoAcademicoVO ca, UsuarioVO usuario) throws Exception;

	public HistoricoVO consultarPorMatricula_matriculaPeriodo_Disciplina(String matricula, Integer matriculaPeriodo, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultarPorMatriculaDisciplina(String matricula, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public HistoricoVO consultarPorMatriculaPeriodoDisciplina(Integer matriculaPeriodo, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public HistoricoVO consultarPorMatriculaPeriodoTurmaDisciplina(Integer matriculaPeriodoTurmaDisciplina, boolean filtroVisaoProfessor, boolean controlarAcesso, UsuarioVO usuario) throws Exception ;

	public HistoricoVO consultarPorMatriula_Disciplina_Semestre_Ano(String matricula, Integer disciplina, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultarPorMatriculaPeriodoLetivoBoletim(String matricula, Integer turma, String ano, String semestre, Boolean apresentarDisciplinaComposta, Integer configuracaoAcademico, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, Integer gradeCurricular) throws Exception;

	public List<HistoricoVO> consultaHistoricoDisciplinasForaGradeRapidaPorMatricula(MatriculaVO matriculaVO, int ordemHistoricoDisciplina, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultarPorMatriculaDisciplinaTurmaSituacaoHistorico(String matricula, Integer disciplina, Integer turma, String situacaoHistorico, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<HistoricoVO> consultarPorMatriculaPeriodoLetivo(String matricula, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public Integer consultarCargaHorariaCumpridaNoHistorico(String matricula, Integer gradeCurricular, UsuarioVO usuario) throws Exception;

	public void validarConsultaDoUsuario(UsuarioVO usuario) throws Exception;

	public List consultarDisciplinasDoHistoricoPorMatriculaTipoDisciplinaSituacao(String matricula, String situacao, String tipoDisciplina1, String tipoDisciplina2, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List consultarPorMatriculaPeriodoSituacao(Integer matriculaPeriodo, String situacao, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultarPorMatriculaPeriodo(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	// public void calcularMediaFrequenciaAluno(ConfiguracaoAcademicoVO
	// configuracaoAcademicoVO, HistoricoVO historicoVO, Boolean
	// controlaFrequencia, UsuarioVO usuario) throws Exception;

	public List consultarPorMatriculaDisciplinaAproveitamento(String matricula, Integer disciplina, String situacao, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void validarUsuarioConsultarMinhasNotas(UsuarioVO usuario) throws Exception;

	public boolean executarValidarMatriculaPeriodoExcedeuLimiteMaximoDisciplinaReprovado(String matricula, Boolean considerarDisciplinasReprovadasPeriodosLetivosAnteriores, Integer numeroMaximoDiscipina, UsuarioVO usuario) throws Exception;

	public List consultarPorMatriculaDisciplinaSituacao(String matricula, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void excluirPorMatriculaPeriodo(Integer codigoMatriculaPeriodo, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultarPorMatriculaDisciplinaAprovado_Cursando(String matricula, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List consultarPorMatriculaDisciplinasAprovadas(String valorConsulta, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void incluirHistorico(MatriculaPeriodoTurmaDisciplinaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHist(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, String situacaoMatricula, String situacaoHistorico, boolean verificarDisciplina, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, int nivelMontarDados, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuario, boolean trazerAlunosTransferenciaMatriz, boolean permiteLancarNotaDisciplinaComposta) throws Exception;

	public List<HistoricoVO> consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessor(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, String situacaoMatricula, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, boolean trazerAlunoPendenteFinanceiramente, ConfiguracaoAcademicoVO configuracaoAcademicoVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	public HistoricoVO consultaRapidaPorMatriula_Disciplina_Semestre_Ano(String matricula, Integer disciplina, String semestre, String ano, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultaRapidaPorMatricula(String matricula, Integer gradeCurricular, int ordemHistoricoDisciplina, boolean apresentarDisciplinaForaGrade, Integer disciplina, boolean controlarAcesso, NivelMontarDados nivelMontarDados, boolean apresentarApenasUltimoHistoricoDisciplina, Boolean xmlDiploma, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultaRapidaPorMatriculaSituacaoHistorico(Integer aluno, String matriculaAluno, Integer codigoGradeCurricular, String[] situacao, int ordemHistoricoDisciplina, Boolean carregarDadosProfessor,  boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario, ConfiguracaoAcademicoVO configuracaoAcademicoVO, boolean isAproveitamento) throws Exception;

	public List<HistoricoVO> excluirHistoricoForaPrazo(HistoricoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultaRapidaPorMatriculaPeriodo(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public HistoricoVO consultaRapidaPorMatricula_matriculaPeriodo_Disciplina(String matricula, Integer matriculaPeriodo, Integer gradeCurricular, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void incluirHistorico(MatriculaPeriodoTurmaDisciplinaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;

	public List consultaRapidaHistoricoPorMatriculaPeriodo(Integer codMatriculaPeriodo, Integer gradeCurricularAtual, UsuarioVO usuario) throws Exception;

	public void excluirHistoricoPorGradeCurricularPorListaMatriculaPeriodo(String listaMatriculaPeriodo, Integer gradeCurricular, UsuarioVO usuario) throws Exception;

	public void excluirPorMatriculaPeriodoTurmaDisciplina(Integer codigoMatriculaPeriodoTurmaDisciplina, UsuarioVO usuario) throws Exception;

	// public void
	// incluirHistoricoTransferenciaGradeCurricular(MatriculaPeriodoTurmaDisciplinaVO
	// obj, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO,
	// Double mediaFinal, Double frequencia, String situacao, Integer
	// mediaFinalNotaConceito, String anoHistorico, String semestreHistorico,
	// String instituicao, Integer cidade, Integer cargaHoraria, Integer
	// cargaHorariaCursada, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,
	// UsuarioVO usuario) throws Exception;

	public HistoricoVO consultaRapidaAlunoAprovadoDisciplinaPorMatriula_Disciplina(String matricula, Integer disciplina, String semestre, String ano, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void verificarAprovacaoAlunos(CursoVO cursoVO, List<HistoricoVO> historicoVOs, UsuarioVO usuario) throws Exception;

	public void verificarAprovacaoAluno(HistoricoVO historicoVO, UsuarioVO usuario) throws Exception;

	public void verificarNotasLancadas(HistoricoVO historicoVO, UsuarioVO usuario);

	public List<HistoricoVO> consultarHistoricoParaAproveitamentoNaTransferenciaInternaPorCursoGrade(Integer aluno, Integer cursoNovo, Integer gradeCurricular, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception;

	public void alterarSituacaoHistoricoPelaMatriculaPeriodo(Integer matriculaPeriodo, String situacao, UsuarioVO usuario) throws Exception;

	public Boolean consultarSeDisciplinaEstaSendoCursadaOuAprovada(String matricula, Integer codigoDisciplina, UsuarioVO usuario) throws Exception;

	public void processarOrganizacaoHistoricosAlunos(String nomeCurso, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultaRapidaPorMatriculaGradeCurricularesEMatriculaPeriodosSomenteDisciplinasDaGradeParaExclusaoForaPrazo(String valorConsulta, List<MatriculaPeriodoVO> listaMatriculaPeriodoVO, int ordemHistoricoDisciplina, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void validarDadosParaMontarListaHistorico(MatriculaVO matricula, Integer periodoLetivo, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> executarMontagemListaHistoricoAluno(MatriculaVO matriculaVO, Integer periodoLetivo, ConfiguracaoAcademicoVO configuracaoAcademico, CursoVO cursoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, String anoSemestre, boolean trazerUltimoHistorico, Boolean validarAcesso,  Boolean trazerQuantidadesDisciplinas , UsuarioVO usuario) throws Exception;

	public void atualizarMediaFinal(HistoricoVO historicoVO) throws Exception;

	public List<HistoricoVO> consultarHistoricoPorListaDeMatriculaPeriodoTurmaDisciplina(List<Integer> listaMPTDs, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public HistoricoVO consultarHistoricoPorMatriculaPeriodoTurmaDisciplina(Integer codigoMPTD, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public void alterarInformacoesInseridasRegistroAulaNota(HistoricoVO histVO) throws Exception;

	public void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public Double calcularFrequenciaAluno(Integer codigoTurma, Integer codigoDisciplina, String semestre, String ano, String matricula, Integer cargaHorariaDisciplina, Double somaPresencaAluno, UsuarioVO usuario) throws Exception;

	public String consultarMediaAlunoDisciplina(String matricula, Integer disciplina) throws Exception;

	public List<HistoricoVO> consultaRapidaPorMatriculaSituacaoAprovadoAproveitamentoMedia(String matricula, boolean aprovadoAproveitamento, boolean aprovadoMedia, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public HashMap<Integer, HistoricoVO> consultaRapidaPorMatriculaDisciplinasHistoricoSemDadosSubordinados(String matricula, List<HistoricoVO> listaHistorico, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultaRapidaPorTurmaUnidadeEnsinoCursoDisciplinaAnoSemestreDataHistoricoOrdenarDisciplina(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, String situacaoMatricula, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, boolean controlarAcesso, int nivelMontarDados, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String tipoAluno, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	public List<HistoricoVO> consultarPorCodigoDisciplinaExcluirDisciplinaPermanecerUnificacaoDisciplina(Integer disciplinaExcluir, Integer disciplinaPermanecer, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public Boolean consultarExistenciaHistoricoPorCodigoDisciplina(Integer disciplina, Integer matriculaPeriodo) throws Exception;

	public void inicializarDadosHistoricoUnificacaoDisciplina(HistoricoVO historicoPermanecer, HistoricoVO historicoExcluir);

	public List<Integer> consultarPorCodigoDisciplina(Integer codigoExcluir);

	public Double calcularFrequenciaAlunoPosGraduacao(Integer codigoTurma, Integer codigoDisciplina, String semestre, String ano, String matricula, Integer cargaHorariaDisciplina, Double somaPresencaAluno, Integer qtdeRegistroAula, Integer cargaHorariaAula, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, Boolean trazerAlunoPendenteFinanceiramente, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permitiVisualizarAlunoTR_CA, Boolean trazerDisciplinaComposta,  int nivelMontarDados, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	public HistoricoVO consultaRapidaPorMatricula_matriculaPeriodo_Disciplina_Turma(String matricula, Integer matriculaPeriodo, Integer gradeCurricular, Integer disciplina, Integer turma, boolean controlarAcesso,Boolean matriculaExterna, UsuarioVO usuario) throws Exception;

	public void validarConsultaDoUsuarioVisaoCoordenador(String identidadeLancamentoNota, UsuarioVO usuario) throws Exception;

	public Integer consultarQuantidadeAlunoMediaCalculada(String nivelEducacional, Integer unidadeEnsino, Integer curso, Integer disciplina, Integer turma, UsuarioVO usuario) throws Exception;

	public void alterarSituacaoHistoricoPorCodigo(final Integer historico, final String situacao, UsuarioVO usuario) throws Exception;

	public void alterarNotaDisciplinaComposta(String nomeNota, Integer historico, Double nota);

	public Boolean executarAtualizacaoDisciplinaComposta(HistoricoVO historicoVO, List<HistoricoVO> historicoDisciplinaFazParteComposicaoVOs, TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico, Boolean alterarHistorico, Boolean resultadoDisciplinaFilha, UsuarioVO usuarioVO) throws Exception;

	public HistoricoVO consultaRapidaPorMatricula_Disciplina_Turma(String matricula, Integer disciplina, TurmaVO turma, String ano, String semestre, Boolean trazerBimestreAnoConclusaoDiscplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultarPeriodoLetivoPorMatriculaPeriodoLetivoBoletim(String matricula, Integer turma, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public Boolean consultaNumeroDisciplinasReprovadasAlunoMaiorPermitido(Integer matriculaPeriodo, String valorConsulta, Integer periodoLetivo, Integer numeroDisciplinasReprovadasPermitido, UsuarioVO usuario) throws Exception;

	public void excluirHistoricoPorMatriculaCodigoDisciplinaAproveitamento(String matricula, Integer codigoDisciplina, UsuarioVO usuario) throws Exception;

	public void excluirComBaseNaMatriculaPeriodo(Integer codMatriculaPeriodo, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public List<HistoricoVO> consultaRapidaPorMatriculaHistoricoTransferenciaMatrizCurricular(String matricula, Integer gradeCurricular, Boolean xmlDiploma, UsuarioVO usuarioVO) throws Exception;

	public List<Integer> consultarPorChavePrimariaDadosConfiguracaoAcademicaDadosMinimos(String matricula, List<HistoricoVO> listaAnoSemestreVOs, UsuarioVO usuarioVO) throws ConsistirException, Exception;

	public List<HistoricoVO> consultarDisciplinaCursadasAlunoPorMatricula(String matricula, Integer gradeCurricular, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limit, Boolean considerarDisciplinasCursando) throws Exception;

	public void validarHistoricoDisciplinaCursandoPorMatricula(String matricula, Integer disciplina, Integer turma, UsuarioVO usuarioVO) throws Exception;

	public HistoricoVO consultaRapidaPorMatriula_Disciplina_Semestre_Ano_Turma(String matricula, Integer disciplina, String semestre, String ano, Integer turma, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void alterarNotificarSolicitacaoReposicao(final Integer codigo, final Boolean notificar) throws Exception;

	public List<HistoricoVO> consultarAlunoSolicitouAvisoAulaFutura() throws Exception;

	public HistoricoVO consultaRapidaPorMatricula_Ano_Semestre_Disciplina(String matricula, String ano, String semestre, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public HistoricoVO consultaRapidaPorMatricula_Ano_Semestre_Disciplina(String matricula, String ano, String semestre, Integer disciplina, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void excluirHistoricoPorMatriculaESituacoes(String matricula, String situacoes) throws Exception;

	public Boolean consultarPorMatriculaDisciplinaSituacao(String matricula, Integer codigoDisciplina, String situacao, UsuarioVO usuario) throws Exception;

	public void alterarHistoricoAlteracaoAproveitamentoDisciplina(final Integer codigo, final DisciplinaAproveitadaAlteradaMatriculaVO obj, UsuarioVO usuario) throws Exception;

	SqlRowSet consultarPorMatriculaPeriodoLetivoBoletimEnsinoMedio(String matricula, Integer turma, String ano, String semestre, Boolean apresentarDisciplinaComposta, Integer configuracaoAcademico, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, Integer gradeCurricular, List<String> listaNotas, Boolean transferenciaGradeCurricular) throws Exception;

	public List consultarPorMatriculaDisciplinaTurmaSituacaoHistoricoAnoSemestre(String matricula, Integer gradeCurricular, Integer disciplina, TurmaVO turmaVO, String situacaoHistorico, String ano, String semestre,  boolean permiteLancarNotaDisciplinaComposta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuario) throws Exception;


	Map<String, SituacaoHistorico> executarCalcularResultadoFinalAluno(Map<String, Map<String, String>> mapMatriculaDisciplina, Map<String, ConfiguracaoAcademicoVO> mapConfiguracaoAcademicoVOs) throws Exception;

	public Integer consultarPeriodoLetivoPorCodigoHistorico(Integer codigoHistorico) throws Exception;

	/**
	 *
	 * @param matriculaVO
	 * @param codigoGradeCurricular
	 * @param configuracaoAcademicoVO
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public MatriculaComHistoricoAlunoVO carregarDadosMatriculaComHistoricoAlunoVO(MatriculaVO matriculaVO, Integer codigoGradeCurricular, Boolean carregarDadosProfessor, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuario) throws Exception;

	void realizarValidacaoNotaLancada(HistoricoVO historicoVO, List<HistoricoVO> historicoDisciplinaFazParteComposicaoVOs,  ConfiguracaoAcademicoVO configuracaoAcademicoVO, int numeroNota) throws Exception;

	/**
	 * HOMOLOGADO VERSAO 5.0 Consulta por históricos de uma matrícula e de uma
	 * matriz curricular. Isto para uma ou mais disciplinas (lista de códigos) e
	 * uma lista de situações do historico. Podendo filtrar assim, somente
	 * históricos aprovados, reprovados e afins. Outros parametros importantes
	 * são filtros do tipo de disciplina. Por exemplo, se passado TRUE para o
	 * parametro somenteHistoricoDeDisciplinasEquivalentes, serao retornados
	 * somente historicos de disciplinas que foram estudadas por serem
	 * equivalentes. Caso seja FALSE, este parametro não será considerado para
	 * filtro - não terá nenhum referÊncia na CLAUSULA WHERE. O mesmo é valido
	 * para os parametros: somenteHistoricoDeDisciplinasDaMatriz e
	 * somenteHistoricoForaMatrizCurricular. passadas como parametro. Também
	 *
	 * @throws Exception
	 */
	public List<HistoricoVO> consultaRapidaPorDisciplinaGradeCurricular(String matricula, Integer gradeCurricular, List<Integer> disciplinas, List<SituacaoHistorico> situacoes, Boolean somenteHistoricoDeDisciplinasDaMatriz, Boolean somenteHistoricoDeDisciplinasEquivalentes, Boolean somenteHistoricoForaMatrizCurricular, Integer mapaEquivalenciaDisciplina, Integer numeroAgrupamentoEquivalenciaDisciplina, Integer historicoIgnorar, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * HOMOLOGADO VERSAO 5.0 DisciplinasAproveitadasVO obj deverá estar com os
	 * dados do AproveitamentoDisciplinaVO montado
	 */
	public HistoricoVO criarHistoricoAPartirDisciplinaAproveitada(DisciplinasAproveitadasVO objItem) throws Exception;

	public void atualizarDadosHistoricoComBaseDisciplinaAproveitada(HistoricoVO historicoVO, DisciplinasAproveitadasVO objItem) throws Exception;

	public void carregarDados(HistoricoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void atualizarDadosMatriculaComHistoricoAlunoVO(MatriculaVO matriculaVO, Integer codigoGradeCurricular, Boolean carregarDadosProfessor, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuario) throws Exception;

	public void consultaRapidaPorMatriculaHistoricoDisciplinaACursar(String matricula, int ordemHistoricoDisciplina, List<HistoricoAlunoDisciplinaRelVO> lista, boolean utilizarNomeCertificacaoPeriodoLetivo, UsuarioVO usuario) throws Exception;

	List<HistoricoVO> consultarHistoricoAptoVincularAtividadeComplementarPorMatricula(String matricula);

	void realizarVinculoHistoricoAtividadeComplementar(Integer historico, boolean historicoDisciplinaAtividadeComplementar) throws Exception;

	public MatriculaComHistoricoAlunoVO carregarDadosMatriculaComHistoricoAlunoVO(MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularCarregada, boolean gradeJaCarregada, Boolean carregarDadosProfessor, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuario) throws Exception;

	public HistoricoVO gerarHistoricoVOComBaseMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricular, UsuarioVO usuario) throws Exception;

	HistoricoVO consultaHistoricoDisciplinaCompostaPorMatriculaGradeCurricularAnoSemestreGradeDisciplinaComposta(String matricula, Integer gradeCurricular, String ano, String semestre, Integer gradeDisciplinaComposta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public HistoricoVO consultaRapidaPorCodigoMatriculaPeriodoTurmaDisciplina(Integer codigoMatriculaPeriodoTurmaDisciplina, Integer codigoMatrizCurricular, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void excluirPorTransferenciaMatrizCurricularMatricula(Integer codigoTransferenciaMatrizCurricularMatricula, Integer codigoMatrizCurricular, UsuarioVO usuario) throws Exception;

	public boolean verificarExisteHistoricoMatriculaVinculadoGradeDestino(String matricula, Integer codigo) throws Exception;

	List<HistoricoVO> verificarExisteHistoricoMatrizNaoCriadoPorTrasnferenciaMatriz(String matricula, Integer codigoGradeCurricularAtual, Integer codigoTransferenciaMatricula) throws Exception;

	public void alterarVinculoHistoricoTransferenciaMatrizCurricularMatricula(final Integer codigoHistorico, final Integer codigoTransferenciaMatrizCurricularMatricula, final Boolean historicoCursandoPorCorrespondenciaAposTransferencia, UsuarioVO usuario) throws Exception;

	Double calcularCoeficienteRendimentoPeriodoLetivoAluno(String matricula, String ano, String semestre) throws Exception;

	Double calcularCoeficienteRendimentoGeralAluno(String matricula) throws Exception;

	public String realizarCriacaoMensagemNotaLancada(HistoricoVO obj, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO);

	public String realizarCriacaoMensagemMediaFrequencia(HistoricoVO obj, UsuarioVO usuarioVO);

	public List<HistoricoVO> excluirDisciplinaAlunoPorDisciplina(HistoricoVO obj, boolean realizandoExclusaoDisciplinaForaPrazo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	void excluirHistoricoForaPrazo(List<HistoricoVO> historicoVOs, HistoricoVO historicoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	List<HistoricoVO> consultarAnoSemestreHistoricoPorMatriculaBoletimAcademicoRel(String matricula, Integer matrizCurricular, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public int consultaTotalCargaHorariaMatricula(String matricula, Integer gradeCurricular, UsuarioVO usuario) throws Exception;

	void realizarCriacaoHistoricoNotaVO(HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs) throws Exception;

	Boolean realizarLancamentoNotaHistoricoAutomaticamente(String variavelNota, Double nota, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean realizarCalculo, UsuarioVO usuarioVO) throws Exception;

	HistoricoVO consultarHistoricoParaRegistroLancamentoNotaHistoricoAutomatico(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;

	public List<HistoricoVO> consultarSomenteAnoSemestreHistoricoPorMatriculaBoletimAcademicoRel(String matricula, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<HistoricoVO> consultaRapidaAlterarConfiguracaoAcadHistorico(String matricula, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Integer configuracaoAcademico, String ano, String semestre, UsuarioVO usuario) throws Exception;

	public void alterarConfiguracaoAcademicaHistorico(HistoricoVO historicoVO, Integer configuracaoaAcadHistorico, UsuarioVO usuarioVO) throws Exception;

	public Integer consultarCargaHorariaQueAlunoEstaCursandoDoUltimoPeriodo(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO);

	void alterarHistoricoPorHistoricoGradeAnteriorAlterada(HistoricoGradeAnteriorAlteradaVO obj, UsuarioVO usuario) throws Exception;

	public void alterarHistoricoPorAlteracaoGradeCurricularCursoIntegral(TurmaVO turmaNovaGradeCurricular, DisciplinaVO novaDisciplina, Integer disciplina, GradeDisciplinaVO gd, GradeCurricularGrupoOptativaDisciplinaVO gcgod, List<MatriculaPeriodoVO> listaMatriculaPeriodo, UsuarioVO usuario) throws Exception;

	public void alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(Boolean isOrigemHistorico, Integer codigoOrigem, Integer matrizCurricular, Integer periodoLetivoCursada, Integer periodoLetivoMatrizCurricular, DisciplinaVO novaDisciplina, GradeDisciplinaVO gd, GradeCurricularGrupoOptativaDisciplinaVO gcgod, Integer mapaEquivalenciaDisciplina, Integer mapaEquivalenciaDisciplinaCursada, Integer mapaEquivalenciaDisciplinaMatrizCurricular, Integer numeroAgrupamentoEquivalenciaDisciplina,  boolean historicoEquivalente, UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoVO> verificarSeExisteHistoricoForaGradePorAlteracaoGradeCurricularCursoIntegral(List<MatriculaPeriodoVO> listaMatriculaPeriodo, Integer disciplina, Integer gradeCurricular, UsuarioVO usuarioVO) throws Exception;

	void alterarHistoricoForaDaGradePorAlteracaoGradeCurricularCursoIntegral(List<MatriculaPeriodoVO> listaMatriculaPeriodo, TurmaVO turmaNovaGradeCurricular, Integer disciplina, Integer gradeCurricular, boolean historicodisciplinaforagrade, UsuarioVO usuario) throws Exception;

	boolean verificarExisteHistoricoVinculadoMatriculaMatrizCurricular(String matricula, Integer matrizCurricular, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 27/03/2015
	 * @param matriculaPeriodo
	 * @param gradeCurricularAtual
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param configuracaoFinanceiroVO
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<HistoricoVO> consultarPorMatriculaPeriodoGradeCurricularAtual(Integer matriculaPeriodo, Integer gradeCurricularAtual, String[] situacaoHistoricoDesconsiderar, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 27/03/2015
	 * @param matricula
	 * @param codigo
	 * @param b
	 * @param usuario
	 * @return
	 */
	List<HistoricoVO> consultarHistoricoEquivalentePorMatriculaMapaEquivalenciaDisciplina(String matricula, Integer codigo, Integer numeroAgrupamentoEquivalenciaDisciplina, boolean b, UsuarioVO usuario) throws Exception;

	HistoricoVO consultarHistoricoDoMapaDisciplinaEquivalenteMatrizCurricular(Integer mptd, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 27/03/2015
	 * @param matricula
	 * @param gradeDisciplina
	 * @param matrizCurricular
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<HistoricoVO> consultarHistoricoDisciplinaFazParteComposicao(String matricula, Integer gradeDisciplina, Integer matrizCurricular, boolean verificarAcesso, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por executar a geração da Falta do primeiro, segundo,
	 * terceiro, quarto bimestre, Total Falta e Frequência do Histórico.
	 *
	 * @author Wellington Rodrigues - 06/04/2015
	 * @param historicoVO
	 * @param configuracaoAcademicoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void executarGeracaoFaltaPrimeiroSegundoTerceiroQuartoBimestreTotalFaltaFrequenciaHistorico(HistoricoVO historicoVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por executar a alteração das faltas do primeiro, segundo,
	 * terceiro, quarto bimestre, total falta e frequencia, considerando
	 * histórico por correspondência, por equivalência e que faz parte de
	 * composição.
	 *
	 * @author Wellington Rodrigues - 07/04/2015
	 * @param historicoVO
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	void executarAlteracaoFaltaPrimeiroSegundoTerceiroQuartoTotalFaltaBimestreFrequenciaConsiderandoHistoricoPorCorrespondenciaEquivalenciaFazParteComposicao(HistoricoVO historicoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<HistoricoVO> consultaRapidaHistoricoFazParteComposicaoPorMatriculaPorGradeCurricularPorMatriculaPeriodo(String matricula, Integer historicoFazParteComposicao, Integer matriculaPeriodo, Integer gradeCurricular, Integer gradeDisciplina, Integer gradeCurricularGrupoOptativaDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void executarCalculoFaltasAlunoBimestreDisciplinaComposta(List<HistoricoVO> historicoVOs, HistoricoVO historicoDisciplinaComposta);

	public void inicializarDadosNotaDisciplinaComposta(HistoricoVO obj, Double nota, String nomeNota);

	public void calcularMedia(HistoricoVO histVO, List<HistoricoVO> historicoDisciplinaFazParteComposicaoVOs, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Integer turma, TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico, Boolean verificarNotasUsadas, UsuarioVO usuarioVO) throws Exception;

	public void executarCalculoMediaParcialDisciplinaComposta(HistoricoVO historicoDisciplinaComposta, List<HistoricoVO> historicoDisciplinaFazParteComposicaoVOs, UsuarioVO usuarioVO) throws Exception;

	public void executarCalculoMediaParcialDisciplinaCompostaPorFormulaCalculo(HistoricoVO historicoDisciplinaComposta, List<HistoricoVO> historicoDisciplinaFazParteComposicaoVOs, UsuarioVO usuarioVO) throws Exception;

	public void executarSomatorioNotaDisciplinaComposta(HistoricoVO historicoDisciplinaComposta, List<HistoricoVO> historicoDisciplinaFazParteComposicaoVOs, UsuarioVO usuarioVO) throws Exception;

	public HistoricoVO consultarPorMatriculaDisciplinaSituacaoHistoricoAnoSemestre(String matricula, Integer disciplina, String situacaoHistorico, String ano, String semestre, Integer configuracaoAcademico, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultarPorMatriculaDisciplinaSituacaoHistoricoAnoSemestrePorAreaConhecimento(String matricula, Integer areaConhecimento, String situacaoHistorico, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public HistoricoVO consultaCompletaPorMatriculaDisciplinaSituacaoHistoricoAnoSemestre(String matricula, Integer disciplina, String situacaoHistorico, String ano, String semestre,  boolean permiteLancarNotaDisciplinaComposta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Boolean realizarVerificacaoAlunoCumpriuCargaHorariaDisciplinaOptativa(
			MatriculaVO matricula, Integer gradeCurricular, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO);

	HistoricoVO consultaRapidaPorChavePrimariaDadosCompletosSemExcessao(Integer codHistorico, UsuarioVO usuario) throws Exception;
	/**
	 * @author Rodrigo Wind - 12/08/2015
	 * @param historico
	 * @param incluirNotaRecuperacao
	 * @param visao
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	String gravarLancamentoNota(HistoricoVO historico, Boolean incluirNotaRecuperacao, String visao, TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico, UsuarioVO usuario) throws Exception;

	public String getSqlCompletoConsultaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, Boolean trazerAlunoPendenteFinanceiramente, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, Integer codigoProfessor, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permitiVisualizarAlunoTR_CA, Boolean trazerDisciplinaComposta, int nivelMontarDados, Integer limite, Integer offSet, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;
	public List<HistoricoVO> consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, Boolean trazerAlunoPendenteFinanceiramente, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, Integer codigoProfessor, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permitiVisualizarAlunoTR_CA, Boolean trazerDisciplinaComposta, int nivelMontarDados, Integer limite, Integer offSet, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	public String getSqlCompletoConsultaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, Boolean trazerAlunoPendenteFinanceiramente, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, Integer codigoProfessor, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permitiVisualizarAlunoTR_CA, Boolean trazerDisciplinaComposta, int nivelMontarDados, Integer limite, Integer offSet, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados, boolean considerarProfessorExclusivo) throws Exception;
	public List<HistoricoVO> consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, Boolean trazerAlunoPendenteFinanceiramente, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, Integer codigoProfessor, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permitiVisualizarAlunoTR_CA, Boolean trazerDisciplinaComposta, int nivelMontarDados, Integer limite, Integer offSet, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados, boolean considerarProfessorExclusivo) throws Exception;

	public Integer consultaRapidaTotalizadoPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, Boolean trazerAlunoPendenteFinanceiramente, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permitiVisualizarAlunoTR_CA, Boolean trazerDisciplinaComposta, int nivelMontarDados, Integer limite, Integer offSet, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;
	public Integer consultaRapidaTotalizadoPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, Boolean trazerAlunoPendenteFinanceiramente, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permitiVisualizarAlunoTR_CA, Boolean trazerDisciplinaComposta, int nivelMontarDados, Integer limite, Integer offSet, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados, boolean considerarProfessorExclusivo) throws Exception;

	public Integer consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaTotalRegistro(String nomeAluno, Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, Boolean trazerAlunoPendenteFinanceiramente, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permitiVisualizarAlunoTR_CA, Boolean trazerDisciplinaComposta,  int nivelMontarDados, Integer limite, Integer offSet, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	public Integer consultarQtdHistorioCursandoPorCalendarioAtividadeMatriculaPorPeriodo(Integer codigoMatriculaPeriodo, Integer mptdAnterior, TipoCalendarioAtividadeMatriculaEnum tipocalendarioatividade,  boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaNomeAluno(String nomeAluno, Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, Boolean trazerAlunoPendenteFinanceiramente, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permitiVisualizarAlunoTR_CA, Boolean trazerDisciplinaComposta,  int nivelMontarDados, Integer limite, Integer offSet, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados, DataModelo dataModelo) throws Exception;

	public void incluirListaHistoricoVisaoProfessor(final List<HistoricoVO> listaHistorico, String idEntidade, final UsuarioVO usuario, final String visao, final Boolean incluirNotaRecuperacao, CalendarioLancamentoNotaVO calendarioLancamentoNotaVO, boolean calcularMediaAoGravar) throws ConsistirException, Exception;

	/**
	 * @author Rodrigo Wind - 19/10/2015
	 * @param tipoNotaAlterar
	 * @param historicoVO
	 * @param nota
	 * @param matriculaPeriodoTurmaDisciplinaVO
	 * @param realizarCalculo
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	Boolean realizarLancamentoNotaHistoricoAutomaticamente(TipoNotaConceitoEnum tipoNotaAlterar, HistoricoVO historicoVO, Double nota, Boolean utilizaNotaConceito, ConfiguracaoAcademicoNotaConceitoVO notaConceito, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean realizarCalculo, boolean alterar, UsuarioVO usuarioVO) throws Exception;

	public Integer consultarCargaHorariaHistPorMatriculaPeriodoTurmaDisciplina(Integer codMatPerTurDisc) throws Exception;

	public List<HistoricoVO> consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaPadraoAnoSemestreSituacaoMatSituacaoHist(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, String situacaoMatricula, String situacaoHistorico, boolean verificarDisciplina, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, List<MatriculaPeriodoVO> matriculaPeriodoVOs, boolean trazerDisciplinaComposta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 08/01/2016
	 * @param historicoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	Boolean realizarVerificacaoEAtualizacaoFrequenciaHistoricoAluno(HistoricoVO historicoVO, UsuarioVO usuarioVO) throws Exception;

	void verificarAprovacaoAlunos(final List<HistoricoVO> alunosTurma, boolean processarComParalelismo,  boolean realizarCalculoMediaApuracaoNotas, final UsuarioVO usuarioVO) throws Exception;

	List<HistoricoVO> consultarPorMatriculaCancelada(Integer matriculaPeriodo, UsuarioVO usuario) throws Exception;

	List<HistoricoVO> consultarPorMatriculaTrancadaAbandonadaJubilada(Integer matriculaPeriodo, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 28/01/2016
	 * @param matriculaPeriodoVOs
	 * @param ano
	 * @param semestre
	 * @throws Exception
	 */
	void realizarAlteracaoHistoricoReprovadoPeriodoLetivoDeAcordoConfiguracaoAcademica(List<MatriculaPeriodoVO> matriculaPeriodoVOs, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * @author Wellington - 16 de fev de 2016
	 * @param obj
	 * @param configuracaoFinanceiroVO
	 * @param usuario
	 * @throws Exception
	 */
	List<HistoricoVO> excluirHistoricoForaPrazoVerificandoHistoricoPorEquivalencia(HistoricoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 22/02/2016
	 * @param matriculaPeriodoTurmaDisciplina
	 * @param mapaEquivalenciaDisciplina
	 * @return
	 * @throws Exception
	 */
	Integer consultarNumeroAgrupamentoEquivalenciaDisciplinaPorMatriculaPeriodoTurmaDisciplina(Integer matriculaPeriodoTurmaDisciplina, Integer mapaEquivalenciaDisciplina) throws Exception;

	void alterarDataRegistroHistorico(final Integer codigo, final Date dataRegistro, final Date dataInicioAula, final Date dataFimAula, UsuarioVO usuario) throws Exception;

	public void realizaExclusaoHistoricoDuplicadoMigracaoMatrizCurricular(String matriculaAluno, Integer gradeCurricular, Integer codigoTransferencia, List<MapaEquivalenciaDisciplinaMatrizCurricularVO> mapaEquivalenciaDisciplinaMatrizCurricularVO, UsuarioVO usuario) throws Exception;

	public void executarSimulacaoAtualizacaoDisciplinaComposta(HistoricoVO historicoDisciplinaComposta, List<HistoricoVO> historicosDisciplinasFazemParteComposicaoVOs, Boolean simulandoDoAproveitamentoDisciplina, UsuarioVO usuarioVO) throws Exception;

	public void alterarNotasHistoricoPorForum(Integer historico, String variavelTipoNota, Double nota, Integer notaConceito, UsuarioVO usuario) throws Exception;

	List<HistoricoVO> listarNotasHistoricoPorForum(String variavelTipoNota, String listaCodHistoricos, Boolean isNotaConceito, UsuarioVO usuario) throws Exception;

	public void excluirHistoricoPorMatriculaCodigoDisciplinaAproveitamentoEquivalencia(String matricula, Integer codigoDisciplina, UsuarioVO usuario) throws Exception;

	public void alterarVinculoHistoricoMatriculaPeriodoTurmaDisciplina(final Integer codigoHistorico, final Integer codigoMatriculaPeriodoTurmaDisciplina, final Boolean historicoCursandoPorCorrespondenciaAposTransferencia, UsuarioVO usuario) throws Exception;

	public void excluirComBaseNaMatriculaPeriodoCodDisciplinaSituacaoHistorico(Integer codMatriculaPeriodo, Integer codDisciplina, String situacao, Integer gradeCurricularAtual, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * @author Victor Hugo de Paula Costa - 6 de set de 2016
	 * @param matriculaPeriodoTurmaDisciplina
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	HistoricoVO consultarPorMatriculaPeriodoTurmaDisciplinaDadosBasicos(Integer matriculaPeriodoTurmaDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * @author Victor Hugo de Paula Costa - 6 de set de 2016
	 * @param historico
	 * @param variavelTipoNota
	 * @param nota
	 * @param usuario
	 * @throws Exception
	 */
	void alterarNotasHistoricoGestaoEventoConteudoTurmaResultadoFinal(Integer matriculaPeriodoTurmaDisciplina, String variavelTipoNota, Double nota, UsuarioVO usuario) throws Exception;
List<HistoricoVO> consultarPorMatriculaPeriodoSituacoes(Integer matriculaPeriodo, String situacoes,
			UsuarioVO usuario) throws Exception;/**
	 * @author Victor Hugo de Paula Costa - 27 de out de 2016
	 * @param matricula
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<HistoricoVO> consultaBasicaHistoricoAplicativo(MatriculaVO matricula, UsuarioVO usuario) throws Exception;

	/**
	 * @author Victor Hugo de Paula Costa - 27 de out de 2016
	 * @param codigoHistorico
	 * @param matriculaVO
	 * @param periodoLetivo
	 * @param configuracaoAcademico
	 * @param cursoVO
	 * @param configuracaoFinanceiro
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<HistoricoVO> executarMontagemListaHistoricoAlunoPorCodigoHistorico(Integer codigoHistorico, MatriculaVO matriculaVO, Integer periodoLetivo, ConfiguracaoAcademicoVO configuracaoAcademico, CursoVO cursoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, String anoSemestre, boolean trazerUltimoHistorico, Boolean validarAcesso,Boolean trazerQuantidadesDisciplinas , UsuarioVO usuario) throws Exception;

	void carregarDadosNotaConceitoHistorico(HistoricoVO historicoVO) throws Exception;


	List<HistoricoVO> executarAtualizacaoDisciplinaFilhaComposicaoComBaseDisciplinaCompostaComposta(
			HistoricoVO historicoDisciplinaComposta, TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico,
			Boolean alterarHistorico, UsuarioVO usuarioVO) throws Exception;

	void alterarFaltaEFrequenciaHistorico(HistoricoVO historicoVO, boolean controlarAcesso, UsuarioVO usuarioVO)
			throws Exception;

	/**
	 * @author Carlos Eugênio - 20/12/2016
	 * @param codMatriculaPeriodo
	 * @param matrizCurricular
	 * @param codDisciplina
	 * @param situacao
	 * @param confFinanVO
	 * @param usuarioLogado
	 * @throws Exception
	 */
	void excluirComBaseNaMatriculaPeriodoMatrizCurricularCodDisciplinaSituacaoHistorico(Integer codMatriculaPeriodo, Integer matrizCurricular, Integer codDisciplina, String situacao, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	List<HistoricoVO> consultarHistoricoPorMatriculaPeriodoFichaAluno(Integer matriculaPeriodo, Integer gradeCurricular, boolean consultarHistoricoDisciplinaCompostaSemFilhas, UsuarioVO usuarioVO) throws Exception;

	HistoricoVO consultarPorMatriculaPeriodoTurmaDisciplinaHistoricoEquivalente(Integer matriculaPeriodoTurmaDisciplina, boolean filtroVisaoProfessor, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void removerVinculoMatriculaPeriodoTurmaDisciplinaHistoricoTransferidoMatrizCurricularAfimDePreservarOHistoricoDaMatrizAntigaCasoTenhaEquivalencia(String matricula, Integer matriculaPeriodoTurmaDisciplina, Integer transferenciaMatrizCurricularMatricula, Integer disciplina, UsuarioVO usuario) throws Exception;

	void alterarVinculoHistoricoCursandoTransferenciaMatrizCurricularMatricula(Integer codigoHistorico, Integer codigoTransferenciaMatrizCurricularMatricula, String situacao, Boolean historicoCursandoPorCorrespondenciaAposTransferencia, UsuarioVO usuario) throws Exception;

	void alterarMatriculaPeriodoTurmaDisciplina(Integer codigoHistorico, Integer codigoMatriculaPeriodoTurmaDisciplina, UsuarioVO usuario) throws Exception;

	void migrarNotasEDadosAcademicosEntreHistoricos(HistoricoVO histOrigem, HistoricoVO histVO);

	void alterarEliminandoVinculoHistoricoCursandoPorCorrespondencia(Integer codigoHistorico, UsuarioVO usuario) throws Exception;

	List<List<HistoricoVO>> realizarSeparacaoHistoricoPorPeriodicidade(List<HistoricoVO> historicoVOs,
			UsuarioVO usuarioVO) throws Exception;

	Boolean realizarVerificacaoAlunoReprovadoPeriodoLetivoDeAcordoConfiguracaoAcademica(Integer matriculaPeriodo, String ano, String semestre) throws Exception;

	void realizarAlteracaoSituacaoHistoricoReprovadoPeriodoLetivoDeAcordoRegraConfiguracaoAcademica(HistoricoVO histVO, String situacaoAtual, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;

	StringBuilder realizarCorrecaoLancamentoNotaEADHistorico(Integer unidadeEnsino, Integer curso, Integer turno,
			Integer turma, Integer disciplina, String ano, String semestre, String nivelEducacional,
			Integer configuracaoAcademica, String situacoesMatriculaPeriodo, UsuarioVO usuarioVO) throws Exception;

	void realizarVerificacaoBloqueiNotaDisciplinaComposta(List<HistoricoVO> historicos,
			ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception;

	public void realizarReplicacaoNotaHistoricoAluno(final HistoricoVO historicoVO, final Boolean utilizarNotaConceito, String variavelNota, TipoNotaConceitoEnum tipoNotaConceitoEnum, FormaReplicarNotaOutraDisciplinaEnum formaReplicarNotaOutraDisciplina, final UsuarioVO usuarioVO) throws Exception;

	public void realizarReplicacaoNota(List<HistoricoVO> historicoVOs, ConfiguracaoAcademicoVO configuracaoAcademicoVO, TipoNotaConceitoEnum tipoNotaConceitoEnum, FormaReplicarNotaOutraDisciplinaEnum formaReplicarNotaOutraDisciplina, UsuarioVO usuarioVO) throws Exception;

	public List<HistoricoVO> consultarPorMatriculaPeriodoLetivoAtualImpressaoDeclaracao(Integer codigoMatriculaPeriodo, Boolean apresentarDisciplinaComposta, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer gradeCurricular) throws Exception;

	public List<HistoricoVO> consultarCodigoNotaFrequenciaPorParametros(String cpf, String email, Integer curso, Integer turma, Integer disciplina, String ano, String semestre, String bimestre) throws Exception;

	List<HistoricoVO> realizarCriacaoHistoricoParaValidacaoConnfiguracaoAcademica(int qtde, List<HistoricoVO> historicoVOs) throws Exception;

	Integer consultarNumeroAgrupamentoEquivalenciaDisciplinaPorCodigo(Integer historico) throws Exception;

	Boolean realizarVerificacaoDataHistoricoPosteriorDataAulaPorMatriculaDisciplina(String matricula, Integer matriculaPeriodo, Integer gradeCurricular, Integer disciplina, UsuarioVO usuario) throws Exception;
	void realizarDesistenciaEquivalencia(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;

	Boolean consultarPorCodigoDesistenciaEquivalencia(Integer codigo, UsuarioVO usuarioVO);

	Integer consultarUltimoNumeroAgrupamentoEquivalenciaDisciplina(String matricula, Integer mapaEquivalenciaDisciplina) throws Exception;

	void realizarAlteracaoSituacaoHistoricoDesistenciaEquivalencia(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;

	void realizarAtualizacaoHoraComplementarHistorico(Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuarioLogado) throws Exception;

	void carregarDadosHorarioAulaAluno(HistoricoVO historicoVO, Boolean montarNomeProfessor);

	void alterarConfiguracaoAcademicaHistoricoConformeTurmaDisciplinaEstatisticaAlunoVO(TurmaVO turmaVO,
			TurmaDisciplinaEstatisticaAlunoVO turmaDisciplinaEstatisticaAlunoVO, UsuarioVO usuario) throws Exception;

	List<HistoricoVO> consultarHistoricoLancamentoNotaMobile(TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception;

	public void carregarDadosProfessorTitularTitulacao(List<HistoricoVO> historicoVOs) throws Exception;

	public Optional<SqlRowSet> consultarProfessorTitularTitulacao(HistoricoVO historicoVO, boolean apresentarApenasProfessorTitulacaoTurmaOrigem, boolean situacaoFormacaoConcluida) throws Exception;

	List<String> consultarAnoSemestreHistoricoPorMatricula(String matricula, String marcadorPrioridade, UsuarioVO usuario, SituacaoMatriculaPeriodoEnum... situacoes) throws Exception;

	void realizarCriacaoDescricaoNotasParciaisHistoricoVOs(List<HistoricoVO> historicoVOs, Boolean permitirApresentarTodasNotasParametrizadasConfiguracaoAcademica, UsuarioVO usuarioVO)
			throws Exception;

	public List<HistoricoVO> consultaRapidaHistoricoPorDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHist(Integer disciplina, TurmaVO turmaVO, String ano, String semestre, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, int nivelMontarDados, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuario, boolean trazerAlunosTransferenciaMatriz, boolean permiteLancarNotaDisciplinaComposta) throws Exception;
	public HashMap<String, Date> carregarUltimoPeriodoAulaDisciplinaAluno(Integer codigoHistorico);

	void realizarAlteracaoHistoricoCursandoPeriodoLetivoDeAcordoConfiguracaoAcademica(
			List<MatriculaPeriodoVO> matriculaPeriodoVOs, String ano, String semestre, UsuarioVO usuarioLogado)
			throws Exception;

	public List consultarDisciplinaCursadasPeriodoLetivoAlunoPorMatricula(String matricula, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limit) throws Exception;

	public Double consultarMediaAprovadasReprovadasAlunoPorMatricula(String matricula, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void incluirHistoricoNotaParcial(List<HistoricoVO> alunosTurma, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuario) throws Exception;

	public void validarProfessorExclusivoLancamentoNota(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO,
			UsuarioVO usuarioLogado) throws ConsistirException;

	boolean professorExclusivoLancamentoNota(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO,
			UsuarioVO usuarioVO);

	String inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido(String matricula, List<SelectItem> itens, SituacaoMatriculaPeriodoEnum... situacoes) throws Exception;

	List<HistoricoVO> consultarPorGradeDisciplinaVinculoHistoricoAlteracaoMatrizCurricular(GradeDisciplinaVO gradeDisciplina, UsuarioVO usuario) throws Exception;

	void alterarHistoricoPorGradeDisciplinaAlteracaoMatrizCurricularAtivaInativa(GradeDisciplinaVO gradeDisciplinaVO, UsuarioVO usuarioVO);

	String consultarMensagemImpactoHistoricoPorGradeDisciplina(GradeDisciplinaVO gradeDisciplina, UsuarioVO usuario) throws Exception;

	void alterarHistoricoParaForaDaGradePorGradeDisciplina(GradeDisciplinaVO gradeDisciplinaVO, UsuarioVO usuario) throws Exception;

	void excluirPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuarioLogado) throws Exception;

	Boolean realizarLancamentoNotaHistoricoAutomaticamente(TipoNotaConceitoEnum tipoNotaAlterar, HistoricoVO historicoVO, Double nota, Boolean utilizaNotaConceito, ConfiguracaoAcademicoNotaConceitoVO notaConceito, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean realizarCalculo, UsuarioVO usuarioVO) throws Exception;

	void removerVinculoTransferenciaMatrizCurricularMatriculaHistorico(int transferenciaMatrizCurricularMatricula, UsuarioVO usuarioVO) throws Exception;

	public void realizarMontagemTotalizadoresCargasHorarias(String filterAnoSemestre,
			HashMap<String, Integer> mapTotalCargasHorarias,
			List<List<HistoricoVO>> listaPrincipalHistoricos) throws Exception ;
	
	boolean consultarSeExisteHistoricoDisciplinaClassificadaTCCAprovadasPorMatricula(String matricula, UsuarioVO usuario) throws Exception;
	
	List<HistoricoVO> consultaHistoricoParaConsolidarNotasPorSalaAulaBlackboard(SalaAulaBlackboardNotaVO obj, UsuarioVO usuarioLogado) throws Exception;
	
	List<HistoricoVO> consultaHistoricoParaApuracaoNotaBlackboard(SalaAulaBlackboardNotaVO obj, boolean isDisciplinaTcc, UsuarioVO usuarioLogado) throws Exception;

	List<HistoricoVO> consultaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatricula(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, Integer disciplina, Integer turma, String ano, String semestre, List<Integer> matriculaPeriodoTurmaDisciplinas, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, int nivelmontardadosTodos, UsuarioVO usuarioLogado) throws Exception;

	public void realizarInclusaoHistoricosAntigosNovaMatriculaPorMatriculaAnterior(MatriculaVO matriculaVO , MatriculaPeriodoVO  matPeriodo ,MatriculaVO matriculaAntigaVO ,List<HistoricoVO> listaHistoricoAproveitar ,  UsuarioVO usuario) throws Exception;

	List<HistoricoVO> consultaRapidaPorMatriculaDadosCompletos(String matricula, boolean controlarAcesso,UsuarioVO usuario) throws Exception;

	public List<HistoricoVO> consultarHistoricoCalculoCoeficienteRendimento(String matricula);

	void verificarMatriculaDohistoricoSituacaoTransferidoERealizarAproveitamentoDisciplinaParaCursoTransferido(HistoricoVO histVO, UsuarioVO usuarioVO) throws Exception;

	public List<HistoricoVO> consultaHistoricoParaFechamentoNotasPorBlackboard(BlackboardFechamentoNotaOperacaoVO obj, FiltroRelatorioAcademicoVO filtroRelatorioAcademico, UsuarioVO usuarioLogado) throws Exception;

	public double consultarPercentualCHIntegralizacaoPorMatriculaGradeCurricular(String matricula, int gradeCurricular, UsuarioVO usuarioVO);
	
	public void incluirHistoricosDeEstagioNaoCriados(MatriculaPeriodoVO ultimaMatriculaPeriodoVO, UsuarioVO usuarioVO);

	public void atualizarHistoricosEstagioCursandoParaAprovado(MatriculaVO matricula);
	
	public List<HistoricoVO> consultarHistoricoEquivalentePorMatriculaMapaEquivalenciaDisciplinaNomeDisciplinaEquivalente(String matricula, Integer mapaEquivalenciaDisciplina, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
}

