package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;

public interface FormaPagamentoNegociacaoPagamentoInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PagamentoVO</code>.
     */
    public FormaPagamentoNegociacaoPagamentoVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PagamentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param formaPagamentoNegociacaoPagamentoVO  Objeto da classe <code>PagamentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(FormaPagamentoNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception;

    public void incluirCheque(Integer rec, Integer cheque, UsuarioVO usuario) throws Exception;

    public void adicionarCodigoPagamentoEmCheque(FormaPagamentoNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PagamentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param formaPagamentoNegociacaoPagamentoVO    Objeto da classe <code>PagamentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(FormaPagamentoNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PagamentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param formaPagamentoNegociacaoPagamentoVO    Objeto da classe <code>PagamentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(FormaPagamentoNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>Pagamento</code> através do valor do atributo
     * <code>codigo</code> da classe <code>NegociacaoPagamento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PagamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoNegociacaoPagamento(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirPagamentos(NegociacaoPagamentoVO nPtRLog, UsuarioVO usuario) throws Exception;

    public void desfazerTodosPagamentos(NegociacaoPagamentoVO nPtRLog, UsuarioVO usuario) throws Exception;

    public void desvincularChequePagamento(ChequeVO chequeVO, Integer contaCorrente, Integer pgto, UsuarioVO usuario) throws Exception;

    public void alterarValorPagoContaPagar(Integer codigoContaReceber, Double valor, UsuarioVO usuario) throws Exception;

    public void alterarContasPagarPagamento(Integer pagamento, String motivo, Integer responsavel, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>PagamentoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirPagamentos</code> e <code>incluirPagamentos</code> disponíveis na classe <code>Pagamento</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarPagamentos(NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception;

    public void prepararContaPagarParaMovimentacao(NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>PagamentoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.NegociacaoPagamento</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirPagamentos(NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception;

    public void criarMovimentacaoCaixa(FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO, NegociacaoPagamentoVO negociacaoPagamentoVO, String tipoMovimentacao, Boolean validarSaldoPagamentoCaixa, UsuarioVO usuario) throws Exception;

    public void criarPendenciaFinanceiraCheque(ChequeVO cheque, NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception;

    public void criarMovimentacaoContaChequeTerceiros(FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO, NegociacaoPagamentoVO negociacaoPagamentoVO, String tipoMovimentacao, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>PagamentoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FormaPagamentoNegociacaoPagamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public void alterarVinculoCheque(Integer codigo, Integer cheque) throws Exception;
    
    public List<ContaCorrenteVO> separarChequesPorContaCorrente(List<FormaPagamentoNegociacaoPagamentoVO> formaPagamento, UsuarioVO usuario, NegociacaoPagamentoVO negociacaoPagamentoVO) throws Exception ;

	FormaPagamentoNegociacaoPagamentoVO consultarFormaPagamentoNegociacaoPagamentoUsadaNaNegociacaoPagamentoPorContaPagar(Integer contaPagar, int nivelMontarDados, UsuarioVO usuario) throws Exception;
  
}
