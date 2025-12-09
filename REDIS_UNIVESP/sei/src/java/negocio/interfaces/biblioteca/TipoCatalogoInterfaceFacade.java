package negocio.interfaces.biblioteca;

import java.util.List;

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;

public interface TipoCatalogoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>TipoCatalogoVO</code>.
	 */
	public TipoCatalogoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>TipoCatalogoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TipoCatalogoVO</code> que será gravado
	 *            no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluir(TipoCatalogoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>TipoCatalogoVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TipoCatalogoVO</code> que será alterada
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterar(TipoCatalogoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>TipoCatalogoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TipoCatalogoVO</code> que será removido
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(TipoCatalogoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TipoCatalogo</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TipoCatalogoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TipoCatalogo</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TipoCatalogoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>TipoCatalogoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public TipoCatalogoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

	public List<TipoCatalogoVO> consultarTipoCatalogoComboBox(boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
	public TipoCatalogoVO consultarPorNomeRegistroUnico(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	TreeNodeCustomizado consultarArvoreTipoCatalogoSuperior(TipoCatalogoVO tipoCatalogoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	TreeNodeCustomizado consultarArvoreTipoCatalogoInferior(TipoCatalogoVO tipoCatalogoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public List<TipoCatalogoVO> consultarSubdivisaoSuperiorPorIdentificador(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}