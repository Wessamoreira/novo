package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ResultadoDisciplinaProcSeletivoVO;

public interface ResultadoDisciplinaProcSeletivoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code>.
	 */
	public ResultadoDisciplinaProcSeletivoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code>. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(ResultadoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(ResultadoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar
	 * esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ResultadoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ResultadoDisciplinaProcSeletivo</code> através do valor do atributo <code>nome</code> da classe
	 * <code>DisciplinasProcSeletivo</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ResultadoDisciplinaProcSeletivoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ResultadoDisciplinaProcSeletivoVO> consultarPorNomeDisciplinasProcSeletivo(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da <code>ResultadoDisciplinaProcSeletivoVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe <code>ResultadoDisciplinaProcSeletivo</code>.
	 * 
	 * @param <code>resultadoProcessoSeletivo</code>
	 *            campo chave para exclusão dos objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void excluirResultadoDisciplinaProcSeletivos(Integer resultadoProcessoSeletivo, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>ResultadoDisciplinaProcSeletivoVO</code> contidos em um Hashtable no BD. Faz uso da
	 * operação <code>excluirResultadoDisciplinaProcSeletivos</code> e <code>incluirResultadoDisciplinaProcSeletivos</code> disponíveis na classe
	 * <code>ResultadoDisciplinaProcSeletivo</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarResultadoDisciplinaProcSeletivos(Integer resultadoProcessoSeletivo, List<ResultadoDisciplinaProcSeletivoVO> objetos, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>ResultadoDisciplinaProcSeletivoVO</code> no BD. Garantindo o relacionamento com a entidade
	 * principal <code>processosel.ResultadoProcessoSeletivo</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirResultadoDisciplinaProcSeletivos(Integer resultadoProcessoSeletivoPrm, List<ResultadoDisciplinaProcSeletivoVO> objetos, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ResultadoDisciplinaProcSeletivoVO consultarPorChavePrimaria(Integer resultadoProcessoSeletivoPrm, Integer disciplinaProcSeletivoPrm, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade);

	/**
	 * @author Wellington - 21 de dez de 2015
	 * @param resultadoProcessoSeletivo
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 */
	List<ResultadoDisciplinaProcSeletivoVO> consultarResultadoDisciplinaProcSeletivos(Integer resultadoProcessoSeletivo, UsuarioVO usuarioLogado) throws Exception;

}