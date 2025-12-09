package negocio.interfaces.academico;

import java.sql.SQLException;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
	 */
	public MatriculaPeriodoTurmaDisciplinaVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PreMatriculaPeriodoTurmaDisciplina</code> através do valor do atributo
	 * <code>Integer Disciplina</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDisciplina(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorMatriculaAtiva(String matricula, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PreMatriculaPeriodoTurmaDisciplina</code> através do valor do atributo
	 * <code>identificadorTurma</code> da classe <code>Turma</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorIdentificadorTurmaTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoTurmaDisciplinaSemestreAno(Integer turma, Integer disciplina, Boolean turmaAgrupada, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PreMatriculaPeriodoTurmaDisciplina</code> através do valor do atributo
	 * <code>Integer matriculaPeriodo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorMatriculaPeriodo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaPeriodoDisciplinaSemestreAno(Integer valorConsulta, Integer disciplina, String semestre, String ano, boolean controlarAcesso,
			int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PreMatriculaPeriodoTurmaDisciplina</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public void retirarReservaTurmaDisciplina(List listaItems,UsuarioVO usuario) throws Exception;

	public void excluirPreMatriculaPeriodoTurmaDisciplinas(Integer matricula, List objetos) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>MatriculaPeriodoVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirMatriculaPeriodos</code> e <code>incluirMatriculaPeriodos</code> disponíveis na classe <code>MatriculaPeriodo</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarPreMatriculaPeriodos(Integer matriculaPeriodo, String matricula, List objetos) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>MatriculaPeriodoVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>academico.Matricula</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirPreMatriculaPeriodoTurmaDisciplinas(Integer matriculaPrm, String matricula, List objetos) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public MatriculaPeriodoTurmaDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarDisciplinaDoAlunoPorMatricula(Integer unidadeEnsino, String matricula, int nivelMontarDados,UsuarioVO usuario) throws SQLException, Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);

        public void incluirPreMatriculaPeriodosForaPrazo(Integer matriculaPeriodo, String matricula, List objetos) throws Exception;
}