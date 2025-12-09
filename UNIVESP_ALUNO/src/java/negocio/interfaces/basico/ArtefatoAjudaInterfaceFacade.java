package negocio.interfaces.basico;
import java.util.List;
import java.util.Map;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ArtefatoAjudaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 * @author Paulo Taucci
*/
public interface ArtefatoAjudaInterfaceFacade {
	
	/**
     * Operação responsável por retornar um novo objeto da classe
     * <code>ArtefatoAjudaVO</code>.
     * @author Paulo Taucci
     */
    public ArtefatoAjudaVO novo() throws Exception;

    /**
     * Método responsavel por verificar se ira incluir ou alterar o objeto.
     * @author Paulo Taucci
     * @param ArtefatoAjudaVO
     * @throws Exception
     */
    public void persistir(ArtefatoAjudaVO obj, UsuarioVO usuario) throws Exception;
    
    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>ArtefatoAjudaVO</code>. Sempre localiza o registro a ser excluído através da
     * chave primária da entidade. Primeiramente verifica a conexão com o banco
     * de dados e a permissão do usuário para realizar esta operacão na
     * entidade. Isto, através da operação <code>excluir</code> da superclasse.
     * @author Paulo Taucci
     * @param obj
     *            Objeto da classe <code>ArtefatoAjudaVO</code> que será removido no
     *            banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(ArtefatoAjudaVO obj) throws Exception;

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ArtefatoAjudaVO</code>. Todos os tipos de consistência de dados são e devem
	 * ser implementadas neste método. São validações típicas: verificação de
	 * campos obrigatórios, verificação de valores válidos para os atributos.
	 * @author Paulo Taucci
     * @param obj
     *            Objeto da classe <code>ArtefatoAjudaVO</code> que terá os campos validados
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public void validarDados(ArtefatoAjudaVO obj) throws ConsistirException;
    
	/**
     * Rotina responsavel por executar as consultas disponiveis na Tela artefatoAjudaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox
     * denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     * @author Paulo Taucci
     */
	public List<ArtefatoAjudaVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    /**
     * Responsável por realizar uma consulta de <code>ArtefatoAjudaVO</code> através do
     * valor do atributo <code>String titulo</code>. Retorna os objetos, com
     * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ArtefatoAjudaVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ArtefatoAjudaVO> consultarPorTitulo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>ArtefatoAjudaVO</code> através do
     * valor do atributo <code>String descricao</code>. Retorna os objetos, com
     * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ArtefatoAjudaVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ArtefatoAjudaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
     * Responsável por realizar uma consulta de <code>ArtefatoAjudaVO</code> através do
     * valor do atributo <code>String palavrasChave</code>. Retorna os objetos, com
     * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ArtefatoAjudaVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ArtefatoAjudaVO> consultarPorPalavrasChave(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
     * Responsável por realizar uma consulta de <code>ArtefatoAjudaVO</code> através do
     * valor do atributo <code>TipoArtefatoAjudaEnum tipoArtefato</code>. Retorna os objetos, com
     * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ArtefatoAjudaVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ArtefatoAjudaVO> consultarPorTipoArtefato(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
     * Responsável por realizar uma consulta de <code>ArtefatoAjudaVO</code> através do
     * valor do atributo <code>UsuarioVO responsavelCadastro</code>. Retorna os objetos, com
     * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ArtefatoAjudaVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ArtefatoAjudaVO> consultarPorResponsavelCadastro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
     * Responsável por realizar uma consulta de <code>ArtefatoAjudaVO</code> através do
     * valor do atributo <code>Integer codigo</code>. Retorna os objetos com
     * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ArtefatoAjudaVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ArtefatoAjudaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
     * Operação responsável por localizar um objeto da classe
     * <code>ArtefatoAjudaVO</code> através de sua chave primária.
     * @author PauloTaucci
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public ArtefatoAjudaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     * @author Paulo Taucci
     */
    public void setIdEntidade(String aIdEntidade);
    

    /**
     * Método responsável por realizar uma consulta de <code>ArtefatoAjudaVO</code> através dos
     * atributos mais relevantes e que esteja desatualizado ou não, conforme o
     * atributo <code>boolean desatualizado</code>. Retorna os objetos com
     * valores iguais ou superiores aos parâmetros fornecidos. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     * @author Paulo Taucci
     * @param valorConsulta
     * @param desatualizado
     * @param controlarAcesso
     * @param nivelMontarDados
     * @return List<ArtefatoAjudaVO> 
     * @throws Exception
     */
    public List<ArtefatoAjudaVO> consultarPorTodosCamposESituacao(String valorConsulta, boolean desatualizado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void realizarSincronizacaoArtefatoAjuda(UsuarioVO usuarioVO);

	List<ArtefatoAjudaVO> consultarArtefatoPorCodigos(String artefatos) throws Exception;

	Map<String, String> consultarArtefatoAjudaDisponivel();

	List<ArtefatoAjudaVO> consultarArtefatoIndice(String valorConsulta) throws Exception;

	void consultarArtefatos(DataModelo controleConsulta, String valorConsulta, String modulo, String subModulo, String recurso) throws Exception;
    
}