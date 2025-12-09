package negocio.interfaces.academico;

import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
//import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaTCCVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaDisciplinaEstatisticaAlunoVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.PermissaoAcessoMenuVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.ead.ConteudoRegistroAcessoVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
//import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.servicos.MatriculaRSVO;
import webservice.servicos.objetos.DisciplinaRSVO;


public interface MatriculaPeriodoTurmaDisciplinaInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
	 */
	public MatriculaPeriodoTurmaDisciplinaVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>MatriculaPeriodoTurmaDisciplinaVO</code>. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>MatriculaPeriodoTurmaDisciplinaVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluir(MatriculaPeriodoTurmaDisciplinaVO obj,  GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;

	public TurmaDisciplinaVO consultarExistenciaVagaTurmaDisciplina(Integer codigoTurma, Integer disciplina, Integer nrVagas, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>MatriculaPeriodoTurmaDisciplinaVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>MatriculaPeriodoTurmaDisciplinaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterar(MatriculaPeriodoTurmaDisciplinaVO obj,  UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>MatriculaPeriodoTurmaDisciplinaVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da
	 * operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>MatriculaPeriodoTurmaDisciplinaVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(String matricula, Integer codigoDisciplina) throws Exception;

	public void excluir(MatriculaPeriodoTurmaDisciplinaVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de
	 * <code>MatriculaPeriodoTurmaDisciplina</code> através do valor do atributo
	 * <code>Integer Disciplina</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>MatriculaPeriodoTurmaDisciplinaVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDisciplina(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorMatriculaAtiva(String matricula, boolean controlarAcesso, int nivelMontarDados, boolean ordernarConsiderandoAnoSemestreAtual, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de
	 * <code>MatriculaPeriodoTurmaDisciplina</code> através do valor do atributo
	 * <code>identificadorTurma</code> da classe <code>Turma</code> Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>MatriculaPeriodoTurmaDisciplinaVO</code> resultantes da
	 *         consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorIdentificadorTurmaTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorCodigoTurmaDisciplinaSemestreAno(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, boolean diferentePendenteFinanceiramente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	

	/**
	 * Responsável por realizar uma consulta de
	 * <code>MatriculaPeriodoTurmaDisciplina</code> através do valor do atributo
	 * <code>Integer matriculaPeriodo</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>MatriculaPeriodoTurmaDisciplinaVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorMatriculaPeriodo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaPeriodoDisciplinaSemestreAno(Integer valorConsulta, Integer disciplina, String semestre, String ano, boolean diferentePendenteFinanceiramente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, String situacaoMatricula, boolean diferenteAprovadoPorAproveitamento, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Date dataInicioPeriodoMatricula, Date dataFimPeriodoMatricula, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	/**
	 * Responsável por realizar uma consulta de
	 * <code>MatriculaPeriodoTurmaDisciplina</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>MatriculaPeriodoTurmaDisciplinaVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluirMatriculaPeriodoTurmaDisciplinasPorMatricula(String matricula,  UsuarioVO usuario) throws Exception;

	public List consultarMatriculaPeriodoTurmaDisciplinaASeremExcluidas(Integer matriculaPeriodo, List objetos, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public Integer consultaQtdMatriculaPeriodoTurmaDisciplinaReposicaoAlunoTurma(Integer gradeCurricular, Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaMatriculaPeriodoTurmaDisciplinaReposicaoAlunoTurma(Integer gradeCurricular, Integer turma, Integer disciplina,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;

	public void retirarReservaTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UsuarioVO usuario) throws Exception;

	public void excluirMatriculaPeriodoTurmaDisciplinas(Integer matricula, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UsuarioVO usuario) throws Exception;
	
	public void validarExclusaoDoHistoricoPorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> listaItems, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>MatriculaPeriodoVO</code> contidos em um Hashtable no BD. Faz uso
	 * da operação <code>excluirMatriculaPeriodos</code> e
	 * <code>incluirMatriculaPeriodos</code> disponíveis na classe
	 * <code>MatriculaPeriodo</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void alterarMatriculaPeriodos(Integer matriculaPeriodo, String matricula, List<MatriculaPeriodoTurmaDisciplinaVO> objetos,  GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;

	public void incluirMatriculaPeriodoTurmaDisciplinas(Integer matriculaPrm, String matricula, List<MatriculaPeriodoTurmaDisciplinaVO> objetos,  GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por incluir objetos da
	 * <code>MatriculaPeriodoVO</code> no BD. Garantindo o relacionamento com a
	 * entidade principal <code>academico.Matricula</code> através do atributo
	 * de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void incluirMatriculaPeriodoTurmaDisciplinas(Integer matriculaPrm, String matricula, List<MatriculaPeriodoTurmaDisciplinaVO> objetos, String semestre, String ano,  GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>MatriculaPeriodoTurmaDisciplinaVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public MatriculaPeriodoTurmaDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarDisciplinaDoAlunoPorMatricula(Integer unidadeEnsino, String matricula, int nivelMontarDados, UsuarioVO usuario) throws SQLException, Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

	public List consultarMatriculaPeriodoTurmaDisciplinas(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaTurmaDisciplinaSemestreAno(String matriculaAluno, Integer codigoTurma, Integer disciplina, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Boolean executarVerificacaoMatriculaRegistradaTurmaDisciplinaSemestreAno(String matriculaAluno, Integer codigoTurma, Integer disciplina, String semestre, String ano, Boolean turmaAgrupada, boolean controlarAcesso, int nivelMontarDados) throws Exception;

	public void excluirMatriculaPeriodoTurmaDisciplinasPorListaMatriculaPeriodo(List<MatriculaPeriodoVO> listaMatriculaPeriodo, Integer disciplina, Integer turma, Integer gradeCurricular, UsuarioVO usuario) throws Exception;
	
	public void excluirMatriculaPeriodoTurmaDisciplinas(List<MatriculaPeriodoTurmaDisciplinaVO> listaMptd, UsuarioVO usuario) throws Exception;

	public void incluirMatriculaPeriodosForaPrazo(Integer matriculaPeriodo, String matricula, List<MatriculaPeriodoTurmaDisciplinaVO> objetos,  GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;

	public void incluirMatriculaPeriodoTurmaDisciplinas(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List<MatriculaPeriodoTurmaDisciplinaVO> objetos,  GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarTurmasAlunoPorMatriculaAtiva(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPeriodoLetivoMatriculaAlunoPorMatriculaAtiva(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorMatriculaEDisciplina(String matricula, Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Hashtable processarRotinaTurmaXMatriculaPeriodoTurmaDisciplina(String valorParametro, UsuarioVO usuario) throws Exception;

	public MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaDisciplina(String matricula, Integer codigoDisciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaPeriodo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaPeriodoTurma(Integer matriculaPeriodo, TurmaVO turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaPeriodoTurmaDisciplina(Integer matriculaPeriodo, TurmaVO turma, DisciplinaVO disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void alterarTurmaPorMatriculaPeriodoTurmaDisciplina(Integer matriculaPeriodoTurmaDisciplina, Integer turma, UsuarioVO usuarioVO) throws Exception;	

	public List<DisciplinaVO> montarListaDisciplinasDeAcordoComMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs);

	public Integer consultarCodigoPelaMatriculaTurmaDisciplina(String matricula, Integer codigoTurma, Integer codigoDisciplina) throws Exception;

	public void excluirComBaseNaMatricula(String matricula,  UsuarioVO usuarioLogado) throws Exception;

	public List consultaRapidaPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorCodigoDisciplinaExcluirDisciplinaPermanecerUnificacaoDisciplina(Integer disciplinaExcluir, Integer disciplinaPermanecer, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Boolean consultarExistenciaMatriculaPeriodoTurmaDisciplinaPorCodigoDisciplina(Integer disciplina, Integer matriculaPeriodo) throws Exception;

	public MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaPeriodoDisciplina(Integer matriculaPeriodo, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void inicializarDadosMatriculaPeriodoTurmaDisciplinaUnificacaoDisciplina(MatriculaPeriodoTurmaDisciplinaVO mptdPermanecer, MatriculaPeriodoTurmaDisciplinaVO mptdExcluir);
	
	public MatriculaPeriodoTurmaDisciplinaVO gerarMatriculaPeriodoTurmaDiscipinaPorAlteracaoMatrizCurricular(TurmaVO turma, TurmaDisciplinaVO td, MatriculaPeriodoVO mp) throws Exception;

	public List<Integer> consultarPorCodigoDisciplina(Integer codigoExcluir);

	public void excluirComBaseNaMatriculaPeriodo(Integer matriculaPeriodo, UsuarioVO usuarioLogado) throws Exception;

	public Boolean consultarExistenciaMatriculaPeriodoturmaDisciplinaPorMatriculaTurmaDisciplinaSemestreAno(String matriculaAluno, Integer turma, Integer disciplina, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorCodigoDisciplinaExcluirMatriculaPeriodoTurmaDisciplinaComAMesmaEquivalente(Integer disciplinaExcluir, Integer disciplinaPermanecer, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(List<RegistroAulaVO> registroAulaVOs, Integer disciplina, String ano, String semestre, String situacaoMatricula, boolean diferenteAprovadoPorAproveitamento, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, String filtroTipoCursoAluno, String tipoLayout, boolean controlarAcesso, UsuarioVO usuario, TurmaVO turmaVO, boolean trazerAlunosTransferenciaMatriz, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaDisciplinaSemRegistro(TurmaVO turmaVO, List<TurmaDisciplinaVO> listaTurmaDisciplinaVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultarMatriculaPeriodoTurmaDisciplinasDaGrade(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultarQuantidadeDisciplinasDaGradePorMatriculaPeriodo(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	boolean consultarMatriculaPeriodoTurmaDisciplinaPorDisciplinaTcc(Integer gradeCurricular, UsuarioVO usuario) throws Exception;

	public void registrarAlunoRecebeuNotificacaoDownloadMaterial(final Integer matriculaperiodo, final Integer disciplina, final Integer turma) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarDisciplinaOnlineDoAlunoPorMatricula(String matricula, int nivelMontarDados, Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuario) throws SQLException, Exception;

	ConteudoVO registrarVinculoMatriculaPeriodoTurmaDisciplinaComConteudo(Integer matriculaperiodoTurmaDisciplina, Integer disciplina, UsuarioVO usuario) throws Exception;

	void atualizarDataNotificacaoFrequenciaBaixa(List<Integer> matriculaPeriodoTurmaDisciplinaVOs);

	public Boolean consultaRapidaReposicaoInclusaoAluno(String matricula, Integer disciplina, Integer codMatriculaPeriodoTurmaDisciplina) throws Exception;

	// void incluirTransferenciaGradeCurricular(MatriculaVO matriculaVO,
	// MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO
	// obj, Double mediaFinal, Double frequencia, Integer
	// mediaFinalNotaConceito, String situacao, String anoHistorico, String
	// semestreHistorico, String instituicao, Integer cidade, Integer
	// cargaHoraria, Integer cargaHorariaCursada
	//  UsuarioVO usuario) throws Exception;

	void atualizarTurmaMatriculaPeriodoTurmaDisciplina(Integer codigo, Integer codigoTurma, boolean disciplinaIncluida) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaTCCVO> consultarPorGradeDisciplina(GradeDisciplinaVO gradeDisciplina, UsuarioVO usuarioVO) throws Exception;

	Integer consultarTotalPorGradeDisciplina(GradeDisciplinaVO gradeDisciplina, UsuarioVO usuarioVO) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarAlunoSubturmaPorTurmaPrincipalAnoSemestreDisciplina(TurmaVO turmaPrincipal, boolean subturma, String ano, String semestre, Integer disciplina, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO, TipoSubTurmaEnum tipoSubTurma) throws Exception;

	public void atualizarNrAlunosMatriculadosTurmaDisciplina(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina, DisciplinaVO disciplinaVO, String ano, String semestre, Boolean considerarAlunosPreMatriculados, Boolean considerarVagasReposicao) throws Exception;

	public void incluir(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, final MatriculaPeriodoTurmaDisciplinaVO obj,  GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;

	public MatriculaPeriodoTurmaDisciplinaVO consultarMatriculaPeriodoTurmaDisciplinaVOPorTipoSalaAulaBlackboardEnumPorNomeAlunoPorCursoPorTurmaPorDisciplinaPorAnoPorSemestre(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum,String nomeAluno, Integer curso, Integer turma, Integer disciplina,  String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	
	public MatriculaPeriodoTurmaDisciplinaVO consultaRapidaPorMatriculaTurmaDisciplinaAnoSemestre(String matricula, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer gradeCurricular, boolean controlarAcesso, boolean consultarDisciplinaEquivalente, UsuarioVO usuario) throws Exception;	

	public void alterarMatriculaPeriodoTurmaDisciplinaTransferenciaMatrizCurricular(final MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception;

	public MatriculaPeriodoTurmaDisciplinaVO consultaRapidaPorTransferenciaMatrizCurricularMatricula(Integer codigoTransferenciaMatrizCurricularMatricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorTransferenciaMatrizCurricularMatricula(Integer codigoTransferenciaMatrizCurricularMatricula, UsuarioVO usuario) throws Exception;

	public void alteraModalidadeMatriculaPeriodoTurmaDisciplina(final Integer turma, final Integer disciplina, final String modalidade) throws Exception;

	void incluirProfessorMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception;

	void consultarCargaHorariaENrCreditosDisciplinaPorCodigoMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Map<String, Integer> cargaHorariaENrCreditos, UsuarioVO usuarioVO) throws Exception;

	MatriculaPeriodoTurmaDisciplinaVO consultarPorTurmaMatriculaOrdemEstudoOnlineMatriculaPeriodoTurmaDisciplinaAnterior(String matricula, Integer turma, Integer ordemEstudoOnline, UsuarioVO usuario) throws Exception;

	void incluirConteudoMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarAlunosConteudoAtrasadoEstudosOnline() throws Exception;

	void realizarAcessoEstudoOnline(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean forcarLiberacao, UsuarioVO usuarioVO) throws Exception;

	void incluirProgramacaoTutoriaOnlineProfessorMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer programacaoTutoriaOnlineProfessor) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaEMatriculaPeriodo(String matricula, Integer disciplina, Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<ConteudoRegistroAcessoVO> consultarDataAcessoPontosPorAcessoDiaETotalAcumuladoGraficoLinhaEvolucaoAluno(Integer codigoMatriculaPeriodoTurmaDisciplina) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorMatriculaEUltimaMatriculaPeriodo(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaDisciplinaCompostaPorMatriculaPeriodo(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaDisciplinaFazParteComposicaoPorMatriculaPeriodoGradeDisciplina(Integer matriculaPeriodo, Integer gradeDisciplina, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarDisciplinaOnlineDoAlunoPorMatriculaIntegracaoEADIPOG(String matricula, int nivelMontarDados, UsuarioVO usuario) throws SQLException, Exception;

	Integer consultarNrAlunosMatriculadosTurmaDisciplina(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, Boolean considerarAlunosPreMatriculados, TipoSubTurmaEnum tipoSubTurma, String matriculaDesconsiderar, Boolean considerarVagaReposicao, Boolean turmaAgrupada) throws Exception;

	/** 
	 * @author Wellington - 24 de set de 2015 
	 * @param matriculaPeriodo
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorMatriculaPeriodoGradeCurricularAtual(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void executarAtualizacaoTurmaBaseTurmaPraticaTurmaTeoricaPorTransferenciaTurma(Integer matriculaPeriodoTurmaDisciplina, Integer turmaBase, Integer turmaPratica, Integer turmaTeorica, UsuarioVO usuarioVO) throws Exception;
	
	void alterarTurmaPorMatriculaPeriodoTurmaDisciplinaTeorica(Integer matriculaPeriodoTurmaDisciplina, Integer turmaTeorica, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington - 14 de out de 2015 
	 * @param matriculaPeriodoTurmaDisciplina
	 * @param turmaPratica
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void alterarTurmaPorMatriculaPeriodoTurmaDisciplinaPratica(Integer matriculaPeriodoTurmaDisciplina, Integer turmaPratica, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington - 14 de out de 2015 
	 * @param matriculaPeriodoTurmaDisciplina
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void removerTurmaPorMatriculaPeriodoTurmaDisciplinaTeorica(Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington - 14 de out de 2015 
	 * @param matriculaPeriodoTurmaDisciplina
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void removerTurmaPorMatriculaPeriodoTurmaDisciplinaPratica(Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 09/10/2015
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
	MatriculaPeriodoTurmaDisciplinaVO consultaRapidaPorChavePrimariaTrazendoMatricula(Integer codigo) throws Exception;

	/**
	 * @author Rodrigo Wind - 09/10/2015
	 * @param matricula
	 * @param turma
	 * @param disciplina
	 * @param ano
	 * @param semestre
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaPorMatriculaTurmaDisciplinaAnoSemestreGradeCurricularAtual(String matricula, Integer turma, Integer disciplina, String ano, String semestre, Boolean trazerDisciplinaComposta, Boolean trazerDisciplinaFazemParteComposicao, Boolean trazerDisciplinaForaGrade, Boolean trazerDisciplinaEquivalente, Boolean trazerDisciplinaPorEquivalenvia, String situacoesMatriculaPeriodo, String situacoesMatricula,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 09/10/2015
	 * @param matricula
	 * @param trazerDisciplinaComposta
	 * @param trazerDisciplinaFazemParteComposicao
	 * @param trazerDisciplinaForaGrade
	 * @param trazerDisciplinaEquivalente
	 * @param trazerDisciplinaPorEquivalenvia
	 * @param situacoesMatriculaPeriodo
	 * @param situacoesMatricula
	 * @return
	 * @throws Exception
	 */
	List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaPorUltimaMatriculaPeriodo(String matricula, Boolean trazerDisciplinaComposta, Boolean trazerDisciplinaFazemParteComposicao, Boolean trazerDisciplinaForaGrade, Boolean trazerDisciplinaEquivalente, Boolean trazerDisciplinaPorEquivalenvia, String situacoesMatriculaPeriodo, String situacoesMatricula) throws Exception;

	/**
	 * @author Rodrigo Wind - 19/01/2016
	 * @param matriculaPeriodoVO
	 * @param matriculaPeriodoTurmaDisciplinaVO
	 * @param configuracaoAcademicoVO
	 * @param usuarioVO
	 * @throws Exception - Caso retorne consistir exception com o boolean Referente Choque Horário Marcado e Com a Lista de Mensagem preenchida então,
	 * neste caso de todas as possibilidades para definir uma turma prática/teórica acabaram gerando choque de horário, neste caso esta matricula periodo turma disciplina
	 * não poderá ser adicionada na matrícula.
	 * 
	 */
	
	void realizarSugestaoTurmaPraticaTeorica(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, DisciplinaVO disciplina, ConfiguracaoAcademicoVO configuracaoAcademicoVO,  Boolean considerarVagasReposicao, UsuarioVO usuarioVO) throws Exception;


	/**
	 * @author Rodrigo Wind - 21/01/2016 - Este foi criado para não ser replicado esta sql em diversos locais, portando basta adicionar no sql que está sendo confeccionado como interno
	 * @param sqlCodigoTurma - neste pode ser passado apenas o inteiro com o código da turma ou a referência de outros sql ex: t.codigo
	 * @param codigoDisciplina
	 * @param ano
	 * @param semestre
	 * @param considerarAlunosPreMatriculados
	 * @param tipoSubTurma
	 * @return
	 * @throws Exception
	 */
	StringBuilder getSqlPadraoConsultarVagasOcupadas(String sqlCodigoTurma, Integer codigoDisciplina, String ano, String semestre, Boolean considerarAlunosPreMatriculados, TipoSubTurmaEnum tipoSubTurma, String matriculaDesconsiderar, Boolean considerarVagaReposicao, Boolean turmaAgrupada) throws Exception;

	/**
	 * @author Rodrigo Wind - 25/01/2016
	 * @param matriculaPeriodoTurmaDisciplinaVO
	 * @param matriculaPeriodoVO
	 * @return
	 * @throws Exception
	 */
	List<MatriculaPeriodoTurmaDisciplinaVO> realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 17 de ago de 2016 
	 * @param matricula
	 * @param turma
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	MatriculaPeriodoTurmaDisciplinaVO consultarPorTurmaMatriculaOrdemEstudoOnlineMatriculaPeriodoTurmaDisciplinaPosterior(String matricula, Integer turma, String ano, String semestre, UsuarioVO usuario) throws Exception;
	
	List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaPeriodoDisciplinasCursandoPorCorrespodencia(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Boolean consultarMatriculaPeriodoTurmaDisciplinaExistePorConteudoPorDisciplinaDiferente(Integer disciplina, Integer conteudo) throws Exception;
	
	List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaPeriodoDisciplinasCursandoPorEquivalencia(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaEMatriculaPeriodoPorAvaliacaoInstitucional(
			String matricula, Integer disciplina, Integer matriculaPeriodo,
			AvaliacaoInstitucionalVO avaliacaoInstitucional,  Boolean trazerRespondido,  boolean controlarAcesso, UsuarioVO usuario)
			throws Exception;
	
	MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaMatriculaPeriodoDisciplinaCargaHoraria(String matricula, Integer matriculaPeriodo, Integer disciplina, Integer cargaHoraria, UsuarioVO usuarioVO) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(
			ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO,
			ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, Integer bimestre, UsuarioVO usuarioVO, Boolean considerarAlunosInativos, Boolean considerarAlunosSemTutor)
			throws Exception;
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaGeracaoPorCargaAutomaticas(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, Integer bimestre, boolean isClassroomAutomatico, UsuarioVO usuarioVO) throws Exception; 

	Integer consultarQtdAlunosAtivoPorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO obj,
			ProgramacaoTutoriaOnlineProfessorVO objProfessor);

	Boolean consultarMatriculaPeriodoTurmaDisciplinaExistePorConteudo(Integer conteudo) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaPorAtividadeDiscursivas(AtividadeDiscursivaVO atividade, UsuarioVO usuarioVO) throws Exception;
	
	List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaPorMatriculaTurmaDisciplinaAnoSemestre(String matricula, Integer turma, Integer disciplina, String ano, String semestre, Boolean gradeCurricularAtual, Boolean trazerDisciplinaComposta, Boolean trazerDisciplinaFazemParteComposicao, Boolean trazerDisciplinaForaGrade, Boolean trazerDisciplinaEquivalente, Boolean trazerDisciplinaPorEquivalenvia, String situacoesMatriculaPeriodo, String situacoesMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	MatriculaPeriodoTurmaDisciplinaVO realizarObtencaoDisciplinaCompostaComBaseDisciplinaFilhaComposicao(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina);

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorMatriculaPeriodoTurmaDisciplinaComModalidadeDiferenteTurma(
			TurmaVO turmaVO, TurmaDisciplinaEstatisticaAlunoVO turmaDisciplinaEstatisticaAlunoVO, UsuarioVO usuario)
			throws Exception;
	
	void alterarModalidadeMatriculaPeriodoTurmaDisciplina(TurmaVO turmaVO,
			TurmaDisciplinaEstatisticaAlunoVO turmaDisciplinaEstatisticaAlunoVO, UsuarioVO usuario) throws Exception;



	List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaDisciplinaFazParteComposicaoPorMatriculaPeriodoGradeCurricularGrupoOptativaDisciplina(
			Integer matriculaPeriodo, Integer gradeCurricularGrupoOptativaDisciplina, boolean verificarAcesso,
			UsuarioVO usuarioVO) throws Exception;
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaSemTutor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception;

	public void alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegral(Integer turma, Integer novaDisciplina, Integer disciplina, Integer gradeDisciplina, Integer gradeCurricularGrupoOptativaDisciplina, List<MatriculaPeriodoVO> listaMatriculaPeriodo, UsuarioVO usuario) throws Exception;
	
	public void alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegralComReposicao(Integer mptd, Integer novaDisciplina, Integer gradeDisciplina, Integer gradeCurricularGrupoOptativaDisciplina, Integer mapaEquivalenciaDisciplina, Integer mapaEquivalenciaDisciplinaCursada, boolean disciplinaPorEquivalencia, UsuarioVO usuario) throws Exception;

	public void alterarDataUltimaAlteracao_Online(String matricula, UsuarioVO usuario) throws Exception;
	
	public void desvincularProgramacaoTutoriaOnlineProfessor(MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception;

	public void alterarConteudoMatriculaPeriodoTurmaDisciplinaComConteudo(Integer conteudo,	Integer matriculaperiodoTurmaDisciplina, UsuarioVO usuario) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarDisciplinaDoAlunoPorMatricula(String matricula, String anoSemestre, PermissaoAcessoMenuVO permissaoAcessoMenuVO, Integer matriculaPeriodoTurmaDisciplina, int nivelMontarDados, UsuarioVO usuario) throws SQLException, Exception;
	
	void getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaTurma(StringBuilder sb);
	
	void getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaDisciplina(StringBuilder sb);

	
	StringBuilder executarInicializacaoSqlConsultaMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, Integer bimestre, Boolean considerarAlunosInativos, Boolean considerarAlunosSemTutor);	

	void alterarPorGradeDisciplinaAlteracaoMatrizAtivaInativa(GradeDisciplinaVO gradeDisciplinaVO) throws Exception;

	void alterarParaForaDaGradePorGradeDisciplina(GradeDisciplinaVO gradeDisciplinaVO, UsuarioVO usuario) throws Exception;

	void excluirPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuarioLogado) throws Exception;


	void excluirComBaseNaMatriculaPeriodoMatriculaCodDisciplina(Integer codMatriculaPeriodo, String matricula,
			Integer codDisciplina, UsuarioVO usuarioLogado) throws Exception;

	public void realizarGravarDisciplinasMatriculaProcessoSeletivo(MatriculaRSVO matriculaRSVO, UsuarioVO usuarioVO) throws Exception;
}
