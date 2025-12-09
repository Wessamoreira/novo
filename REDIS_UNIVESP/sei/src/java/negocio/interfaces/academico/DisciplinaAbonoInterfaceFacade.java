package negocio.interfaces.academico;

import java.sql.SQLException;
import java.util.List;

import negocio.comuns.academico.DisciplinaAbonoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface DisciplinaAbonoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>AbonoFaltaVO</code>.
	 */
	public DisciplinaAbonoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>TurmaVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>TurmaVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(DisciplinaAbonoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>TurmaVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>TurmaVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(DisciplinaAbonoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>TurmaVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>TurmaVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(DisciplinaAbonoVO obj, UsuarioVO usuario) throws Exception;

	public List<DisciplinaAbonoVO> consultarProAbonoFalta(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws SQLException;

	public List consultarPorDisciplinaRegistroAulaMatricula(Integer disciplina, Integer registroAula, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>TurmaVO</code>
	 * através de sua chave primária.
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public DisciplinaAbonoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);
	
	public DisciplinaAbonoVO consultarDisciplinaAbonoPorRegistroAula(Integer registroAula, String matricula, String horario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}