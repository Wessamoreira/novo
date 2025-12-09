/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoValidacaoChoqueHorarioEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 *
 * @author rodrigo
 */
public interface HorarioTurmaInterfaceFacade {

//    public HorarioTurmaVO novo() throws Exception;

    public void incluir( UsuarioVO usuario) throws Exception;

    public void alterar( boolean zerarListaConcorrencia, UsuarioVO usuario) throws Exception;

    public void excluir( UsuarioVO usuario) throws Exception;

//    public HorarioTurmaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//
//    public List<HorarioTurmaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//
//    public List<HorarioTurmaVO> consultarPorIdentificadorTurmaTurma(String valorConsulta, Integer unidadeEnsino, String semestreVigente, String anoVigente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//
//    public List<HorarioTurmaVO> consultarPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//
//    public HorarioTurmaVO consultarPorCodigoTurmaUnico(Integer codigoTurma, String semestreVigente, String anoVigente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//
//    public List<HorarioTurmaVO> consultarHorarioTurmaPelaMatriculaAluno(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//
//    public List<HorarioTurmaVO> consultarHorarioTurmaPeloCodigoTurmaTrazendoTurmaAgrupada(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

//    public void inicializarDadosProgramarAulaDiaDiaAvulso(HorarioTurmaVO horarioTurmaVO) throws ConsistirException;

//    public HorarioProfessorVO executarAtualizacaoDisponibilidadeHorarioProfessorProgramacaoAulaDiaADiaAvulso(HorarioTurmaVO horarioTurmaVO, UsuarioVO usuario) throws ConsistirException, Exception;

//    public Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> executarAdicionarDadosHorarioTurmaProfessor(HorarioTurmaVO horarioTurmaVO, HorarioTurmaDiaVO horarioTurmaDiaVO, Boolean avulso, HorarioProfessorVO horarioProfessorVO, Boolean validarHorarioDiaPeriodo, UsuarioVO usuario, boolean controlaNumeroMaximoAulaProgramadaProfessorDia, Integer numeroAulaMaximaProgramarProfessorDia, boolean liberarProgramacaoAulaProfessorAcimaPermitido) throws Exception;

//    public HorarioProfessorVO inicializarDadosProgramarAulaDiaDiaPeriodo(HorarioTurmaVO horarioTurmaVO, UsuarioVO usuario) throws ConsistirException, Exception;

    public void setIdEntidade(String aIdEntidade);
    
//    public List<PessoaVO> executarMontagemListaProfessorComAulaProgramadaTurma(HorarioTurmaVO horarioTurmaVO);

//    public List<HorarioTurmaDiaVO> inicializarDadosHorarioTurmaDiaVOPorProfessor(Integer professor, Integer disciplina, HorarioTurmaVO horarioTurmaVO) throws Exception;

    public void inicializarDadosHorarioTurmaPorTurma( boolean montarDataCalendarioSemProgramacaoAula, UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuario) throws Exception;
   
//    public HorarioTurmaVO consultarPorHorarioTurmaPorIdentificadorTurmaTurma(String valorConsulta, Integer unidadeEnsino, String semestreVigente, String anoVigente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//
//    public HorarioTurmaVO consultarPorHorarioTurmaPorIdentificadorTurmaTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void inicializarDadosHorarioTurmaPorHorarioTurma( boolean montarDataCalendarioSemProgramacaoAula, UsuarioVO usuario) throws Exception;

    public List<Integer> consultarProfessorLecionaDisciplina( Integer codigoDisciplina) throws Exception;

    public Boolean realizarVerificacaoProfessorLecionaAlgumaDisciplina(Integer pessoa);

//    public List<HorarioTurmaVO> consultarPorData(Date data, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//
//    public List<HorarioTurmaVO> consultarPorTurmaDisciplinaAnoSemestre(int codigoTurma, int codigoDisciplina, String anoVigente, String semestreVigente, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//
//    public List<HorarioTurmaVO> consultarPorIdentificadorTurmaTurmaMontagemCompleta(String valorConsulta, Integer unidadeEnsino, String semestreVigente, String anoVigente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//
//    public List<HorarioTurmaVO> consultarTodos(int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void executarMigracaoDados(UsuarioVO usuario) throws Exception;

//    public List<HorarioTurmaVO> consultaRapidaPorIdentificadorTurmaTurma(String valorConsulta, Integer unidadeEnsino, String semestreVigente, String anoVigente, String situacaoTurma, String tipoTurma, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
//
//    public HorarioTurmaVO consultarHorarioTurmaPorCodigo(Integer codigoPrm, boolean acesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//
//    public HorarioTurmaVO consultarPorCodigoTurmaAgrupadaDisciplinaUnico(Integer turma, Integer disciplina, String semestreVigente, String anoVigente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void realizarEnvioEmailAlunosTurmaPadrao( PersonalizacaoMensagemAutomaticaVO msg, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean enviarPorEmail) throws Exception;

	void realizarEnvioEmailAlunosInclusaoDisciplina( PersonalizacaoMensagemAutomaticaVO msg, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean enviarPorEmail, Map<Integer, String> hashDisciplinasAlteradas) throws Exception;

	Date consultarPrimeiroDiaAulaTurmaDisciplina(int turma, Integer disciplina, String ano, String semeste) throws Exception;

//	public List<HorarioTurmaDisciplinaSemanalVO> consultarHorarioTurmaSemanalPorTurmaDisciplina(Integer turma, Integer disciplina, String ano, String semestre, UsuarioVO usuarioVO);

	void inicializarListaHorarioTurmaDisciplinaProgramada( boolean permitirProgramacaoAulaComClassroom, boolean permitirProgramacaoAulaComBlackboard, UsuarioVO usuarioVO) throws Exception;

//	List<HorarioTurmaDisciplinaProgramadaVO> consultarHorarioTurmaDisciplinaProgramadaPorTurma(Integer turma, boolean trazerDisciplinaCompostaPrincipal, Boolean permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica, Integer horarioTurma) throws Exception;

	void carregarDadosHorarioTurma( Integer professor, Integer disciplina, UsuarioVO usuario) throws Exception;

//	Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> realizarExclusaoHorarioTurmaPorProfessorDisciplina(HorarioTurmaVO horarioTurmaVO, HorarioTurmaDiaVO horarioTurmaDiaVO, Integer professor, Integer disciplina, int numeroAula, boolean alterarTodasAulas, UsuarioVO usuario, boolean retornarExecaoRegistroAulaLancada) throws Exception;

	void realizarCriacaoCalendarioHorarioTurma( UsuarioVO usuario) throws Exception;

//	Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> executarVerificarDisponibilidadeHorarioTurmaEHorarioProfessor(HorarioTurmaVO horarioTurmaVO, Boolean avulso, HorarioProfessorVO horarioProfessorVO, UsuarioVO usuario, Boolean retornarExcecao, boolean controlaNumeroMaximoAulaProgramadaProfessorDia, Integer numeroAulaMaximaProgramarProfessorDia, boolean liberarProgramacaoAulaProfessorAcimaPermitido) throws Exception;
//
//	Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> executarAlteracaoAulaHorarioTurmaProfessorDisciplina(HorarioTurmaVO horarioTurmaVO, HorarioTurmaDiaVO horarioTurmaDiaVO, int numeroAula, boolean alterarTodasAulas, UsuarioVO usuario, boolean retornarExecaoRegistroAulaLancada, boolean controlaNumeroMaximoAulaProgramadaProfessorDia, Integer numeroAulaMaximaProgramarProfessorDia, boolean liberarProgramacaoAulaProfessorAcimaPermitido, FuncionarioCargoVO funcionarioCargoVO) throws Exception;

	StringBuilder getSqlConsultaCompleta(Integer horarioTurma, Integer turma, String identificadorTurma, String ano, String semestre, Integer professor, Integer disciplina, Date dataInicio, Date dataFim, Integer unidadeEnsino);

	String getSqlOrdenarConsultaCompleta();

	Boolean validarDadosChoqueHorarioTurmaComHorarioProfessor(int minutosInicioTurnoADarAula, int minutosInicioTurno, int minutosFinalTurno, int minutosFinalTurnoADarAula, Date dataBase, Integer numeroAulaBase, String horarioBase, PessoaVO professorBase, DisciplinaVO disciplinaBase, UsuarioVO usuario, boolean retornarExcecao) throws Exception;

//	List<DisciplinaVO> realizarObtencaoDisciplinaLecionadaProfessor(HorarioTurmaVO horarioTurmaVO, Integer professor);
//
//	List<Date> realizarObtencaoDataSeremAlteradoExcluido(HorarioTurmaVO horarioTurmaVO, Date dataBase, boolean alterarTodasAulas, int professor, Integer disciplina, List<RegistroAulaVO> registroAulaVOs);

	public Date consultarUltimaDataAulaPorMatriculaConsiderandoReposicao(Integer codMptd) throws Exception;
		
	public Date consultarUltimaDataAulaPorMatriculaConsiderandoReposicao(String matricula) throws Exception ;
	
//	public void alterarObservacao(UsuarioVO usuario) throws Exception;

	Map<String, Date> consultarPeriodoAulaDisciplinaPorTurmaDisciplinaAnoSemestre(Integer turma, Integer disciplina, String ano, String semestre) throws Exception;
	
//	public List<HorarioTurmaDisciplinaProgramadaVO> realizarVerificacaoExisteAulaProgramadaParaDisciplinaEEquivalenteParaOutraTurmaOuTurmaAgrupada(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, Boolean retornarExcecao) throws Exception ;

	/**
	 * @author Carlos Eugênio - 27/08/2015
	 * @param horarioTurmaVO
	 * @param disciplinaBase
	 * @param dataBase
	 * @param numeroAula
	 * @param usuario
	 * @param retornarExcecao
	 * @return
	 * @throws Exception
	 */
	Boolean realizarVerificacaoChoqueHorarioTurmaDiaItemComHorarioTurmaOutroAnoSemestre( DisciplinaVO disciplinaBase, Date dataBase, int numeroAula, UsuarioVO usuario, boolean retornarExcecao) throws Exception;

	/**
	 * @author Carlos Eugênio - 27/08/2015
	 * @param horarioTurmaVO
	 * @param disciplina
	 * @param dataBase
	 * @param disponibilidadeHorarioTurmaProfessorVO
	 * @param horarioProfessorVO
	 * @param usuario
	 * @param retornarExececao
	 * @return
	 * @throws Exception
	 */
	Boolean realizarVerificacaoChoqueHorarioProfessorPorDisponibilidadeHorarioTurmaProfessorVO( DisciplinaVO disciplina, Date dataBase,  UsuarioVO usuario, boolean retornarExececao) throws Exception;

	/**
	 * @author Carlos Eugênio - 27/08/2015
	 * @param horarioTurmaVO
	 * @param dataBase
	 * @param disponibilidadeHorarioTurmaProfessor
	 * @param retornarExcecao
	 * @return
	 * @throws Exception
	 */
	Boolean executarVerificarDisponibilidadeHorarioMarcadoTurmaPorDia( Date dataBase, boolean retornarExcecao) throws Exception;

	/**
	 * @author Rodrigo Wind - 10/09/2015
	 * @param horarioTurmaDiaVO
	 * @param nrAula
	 * @return
	 */
//	HorarioTurmaDiaItemVO realizadaObtencaoHorarioTurmaDiaItemPorNrAula(HorarioTurmaDiaVO horarioTurmaDiaVO, Integer nrAula);

	/**
	 * @author Rodrigo Wind - 10/09/2015
	 * @param horarioTurmaDiaVOs
	 * @param data
	 * @return
	 */
//	HorarioTurmaDiaVO realizadaObtencaoHorarioTurmaDiaPorData(List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, Date data);

	/**
	 * @author Rodrigo Wind - 17/09/2015
	 * @param horarioTurmaVO
	 * @return
	 */
//	List<ProgramacaoAulaResumoSemanaVO> realizarMontagemListaProgramacaoAulaResumoSemanaVO(HorarioTurmaVO horarioTurmaVO);

	/** 
	 * @author Wellington - 19 de out de 2015 
	 * @param unidadeEnsino
	 * @param curso
	 * @param turma
	 * @param disciplina
	 * @param professor
	 * @param ano
	 * @param semestre
	 * @param dataInicio
	 * @param dataFim
	 * @param mes
	 * @param anoMes
	 * @return
	 * @throws Exception 
	 */
	SqlRowSet executarConsultaAulasProgramadasNaoRegistradas(Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, String mes, String anoMes, String periodicidade, String tipoFiltroPeriodo) throws Exception;

	SqlRowSet executarConsultaAulasProgramadasNaoRegistradasRegistroAulaLancadaNaoLancadaRel(Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, String mes, String anoMes, String periodicidade) throws Exception;

	/** 
	 * @author Wellington - 11 de mar de 2016 
	 * @param turma
	 * @param ano
	 * @param semestre
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	Date consultarUpdatedPorTurmaAnoSemestre(Integer turma, String ano, String semestre, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	/**
	 * @author Rodrigo Wind - 11/03/2016
	 * @param turma
	 * @param disciplina
	 * @param ano
	 * @param semestre
	 * @param trazerDisciplinaCompostaPrincipal
	 * @return
	 * @throws Exception
	 */
//	List<HorarioTurmaDisciplinaProgramadaVO> consultarHorarioTurmaDisciplinaProgramadaPorTurmaEDisciplinaTrazendoSala(Integer turma, Integer disciplina, String ano, String semestre, boolean trazerDisciplinaCompostaPrincipal) throws Exception;

	/** 
	 * @author Wellington - 11 de mar de 2016 
	 * @param horarioTurma
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
//	void alterarUpdatedPorCodigo(HorarioTurmaVO horarioTurma, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
//
//	public HorarioTurmaVO consultarPorTurmaDisciplina(Integer turma, Integer disciplina) throws Exception;

	/**
	 * Realiza a consulta dos cursos que tiveram alteracao em seus registros nas ultimas horas @param
	 * @param horas
	 * @return
	 */
	public SqlRowSet consultarCursosQueSofreramAlteracao(Integer horas) throws Exception;
	
	
	/**
	 * Consulta as matriculas dos alunos as quais algum dado foi alterado no intervalo de horas informado
	 * @param horas
	 * @return
	 */
	public SqlRowSet consultarVinculacaoAlunoAlterado(Integer horas) throws Exception;

	/**
	 * Retorna todos os agendamentos de professores os quais algum dado foi alterado no intervalo de horas informado
	 * 
	 * @param horas
	 * @return
	 */
	public SqlRowSet consultarVinculacaoProfessorAlterado(Integer horas) throws Exception;
	
	
	/**
	 * Retorna todas as vinculacoes de coordenadores as quais algum dado foi alterado no intervalo de horas informado
	 * @param horas
	 * @return
	 */
	public SqlRowSet consultarVinculacaoCoordenador(Integer horas) throws Exception;
	
//	public List<HorarioTurmaVO> consultarPorUnidadeEnsino(Integer unidadeEnsino) throws Exception;

	public SqlRowSet consultarVinculacaoProfessorRemover(Integer horas);
	
	public void alterarCursosSetandoDataAlteracaoInicial();
	
	public void alterarMatriculasSetandoDataAlteracaoInicial();
	
	public void alterarProfessoresSetandoDataAlteracaoInicial();

	void excluirHorarioTurmaPorAlteracaoGradeCurricularCursoIntegral(TurmaVO turma, DisciplinaVO disciplina, UsuarioVO usuario) throws Exception;

	void alterarHorarioTurmaPorAlteracaoGradeCurricularCursoIntegral(TurmaVO turma, TurmaDisciplinaVO turmaDisciplina, UsuarioVO usuario) throws Exception;

	public void realizarEnvioEmailNotificacaoAlteracaoCronogramaAula(TemplateMensagemAutomaticaEnum templateMensagenAutomatica , UsuarioVO usuarioLogado,
			ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema, Boolean enviarComunicadoPorEmail,Map<Integer, String> hashDisciplinasAlteradas) throws Exception;

	
}