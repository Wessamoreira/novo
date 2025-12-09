package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.DevolucaoChequeVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface FluxoCaixaInterfaceFacade {

	public FluxoCaixaVO novo() throws Exception;

	public void incluir(FluxoCaixaVO obj, UsuarioVO usuario) throws Exception;
	
	public void alterar(FluxoCaixaVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(FluxoCaixaVO obj, UsuarioVO usuario) throws Exception;

	public void fecharCaixa(FluxoCaixaVO obj, UsuarioVO usuario) throws Exception;

	public FluxoCaixaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FluxoCaixaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FluxoCaixaVO> consultarPorDataAbertura(Date prmIni, Date prmFim, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FluxoCaixaVO> consultarPorFluxoCaixaAbertoHoje(Date prmIni, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FluxoCaixaVO> consultarPorNomeUsuario(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FluxoCaixaVO> consultarPorNumeroContaCorrente(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FluxoCaixaVO consultarPorFluxoCaixaAberto(Date prmIni, Integer contaCorrente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void criarMovimentacaoCaixa(Double valor, Integer contaCorrente, String tipoMovimentacao, String tipoOrigem, Integer formaPagamento, Integer codigoOrigem, Integer responsavel, Integer pessoa, Integer fornecedor, Integer banco, Integer parceiro, ChequeVO chequeVO, Integer operadoraCartao, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void criarMovimentacaoCaixaPagamentoCheque(FormaPagamentoNegociacaoRecebimentoVO fpnrVO, Integer contaCorrente, String tipoMovimentacao, String tipoOrigem, Integer formaPagamento, Integer codigoOrigem, Integer responsavel, Integer pessoa, Integer fornecedor, Integer banco, Integer parceiro, Integer operadoraCartao, UsuarioVO usuarioLogado) throws Exception;

	public void criarMovimentacaoCaixaPagamentoCartao(Double valor, Integer contaCorrente, String tipoMovimentacao, String tipoOrigem, Integer formaPagamento, Integer codigoOrigem, Integer responsavel, Integer pessoa, Integer fornecedor, Integer banco, Integer parceiro, Integer operadoraCartao, UsuarioVO usuarioVO, Boolean alterarSaldoCartao) throws Exception;

	public void criarMovimentacaoCaixaPagamentoDepositoConta(Double valor, Integer contaCorrente, String tipoMovimentacao, String tipoOrigem, Integer formaPagamento, Integer codigoOrigem, Integer responsavel, Integer pessoa, Integer fornecedor, Integer banco, Integer parceiro, UsuarioVO usuarioLogado) throws Exception;

	public Double consultarSaldoBoletoBancario(Integer codigoPrm, Date dataInicio, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, Integer usuario) throws Exception;

	public void validarSaldoCaixa(Double valorTroco, Double valorRecebimento, Integer contaCorrente, UsuarioVO usuario) throws Exception;

	public void alterarSaldoFluxoCaixaPorMovimentacaoCaixaEstornoMovimentacaoFinanceira(MovimentacaoFinanceiraVO movimentacaoFinanceira, boolean possuiPermissaoMovimentarCaixaFechado, UsuarioVO usuario) throws Exception;

	public void alterarSaldoInicialFinalFluxoCaixa(final FluxoCaixaVO obj) throws Exception;

	public void alterarSaldoFluxoCaixaPorMovimentacaoCaixaEstornoMovimentacaoFinanceira(DevolucaoChequeVO devCheque, Double valor, UsuarioVO usuario) throws Exception;

	public Double executarConsultaValorRecebidoChequeSaldoFechamentoCaixa(Integer codigoCaixa, Date dataAbertura, Date dataFechamento, Integer codigoFluxoCaixa, Integer usuario) throws Exception;

	public Double executarConsultaValorRecebidoDinheiroSaldoFechamentoCaixa(Integer codigoCaixa, Date dataAbertura, Date dataFechamento, Integer codigoFluxoCaixa, Integer usuario) throws Exception;

	public FluxoCaixaVO consultarPorCodigoFluxoCaixaAberto(Date prmIni, Integer fluxoCaixa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void validarSaldoDinheiroPagamento(FormaPagamentoNegociacaoPagamentoVO obj, UsuarioVO usuarioVO) throws Exception;

	public FluxoCaixaVO consultarUltimoFluxoCaixaPorContaCaixa(Integer contaCaixa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por executar a geracao da movimentação caixa se realizar alteração no saldo do fluxo de caixa de <code>TipoFormaPagamento</code>
	 * BOLETO_BANCARIO, DEPOSITO, DEBITO_EM_CONTA_CORRENTE, ISENCAO, PERMUTA. Para tal, só é possível realizar geração da movimentação caixa nestes
	 * casos quando a conta caixa estiver selecionada.
	 * 
	 * @author Wellington - 18 de mar de 2016
	 * @param valor
	 * @param contaCorrente
	 * @param tipoMovimentacao
	 * @param tipoOrigem
	 * @param formaPagamento
	 * @param codigoOrigem
	 * @param responsavel
	 * @param pessoa
	 * @param fornecedor
	 * @param banco
	 * @param parceiro
	 * @param usuario
	 * @throws Exception
	 */
	void executarGeracaoMovimentacaoCaixaBoletoBancarioDepositoDebitoContaCorrenteIsencaoPermuta(Double valor, Integer contaCorrente, String tipoMovimentacao, String tipoOrigem, Integer formaPagamento, Integer codigoOrigem, Integer responsavel, Integer pessoa, Integer fornecedor, Integer banco, Integer parceiro, Integer operadoraCartao, UsuarioVO usuario) throws Exception;

	public FluxoCaixaVO consultarPorFluxoExistenciaCaixaAberto(Integer contaCorrente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void atualizarDadosSaldoInicial(FluxoCaixaVO fluxoCaixaVO, UsuarioVO usuario)  throws Exception ;

	public void consultarPorEnumCampoConsulta(DataModelo controleConsultaOtimizado, String situacao) throws Exception;
	
	public FluxoCaixaVO consultarPorFluxoCaixaAbertoEmDataRetroativa(Date prmIni, Integer contaCorrente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Boolean validarExistenciaFluxoCaixaAbertoDataAtual(Date prmIni, Integer contaCorrente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}