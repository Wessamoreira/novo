package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

public interface FiliacaoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>FiliacaoVO</code>.
	 */
	public FiliacaoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>FiliacaoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FiliacaoVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluir(final FiliacaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>FiliacaoVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FiliacaoVO</code> que será alterada no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterar(final FiliacaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>FiliacaoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FiliacaoVO</code> que será removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(FiliacaoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>Filiacao</code> através do
	 * valor do atributo <code>nome</code> da classe <code>Pessoa</code> Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>FiliacaoVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<FiliacaoVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>Filiacao</code> através do
	 * valor do atributo <code>String RG</code>. Retorna os objetos, com início
	 * do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>FiliacaoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	// public List consultarPorRG(String valorConsulta, boolean controlarAcesso,
	// UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>Filiacao</code> através do
	 * valor do atributo <code>String CPF</code>. Retorna os objetos, com início
	 * do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>FiliacaoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	// public List consultarPorCPF(String valorConsulta, boolean
	// controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>Filiacao</code> através do
	 * valor do atributo <code>String tipo</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>FiliacaoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	// public List consultarPorTipo(String valorConsulta, boolean
	// controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>Filiacao</code> através do
	 * valor do atributo <code>String nome</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>FiliacaoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	// public List consultarPorNome(String valorConsulta, boolean
	// controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>Filiacao</code> através do
	 * valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>FiliacaoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	// public List consultarPorCodigo(Integer valorConsulta, boolean
	// controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>FiliacaoVO</code> no BD. Faz uso da operação <code>excluir</code>
	 * disponível na classe <code>Filiacao</code>.
	 * 
	 * @param <code>aluno</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void excluirFiliacaos(Integer aluno, UsuarioVO usuario) throws Exception;

	public void excluirFiliacaos(Integer aluno, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	public void alterarFiliacaos(PessoaVO aluno, List<FiliacaoVO> objetos, Boolean deveValidarCPF, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>FiliacaoVO</code> no
	 * BD. Garantindo o relacionamento com a entidade principal
	 * <code>basico.Pessoa</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void incluirFiliacaos(PessoaVO alunoPrm, List<FiliacaoVO> objetos, Boolean deveValidarCPF, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>FiliacaoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public FiliacaoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

	public List<FiliacaoVO> consultarPorCodigoPessoaTipo(Integer valorConsulta, String tipo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public FiliacaoVO carregarApenasUmPorCPF(FiliacaoVO obj, PessoaVO pessoaVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

	List<FiliacaoVO> consultarFiliacaos(Integer aluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Boolean consultarSeExisteFiliacaoPorPais(Integer codigoPais, UsuarioVO usuario) throws Exception;

	void realizarCorrecaoMatriculaCRM(PessoaVO aluno, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuariologado) throws Exception;

	String consultarNomeFiliacaoPorTipoFiliacaoEPessoa(String tipoFiliacao, Integer pessoa) throws Exception;
	
	public List<FiliacaoVO> incluirFiliacaoConformeProspect(Integer codigoProspect , List<FiliacaoVO> filiacaoPessoa , UsuarioVO usuario) throws Exception;

}