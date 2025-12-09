package negocio.interfaces.administrativo;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface UnidadeEnsinoCursoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code>.
	 */
	public UnidadeEnsinoCursoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UnidadeEnsinoCursoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluir(UnidadeEnsinoCursoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UnidadeEnsinoCursoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterar(UnidadeEnsinoCursoVO obj) throws Exception;
	
	public void alterarValoresCursoGinfes(final UnidadeEnsinoCursoVO obj, boolean verificarAcesso, UsuarioVO usuario); 

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UnidadeEnsinoCursoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(UnidadeEnsinoCursoVO obj) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsinoCurso</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Turno</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>UnidadeEnsinoCursoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoCursoVO> consultarPorNomeTurno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsinoCurso</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Curso</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>UnidadeEnsinoCursoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoCursoVO> consultarPorNomeCurso(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCurso(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCurso(Integer valorConsulta, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsinoCurso</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>UnidadeEnsino</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>UnidadeEnsinoCursoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoCursoVO> consultarPorNomeUnidadeEnsino(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCursoUnidadeEnsino(Integer curso, Integer unidadeEnsino, String situacaoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCursoUnidadeEnsinoPeriodicidade(Integer valorConsulta, Integer unidadeEnsinoCodigo, String periodicidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorNomeCursoUnidadeEnsino(String nomeCurso, Integer unidadeEnsino, boolean apresentarHomePreInscricao, String situacaoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorNomeCursoUnidadeEnsinoPeriodicidade(String valorConsulta, Integer unidadeEnsinoCodigo, String periodicidade, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>UnidadeEnsinoCursoVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe <code>UnidadeEnsinoCurso</code>
	 * .
	 * 
	 * @param <code>unidadeEnsino</code> campo chave para exclusão dos objetos
	 *        no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void excluirUnidadeEnsinoCursos(Integer unidadeEnsino) throws Exception;

	public void excluirUnidadeEnsinoCursos(Integer unidadeEnsino, List<UnidadeEnsinoCursoVO> objetos) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>UnidadeEnsinoCursoVO</code> contidos em um Hashtable no BD. Faz uso
	 * da operação <code>excluirUnidadeEnsinoCursos</code> e
	 * <code>incluirUnidadeEnsinoCursos</code> disponíveis na classe
	 * <code>UnidadeEnsinoCurso</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void alterarUnidadeEnsinoCursos(Integer unidadeEnsinoPrm, List<UnidadeEnsinoCursoVO> objetos, UsuarioVO usuario) throws Exception;

	public void alterarCoordenadorTCC(final UnidadeEnsinoCursoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por incluir objetos da
	 * <code>UnidadeEnsinoCursoVO</code> no BD. Garantindo o relacionamento com
	 * a entidade principal <code>administrativo.UnidadeEnsino</code> através do
	 * atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void incluirUnidadeEnsinoCursos(Integer unidadeEnsinoPrm, List<UnidadeEnsinoCursoVO> objetos, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public UnidadeEnsinoCursoVO consultarPorChavePrimaria(Integer cursoPrm, Integer unidadeEnsinoPrm, Integer turnoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public UnidadeEnsinoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

	public List<UnidadeEnsinoCursoVO> consultarUnidadeEnsinoCursos(Integer prm, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

	public void carregarDados(UnidadeEnsinoCursoVO obj, UsuarioVO usuario) throws Exception;

	public void carregarDados(UnidadeEnsinoCursoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public UnidadeEnsinoCursoVO consultaRapidaPorCursoUnidadeTurno(Integer cursoPrm, Integer unidadeEnsinoPrm, Integer turnoPrm, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCursoUnidadeEnsinoNivelEducacional(Integer valorConsulta, Integer unidadeEnsinoCodigo, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultaRapidaPorNomeCursoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<UnidadeEnsinoCursoVO> consultaRapidaPorNomeCursoUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> unidadesEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultaRapidaPorNomeCursoUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> unidadesEnsino, boolean controlarAcesso, String nivelEducacional, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorNomeCursoUnidadeEnsinoNivelEducacional(String valorConsulta, Integer unidadeEnsinoCodigo, String nivelEducacional, boolean b, int nivelMontarDados, UsuarioVO usuario, String periodicidade) throws Exception;

	public UnidadeEnsinoCursoVO consultarPorMatriculaAluno(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public UnidadeEnsinoCursoVO consultarPorCursoTurnoPlanoFinanceiro(Integer curso, Integer turno, Integer planoFinanceiro, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultaRapidaPorCursoUnidade(Integer cursoPrm, Integer unidadeEnsinoPrm, UsuarioVO usuario) throws Exception;

	public void validarDadosFechamentoPeriodoLetivo(String periodicidade, String ano, String semestre, String nivelEducacionalApresentar) throws Exception;

	public void consultar(DataModelo dataModelo, UnidadeEnsinoCursoVO obj);
	
	public List<UnidadeEnsinoCursoVO> consultar(String campoConsultaCurso, String valorConsultaCurso, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelmontardadosDado, UsuarioVO usuarios) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorNomeTurnoUnidadeEnsino(String valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public UnidadeEnsinoCursoVO consultarPorCursoUnidadeEnsino(Integer curso, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultaRapidaPorUnidadeEnsino(Integer unidadeEnsinoPrm, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorProfessor(String nome, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorProcessoMatricula(String nomeCurso, Integer processomatricula, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultaRapidaPorProcSeletivo(Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultaRapidaPorUnidadeEnsino(Integer unidadeEnsinoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoCursoVO> consultarCursoPorCodigoUnidadeEnsino(Integer codigoCurso, Integer unidadeEnsinoCodigo, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultaUnidadeEnsinoCursoDoProfessor(Integer professor, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoCursoVO> consultaRapidaPorUnidadeEnsinoENivelEducacional(Integer unidadeEnsinoPrm, String nivelEducacional, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<UnidadeEnsinoCursoVO> consultaRapidaPorCursoProcSeletivo(String nomeCurso, Integer procSeletivo, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void criarUnidadeEnsinoCursoCadastroCurso(List<UnidadeEnsinoVO> listaUnidadeEnsino, CursoVO curso, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Wellington Rodrigues - 17 de jul de 2015 
	 * @param valorConsulta
	 * @param unidadeEnsino
	 * @param periodicidade
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<UnidadeEnsinoCursoVO> consultaRapidaPorNomeCursoUnidadeEnsinoPeriodicidade(String valorConsulta, Integer unidadeEnsino, String periodicidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington Rodrigues - 24 de jul de 2015 
	 * @param valorConsulta
	 * @param unidadeEnsinoCodigo
	 * @param verificarAcesso
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<UnidadeEnsinoCursoVO> consultarPorNomeCursoUnidadeEnsino(String valorConsulta, Integer unidadeEnsinoCodigo, boolean verificarAcesso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<UnidadeEnsinoCursoVO> consultaRapidaPorCursoUnidadeTurnoLista(Integer cursoPrm, Integer unidadeEnsinoPrm, Integer turnoPrm, UsuarioVO usuario) throws Exception;

	Boolean validarUnidadeEnsinoCursoExistenteMatriculaPeriodo(UnidadeEnsinoCursoVO obj, boolean isAlterando) throws Exception;

	public List<UnidadeEnsinoCursoVO> consultarPorCursoAgrupandoPorUnidadeEnsino(Integer codigo);

	public void atualizarUnidadeEnsinoCurso(List<UnidadeEnsinoCursoVO> listaUnidadeEnsinoCursos , CursoVO curso) throws Exception;
	
	Boolean verificarUnidadeEnsinoCursoExistente(Integer unidadeEnsinoCodigo,Integer turnoCodigo,Integer cursoCodigo) throws Exception;

	UnidadeEnsinoCursoVO consultarPorCursoUnidadeTurno(Integer cursoPrm, Integer unidadeEnsinoPrm, Integer turnoPrm, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoCursoVO inicializarDadosUnidadeEnsinoCursoImportarCandidatoProcSeletivo(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, UsuarioVO usuario) throws Exception;
	
	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCursosUnidadeEnsino(List<CursoVO> cursos, Integer unidadeEnsino, String situacaoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoCursoVO> consultarPorCodigoCursoListaUnidadeEnsinoNivelEducacional(Integer valorConsulta,List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoCursoVO> consultarPorNomeCursoListaUnidadeEnsinoNivelEducacional(String valorConsulta,List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}

