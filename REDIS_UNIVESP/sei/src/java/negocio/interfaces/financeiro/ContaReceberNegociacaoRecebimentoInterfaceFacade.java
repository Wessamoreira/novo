package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;

public interface ContaReceberNegociacaoRecebimentoInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
     */
    public ContaReceberNegociacaoRecebimentoVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(ContaReceberNegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(ContaReceberNegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(ContaReceberNegociacaoRecebimentoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberNegociacaoRecebimento</code> através do valor do atributo
     * <code>Integer contaReceber</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorContaReceber(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberNegociacaoRecebimento</code> através do valor do atributo
     * <code>Double valorRecebido</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorRecebido(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberNegociacaoRecebimento</code> através do valor do atributo
     * <code>Double valorContaReceber</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorContaReceber(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberNegociacaoRecebimento</code> através do valor do atributo
     * <code>codigo</code> da classe <code>NegociacaoRecebimento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ContaReceberNegociacaoRecebimentoVO> consultarPorCodigoNegociacaoRecebimento(Integer valorConsulta, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberNegociacaoRecebimento</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>ContaReceberNegociacaoRecebimentoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ContaReceberNegociacaoRecebimento</code>.
     * @param <code>negociacaoRecebimento</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirContaReceberNegociacaoRecebimentos(Integer negociacaoRecebimento, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>ContaReceberNegociacaoRecebimentoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirContaReceberNegociacaoRecebimentos</code> e <code>incluirContaReceberNegociacaoRecebimentos</code> disponíveis na classe <code>ContaReceberNegociacaoRecebimento</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarContaReceberNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento, List objetos, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>ContaReceberNegociacaoRecebimentoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.NegociacaoRecebimento</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirContaReceberNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimentoPrm, List<ContaReceberNegociacaoRecebimentoVO> objetos, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ContaReceberNegociacaoRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public void alterarSituacaoParaNotificacaoSerasa(final Boolean notificarSerasa, final Integer codigo, UsuarioVO usuario) throws Exception;

    public void alterarSituacaoTodasContaReceberParaNotificacaoSerasaPorMatricula(final Boolean notificarSerasa, final String matricula, UsuarioVO usuario) throws Exception;
    
    Boolean consultarExistenciaContaReceberNegociacaoRecebimentoPorContaReceber(Integer contaReceber) throws Exception;
    
}
