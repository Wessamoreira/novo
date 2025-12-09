package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.academico.FuncionarioDependenteVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;

@SuppressWarnings("rawtypes")
public interface FuncionarioDependenteInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>FuncionarioDependenteVO</code>.
	 */
	public FuncionarioDependenteVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>FuncionarioDependenteVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>FuncionarioDependenteVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(FuncionarioDependenteVO obj) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>FuncionarioDependenteVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>FuncionarioDependenteVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(FuncionarioDependenteVO obj) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>FuncionarioDependenteVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>FuncionarioDependenteVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(FuncionarioDependenteVO obj) throws Exception;

	public List consultarPorFuncionario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;


	public void excluirFuncionarioDependente(Integer funcionario) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>FuncionarioDependenteVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirFuncionarioDependentes</code> e <code>incluirFuncionarioDependentes</code> disponíveis na classe <code>FuncionarioDependente</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarFuncionarioDependentes(Integer funcionario, List objetos) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>FuncionarioDependenteVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>administrativo.funcionario</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirFuncionarioDependentes(Integer funcionarioPrm, List objetos) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>FuncionarioDependenteVO</code>
	 * através de sua chave primária.
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public FuncionarioDependenteVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

	FuncionarioDependenteVO consultarPorCodigo(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
     * Consulta a quantidade de pendentes cadastrados para o funcionario
     * 
     * @param codigoFuncionario
     * @return
     * @throws Exception
     */
	public Integer consultarQuantidadeDependentesDoFuncinarioNoIRRF(Integer codigoFuncionario);

    public void validarDadosDependente(FuncionarioDependenteVO funcionarioDependenteVO);

    public void adicionarEventosDePensaoDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, UsuarioVO usuario);

	public List<FuncionarioDependenteVO> consultarFuncionariosDependentes() throws Exception;

	public void alterarFuncionariosDependentes(FuncionarioDependenteVO funcionarioDependenteVO);
	
    /**
     * Consulta a quantidade de pendentes para o Salario Familia cadastrados para o funcionario
     * 
     * @param codigoFuncionario
     * @return
     * @throws Exception
     */
    public Integer consultarQuantidadeDependentesDoFuncinarioNoSalarioFamilia(Integer codigoFuncionario);
    
    public void validarDados(FuncionarioDependenteVO obj) throws ConsistirException;
}