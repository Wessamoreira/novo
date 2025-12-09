package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.compras.DevolucaoCompraItemImagemVO;

public interface DevolucaoCompraItemImagemInterfaceFacade {

	public DevolucaoCompraItemImagemVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>DevolucaoCompraItemVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>DevolucaoCompraItemVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(DevolucaoCompraItemImagemVO obj) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>DevolucaoCompraItemVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>DevolucaoCompraItemVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(DevolucaoCompraItemImagemVO obj) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>DevolucaoCompraItemVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>DevolucaoCompraItemVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(DevolucaoCompraItemImagemVO obj) throws Exception;

	public void excluirDevolucaoCompraItemImagems(Integer devolucaoCompraItem) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>DevolucaoCompraItemVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirDevolucaoCompraItems</code> e <code>incluirDevolucaoCompraItems</code> disponíveis na classe <code>DevolucaoCompraItem</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarDevolucaoCompraItemImagems(Integer devolucaoCompraItem, List objetos) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>DevolucaoCompraItemVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>compra.DevolucaoCompra</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirDevolucaoCompraItemImagems(Integer devolucaoCompraItemPrm, List objetos) throws Exception;

}