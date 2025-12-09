package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.CieloCodigoRetornoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.TransacaoCartaoOnlineVO;
import negocio.comuns.financeiro.enumerador.SituacaoTransacaoEnum;
import negocio.comuns.financeiro.enumerador.TipoOrigemOperadoraCodigoRetornoDCC;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

public interface TransacaoCartaoOnlineInterfaceFacade {

	void incluir(TransacaoCartaoOnlineVO transacaoCartaoOnlineVO, UsuarioVO usuarioVO) throws Exception;
	
	TransacaoCartaoOnlineVO montarDados(SqlRowSet resultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 23 de mai de 2016 
	 * @param tipoPessoa
	 * @param codigoPessoa
	 * @param codigoParceiro
	 * @param codigoFornecedor
	 * @param codigoContaReceber
	 * @param codigoContaReceberRecebimento
	 * @param nivelMontarDados
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	List<TransacaoCartaoOnlineVO> consultarPorCodigoFormaPagamentoNegociacaoRecebimento(String tipoPessoa, Integer codigoPessoa, Integer codigoParceiro, Integer codigoFornecedor, Integer codigoContaReceber, Integer codigoContaReceberRecebimento, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 23 de mai de 2016 
	 * @param requisicao
	 * @param respostaRequisicao
	 * @param chavePedido
	 * @param chaveTransacao
	 * @param mensagem
	 * @param formaPagamentoNegociacaoRecebimentoCartaoCreditoVO
	 * @param contaReceberRecebimento
	 * @param situacaoTransacaoEnum
	 * @param cartao
	 * @param numeroParcela
	 * @param valorParcela
	 * @param dataVencimento
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	TransacaoCartaoOnlineVO montarTransacaoCartaoOnline(String requisicao, String respostaRequisicao, String chavePedido, String chaveTransacao, String mensagem, FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO, String contaReceberRecebimento, SituacaoTransacaoEnum situacaoTransacaoEnum, String cartao, String numeroParcela, Double valorParcela, Date dataVencimento, TipoCartaoOperadoraCartaoEnum tipoCartao, ContaReceberVO contaReceberVO, TipoOrigemOperadoraCodigoRetornoDCC tipoOrigemOperadoraCodigoRetornoDCC, Integer codigoOrigemOperadoraCodigoRetornoDCC, Boolean transacaoProvenienteRecorrencia, Boolean jobExecutadaManualmente, Integer cartaoCreditoDebitoRecorrenciaPessoa,  String nomeCustomer, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 23 de mai de 2016 
	 * @param requisicao
	 * @param respostaRequisicao
	 * @param chavePedido
	 * @param chaveTransacao
	 * @param mensagem
	 * @param formaPagamentoNegociacaoRecebimentoCartaoCreditoVO
	 * @param contaReceberRecebimento
	 * @param situacaoTransacaoEnum
	 * @param cartao
	 * @param numeroParcela
	 * @param valorParcela
	 * @param dataVencimento
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void incluirLogTransacaoCartaoOnline(String requisicao, String respostaRequisicao, String chavePedido, String chaveTransacao, String mensagem, FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO, String contaReceberRecebimento, SituacaoTransacaoEnum situacaoTransacaoEnum, String cartao, String numeroParcela, Double valorParcela, Date dataVencimento, TipoCartaoOperadoraCartaoEnum tipoCartao, ContaReceberVO contaReceberVO, TipoOrigemOperadoraCodigoRetornoDCC tipoOrigemOperadoraCodigoRetornoDCC, Integer codigoOrigemOperadoraCodigoRetornoDCC, Boolean transacaoProvenienteRecorrencia, Boolean jobExecutadaManualmente, Integer cartaoCreditoDebitoRecorrenciaPessoa, String nomeCustomer, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 2 de mai de 2016 
	 * @param codigoFormaPagamentoNegociacaoRecebimento
	 * @param nivelMontarDados
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	List<TransacaoCartaoOnlineVO> consultarPorCodigoFormaPagamentoNegociacaoRecebimentoDCC(Integer codigoFormaPagamentoNegociacaoRecebimento, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	List<TransacaoCartaoOnlineVO> consultarPorFiltrosMapaPendenciaCartaoCreditoDCC(String tipoTransacao, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<OperadoraCartaoVO> listaOperadoraCartaoVOs, String matricula, TipoPessoa tipoPessoa, PessoaVO pessoaVO, ParceiroVO parceiroVO, FornecedorVO fornecedorVO, Date dataInicioTransacao, Date dataFimTransacao, String chaveTransacao, UsuarioVO usuarioVO) throws Exception;

	List<TransacaoCartaoOnlineVO> consultarPorMatricula(String matricula, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public void removerVinculoTransacaoCartaoOnlineMatriculaPeriodo(Integer matriculaPeriodo, UsuarioVO usuarioVO)
			throws Exception;
}
