package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import integracoes.cartao.cielo.sdk.SaleFormaPagamentoNegociacaoRecebimentoVO;
import integracoes.cartao.erede.VendaRede;
import negocio.comuns.arquitetura.UsuarioVO;import negocio.comuns.financeiro.enumerador.TipoOrigemOperadoraCodigoRetornoDCC;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.enumerador.SituacaoTransacaoEnum;
import negocio.comuns.financeiro.enumerador.TipoOrigemOperadoraCodigoRetornoDCC;
import webservice.mundipagg.v1.service.CreateOrderResponse;


public interface FormaPagamentoNegociacaoRecebimentoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code>.
	 */
	public FormaPagamentoNegociacaoRecebimentoVO novo() throws Exception;
	
	public void montarDataCreditoPorConfiguracaoFinanceiraCartao (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, Date dataBaixa, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */

	public void incluirCheque(Integer rec, Integer cheque, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(FormaPagamentoNegociacaoRecebimentoVO obj, ContaCorrenteVO contaCorrente, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(FormaPagamentoNegociacaoRecebimentoVO obj, UsuarioVO usuario) throws Exception;
	
	public void excluirPorMapaPendenciaCartaoCredito(List<Integer> lista, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do atributo 
	 * <code>Integer cheque</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCheque(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do atributo 
	 * <code>Double percJuro</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorPercJuro(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do atributo 
	 * <code>Integer contaCorrente</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorContaCorrente(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do atributo 
	 * <code>Double valorRecebimento</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorRecebimento(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do atributo 
	 * <code>Integer formaPagamento</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorFormaPagamento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do atributo 
	 * <code>codigo</code> da classe <code>NegociacaoRecebimento</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoNegociacaoRecebimento(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do atributo 
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<Integer> consultarCodigoPorNegociacaoRecebimento(Integer valorConsulta, Integer formaPagamento, Integer contaCorrente, Integer operadoraCartao,  UsuarioVO usuario) throws Exception;
	
	public Boolean consultarSeExisteFormaPagamentoNegociacaoRecebimentoRecebidaCartaoCredito(Integer negociacaorecebimento, Integer formaPagamento, Integer contaCorrente, Integer operadoraCartao, String situacao, List<Integer>listaFormaPagamentoNegociacaoRecebimentoExistente, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da <code>FormaPagamentoNegociacaoRecebimentoVO</code> no BD.
	 * Faz uso da operação <code>excluir</code> disponível na classe <code>FormaPagamentoNegociacaoRecebimento</code>.
	 * @param <code>negociacaoRecebimento</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void excluirFormaPagamentoNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, boolean exclusao) throws Exception;

	public void desfazerTodosRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, boolean exclusao) throws Exception;

	public void alterarContasReceberRecebimento(NegociacaoRecebimentoVO negociacaoRecebimento, Integer recebimento, String motivo, Integer responsavel,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
	

	/**
	 * Operação responsável por alterar todos os objetos da <code>FormaPagamentoNegociacaoRecebimentoVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirFormaPagamentoNegociacaoRecebimentos</code> e <code>incluirFormaPagamentoNegociacaoRecebimentos</code> disponíveis na classe <code>FormaPagamentoNegociacaoRecebimento</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarFormaPagamentoNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>FormaPagamentoNegociacaoRecebimentoVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>financeiro.NegociacaoRecebimento</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirFormaPagamentoNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento, UsuarioVO usuario) throws Exception;
	
	public void incluirFormaPagamentoNegociacaoRecebimentosPorMapaPendenciaCartaoCredito(MapaPendenciaCartaoCreditoVO mpcc, UsuarioVO usuario) throws Exception;

	public void criarMovimentacaoCaixa(FormaPagamentoNegociacaoRecebimentoVO obj, NegociacaoRecebimentoVO negociacao, String tipoMovimentacao, UsuarioVO usuario) throws Exception;

	public void criarMovimentacaoContaCartao(FormaPagamentoNegociacaoRecebimentoVO obj, NegociacaoRecebimentoVO negociacao, String tipoMovimentacao, UsuarioVO usuario) throws Exception;    
	
	public void criarMovimentacaoContaCheque(FormaPagamentoNegociacaoRecebimentoVO obj, NegociacaoRecebimentoVO negociacao, String tipoMovimentacao,UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public FormaPagamentoNegociacaoRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);
	
	void incluir(FormaPagamentoNegociacaoRecebimentoVO obj, UsuarioVO usuario) throws Exception;

	void atualizarValoresDeDatasParaFormaPagamentoNegociacaoRecebimento(NegociacaoRecebimentoVO obj, UsuarioVO usuario) throws Exception;
	 
	void preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO obj, FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO, ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO, Date dataAnterior, int nrParcela, int qtdeParcelasCartaoCredito, Double valorRecebimento, UsuarioVO usuarioVO) throws Exception;
	
	void atualizarFormaPagamentoNegociacaoRecebimentoCartaoCreditoComChaveDeTransacao(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, CreateOrderResponse response, UsuarioVO usuario) throws Exception;

	void removerCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception;

	void verificarExistenciaFormaPagamentoNegociacaoRecebimentoValorRecebimentoZerado(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception;

	//void realizarSplit(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs);

	//List<FormaPagamentoNegociacaoRecebimentoVO> consultarFormaPagamentoNegociacaoRecebimentosSplitNaoRealizado() throws Exception;

	//Integer consultarFormaPagamentoNegociacaoRecebimentosSplitNaoRealizadoEnvioSMS() throws Exception;

	void calcularTotalPago(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 20 de abr de 2016 
	 * @param negociacaoRecebimentoVO
	 * @param configuracaoFinanceiroCartaoVO
	 * @param tipoFinanciamentoEnum
	 * @param quantidadeContasReceber
	 * @param quantidadeCartao
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	public FormaPagamentoNegociacaoRecebimentoVO adicionarNovoCartaoCredito(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO, Integer quantidadeContasReceber, Integer quantidadeCartao, UsuarioVO usuarioVO) throws Exception; 

	/** 
	 * @author Victor Hugo de Paula Costa - 2 de jun de 2016 
	 * @param formaPagamentoNegociacaoRecebimentoVO
	 * @param usuario
	 * @throws Exception 
	 */
	void incluirMotivoCancelamento(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 12 de jul de 2016 
	 * @param negociacaoRecebimento
	 * @param usuario
	 * @throws Exception 
	 */
	void incluirFormaPagamentoNegociacaoRecebimentosBaixaDCC(NegociacaoRecebimentoVO negociacaoRecebimento, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 12 de jul de 2016 
	 * @param formaPagamentoNegociacaoRecebimentoVOs
	 * @param situacaoTransacaoEnum
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void incluirLogBaixaCartaoCreditoDCC(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, SituacaoTransacaoEnum situacaoTransacaoEnum, UsuarioVO usuarioVO) throws Exception;
	
	void atualizarFormaPagamentoNegociacaoRecebimentoCartaoCreditoCielo(List<SaleFormaPagamentoNegociacaoRecebimentoVO> vendas, ContaReceberVO contaReceberVO, UsuarioVO usuario, String erro, TipoOrigemOperadoraCodigoRetornoDCC tipoOrigemOperadoraCodigoRetornoDCC, Integer codigoOrigemOperadoraCodigoRetornoDCC, Boolean transacaoProvenienteRecorrencia, Boolean jobExecutadaManualmente, Integer cartaoCreditoDebitoRecorrenciaPessoa, String nomeCustomer) throws Exception;
	
	void atualizarFormaPagamentoNegociacaoRecebimentoCartaoRede(List<VendaRede> vendas, UsuarioVO usuario, Boolean contemErro) throws Exception;

	void alterarTaxaCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception;

	void alterarTaxaAntecipacaoCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception;

	List<FormaPagamentoNegociacaoRecebimentoVO> consultarFormaPagamentoNegociacaoRecebimentoUsadaNaNegociacaoRecebimentoPorContaReceber(Integer contaReceber, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	void inicializarDadosCartaoCreditoRecorrenciaCadastrada(FormaPagamentoNegociacaoRecebimentoVO obj, String matricula, UsuarioVO usuarioVO) throws Exception;
}