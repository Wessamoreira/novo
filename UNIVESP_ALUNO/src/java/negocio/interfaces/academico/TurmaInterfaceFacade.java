package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.academico.RenovarMatriculaControle;
import controle.arquitetura.ControleConsultaTurma;
import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.TurmaAberturaVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.servicos.TurmaRSVO;

/**
 * Interface reponsvel por criar uma estrutura padro de comunidao entre a
 * camada de controle e camada de negcio (em especial com a classe Faade). Com
 * a utilizao desta interface  possvel substituir tecnologias de uma camada
 * da aplicao com mnimo de impacto nas demais. Alm de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negcio, por
 * intermdio de sua classe Faade (responsvel por persistir os dados das
 * classes VO).
 */
public interface TurmaInterfaceFacade {

	public TurmaVO novo() throws Exception;

	public void incluir(TurmaVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(TurmaVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(TurmaVO obj, UsuarioVO usuarioVO) throws Exception;

	public TurmaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultarPorUnidadeEnsinoCursoTurno(Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorIdentificadorTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorIdentificadorTurmaUnidadeEnsinoCursoTurno(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, String nivelEducacional, UsuarioVO usuario) throws Exception;

	public TurmaVO consultarTurmaPorIdentificadorTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarTurmaPorPessoa(Integer codigoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public TurmaVO consultarPorCodigoUnidadeEnsinoIdentificadorTurma(Integer unidadeEnsino, String identificadorTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorNomeCurso(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorNomeCursoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<TurmaVO> consultarPorNomeCursoUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinos, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;

	public List<TurmaVO> consultarPorNomeTurno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorPeriodoLetivoUnidadeEnsinoCursoTurno(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorPeriodoLetivoPorIdentificadorPeriodoLetivoEUnidadeEnsinoCursoTurno(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorPeriodoLetivoUnidadeEnsinoCurso(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List consultarTurmaPorProfessor(Integer codigoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorDisciplinaMatricula(Integer codDisciplina, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorDisciplina(Integer codDisciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarTurmaPorDisciplina(String nome, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

	public List consultarTurmaDoAluno(Integer codigo, Integer codigo2, Boolean somenteMatriculaPeriodoAtiva, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

	public List consultarPorDisciplinaSituacaoNrVagasIncluindoTurmasLotadas(Integer codigo, String string, Integer codigo2, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

	public List consultarPorDisciplinaEquivalenteSituacaoUnidadeEnsinoCurso(Integer codigo, String string, Integer codigo2, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

	// public List<TurmaVO> consultarPorCurso(Integer codigo, boolean b, int
	// nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;
	public List<TurmaVO> consultarPorCurso(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorUnidadeEnsinoCurso(Integer codigo, Integer codigo0, boolean b, int NIVELMONTARDADOS_DADOSMINIMOS, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer turmaOrigem, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer turmaOrigem, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, Integer turmaOrigem, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception;

	public List<TurmaVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception;

	public void carregarDados(TurmaVO obj, UsuarioVO usuario) throws Exception;

	public TurmaVO carregarDadosTurmaAgrupada(TurmaVO obj, UsuarioVO usuario) throws Exception;

	public SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer turma, UsuarioVO usuario) throws Exception;

	public TurmaVO consultaRapidaPorChavePrimariaDadosBasicosTurmaAgrupada(Integer turma, UsuarioVO usuario) throws Exception;

	public void carregarDados(TurmaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorTurno(String valorConsulta, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorTurno(String valorConsulta, Integer turmaOrigem, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaNomeCurso(String valorConsulta, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(Integer codigoPessoa, String semestre, String ano, Boolean buscarTurmasAnteriores, String situacao, Integer unidadeEnsino, Integer curso, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean consultarTurmasEAD, PeriodicidadeEnum periodicidadeEnum , Boolean consultaTurmaForum, Integer disciplina) throws Exception;
	
	public List<TurmaVO> consultaRapidaNomeCurso(String valorConsulta, Integer turmaOrigem, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	// public TurmaVO carregarDadosTurmaAgrupada(TurmaVO obj) throws Exception;
	public TurmaVO consultaRapidaPorIdentificadorTurma(TurmaVO obj, String identificador, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void executarInicializacaoDisciplinasTurma(TurmaVO turmaVO, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorDisciplina(Integer codDisciplina, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorDisciplina(Integer codDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorDisciplinaMatricula(Integer codDisciplina, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaTurmaPorProfessor(Integer codigoPessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public TurmaVO consultarPorChavePrimaria(Integer codTurma, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamento(Integer codigoTurma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorPeriodoLetivoUnidadeEnsinoCursoTurno(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurno(Integer nrPeridoLetivo, Integer unidadeEnsino, Integer curso, Integer turno, Integer gradeCurricular, boolean novaMatricula, boolean renovandoMatricula, boolean editandoMatricula, boolean trazerApenasTurmaComAulaProgramada, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario,  Boolean trazerApenasTurmasApresentarRenovacaoOnline) throws Exception;

	public List<TurmaVO> consultaRapidaTurmaPorProfessorSemestreAnoSituacao(Integer codigoPessoa, String semestre, String ano, String situacao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, boolean nivelEducacionalPosGraduacao, boolean nivelEducacionalDiferentePosGraduacao) throws Exception;

	public List<TurmaVO> consultaRapidaPorDisciplinaUnidadeEnsino(Integer codDisciplina, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorPeriodoLetivoUnidadeEnsinoCurso2(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorIdentificadorTurmaCodigoCurso(String valorConsultaTurma, Integer codigo, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarTurmaPorProfessorAnoSemestre(Integer codigoProfessor, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorGradeCurricular(Integer gradeCurricular, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorCodigoUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCurso(String identificador, Integer curso, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorUnidadeEnsinoCurso(String valorConsulta, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception;	

	public List<TurmaVO> consultaRapidaPorTurnoCurso(String valorConsulta, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarRapidaUnidadeEnsinoCursoComboBox(Integer codigoUnidadeEnsino, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorPeriodoLetivoGradeCurricularUnidadeEnsinoCurso(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorUnidadeEnsinoCursoTurnoPeriodoLetivo(Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurno, Integer codigoPeriodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public TurmaVO consultarTurmaPorIdentificadorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultar(String campoConsultaTurma, String valorConsultaTurma, Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorCursoOrdenadoPorTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	List<TurmaVO> consultarPorCodigoCurso(Integer codigoUnidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultarPorCodigoNivelEducacionalCurso(Integer valorConsulta, Integer unidadeEnsino, String nivelEducacional, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorNomeTurnoCurso(String valorConsulta, Integer curso, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorCodigoCursoTurno(Integer turma, Integer curso, Integer turno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCursoTurno(String identificador, Integer curso, Integer turno, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCursoTurno(String identificador, Integer curso, Integer turno, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, boolean filtroPorTabelaCurso) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCodigoDisciplina(String nomeTurma, Integer codDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(Integer codigoPessoa, String semestre, String ano, String situacao, Integer unidadeEnsino, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao) throws Exception;
	
	public List<TurmaVO> consultarTurmaPorProfessorAnoSemestreEturmaEadNivelDadosCombobox(Integer codigoPessoa, String identificadorTurma, String semestre, String ano, String situacao, Integer unidadeEnsino, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorCodigoTurmaCursoUnidadeEnsino(Integer turma, Integer curso, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	public TurmaVO consultaRapidaPorIdentificadorTurma(TurmaVO obj, String identificador, Integer curso, Integer unidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Boolean consultarExisteMatriculaVinculadaTurma(TurmaVO turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarExisteSubTurmaCadastradaTurmaPrincipal(Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarExisteTurmaAgrupadaEnvolvendoTurmaPrincipal(Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public TurmaVO consultaRapidaPorMatriculaUltimaMatriculaPeriodo(String matricula, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorCoordenador(Integer coordenador, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean trazerTurmaAgrupada, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void executarAtualizacaoDisciplinaAlunosTurma(Boolean atualizarDisciplinaAlunos, List<MatriculaPeriodoVO> listaMatriculaPeriodoVO, TurmaVO turmaVO,  UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorCoordenadorComMatriculaPeriodoAtiva(Integer coordenador, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorProfessorCoordenador(Integer professor, Integer coordenador, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void adicionarObjTurmaAberturaVOs(TurmaVO turmaVO, TurmaAberturaVO obj) throws Exception;

	public TurmaVO consultarTurmaDoAlunoPorMatriculaPeriodo(Integer matriculaPeriodo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultarQuantidadeTurmasEncerrandoPeriodo(Date dataInicio, Date dataFim);

	public Integer consultarQuantidadeTurmasNaoEncerrandoPeriodo(Date dataInicio, Date dataFim);

	public Double consultarReceitaPrevistaPeriodo(Date dataInicio, Date dataFim);

	public Double consultarDespesaPrevistaPeriodo(Date dataInicio, Date dataFim);

	public Integer consultarQuantidadeNovasTurmasPrevistaPeriodo(Date dataInicio, Date dataFim);

	public boolean verificarTurmaAgrupada(Integer turma) throws Exception;

	public List<TurmaVO> consultarPorTurnoCursoUnidadeEnsino(Integer turno, Integer curso, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public TurmaVO consultaRapidaPorIdentificadorTurmaEspecifico(TurmaVO obj, String identificador, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public TurmaVO consultarTurmaPorIdentificadorTurmaEspecifico(String valorConsulta, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCursoDisciplina(String identificadorTurma, Integer curso, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarTurmaAgrupadaPorProfessorAnoSemestreNivelDadosCombobox(Integer codigoPessoa, String semestre, String ano, String situacao, Integer unidadeEnsino, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean permitirRegistrarAulaRetroativa) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public TurmaVO consultaRapidaPorDisciplinaMatriculaPeriodo(Integer codDisciplina, Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public TurmaVO consultaRapidaPorMatriculaPeriodoDadosContaCorrente(Integer matriculaPeriodo, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorSituacaoTurma(String valorConsulta, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultarPorNomeTurmaCursoEUnidadeEnsino(String valorConsulta, Integer codigoCurso, Integer unidadeEnsinoCodigo, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultarPorCodigoTurmaCursoEUnidadeEnsino(Integer valorConsulta, Integer codigoCurso, Integer unidadeEnsinoCodigo, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorDisciplinaUnidadeEnsinoSituacao(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacaoTurma, boolean visaoAluno, Integer curso, boolean trazerSomenteTurmaComProgramacaoAula, String ano, String semestre, boolean trazerSubTurmaGeral, boolean trazerSomenteTurmaAulaNaoOcorreu, boolean trazerApenasTurmaReposicao, Boolean trazerSomenteTurmaComTutoriaOnline, boolean controlarAcesso, UsuarioVO usuario, Boolean validarAulaProgramadaCursoIntegral) throws Exception ;
	
	public boolean consultaSeExisteTurmaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacaoTurma, boolean visaoAluno, Integer curso, Integer matrizCurricular, boolean trazerSomenteTurmaComProgramacaoAula, String ano, String semestre, boolean trazerSubTurmaGeral, boolean trazerSomenteTurmaAulaNaoOcorreu, boolean trazerApenasTurmaReposicao, Boolean trazerSomenteTurmaComTutoriaOnline, boolean controlarAcesso, UsuarioVO usuario, Boolean validarAulaProgramadaCursoIntegral,Boolean apresentarRenovacaoOnline, MatriculaVO matriculaVO) throws Exception;
	
	public List<TurmaVO> consultaTurmaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacaoTurma, boolean visaoAluno, Integer curso, Integer matrizCurricular, boolean trazerSomenteTurmaComProgramacaoAula, String ano, String semestre, boolean trazerSubTurmaGeral, boolean trazerSomenteTurmaAulaNaoOcorreu, boolean trazerApenasTurmaReposicao, Boolean trazerSomenteTurmaComTutoriaOnline, boolean controlarAcesso, UsuarioVO usuario, Boolean validarAulaProgramadaCursoIntegral , Boolean apresentarRenovacaoOnline, MatriculaVO matriculaVO) throws Exception;
	
	public List<TurmaVO> consultaRapidaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacaoTurma, boolean visaoAluno, Integer curso,  Integer turno, boolean trazerSomenteTurmaComProgramacaoAula, String ano, String semestre, boolean trazerSubTurmaGeral, boolean trazerSomenteTurmaAulaNaoOcorreu, boolean trazerApenasTurmaReposicao, Boolean trazerSomenteTurmaComTutoriaOnline, boolean controlarAcesso, UsuarioVO usuario, Boolean validarAulaProgramadaCursoIntegral, Boolean apresentarRenovacaoOnline, MatriculaVO matriculaVO) throws Exception ;
	
	List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultarRapidaPorNomeCurso(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCurso(String valorConsulta, Integer unidadeEnsino, Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultaTurmaDoProfessor(Integer professor, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	TurmaVO consultaRapidaPorIdentificadorTurmaEspecificoProfessorUnidadeEnsino(TurmaVO obj, String identificador, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorDisciplinaDataAula(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorDisciplinaEquivalenteUnidadeEnsino(Integer codDisciplina, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamentoPossuemAlunos(Integer codigoTurma, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;	

	public Boolean consultarLiberarRegistroAulaEnterPeriodoTurmaAgrupada(Integer turma, UsuarioVO usuarioVO);

	List<TurmaVO> consultarPorNomeCursoUnidadeEnsinoCursoTurno(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	TurmaVO consultaRapidaPorIdentificadorTurmaUnicoCursoTurno(String identificador, Integer curso, Integer turno, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultaRapidaPorCoordenadorAnoSemestre(Integer coordenador, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean trazerTurmaAgrupada, Boolean buscarTurmasAnteriores, String ano, String semestre, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaNivelEducacional(String identificador, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, String nivelEducacional, List<TurnoVO> turnoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorUnidadeEnsinoNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorTurnoNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaNomeCursoNivelEducacional(String valorConsulta, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, String nivelEducacional, List<TurnoVO> turnoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarNotasPorCursoTurma(Integer codigoTurma) throws Exception;

	public Boolean consultarTurmaAgrupadaPorCodigoTurma(Integer codigoTurma) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCoordenador(Integer coordenador, String identificadorTurma, Integer unidadeEnsino, boolean controlarAcesso, boolean buscarApenasPorSituacaoAberto, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultarPorSubTurma(TurmaVO turma, Integer disciplina, boolean subturma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, TipoSubTurmaEnum tipoSubTurma, Boolean trazerApenasTurmaComProgramacaoAula, String ano, String semestre, String situacao) throws Exception;

	String consultarPeriodicidadePorCodigoTurma(Integer turma) throws Exception;

	public Boolean consultarLiberarRegistroAulaEntrePeriodoConsiderandoTodosCursosTurmaAgrupada(TurmaVO turmaVO) throws Exception;

	List<TurmaVO> consultaRapidaPorIdentificadorTurmaSubturma(String identificador, Integer turmaOrigem, boolean subturma, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultaRapidaPorUnidadeEnsinoSubturma(String valorConsulta, Integer turmaOrigem, boolean subturma, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultaRapidaPorTurnoSubturma(String valorConsulta, Integer turmaOrigem, boolean subturma, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultaRapidaNomeCursoSubturma(String valorConsulta, Integer turmaOrigem, boolean subturma, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public TurmaVO consultaRapidaPorMatriculaUltimaMatriculaPeriodoPorAnoSemestrePeriodoLetivo(String matricula, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultarTurmaPorDisciplinaMatriculaPeriodoTransferenciaTurma(Integer codDisciplina, Integer matriculaPeriodo, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public TurmaDisciplinaVO obterLocalSalaTurmaDisciplinaLog(TurmaDisciplinaVO turmaDisc, Integer turma) throws Exception;

	List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer turmaOrigem, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean trazerSubturma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	TurmaVO consultarTurmaPorIdentificadorTurmaSubturma(String identificador, Integer turmaOrigem, boolean subturma, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public String verificaQtdAlunoAtivoPreReposicao(Integer turma, Integer gradeDisciplina, Integer codDisciplina) throws Exception;

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsinoECurso(String identificador, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorNomeUnidadeEnsinoCodigoUnidadeEnsinoTurmaPrincipalECodigoCursoTurmaPrincipal(String valorConsulta, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaNomeCursoPorCodigoUnidadeEnsinoTurmaPrincipalECodigoCursoTurmaPrincipal(String valorConsulta, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TurmaVO> consultaRapidaPorTurnoPorCodigoUnidadeEnsinoTurmaPrincipalECodigoCursoTurmaPrincipal(String valorConsulta, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void alterarDataPrevisaoFinalizacao(Integer turma, Date data, UsuarioVO usuario) throws Exception;
	
	public void alterarDataBaseGeracaoTurmaParcela(final TurmaVO turma, Date dataBaseGeracaoParcelas, List<MatriculaVO> listaMatriculaComControleGeracaoParcelaDataBase, UsuarioVO usuario) throws Exception;

	List<TurmaVO> consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(Integer codigoPessoa, String semestre, String ano, Boolean buscarTurmasAnteriores, String situacao, Integer unidadeEnsino, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean consultarTurmasEAD) throws Exception;

	public List<TurmaVO> consultarTurmasEADProfessor(Integer codigoPessoa, Integer unidadeEnsino, Integer curso,  PeriodicidadeEnum periodicidadeEnum) throws Exception ;

	List<TurmaVO> consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurnoDisciplina(Integer nrPeridoLetivo, Integer unidadeEnsino, Integer curso, Integer turno, Integer gradeCurricular, Integer disciplina, boolean novaMatricula, boolean renovandoMatricula, boolean editandoMatricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 27/05/2015
	 * @param valorConsulta
	 * @param unidadeEnsino
	 * @param curso
	 * @param turno
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurnoConsiderandoTurmaAgrupada(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void processarDadosPermitinentesTurmaSelecionada(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuarioVO) throws Exception;
	
	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCursoPeriodicidade(String identificador, Integer curso, Integer unidade, String periodicidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	/**
	 * @author Rodrigo Wind - 17/10/2015
	 * @param usuarioVO
	 * @param semestre
	 * @param ano
	 * @param buscarTurmasAnteriores
	 * @param unidadeEnsino
	 * @return
	 * @throws Exception
	 */
	List<TurmaVO> consultarTurmaComAtividadeDiscursivaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(UsuarioVO usuarioVO, String semestre, String ano, Boolean buscarTurmasAnteriores, Integer unidadeEnsino) throws Exception;

	/**
	 * @author Rodrigo Wind - 20/11/2015
	 * @param campoConsulta
	 * @param valorConsulta
	 * @param situacaoTurma
	 * @param tipoTurma
	 * @param unidadeEnsino
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<TurmaVO> consultarTurmaProgramacaoAula(String campoConsulta, String valorConsulta, String situacaoTurma, String tipoTurma, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

		public Integer consultaCargaHorariaRapidaPorDisciplinaTurmaDataAula(Integer turma, Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
		
		List<TurmaVO> consultaRapidaPorIdentificadorTurmaCurso(String identificador, List<CursoVO> cursos, List<UnidadeEnsinoVO> unidades, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		List<TurmaVO> consultaRapidaPorUnidadeEnsinoCurso(String valorConsulta, List<CursoVO> cursos, List<UnidadeEnsinoVO> unidades, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		List<TurmaVO> consultaRapidaPorTurnoCurso(String valorConsulta, List<CursoVO> cursos, List<UnidadeEnsinoVO> unidades, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		List<TurmaVO> consultaRapidaNomeCurso(String valorConsulta, List<UnidadeEnsinoVO> unidades, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		/**
		 * @author Rodrigo Wind - 19/01/2016
		 * @param turmaPrincipal
		 * @param disciplina
		 * @param disciplinaFazParteComposicao
		 * @param tipoSubturma
		 * @param ano
		 * @param semestre
		 * @param considerarVagaPreMatricula
		 * @param considerarSubTurmaAgrupada
		 * @param considerarApenasTurmaAulaProgramada
		 * @return
		 */
		StringBuilder getSqlConsultaSubTurmaParaDistribuicaoAutomatica(Integer turmaPrincipal, Integer disciplina, Boolean disciplinaFazParteComposicao, TipoSubTurmaEnum tipoSubturma, String ano, String semestre, Boolean considerarVagaPreMatricula, Boolean considerarSubTurmaAgrupada, Boolean considerarApenasTurmaAulaProgramada, String matriculaDesconsiderar, Boolean considerarVagasReposicao) throws Exception;

		/**
		 * @author Rodrigo Wind - 21/01/2016
		 * @param matriculaPeriodoTurmaDisciplinaVO
		 * @param ano
		 * @param semestre
		 * @param considerarAlunosPreMatriculados
		 * @throws Exception
		 */
		void consultarNumeroVagasDisponivelParaMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, DisciplinaVO disciplina, String ano, String semestre, Boolean considerarAlunosPreMatriculados, Boolean considerarVagasReposicao) throws Exception;

		/** 
		 * @author Wellington - 22 de jan de 2016 
		 * @param turmaPrincipal
		 * @param disciplina
		 * @param disciplinaFazParteComposicao
		 * @param tipoSubturma
		 * @param ano
		 * @param semestre
		 * @param nivelMontarDados
		 * @param usuarioVO
		 * @return
		 * @throws Exception 
		 */
		List<TurmaVO> consultarSubturmasRealizarTransferencia(Integer turmaPrincipal, Integer disciplina, Boolean disciplinaFazParteComposicao, TipoSubTurmaEnum tipoSubturma, String ano, String semestre, Boolean considerarVagaReposicao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
		
 
		public List<TurmaVO> consultaRapidaNomeCurso(String valorConsulta, List<CursoVO> cursoVOs,Integer turmaOrigem, List<UnidadeEnsinoVO> unidadeVOs, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		/** 
		 * @author Wellington - 9 de mar de 2016 
		 * @param turma
		 * @param disciplina
		 * @param controlarAcesso
		 * @param usuario
		 * @return
		 * @throws Exception 
		 */
		boolean consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

		/** 
		 * @author Wellington - 10 de mar de 2016 
		 * @param turmaVO
		 * @param turmaDisciplinaVOs
		 * @param usuarioVO
		 * @throws Exception 
		 */
		void executarVerificacaoTurmaDisciplinaManterAtualizacaoDisciplina(TurmaVO turmaVO, List<TurmaDisciplinaVO> turmaDisciplinaVOs, boolean verificarDisciplinasExcluir, UsuarioVO usuarioVO) throws Exception;

		/** 
		 * @author Wellington - 10 de mar de 2016 
		 * @param turmaVO
		 * @param gradeDisciplinaVOs
		 * @param usuarioVO
		 * @throws Exception 
		 */
		void executarGeracaoTurmaDisciplinaVOs(TurmaVO turmaVO, List<GradeDisciplinaVO> gradeDisciplinaVOs, UsuarioVO usuarioVO) throws Exception;

		/** 
		 * @author Wellington - 10 de mar de 2016 
		 * @param turmaVO
		 * @param obj
		 * @param usuarioVO
		 * @throws Exception 
		 */
		void executarGeracaoTurmaDisciplinaGradeCurricularGrupoOptativaDisciplina(TurmaVO turmaVO, GradeCurricularGrupoOptativaDisciplinaVO obj, UsuarioVO usuarioVO) throws Exception;

		/** 
		 * @author Wellington - 10 de mar de 2016 
		 * @param tdVO
		 * @param turmaVO
		 * @param usuarioVO
		 * @return
		 * @throws Exception 
		 */
		boolean executarVerificacaoDisciplinaCursandoEProgramacaoAula(TurmaDisciplinaVO tdVO, TurmaVO turmaVO, UsuarioVO usuarioVO) throws Exception;

		public List<TurmaVO> consultaRapidaNivelComboboxPorListaUnidadeEnsinoIdentificadorTurma(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String identificadorTurma, UsuarioVO usuario) throws Exception;
		
		public List<TurmaVO> consultaRapidaNivelComboboxPorNomeCursoListaUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception;

		/**
		 * @author Rodrigo Wind - 13/05/2016
		 * @param turma
		 * @param codDisciplina
		 * @param ano
		 * @param semestre
		 * @param situacaoTurma
		 * @return
		 * @throws Exception
		 */
		Boolean consultarExistenciaAulaProgramadaTurmaConsiderandoTurmaAgrupada(Integer turma, Integer codDisciplina, String ano, String semestre, String situacaoTurma) throws Exception;
		
		List<TurmaVO> consultaRapidaNivelComboboxPorForum(ForumVO forum, UsuarioVO usuario) throws Exception;
		
		public List<TurmaVO> consultaRapidaNomeCursoSituacaoTruma(String valorConsulta, Integer turmaOrigem, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, String situacao,UsuarioVO usuario) throws Exception;
		
		/**
		 * @author Carlos Eugnio - 29/09/2016
		 * @param identificador
		 * @param curso
		 * @param unidade
		 * @param controlarAcesso
		 * @param nivelMontarDados
		 * @param usuario
		 * @return
		 * @throws Exception
		 */
		List<TurmaVO> consultaRapidaPorUnidadeIdentificadorTurmaCurso(String identificador, Integer curso, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		void alterarSituacaoTurma(TurmaVO obj, UsuarioVO usuario) throws Exception;
		
		void validarDadosRemocaoTurmaAgrupada(TurmaVO turmaVO, TurmaAgrupadaVO turmaAgrupadaVO, UsuarioVO usuarioVO)
				throws ConsistirException;
		
		public List<TurmaVO> consultaRapidaTurmasDRE(List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO cursoVO, TurmaVO turmaParamVO, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception;
		
		public Double consultaRapidaMatriculaDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception;
		public Double consultaRapidaMensalidadeDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception;
		public Double consultaRapidaTributosDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal)  throws Exception;
		public Double consultaRapidaDescontosDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal)  throws Exception;
		public Double consultaRapidaCancelamentosDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal)  throws Exception ;
		public Double consultaRapidaReceitaLiquidaDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal)  throws Exception ;
//		public Double consultaRapidaCustosDespesaVariavelDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal)  throws Exception ;
		public Double consultaRapidaMargemContribuicaoDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal)  throws Exception ;
		public Double consultaRapidaDespesasFixasDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal)  throws Exception ;
		public Double consultaRapidaCategoriaDespesaDRE(Integer turma, String identificadorCategoriaDespesa, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal)  throws Exception;
		public TurmaVO consultaRapidaDadosIntegracao(TurmaVO turma) throws Exception;
		Boolean consultarExistenciaTurmaVinculadaIndiceReajustePreco(Integer indiceReajuste, UsuarioVO usuarioVO);
		public List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsinoPeriodicidadeCurso(String identificador, Integer unidadeEnsino, String periodicidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		List<TurmaVO> consultarPorIdentificadorTurmaUnidadeEnsinoCondicaoRenegociacao(String valorConsulta,  Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		public List<TurmaVO> consultarTurmaPorCursoVOsEUnidadeEnsinoVOs(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, UsuarioVO usuario) throws Exception;

		void realizarAtualizacaoGradeCurricularCursoIntegral(TurmaVO turmaAtual, TurmaVO turmaNovaGradeCurricular,  UsuarioVO usuarioVO, List<MatriculaPeriodoVO> listaMatriculaPeriodo);
		
		public Integer consultaRapidaTotalRegistroPorUnidadeEnsinoCurso(String valorConsulta, Integer curso, Integer unidadeEnsino) throws Exception;
		
		
		void getSQLPadraoJoinCursoTurma(String join, String turma, String curso, StringBuilder stringBuilder);
		
		public TurmaVO clonar(TurmaVO turmaVO, UsuarioVO usuarioLogado) throws Exception;	
		
		public void inicializarDadosUnidadeEnsinoSelecionada(TurmaVO turmaVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuarioVO);

		public void persistirTurmaClone(TurmaVO turmaVO, UsuarioVO usuarioLogado) throws Exception;
		public void consultarTurmaBaseVinculadaTurmaBase(TurmaVO turmaVO, UsuarioVO usuarioVO) throws Exception;
		
		void persistirAlteracaoUnidadeEnsinoTurma(TurmaVO turmaVO,UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioVO, Boolean permitirAgruparTurmasUnidadeEnsinoDiferente)  throws Exception;
		
		List<TurmaVO> consultarTurmaPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuarioVO);
		
		public void montarListaApresentacaoTurmaAgrupada(Map<Integer, List<TurmaVO>> mapTurmas, List<TurmaVO> turmaVOs, List<TurmaVO> listaTurmaIncluir, UsuarioVO usuarioVO) throws Exception;

		public List<TurmaVO> consultarPorUnidadeEnsinoCurso(String campoConsulta, String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, UsuarioVO usuarioVO) throws Exception;

		List<TurmaVO> consultarPorUnidadeEnsinoIdentificadorTurma(Integer integer, String identificadorTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		List<TurmaVO> consultarPorListaUnidadeEnsinoIdentificadorTurma(List<UnidadeEnsinoVO> unidadeEnsinos, String identificadorTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		public List consultaRapidaResumidaPorIdentificador(ControleConsulta controleConsulta, DataModelo controleConsultaOtimizado, List<UnidadeEnsinoVO> unidadeEnsinoVOs, int nivelmontardadosDadosminimos, UsuarioVO usuarioLogado) throws Exception;
		
		public List<TurmaVO> consultarPorIdentificadorTurmaUnidadeEnsinoCursoTurno(String valorConsulta, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, String nivelEducacional, UsuarioVO usuario) throws Exception;
		
		public List<TurmaVO> consultaPorCoordenadorParametizada(Integer coordenador, String valorConsulta, String campoConsulta, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean trazerTurmaAgrupada, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

		StringBuilder getSqlConsultaCriacaoTurmaSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(
				Integer unidadeensino, Integer curso, Integer turno, Integer gradeCurricular, Integer periodoLetivo,
				String ano, String semestre, Integer planofinanceirocurso)
				throws Exception;

		TurmaVO realizarCriacaoIdentificadorTurmaPadraoSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(
				Integer unidadeensino, Integer curso, Integer turno, Integer gradeCurricular, Integer periodoLetivo,
				String ano, String semestre, Integer planofinanceirocurso, boolean controlarAcesso, UsuarioVO usuario)
				throws Exception;

		StringBuilder getSqlInsertCriacaoTurmaSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(Integer unidadeensino, Integer curso, Integer turno, Integer gradeCurricular, Integer periodoLetivo,
				String ano, String semestre, Integer planofinanceirocurso) throws Exception;

		TurmaVO realizarCriacaoTurmaPadraoSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(
				MatriculaVO matriculaVO , MatriculaPeriodoVO matriculaPeriodoVO , boolean controlarAcesso, UsuarioVO usuario)
				throws Exception;

		List<TurmaRSVO> consultarTurmaPorPeriodoLetivoUnidadeEnsinoCursoTurnoParaMatriculaOnlineProcessoSeletivo(Integer aluno,
				Integer codigoUnidadeEnsino, Integer codigoPeriodoLetivo, Integer numeroPeriodoletivo,
				Integer codigoCurso, Integer codigoTurno, Integer codigoGradeCurricular,
				 RenovarMatriculaControle renovarMatriculaControle, UsuarioVO usuarioVO );

		void consultarTurma(ControleConsultaTurma controleConsultaTurma, Integer unidadeEnsino,
				List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, List<CursoVO> cursoVOs, Integer turno,
				List<TurnoVO> turnoVOs, Integer gradeCurricular, Integer codDisciplina, PeriodicidadeEnum[] periodicidadeEnuns, TipoNivelEducacional[] nivelEducacionalEnuns, UsuarioVO usuarioVO) throws Exception;
		void consultarTurma(ControleConsultaTurma controleConsultaTurma, Integer unidadeEnsino,
				List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, List<CursoVO> cursoVOs, Integer turno,
				List<TurnoVO> turnoVOs, Integer gradeCurricular, Integer codDisciplina,  UsuarioVO usuarioVO) throws Exception;

		void consultarTurma(ControleConsultaTurma controleConsultaTurma, Integer unidadeEnsino,
				List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, List<CursoVO> cursoVOs, Integer turno,
				List<TurnoVO> turnoVOs, Integer gradeCurricularVO, Integer codDisciplina,
				PeriodicidadeEnum[] periodicidadeEnuns, TipoNivelEducacional[] nivelEducacionalEnuns,
				List<TurmaVO> turmasDesconsiderar, UsuarioVO usuarioVO) throws Exception;

		public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCursoPorUnidadeEnsino(String identificador, Integer curso, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		
}

