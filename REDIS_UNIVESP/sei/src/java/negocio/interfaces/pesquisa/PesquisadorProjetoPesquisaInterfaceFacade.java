package negocio.interfaces.pesquisa;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.pesquisa.PesquisadorProjetoPesquisaVO;

public interface PesquisadorProjetoPesquisaInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>PesquisadorProjetoPesquisaVO</code>.
	 */
	public PesquisadorProjetoPesquisaVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>PesquisadorProjetoPesquisaVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>PesquisadorProjetoPesquisaVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(PesquisadorProjetoPesquisaVO obj) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>PesquisadorProjetoPesquisaVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>PesquisadorProjetoPesquisaVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(PesquisadorProjetoPesquisaVO obj) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>PesquisadorProjetoPesquisaVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>PesquisadorProjetoPesquisaVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(PesquisadorProjetoPesquisaVO obj) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PesquisadorProjetoPesquisa</code> através do valor do atributo 
	 * <code>nome</code> da classe <code>ProjetoPesquisa</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>PesquisadorProjetoPesquisaVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeProjetoPesquisa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PesquisadorProjetoPesquisa</code> através do valor do atributo 
	 * <code>codigo</code> da classe <code>PesquisadorLinhaPesquisa</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>PesquisadorProjetoPesquisaVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoPesquisadorLinhaPesquisa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PesquisadorProjetoPesquisa</code> através do valor do atributo 
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>PesquisadorProjetoPesquisaVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da <code>PesquisadorProjetoPesquisaVO</code> no BD.
	 * Faz uso da operação <code>excluir</code> disponível na classe <code>PesquisadorProjetoPesquisa</code>.
	 * @param <code>projetoPesquisa</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void excluirPesquisadorProjetoPesquisas(Integer projetoPesquisa) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>PesquisadorProjetoPesquisaVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirPesquisadorProjetoPesquisas</code> e <code>incluirPesquisadorProjetoPesquisas</code> disponíveis na classe <code>PesquisadorProjetoPesquisa</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarPesquisadorProjetoPesquisas(Integer projetoPesquisa, List objetos) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>PesquisadorProjetoPesquisaVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>pesquisa.ProjetoPesquisa</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirPesquisadorProjetoPesquisas(Integer projetoPesquisaPrm, List objetos) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>PesquisadorProjetoPesquisaVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public PesquisadorProjetoPesquisaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);

}