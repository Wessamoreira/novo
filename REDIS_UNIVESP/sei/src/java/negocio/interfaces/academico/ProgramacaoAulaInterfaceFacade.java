package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ProgramacaoAulaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ProgramacaoAulaInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ProgramacaoAulaVO</code>.
	 */
	public ProgramacaoAulaVO novo() throws Exception;

	public void validarQuantidadeAulas(ProgramacaoAulaVO obj) throws Exception;

	public void validarSePeriodoAulaInicioFimJaExiste(ProgramacaoAulaVO obj, UsuarioVO usuario) throws Exception;

	public void validarSeProfessorJaMinistraAulaNesseHorario(ProgramacaoAulaVO obj, UsuarioVO usuario) throws Exception;

	public List consultarProgramacaoAulaQueProfessorMinistra(ProgramacaoAulaVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarProgramacaoAulaQueProfessorMinistraPorProfessor(Integer professor, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarProgramacaoAulaPorTurmaPorDisciplinaPorProfessor(ProgramacaoAulaVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarProgramacaoAulaPorTurmaPorDisciplina(Integer turma, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarProgramacaoAulaPorTurmaPorDisciplinaPorDiaSemana(Integer turma, Integer disciplina, String diasemana, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ProgramacaoAulaVO consultarProgramacaoAulaPorTurmaPorDisciplinaPorDiaSemanaPorPeriodo(Integer turma, Integer disciplina, String diasemana, Integer aulaInicio, Integer aulaFim,
			boolean programada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void validarSeProfessorTemDisponibilidadeNesseHorario(ProgramacaoAulaVO obj) throws Exception;

        

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ProgramacaoAulaVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>ProgramacaoAulaVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 **/
	public void incluir(ProgramacaoAulaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProgramacaoAulaVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>ProgramacaoAulaVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(ProgramacaoAulaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ProgramacaoAulaVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>ProgramacaoAulaVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ProgramacaoAulaVO obj, UsuarioVO usuarioVO) throws Exception;

	public List consultarPorTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoAula</code> através do valor do atributo 
	 * <code>String local</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>ProgramacaoAulaVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorLocal(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoAula</code> através do valor do atributo 
	 * <code>String diaSemana</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>ProgramacaoAulaVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDiaSemana(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoAula</code> através do valor do atributo 
	 * <code>nome</code> da classe <code>Pessoa</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>ProgramacaoAulaVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoAula</code> através do valor do atributo 
	 * <code>nome</code> da classe <code>Disciplina</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>ProgramacaoAulaVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoAula</code> através do valor do atributo 
	 * <code>identificadorTurma</code> da classe <code>Turma</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>ProgramacaoAulaVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorIdentificadorTurmaTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoAula</code> através do valor do atributo 
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>ProgramacaoAulaVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>ProgramacaoAulaVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ProgramacaoAulaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);

}