package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.DevolucaoCompraItemVO;

public interface DevolucaoCompraItemInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>DevolucaoCompraItemVO</code>.
	 */
	public DevolucaoCompraItemVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>DevolucaoCompraItemVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>DevolucaoCompraItemVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(DevolucaoCompraItemVO obj, Integer compra, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	public void retirarProdutoEstoque(DevolucaoCompraItemVO obj, Integer compra, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	public void adicionarItemAoRecebimentoCompra(DevolucaoCompraItemVO obj, Integer compra, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;	

	public void subtrairQuantidadeRecebidaItemCompra(DevolucaoCompraItemVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>DevolucaoCompraItemVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>DevolucaoCompraItemVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(DevolucaoCompraItemVO obj) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>DevolucaoCompraItemVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>DevolucaoCompraItemVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(DevolucaoCompraItemVO obj) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>DevolucaoCompraItem</code> através do valor do atributo 
	 * <code>Integer compraItem</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>DevolucaoCompraItemVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCompraItem(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>DevolucaoCompraItem</code> através do valor do atributo 
	 * <code>codigo</code> da classe <code>DevolucaoCompra</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>DevolucaoCompraItemVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoDevolucaoCompra(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>DevolucaoCompraItem</code> através do valor do atributo 
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>DevolucaoCompraItemVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da <code>DevolucaoCompraItemVO</code> no BD.
	 * Faz uso da operação <code>excluir</code> disponível na classe <code>DevolucaoCompraItem</code>.
	 * @param <code>devolucaoCompra</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void excluirDevolucaoCompraItems(Integer devolucaoCompra) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>DevolucaoCompraItemVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirDevolucaoCompraItems</code> e <code>incluirDevolucaoCompraItems</code> disponíveis na classe <code>DevolucaoCompraItem</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarDevolucaoCompraItems(Integer devolucaoCompra, Integer compra, Integer unidadeEnsino, List objetos, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>DevolucaoCompraItemVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>compra.DevolucaoCompra</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirDevolucaoCompraItems(Integer devolucaoCompraPrm, Integer compra, Integer unidadeEnsino, List<DevolucaoCompraItemVO> objetos, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>DevolucaoCompraItemVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public DevolucaoCompraItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);

}