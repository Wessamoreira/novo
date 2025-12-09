package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberHistoricoVO;
import negocio.comuns.financeiro.ContaReceberVO;

public interface ContaReceberHistoricoInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ContaReceberHistoricoVO</code>.
     */
    public ContaReceberHistoricoVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaReceberHistoricoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ContaReceberHistoricoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(ContaReceberHistoricoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaReceberHistoricoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberHistoricoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(ContaReceberHistoricoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContaReceberHistoricoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberHistoricoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(ContaReceberHistoricoVO obj, UsuarioVO usuario) throws Exception;


    /**
     * Responsável por realizar uma consulta de <code>ContaReceberHistorico</code> através do valor do atributo
     * <code>codigo</code> da classe <code>ContaReceber</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ContaReceberHistoricoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoContaReceber(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberHistorico</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberHistoricoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>ContaReceberHistoricoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ContaReceberHistorico</code>.
     * @param <code>ContaReceber</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirContaReceberHistoricos(Integer ContaReceber, UsuarioVO usuario) throws Exception;

    public void excluirContaReceberHistoricos(Integer ContaReceber, List objeto, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>ContaReceberHistoricoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirContaReceberHistoricos</code> e <code>incluirContaReceberHistoricos</code> disponíveis na classe <code>ContaReceberHistorico</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarContaReceberHistoricos(Integer ContaReceber, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>ContaReceberHistoricoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.ContaReceber</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirContaReceberHistoricos(Integer ContaReceberPrm, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>ContaReceberHistoricoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ContaReceberHistoricoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

	void criarContaReceberHistoricoPorBaixaAutomaticas(ContaReceberVO contaReceberVO, UsuarioVO responsavel,
			UsuarioVO usuario) throws Exception;
}
