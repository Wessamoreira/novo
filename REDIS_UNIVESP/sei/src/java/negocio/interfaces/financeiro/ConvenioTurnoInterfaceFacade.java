package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConvenioTurnoVO;

public interface ConvenioTurnoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ConvenioTurnoVO</code>.
	 */
	public ConvenioTurnoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ConvenioTurnoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>ConvenioTurnoVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(ConvenioTurnoVO obj) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ConvenioTurnoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>ConvenioTurnoVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(ConvenioTurnoVO obj) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ConvenioTurnoVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>ConvenioTurnoVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ConvenioTurnoVO obj) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ConvenioTurno</code> através do valor do atributo 
	 * <code>nome</code> da classe <code>Turno</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>ConvenioTurnoVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeTurno(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ConvenioTurno</code> através do valor do atributo 
	 * <code>descricao</code> da classe <code>Convenio</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>ConvenioTurnoVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricaoConvenio(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da <code>ConvenioTurnoVO</code> no BD.
	 * Faz uso da operação <code>excluir</code> disponível na classe <code>ConvenioTurno</code>.
	 * @param <code>convenio</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void excluirConvenioTurnos(Integer convenio) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>ConvenioTurnoVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirConvenioTurnos</code> e <code>incluirConvenioTurnos</code> disponíveis na classe <code>ConvenioTurno</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarConvenioTurnos(Integer convenio, List objetos) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>ConvenioTurnoVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>financeiro.Convenio</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirConvenioTurnos(Integer convenioPrm, List objetos) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>ConvenioTurnoVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ConvenioTurnoVO consultarPorChavePrimaria(Integer convenioPrm, Integer turnoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);

}