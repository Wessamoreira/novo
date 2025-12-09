package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.GsonBuilder;

import integracoes.cartao.cielo.sdk.SaleFormaPagamentoNegociacaoRecebimentoVO;
import integracoes.cartao.erede.VendaRede;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.ExcluirJsonStrategy;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.CartaoCreditoDebitoRecorrenciaPessoaVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.CieloCodigoRetornoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoTotalVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.SituacaoTransacaoEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.TipoOrigemOperadoraCodigoRetornoDCC;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.FormaPagamentoNegociacaoRecebimentoInterfaceFacade;
import webservice.mundipagg.v1.service.CreateOrderResponse;
import webservice.mundipagg.v1.service.CreditCardTransactionResult;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe
 * <code>FormaPagamentoNegociacaoRecebimentoVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see FormaPagamentoNegociacaoRecebimentoVO
 * @see ControleAcesso
 * @see NegociacaoRecebimento
 */
@Repository
@Scope("singleton")
@Lazy
public class FormaPagamentoNegociacaoRecebimento extends ControleAcesso implements FormaPagamentoNegociacaoRecebimentoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1642635279037329401L;
	protected static String idEntidade;

	public FormaPagamentoNegociacaoRecebimento() throws Exception {
		super();
		setIdEntidade("NegociacaoRecebimento");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code>.
	 */
	public FormaPagamentoNegociacaoRecebimentoVO novo() throws Exception {
		FormaPagamentoNegociacaoRecebimento.incluir(getIdEntidade());
		FormaPagamentoNegociacaoRecebimentoVO obj = new FormaPagamentoNegociacaoRecebimentoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code>. Primeiramente valida
	 * os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>FormaPagamentoNegociacaoRecebimentoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final FormaPagamentoNegociacaoRecebimentoVO obj, UsuarioVO usuario) throws Exception {
		FormaPagamentoNegociacaoRecebimentoVO.validarDados(obj);
		obj.realizarUpperCaseDados();
		final String sql = "INSERT INTO FormaPagamentoNegociacaoRecebimento( negociacaoRecebimento, formaPagamento, valorRecebimento, contaCorrente, qtdeParcelasCartaoCredito, operadoraCartao, taxaOperadora, taxaAntecipacao, categoriaDespesa, contaCorrenteOperadoraCartao, formaPagamentoNegociacaoRecebimentoCartaoCredito, usuarioDesbloqueouFormaRecebimentoNoRecebimento, dataUsuarioDesbloqueouFormaRecebimentoNoRecebimento, configuracaorecebimentocartaocredito, dataCredito) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
				if (obj.getNegociacaoRecebimento().intValue() != 0) {
					sqlInserir.setInt(1, obj.getNegociacaoRecebimento().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				sqlInserir.setInt(2, obj.getFormaPagamento().getCodigo().intValue());
				sqlInserir.setDouble(3, obj.getValorRecebimento().doubleValue());
				if (obj.getContaCorrente().getCodigo().intValue() != 0) {
					sqlInserir.setInt(4, obj.getContaCorrente().getCodigo().intValue());
				} else {
					sqlInserir.setNull(4, 0);
				}
				if (!obj.getQtdeParcelasCartaoCredito().equals(0)) {
					sqlInserir.setInt(5, obj.getQtdeParcelasCartaoCredito());
				} else {
					sqlInserir.setNull(5, 0);
				}
				if (!obj.getOperadoraCartaoVO().getCodigo().equals(0)) {
					sqlInserir.setInt(6, obj.getOperadoraCartaoVO().getCodigo());
				} else {
					sqlInserir.setNull(6, 0);
				}
				Double taxa = obj.getConfiguracaoFinanceiroCartaoVO().getTaxaBancaria(obj.getQtdeParcelasCartaoCredito(), obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum());
				if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSplitRealizado()) {
					if (obj.getTaxaDeOperacao() > 0.0) {
						taxa = obj.getTaxaDeOperacao();
					}
				}
				if (!taxa.equals(0.0)) {
					sqlInserir.setDouble(7, taxa);
				} else if (!obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeOperacao().equals(0.0)) {
					sqlInserir.setDouble(7, obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeOperacao());
				} else {
					sqlInserir.setNull(7, 0);
				}
				
				
				if (!obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeAntecipacao().equals(0.0)) {
					sqlInserir.setDouble(8, obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeAntecipacao());
				} else {
					sqlInserir.setNull(8, 0);
				}
				if (!obj.getCategoriaDespesaVO().getCodigo().equals(0)) {
					sqlInserir.setInt(9, obj.getCategoriaDespesaVO().getCodigo());
				} else {
					sqlInserir.setNull(9, 0);
				}
				if (obj.getContaCorrenteOperadoraCartaoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(10, obj.getContaCorrenteOperadoraCartaoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(10, 0);
				}
				if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(11, obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(11, 0);
				}
				if (obj.getUsuarioDesbloqueouFormaRecebimentoNoRecebimento() != null) {
					sqlInserir.setInt(12, obj.getUsuarioDesbloqueouFormaRecebimentoNoRecebimento().getCodigo().intValue());
				} else {
					sqlInserir.setNull(12, 0);
				}
				if (obj.getDataUsuarioDesbloqueouFormaRecebimentoNoRecebimento() != null) {
					sqlInserir.setDate(13, Uteis.getDataJDBC(obj.getDataUsuarioDesbloqueouFormaRecebimentoNoRecebimento()));
				} else {
					sqlInserir.setNull(13, 0);
				}	
				if(!obj.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0)) {
					sqlInserir.setInt(14, obj.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo());
				} else {
					sqlInserir.setNull(14, 0);
				}
				sqlInserir.setDate(15, Uteis.getDataJDBC(obj.getDataCredito()));
				return sqlInserir;
			}
		}, new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return rs.getInt("codigo");
				}
				return null;
			}
		}));
		if (obj.getFormaPagamento().getTipo().equals("CH")) {
			// obj.getCheque().setCodigo(0);
			Integer cheque = getFacadeFactory().getChequeFacade().incluirChequeRecebimento(obj.getCodigo(), obj.getNegociacaoRecebimentoVO().getContaCorrenteCaixa().getCodigo(), obj.getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), obj.getCheque(), usuario);
			incluirCheque(obj.getCodigo(), cheque, usuario);
		}
		// if (obj.getFormaPagamento().getTipo().equals("CA") ||
		// obj.getFormaPagamento().getTipo().equals("DE")) {
		// preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(obj);
		// getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().persistir(obj.getListaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(),
		// usuario);
		// }
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirCheque(final Integer rec, final Integer cheque, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE FormaPagamentoNegociacaoRecebimento set cheque=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				if (cheque.intValue() != 0) {
					sqlAlterar.setInt(1, cheque.intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setInt(2, rec.intValue());
				return sqlAlterar;
			}
		});
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>FormaPagamentoNegociacaoRecebimentoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final FormaPagamentoNegociacaoRecebimentoVO obj, final ContaCorrenteVO contaCorrente, UsuarioVO usuario) throws Exception {
		FormaPagamentoNegociacaoRecebimentoVO.validarDados(obj);
		obj.realizarUpperCaseDados();
		final String sql = "UPDATE FormaPagamentoNegociacaoRecebimento set negociacaoRecebimento=?, formaPagamento=?, valorRecebimento=?, contaCorrente=?, cheque=?, qtdeParcelasCartaoCredito = ?, operadoraCartao = ?, taxaOperadora = ?, taxaAntecipacao = ?, categoriaDespesa = ?, contaCorrenteOperadoraCartao = ?, formaPagamentoNegociacaoRecebimentoCartaoCredito=?, configuracaorecebimentocartaocredito=?, dataCredito=? WHERE ((codigo = ?))";
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				if (obj.getNegociacaoRecebimento().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getNegociacaoRecebimento().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setInt(2, obj.getFormaPagamento().getCodigo().intValue());
				sqlAlterar.setDouble(3, obj.getValorRecebimento().doubleValue());
				if (obj.getContaCorrente().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(4, obj.getContaCorrente().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(4, 0);
				}
				if (obj.getCheque().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(5, obj.getCheque().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(5, 0);
				}
				if (!obj.getQtdeParcelasCartaoCredito().equals(0)) {
					sqlAlterar.setInt(6, obj.getQtdeParcelasCartaoCredito());
				} else {
					sqlAlterar.setNull(6, 0);
				}
				if (!obj.getOperadoraCartaoVO().getCodigo().equals(0)) {
					sqlAlterar.setInt(7, obj.getOperadoraCartaoVO().getCodigo());
				} else {
					sqlAlterar.setNull(7, 0);
				}
				if (!obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeOperacao().equals(0.0)) {
					sqlAlterar.setDouble(8, obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeOperacao());
				} else {
					sqlAlterar.setNull(8, 0);
				}
				if (!obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeAntecipacao().equals(0.0)) {
					sqlAlterar.setDouble(9, obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeAntecipacao());
				} else {
					sqlAlterar.setNull(9, 0);
				}
				if (!obj.getCategoriaDespesaVO().getCodigo().equals(0)) {
					sqlAlterar.setInt(10, obj.getCategoriaDespesaVO().getCodigo());
				} else {
					sqlAlterar.setNull(10, 0);
				}
				if (obj.getContaCorrenteOperadoraCartaoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(11, obj.getContaCorrenteOperadoraCartaoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(11, 0);
				}
				if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(12, obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(12, 0);
				}
				if(!obj.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0)) {
					sqlAlterar.setInt(13, obj.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo());
				} else {
					sqlAlterar.setNull(13, 0);
				}
				sqlAlterar.setDate(14, Uteis.getDataJDBC(obj.getDataCredito()));
				sqlAlterar.setInt(15, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		}) == 0) {
			incluir(obj, usuario);
			return;
		}
		if (obj.getFormaPagamento().getTipo().equals("CH")) {
			Integer cheque = getFacadeFactory().getChequeFacade().incluirChequeRecebimento(obj.getCodigo(), contaCorrente.getCodigo(), contaCorrente.getUnidadeEnsinoContaCorrenteVOs().get(0).getUnidadeEnsino().getCodigo(), obj.getCheque(), usuario);
			incluirCheque(obj.getCodigo(), cheque, usuario);
		}
		// if (obj.getFormaPagamento().getTipo().equals("CA") ||
		// obj.getFormaPagamento().getTipo().equals("DE")) {
		// preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(obj);
		// getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().persistir(obj.getListaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(),
		// usuario);
		// }
	}
	
	@Override
	public void atualizarValoresDeDatasParaFormaPagamentoNegociacaoRecebimento(NegociacaoRecebimentoVO obj, UsuarioVO usuario) throws Exception {
		for (FormaPagamentoNegociacaoRecebimentoVO fpnr : obj.getFormaPagamentoNegociacaoRecebimentoVOs()) {
			if(fpnr.getFormaPagamento().isCartaoCredito()) {
				atualizarDataVencimentoFormaPagamentoNegociacaoRecebimentoCartaoCredito(fpnr, fpnr.getConfiguracaoFinanceiroCartaoVO(), obj.getData(), fpnr.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConverterParcelaEmNumero(), fpnr.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuario);
			}else if(fpnr.getFormaPagamento().isCartaoDebito()) {
				montarDataCreditoPorConfiguracaoFinanceiraCartao(fpnr, obj.getData(), usuario);
			}
		}
	}

	
	/*@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO) throws Exception {
		FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCredito = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
		if (obj.getQtdeParcelasCartaoCredito().equals(0)) {
			obj.setQtdeParcelasCartaoCredito(1);
		}
		double valorParcela = Uteis.arredondar((obj.getValorRecebimento() / obj.getQtdeParcelasCartaoCredito()), 2, 0);
		Date dataAnterior = new Date();

		for (int nrParcela = 1; nrParcela <= obj.getQtdeParcelasCartaoCredito(); nrParcela++) {
			// formaPagamentoNegociacaoRecebimentoCartaoCredito.setFormaPagamentoNegociacaoRecebimento(obj.getCodigo());
			formaPagamentoNegociacaoRecebimentoCartaoCredito.setNumeroParcela(nrParcela + "/" + obj.getQtdeParcelasCartaoCredito());
			if (nrParcela < obj.getQtdeParcelasCartaoCredito()) {
				formaPagamentoNegociacaoRecebimentoCartaoCredito.setValorParcela(valorParcela);
			} else {
				formaPagamentoNegociacaoRecebimentoCartaoCredito.setValorParcela(Uteis.arredondar((obj.getValorRecebimento() - (valorParcela * (nrParcela - 1))), 2, 0));
			}
			formaPagamentoNegociacaoRecebimentoCartaoCredito.setDataEmissao(new Date());
			dataAnterior.setHours(23);
			formaPagamentoNegociacaoRecebimentoCartaoCredito.setDataVencimento(Uteis.obterDataFutura(dataAnterior, configuracaoFinanceiroCartaoVO.getDiaBaseCreditoConta()));
			dataAnterior = formaPagamentoNegociacaoRecebimentoCartaoCredito.getDataVencimento();

			// obj.getListaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().add(formaPagamentoNegociacaoRecebimentoCartaoCredito);
			formaPagamentoNegociacaoRecebimentoCartaoCredito = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
		}
	}*/

	@Override
	public void preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO obj, FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO, ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO, Date dataAnterior, int nrParcela, int qtdeParcelasCartaoCredito, Double valorRecebimento, UsuarioVO usuarioVO) throws Exception {		
		FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCredito = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
		if (obj.getQtdeParcelasCartaoCredito().equals(0)) {
			obj.setQtdeParcelasCartaoCredito(1);
		}
		if(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)){
			Double valorParcela = Uteis.realizarCalculoParaDivisaoComBigDecimal(valorRecebimento, new Double(qtdeParcelasCartaoCredito));
			if(obj.getOperadoraCartaoVO().getTipoFormaArredondamentoEnum().isArredondado()) {
				valorParcela = Uteis.arredondar(valorParcela, 2, 0); 
			}else {
				valorParcela = Uteis.truncarForcando2CadasDecimais(valorParcela);	
			}
			formaPagamentoNegociacaoRecebimentoCartaoCredito.setNumeroParcela(nrParcela + "/" + qtdeParcelasCartaoCredito);
			if((obj.getOperadoraCartaoVO().isRegraAplicarDiferencaValorReceberPrimeiraParcela() && nrParcela == 1)
					|| (!obj.getOperadoraCartaoVO().isRegraAplicarDiferencaValorReceberPrimeiraParcela() && nrParcela == qtdeParcelasCartaoCredito) ) {
				formaPagamentoNegociacaoRecebimentoCartaoCredito.setValorParcela(Uteis.arredondar((valorRecebimento - (valorParcela * (qtdeParcelasCartaoCredito - 1))), 2, 0));
			}else {
				formaPagamentoNegociacaoRecebimentoCartaoCredito.setValorParcela(valorParcela);
			}
			obj.setValorRecebimento(formaPagamentoNegociacaoRecebimentoCartaoCredito.getValorParcela());
			if (qtdeParcelasCartaoCredito > 1) {
				obj.setTaxaDeOperacao(obj.getConfiguracaoFinanceiroCartaoVO().getTaxaBancaria(qtdeParcelasCartaoCredito, TipoFinanciamentoEnum.INSTITUICAO));
			}
		}else if(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.OPERADORA)){
			formaPagamentoNegociacaoRecebimentoCartaoCredito.setNumeroParcela(qtdeParcelasCartaoCredito+"/"+qtdeParcelasCartaoCredito);
			formaPagamentoNegociacaoRecebimentoCartaoCredito.setValorParcela(valorRecebimento);
			obj.setValorRecebimento(formaPagamentoNegociacaoRecebimentoCartaoCredito.getValorParcela());
		}
		atualizarDataVencimentoFormaPagamentoNegociacaoRecebimentoCartaoCredito(obj, configuracaoFinanceiroCartaoVO, dataAnterior, nrParcela, formaPagamentoNegociacaoRecebimentoCartaoCredito, usuarioVO);
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setSituacao(SituacaoContaReceber.A_RECEBER.getValor());
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setNomeCartaoCredito(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getNomeCartaoCredito());
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setNumeroCartao(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getNumeroCartao());
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setMesValidade(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getMesValidade());
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setAnoValidade(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getAnoValidade());
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setCodigoVerificacao(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getCodigoVerificacao());
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setTipoFinanciamentoEnum(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getTipoFinanciamentoEnum());
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setConfiguracaoFinanceiroCartaoVO(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getConfiguracaoFinanceiroCartaoVO());
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setNumeroReciboTransacao(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getNumeroReciboTransacao());
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setSituacaoTransacao(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getSituacaoTransacao());
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setChaveDaTransacao(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getChaveDaTransacao());
		formaPagamentoNegociacaoRecebimentoCartaoCredito.setContaReceberRecebimento(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getContaReceberRecebimento());
		if (qtdeParcelasCartaoCredito > 1) {
			formaPagamentoNegociacaoRecebimentoCartaoCredito.setSplitRealizado(true);
		} else {
			formaPagamentoNegociacaoRecebimentoCartaoCredito.setSplitRealizado(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getSplitRealizado());
		}
		obj.setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(formaPagamentoNegociacaoRecebimentoCartaoCredito);
	}
	
	private void atualizarDataVencimentoFormaPagamentoNegociacaoRecebimentoCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO, Date dataAnterior, int nrParcela, FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO novoFpnrcc, UsuarioVO usuarioVO) throws Exception {
		novoFpnrcc.setDataEmissao(dataAnterior);
		dataAnterior = UteisData.adicionarHoraEmData(dataAnterior, "23:00");
		Integer diasAvancar = configuracaoFinanceiroCartaoVO.getDiaBaseCreditoConta();
		if (diasAvancar > 0) {
			if(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)){
				diasAvancar = ((diasAvancar * nrParcela) - (nrParcela - 1));
			}
		}		
		
		if (configuracaoFinanceiroCartaoVO.getDiaBaseCreditoContaDiasUteis()) {
			novoFpnrcc.setDataVencimento(getFacadeFactory().getFeriadoFacade().obterDataFuturaOuRetroativaApenasDiasUteis(dataAnterior, diasAvancar, 0, true, true, ConsiderarFeriadoEnum.FINANCEIRO));
		} else {
			Date dataVencimentoTemp = null;
			dataVencimentoTemp = Uteis.obterDataFutura(dataAnterior, diasAvancar);
			novoFpnrcc.setDataVencimento(getFacadeFactory().getFeriadoFacade().obterDataFuturaProximoDiaUtil(dataVencimentoTemp, obj.getNegociacaoRecebimentoVO().getUnidadeEnsino().getCidade().getCodigo(), false, false, ConsiderarFeriadoEnum.FINANCEIRO, usuarioVO));
		}
	}
	
	

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da
	 * operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>FormaPagamentoNegociacaoRecebimentoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(FormaPagamentoNegociacaoRecebimentoVO obj, UsuarioVO usuario) throws Exception {
		excluirFormaPagamentoNegociacaoRecebimentoCartaoCredito(obj, usuario);
		String sql = "DELETE FROM FormaPagamentoNegociacaoRecebimento WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorMapaPendenciaCartaoCredito(List<Integer> lista, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" DELETE FROM FormaPagamentoNegociacaoRecebimentoCartaoCredito ");
		sql.append(" where codigo in  ( ");
		sql.append(" select FormaPagamentoNegociacaoRecebimentoCartaoCredito from formaPagamentoNegociacaoRecebimento where codigo in (").append(UteisTexto.converteListaInteiroParaString(lista)).append(") ");
		sql.append(" ) or formaPagamentoNegociacaoRecebimento in (").append(UteisTexto.converteListaInteiroParaString(lista)).append(")  ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString());
		sql = new StringBuilder(" DELETE FROM FormaPagamentoNegociacaoRecebimento WHERE codigo in (").append(UteisTexto.converteListaInteiroParaString(lista)).append(") ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do
	 * atributo <code>Integer cheque</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCheque(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FormaPagamentoNegociacaoRecebimento WHERE cheque >= " + valorConsulta.intValue() + " ORDER BY cheque";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do
	 * atributo <code>Double percJuro</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorPercJuro(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FormaPagamentoNegociacaoRecebimento WHERE percJuro >= " + valorConsulta.doubleValue() + " ORDER BY percJuro";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do
	 * atributo <code>Integer contaCorrente</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorContaCorrente(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FormaPagamentoNegociacaoRecebimento WHERE contaCorrente >= " + valorConsulta.intValue() + " ORDER BY contaCorrente";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do
	 * atributo <code>Double valorRecebimento</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorRecebimento(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FormaPagamentoNegociacaoRecebimento WHERE valorRecebimento >= " + valorConsulta.doubleValue() + " ORDER BY valorRecebimento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do
	 * atributo <code>Integer formaPagamento</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorFormaPagamento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FormaPagamentoNegociacaoRecebimento WHERE formaPagamento >= " + valorConsulta.intValue() + " ORDER BY formaPagamento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do
	 * atributo <code>codigo</code> da classe <code>NegociacaoRecebimento</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<FormaPagamentoNegociacaoRecebimentoVO> consultarPorCodigoNegociacaoRecebimento(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT FormaPagamentoNegociacaoRecebimento.* FROM FormaPagamentoNegociacaoRecebimento, NegociacaoRecebimento WHERE FormaPagamentoNegociacaoRecebimento.negociacaoRecebimento = NegociacaoRecebimento.codigo and NegociacaoRecebimento.codigo = " + valorConsulta.intValue() + " ORDER BY NegociacaoRecebimento.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public List<Integer> consultarCodigoPorNegociacaoRecebimento(Integer negociacaorecebimento, Integer formaPagamento, Integer contaCorrente, Integer operadoraCartao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sqlStr = new StringBuilder(" Select codigo FROM formapagamentonegociacaorecebimento ");
		sqlStr.append(" where negociacaorecebimento = ").append(negociacaorecebimento);
		sqlStr.append(" and contacorrente = ").append(contaCorrente);
		sqlStr.append(" and operadoracartao = ").append(operadoraCartao);
		sqlStr.append(" and formapagamento = ").append(formaPagamento);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());		
		List<Integer> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			lista.add(tabelaResultado.getInt("codigo"));
		}
		return lista;
	}
	
	public Boolean consultarSeExisteFormaPagamentoNegociacaoRecebimentoRecebidaCartaoCredito(Integer negociacaorecebimento, Integer formaPagamento, Integer contaCorrente, Integer operadoraCartao, String situacao, List<Integer>listaFormaPagamentoNegociacaoRecebimentoExistente, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT count(formapagamentonegociacaorecebimento.codigo) as qtd ");
		sqlStr.append(" FROM formapagamentonegociacaorecebimento  ");
		sqlStr.append(" inner join formapagamentonegociacaorecebimentocartaocredito on formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito = formapagamentonegociacaorecebimentocartaocredito.codigo ");
		sqlStr.append(" where formapagamentonegociacaorecebimento.negociacaorecebimento = ").append(negociacaorecebimento);
		sqlStr.append(" and formapagamentonegociacaorecebimento.contacorrente = ").append(contaCorrente);
		sqlStr.append(" and formapagamentonegociacaorecebimento.operadoracartao = ").append(operadoraCartao);
		sqlStr.append(" and formapagamentonegociacaorecebimento.formapagamento = ").append(formaPagamento);
		if(Uteis.isAtributoPreenchido(situacao)){
			sqlStr.append(" and formapagamentonegociacaorecebimentocartaocredito.situacao = '").append(situacao).append("' ");
		}
		if(Uteis.isAtributoPreenchido(listaFormaPagamentoNegociacaoRecebimentoExistente)){
			sqlStr.append(" and formapagamentonegociacaorecebimento.codigo not in (").append(UteisTexto.converteListaInteiroParaString(listaFormaPagamentoNegociacaoRecebimentoExistente)).append(")  ");	
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return Uteis.isAtributoPreenchido(rs, "qtd", TipoCampoEnum.INTEIRO);
		
		
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do
	 * atributo <code>Integer codigo</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FormaPagamentoNegociacaoRecebimento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da
	 *         consulta.
	 */
	public static List<FormaPagamentoNegociacaoRecebimentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<FormaPagamentoNegociacaoRecebimentoVO> vetResultado = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code>.
	 * 
	 * @return O objeto da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static FormaPagamentoNegociacaoRecebimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FormaPagamentoNegociacaoRecebimentoVO obj = new FormaPagamentoNegociacaoRecebimentoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNegociacaoRecebimento(new Integer(dadosSQL.getInt("negociacaoRecebimento")));
		obj.getFormaPagamento().setCodigo(new Integer(dadosSQL.getInt("formaPagamento")));
		obj.setValorRecebimento(new Double(dadosSQL.getDouble("valorRecebimento")));
		obj.getContaCorrente().setCodigo(new Integer(dadosSQL.getInt("contaCorrente")));
		obj.getContaCorrenteOperadoraCartaoVO().setCodigo(new Integer(dadosSQL.getInt("contaCorrenteOperadoraCartao")));
		obj.getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("operadoraCartao"));
		obj.setQtdeParcelasCartaoCredito(dadosSQL.getInt("qtdeParcelasCartaoCredito"));
		obj.setTaxaDeOperacao(dadosSQL.getDouble("taxaoperadora"));
		obj.getCheque().setCodigo(new Integer(dadosSQL.getInt("cheque")));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigo(dadosSQL.getInt("formaPagamentoNegociacaoRecebimentoCartaoCredito"));
		obj.setDataCredito(dadosSQL.getDate("dataCredito"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("configuracaorecebimentocartaocredito"))) {
			obj.getConfiguracaoRecebimentoCartaoOnlineVO().setCodigo(dadosSQL.getInt("configuracaorecebimentocartaocredito"));
			obj.setConfiguracaoRecebimentoCartaoOnlineVO(getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().consultarPorChavePrimaria(obj.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setContaCorrenteOperadoraCartaoVO(Uteis.montarDadosVO(dadosSQL.getInt("contaCorrenteOperadoraCartao"), ContaCorrenteVO.class, p -> getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(p, false, nivelMontarDados, usuario)));
		montarDadosCheque(obj, nivelMontarDados, usuario);
		montarDadosContaCorrente(obj, nivelMontarDados, usuario);
		montarDadosFormaPagamento(obj, nivelMontarDados, usuario);
		montarDadosOperadoraCartao(obj, nivelMontarDados, usuario);
		montarDadosFormaPagamentoNegociacaoRecebimentoCartaoCredito(obj, nivelMontarDados, usuario);
		obj.setNegociacaoRecebimentoVO(getFacadeFactory().getNegociacaoRecebimentoFacade().consultarPorChavePrimaria(obj.getNegociacaoRecebimento(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, usuario));
		return obj;
	}

	public static void montarDadosCheque(FormaPagamentoNegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCheque().getCodigo().intValue() == 0) {
			obj.setCheque(new ChequeVO());
			return;
		}
		obj.setCheque(getFacadeFactory().getChequeFacade().consultarPorChavePrimaria(obj.getCheque().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosFormaPagamento(FormaPagamentoNegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFormaPagamento().getCodigo().intValue() == 0) {
			obj.setFormaPagamento(new FormaPagamentoVO());
			return;
		}
		obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosOperadoraCartao(FormaPagamentoNegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getOperadoraCartaoVO().getCodigo().intValue() == 0) {
			obj.setOperadoraCartaoVO(new OperadoraCartaoVO());
			return;
		}
		obj.setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(obj.getOperadoraCartaoVO().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosFormaPagamentoNegociacaoRecebimentoCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().intValue() == 0) {
			obj.setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO());
			return;
		}
		obj.setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().consultarPorChavePrimaria(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosContaCorrente(FormaPagamentoNegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCorrente().getCodigo().intValue() == 0) {
			obj.setContaCorrente(new ContaCorrenteVO());
			return;
		}
		obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code> no BD. Faz uso da
	 * operação <code>excluir</code> disponível na classe
	 * <code>FormaPagamentoNegociacaoRecebimento</code>.
	 * 
	 * @param <code>negociacaoRecebimento</code> campo chave para exclusão dos
	 *        objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void excluirFormaPagamentoNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, boolean exclusao) throws Exception {
		try {
			desfazerTodosRecebimentos(negociacaoRecebimento, configuracaoFinanceiroVO, usuario, exclusao);
			String sql = "DELETE FROM FormaPagamentoNegociacaoRecebimento WHERE (negociacaoRecebimento = ?)";
			getConexao().getJdbcTemplate().update(sql, new Object[] { negociacaoRecebimento.getCodigo() });
			Iterator i = negociacaoRecebimento.getChequeVOs().iterator();
			// while (i.hasNext()) {
			// ChequeVO ch = (ChequeVO) i.next();
			// getFacadeFactory().getChequeFacade().excluir(ch);
			// }
		} catch (Exception e) {
			if (e.getLocalizedMessage().toLowerCase().contains("devolucaocheque")) {
				throw new Exception("Não é possivel estornar o recebimento, pois existe uma devolução de cheque vinculado ao cheque utilizado neste recebimento.");
			}
			if (e.getLocalizedMessage().toLowerCase().contains("movimentacaofinanceiraitem")) {
				throw new Exception("Não é possivel estornar o recebimento, pois existe uma movimentação financeiro vinculado ao cheque utilizado neste recebimento.");
			}
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void desfazerTodosRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, boolean exclusao) throws Exception {
		List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>(0);
		try {
			formaPagamentoNegociacaoRecebimentoVOs = consultarPorCodigoNegociacaoRecebimento(negociacaoRecebimento.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario); // ok
			for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : formaPagamentoNegociacaoRecebimentoVOs) {
				alterarContasReceberRecebimento(negociacaoRecebimento, formaPagamentoNegociacaoRecebimentoVO.getCodigo(), negociacaoRecebimento.getMotivoAlteracao(), usuario != null ? usuario.getCodigo() : negociacaoRecebimento.getResponsavel().getCodigo(), configuracaoFinanceiroVO, usuario);
				if(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento() > 0.0) {
					criarMovimentacaoCaixa(formaPagamentoNegociacaoRecebimentoVO, negociacaoRecebimento, "SA", usuario);
					criarMovimentacaoCaixaContaCorrente(formaPagamentoNegociacaoRecebimentoVO, negociacaoRecebimento, TipoMovimentacaoFinanceira.SAIDA, usuario, exclusao);
				}
				if ((TipoFormaPagamento.CARTAO_DE_CREDITO.getValor().equals(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo()) || TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE.getValor().equals(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo())) && formaPagamentoNegociacaoRecebimentoVO.getContaCorrente().getContaCaixa()) {
					atualizarSaldoContaCorrenteQuandoHouverEstornoCartaoCredito(formaPagamentoNegociacaoRecebimentoVO, negociacaoRecebimento, usuario);
					excluirFormaPagamentoNegociacaoRecebimentoCartaoCredito(formaPagamentoNegociacaoRecebimentoVO, usuario);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			formaPagamentoNegociacaoRecebimentoVOs = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterarContasReceberRecebimento(NegociacaoRecebimentoVO negociacaoRecebimento, Integer codigoFormaPagamentoNegociacaoRecebimento, String motivo, Integer responsavel, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs = new ArrayList<ContaReceberRecebimentoVO>(0);
		try {
			contaReceberRecebimentoVOs = getFacadeFactory().getContaReceberRecebimentoFacade().consultarPorFormaPagamentoNegociacaoRecebimento(codigoFormaPagamentoNegociacaoRecebimento, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario); // ok
			if (!contaReceberRecebimentoVOs.isEmpty()) {
				for (ContaReceberRecebimentoVO contaReceberRecebimentoVO : contaReceberRecebimentoVOs) {
					contaReceberRecebimentoVO.setFormaPagamentoNegociacaoRecebimento(0);
					getFacadeFactory().getContaReceberRecebimentoFacade().alterar(contaReceberRecebimentoVO, usuario); // ok
					contaReceberRecebimentoVO.setTipoRecebimento("DE");
					contaReceberRecebimentoVO.setDataRecebimeto(new Date());
					contaReceberRecebimentoVO.setMotivo(motivo);
					contaReceberRecebimentoVO.getResponsavel().setCodigo(responsavel);
					getFacadeFactory().getContaReceberRecebimentoFacade().incluir(contaReceberRecebimentoVO, usuario); // ok
					getFacadeFactory().getContaReceberFacade().alterarValoresDaContaReceberPorEstorno(contaReceberRecebimentoVO.getContaReceber(), "RE", configuracaoFinanceiroVO, negociacaoRecebimento.getBloqueioPorFechamentoMesLiberado(), usuario);
				}
			} else {
				List<ContaReceberVO> listaContaReceber = getFacadeFactory().getContaReceberFacade().consultaRapidaPorCodigoFormaPagamentoNegociacaoRecebimento(codigoFormaPagamentoNegociacaoRecebimento, false, configuracaoFinanceiroVO, usuario);
				for (ContaReceberVO contaReceberVO : listaContaReceber) {					
					getFacadeFactory().getContaReceberFacade().alterarValoresDaContaReceberPorEstorno(contaReceberVO.getCodigo(), "RE", configuracaoFinanceiroVO, negociacaoRecebimento.getBloqueioPorFechamentoMesLiberado(), usuario);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			contaReceberRecebimentoVOs = null;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void montarDataCreditoPorConfiguracaoFinanceiraCartao (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, Date dataBaixa, UsuarioVO usuario) throws Exception{
		if (TipoFormaPagamento.CARTAO_DE_DEBITO.getValor().equals(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo())) {
			Integer diasAvancar = formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getDiaBaseCreditoConta();
			if (formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getDiaBaseCreditoContaDiasUteis()) {
				formaPagamentoNegociacaoRecebimentoVO.setDataCredito(getFacadeFactory().getFeriadoFacade().obterDataFuturaOuRetroativaApenasDiasUteis(dataBaixa, diasAvancar, 0, false, false, ConsiderarFeriadoEnum.FINANCEIRO));
			} else if (!diasAvancar.equals(0)) {
				Date dataVencimentoTemp =Uteis.obterDataAvancada(dataBaixa, diasAvancar);
				formaPagamentoNegociacaoRecebimentoVO.setDataCredito(getFacadeFactory().getFeriadoFacade().obterDataFuturaProximoDiaUtil(dataVencimentoTemp, formaPagamentoNegociacaoRecebimentoVO.getNegociacaoRecebimentoVO().getUnidadeEnsino().getCidade().getCodigo(), false, false, ConsiderarFeriadoEnum.FINANCEIRO, usuario));
				
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirFormaPagamentoNegociacaoRecebimentoCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuario) throws Exception {
		if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals("CA") || formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals("DE")) {
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().excluirPelaFormaPagamentoNegociacaoRecebimento(formaPagamentoNegociacaoRecebimentoVO.getCodigo(), usuario);
		}
	}

	

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code> contidos em um
	 * Hashtable no BD. Faz uso da operação
	 * <code>excluirFormaPagamentoNegociacaoRecebimentos</code> e
	 * <code>incluirFormaPagamentoNegociacaoRecebimentos</code> disponíveis na
	 * classe <code>FormaPagamentoNegociacaoRecebimento</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarFormaPagamentoNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		excluirFormaPagamentoNegociacaoRecebimentos(negociacaoRecebimento, configuracaoFinanceiroVO, usuario, false);
		incluirFormaPagamentoNegociacaoRecebimentos(negociacaoRecebimento, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirFormaPagamentoNegociacaoRecebimentosPorMapaPendenciaCartaoCredito(MapaPendenciaCartaoCreditoVO mpcc, UsuarioVO usuario) throws Exception {
		for (FormaPagamentoNegociacaoRecebimentoVO fpnr : mpcc.getListaFormaPagamentoNegociacaoRecebimentoVO()) {
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().persistir(fpnr.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuario);
			incluir(fpnr ,usuario);
		}
	}

	/**
	 * Operação responsável por incluir objetos da
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code> no BD. Garantindo o
	 * relacionamento com a entidade principal
	 * <code>financeiro.NegociacaoRecebimento</code> através do atributo de
	 * vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirFormaPagamentoNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento, UsuarioVO usuario) throws Exception {
		Iterator<FormaPagamentoNegociacaoRecebimentoVO> e = negociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoVOs().iterator();
		negociacaoRecebimento.setContaCorrenteCaixa(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(negociacaoRecebimento.getContaCorrenteCaixa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		
		while (e.hasNext()) {
			FormaPagamentoNegociacaoRecebimentoVO obj = e.next();
			obj.setNegociacaoRecebimento(negociacaoRecebimento.getCodigo());
			if(Uteis.isAtributoPreenchido(obj.getFormaPagamento()) && !Uteis.isAtributoPreenchido(obj.getFormaPagamento().getTipo())) {
				obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			if (TipoFormaPagamento.CARTAO_DE_CREDITO.getValor().equals(obj.getFormaPagamento().getTipo())) {
				if (!Uteis.isAtributoPreenchido(negociacaoRecebimento.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo())
						&& Uteis.isAtributoPreenchido(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao())) {
					String cartaoFinal = obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().substring(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().length() - 4);
					String cartaoInical = obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().substring(0, 4);
					obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroCartao(cartaoInical + ".XXXX.XXXX." + cartaoFinal);
				}
				getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().persistir(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuario);
			}
			montarDataCreditoPorConfiguracaoFinanceiraCartao(obj, negociacaoRecebimento.getData(), usuario);
			obj.setNegociacaoRecebimentoVO(negociacaoRecebimento);
			incluir(obj ,usuario);
			criarMovimentacaoCaixa(obj, negociacaoRecebimento, "EN", usuario);
			criarMovimentacaoCaixaContaCorrente(obj, negociacaoRecebimento, TipoMovimentacaoFinanceira.ENTRADA, usuario, false);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarMovimentacaoCaixaContaCorrente(FormaPagamentoNegociacaoRecebimentoVO fpnrVO, NegociacaoRecebimentoVO negociacaoRecebimentoVO, TipoMovimentacaoFinanceira tipoMovimentacao, UsuarioVO usuario, boolean exclusao) throws Exception {
		Integer codigoSacado = 0;
		String nomeSacado = "";
		Double taxaBancariaCartaoDebito = 0.0;
		TipoSacadoExtratoContaCorrenteEnum tipoSacado = null;
		Date dataOcorrido = negociacaoRecebimentoVO.getData();
		if (negociacaoRecebimentoVO.getRecebimentoBoletoAutomatico()) {
			dataOcorrido = negociacaoRecebimentoVO.getDataCreditoBoletoBancario();
		} else if (fpnrVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.BOLETO_BANCARIO.getValor())
				|| fpnrVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor())) {
			dataOcorrido = fpnrVO.getDataCredito();
		}
		if (tipoMovimentacao.equals(TipoMovimentacaoFinanceira.SAIDA) && exclusao) {
			dataOcorrido = negociacaoRecebimentoVO.getDataEstorno();
			 if (fpnrVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor())
						&& UteisData.getCompareData(fpnrVO.getDataCredito(), negociacaoRecebimentoVO.getDataEstorno()) > 0){		
					dataOcorrido = fpnrVO.getDataCredito();
				}
		}
		if (fpnrVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor()) 
				&& (fpnrVO.getContaCorrenteOperadoraCartaoVO().isUtilizaTaxaCartaoDebito())) {			
			taxaBancariaCartaoDebito = fpnrVO.getConfiguracaoFinanceiroCartaoVO().getTaxaBancaria(fpnrVO.getQtdeParcelasCartaoCredito(), fpnrVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum());
			if(Uteis.isAtributoPreenchido(taxaBancariaCartaoDebito)){
				taxaBancariaCartaoDebito = Uteis.arrendondarForcandoCadasDecimais(fpnrVO.getValorRecebimento() * (taxaBancariaCartaoDebito / 100),2);	
			}
		}

		if (negociacaoRecebimentoVO.getTipoFuncionario()) {
			codigoSacado = negociacaoRecebimentoVO.getPessoa().getCodigo();
			nomeSacado = negociacaoRecebimentoVO.getPessoa().getNome();
			tipoSacado = TipoSacadoExtratoContaCorrenteEnum.FUNCIONARIO_PROFESSOR;
		} else if (negociacaoRecebimentoVO.getTipoAluno()) {
			codigoSacado = negociacaoRecebimentoVO.getPessoa().getCodigo();
			nomeSacado = negociacaoRecebimentoVO.getPessoa().getNome();
			tipoSacado = TipoSacadoExtratoContaCorrenteEnum.ALUNO;
		} else if (negociacaoRecebimentoVO.getTipoResponsavelFinanceiro()) {
			codigoSacado = negociacaoRecebimentoVO.getPessoa().getCodigo();
			nomeSacado = negociacaoRecebimentoVO.getPessoa().getNome();
			tipoSacado = TipoSacadoExtratoContaCorrenteEnum.RESPONSAVEL_FINANCEIRO;
		} else if (negociacaoRecebimentoVO.getTipoCandidato()) {
			codigoSacado = negociacaoRecebimentoVO.getPessoa().getCodigo();
			nomeSacado = negociacaoRecebimentoVO.getPessoa().getNome();
			tipoSacado = TipoSacadoExtratoContaCorrenteEnum.CANDIDATO;
			if (nomeSacado.equals("")) {
				nomeSacado = getFacadeFactory().getPessoaFacade().consultarNomePorCodigo(codigoSacado);
			}
		} else if (negociacaoRecebimentoVO.getTipoParceiro()) {
			codigoSacado = negociacaoRecebimentoVO.getParceiroVO().getCodigo();
			nomeSacado = negociacaoRecebimentoVO.getParceiroVO().getNome();
			tipoSacado = TipoSacadoExtratoContaCorrenteEnum.PARCEIRO;
		} else if (negociacaoRecebimentoVO.getTipoRequerente()) {
			codigoSacado = negociacaoRecebimentoVO.getPessoa().getCodigo();
			nomeSacado = negociacaoRecebimentoVO.getPessoa().getNome();
			tipoSacado = TipoSacadoExtratoContaCorrenteEnum.REQUERENTE;
		} else if (negociacaoRecebimentoVO.getTipoFornecedor()) {
			codigoSacado = negociacaoRecebimentoVO.getFornecedor().getCodigo();
			nomeSacado = negociacaoRecebimentoVO.getFornecedor().getNome();
			tipoSacado = TipoSacadoExtratoContaCorrenteEnum.FORNECEDOR;
		}

		if ((fpnrVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) 
				|| fpnrVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.BOLETO_BANCARIO.getValor()) 
				|| fpnrVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DEPOSITO.getValor()) 
				|| fpnrVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE.getValor())) 
				&& !fpnrVO.getContaCorrente().getContaCaixa()) {
			getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(fpnrVO.getValorRecebimento(), dataOcorrido, OrigemExtratoContaCorrenteEnum.RECEBIMENTO, tipoMovimentacao, negociacaoRecebimentoVO.getCodigo(), null, nomeSacado, codigoSacado, tipoSacado, fpnrVO, fpnrVO.getFormaPagamento(), fpnrVO.getContaCorrente(), negociacaoRecebimentoVO.getUnidadeEnsino(), null, negociacaoRecebimentoVO.isDesconsiderarConciliacaoBancaria(), negociacaoRecebimentoVO.getValorTaxaBancaria(), negociacaoRecebimentoVO.getBloqueioPorFechamentoMesLiberado(), negociacaoRecebimentoVO.getResponsavel());
		}
		if (tipoMovimentacao.equals(TipoMovimentacaoFinanceira.SAIDA) 
				&& fpnrVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()) 
				&& fpnrVO.getCheque().getPago() 
				&& fpnrVO.getCheque().getSituacao().equals("BA")) {
			getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(fpnrVO.getValorRecebimento(), dataOcorrido, OrigemExtratoContaCorrenteEnum.RECEBIMENTO, tipoMovimentacao, negociacaoRecebimentoVO.getCodigo(), fpnrVO.getCheque(), nomeSacado, codigoSacado, tipoSacado, fpnrVO, fpnrVO.getFormaPagamento(), fpnrVO.getCheque().getLocalizacaoCheque(), negociacaoRecebimentoVO.getUnidadeEnsino(), null, negociacaoRecebimentoVO.isDesconsiderarConciliacaoBancaria(), negociacaoRecebimentoVO.getValorTaxaBancaria(), negociacaoRecebimentoVO.getBloqueioPorFechamentoMesLiberado(),  negociacaoRecebimentoVO.getResponsavel());
		}
		if (tipoMovimentacao.equals(TipoMovimentacaoFinanceira.SAIDA) 
				&& fpnrVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())) {
			List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> formaPagamentoNegociacaoRecebimentoCartaoCreditoVOs = getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().consultarPorFormaPagamentoNegociacaoRecebimento(fpnrVO.getCodigo());
			for (FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO : formaPagamentoNegociacaoRecebimentoCartaoCreditoVOs) {
				if (formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getSituacao().equals("RE")) {
					getFacadeFactory().getExtratoContaCorrenteFacade().validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(OrigemExtratoContaCorrenteEnum.COMPENSACAO_CARTAO, fpnrVO.getCodigo(), negociacaoRecebimentoVO.isDesconsiderarConciliacaoBancaria(), 0, false, usuario);
					getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(fpnrVO.getValorRecebimento(), dataOcorrido, OrigemExtratoContaCorrenteEnum.COMPENSACAO_CARTAO, tipoMovimentacao, negociacaoRecebimentoVO.getCodigo(), null, nomeSacado, codigoSacado, tipoSacado, fpnrVO, fpnrVO.getFormaPagamento(), fpnrVO.getContaCorrenteOperadoraCartaoVO(), negociacaoRecebimentoVO.getUnidadeEnsino(), fpnrVO.getOperadoraCartaoVO(), negociacaoRecebimentoVO.isDesconsiderarConciliacaoBancaria(), negociacaoRecebimentoVO.getValorTaxaBancaria(), negociacaoRecebimentoVO.getBloqueioPorFechamentoMesLiberado(),  negociacaoRecebimentoVO.getResponsavel());
					break;
				}
			}
		}
		if (fpnrVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor())) {			
			getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(fpnrVO.getValorRecebimento(), dataOcorrido, OrigemExtratoContaCorrenteEnum.RECEBIMENTO, tipoMovimentacao, negociacaoRecebimentoVO.getCodigo(), null, nomeSacado, codigoSacado, tipoSacado, fpnrVO, fpnrVO.getFormaPagamento(), fpnrVO.getContaCorrenteOperadoraCartaoVO(), negociacaoRecebimentoVO.getUnidadeEnsino(), fpnrVO.getOperadoraCartaoVO(), negociacaoRecebimentoVO.isDesconsiderarConciliacaoBancaria(), taxaBancariaCartaoDebito, negociacaoRecebimentoVO.getBloqueioPorFechamentoMesLiberado(),  negociacaoRecebimentoVO.getResponsavel());			
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void criarMovimentacaoCaixa(FormaPagamentoNegociacaoRecebimentoVO obj, NegociacaoRecebimentoVO negociacao, String tipoMovimentacao, UsuarioVO usuario) throws Exception {
		if (TipoFormaPagamento.DINHEIRO.getValor().equals(obj.getFormaPagamento().getTipo())) {
			getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getValorRecebimento(), negociacao.getContaCorrenteCaixa().getCodigo(), tipoMovimentacao, "RE", obj.getFormaPagamento().getCodigo(), negociacao.getCodigo(), negociacao.getResponsavel().getCodigo(), negociacao.getPessoa().getCodigo(), negociacao.getFornecedor().getCodigo(), 0, negociacao.getParceiroVO().getCodigo(), obj.getCheque(), 0, usuario);
		} else if ((TipoFormaPagamento.CARTAO_DE_CREDITO.getValor().equals(obj.getFormaPagamento().getTipo()) || TipoFormaPagamento.CARTAO_DE_DEBITO.getValor().equals(obj.getFormaPagamento().getTipo())) && obj.getContaCorrente().getContaCaixa()) {
			criarMovimentacaoContaCartao(obj, negociacao, tipoMovimentacao, usuario);
		} else if (TipoFormaPagamento.CHEQUE.getValor().equals(obj.getFormaPagamento().getTipo())) {
			criarMovimentacaoContaCheque(obj, negociacao, tipoMovimentacao, usuario);
		} else if (Uteis.isAtributoPreenchido(negociacao.getContaCorrenteCaixa()) && negociacao.getContaCorrenteCaixa().getContaCaixa()) {
			Double valor = obj.getValorRecebimento();
			if (TipoFormaPagamento.ISENCAO.getValor().equals(obj.getFormaPagamento().getTipo())) {
				valor = 0.0;
				for (ContaReceberNegociacaoRecebimentoVO crnrVO : negociacao.getContaReceberNegociacaoRecebimentoVOs()) {
					valor += crnrVO.getContaReceber().getValorCalculadoDescontoLancadoRecebimento();
				}
			}
			if (valor > 0.0) {
				getFacadeFactory().getFluxoCaixaFacade().executarGeracaoMovimentacaoCaixaBoletoBancarioDepositoDebitoContaCorrenteIsencaoPermuta(valor, negociacao.getContaCorrenteCaixa().getCodigo(), tipoMovimentacao, "RE", obj.getFormaPagamento().getCodigo(), negociacao.getCodigo(), negociacao.getResponsavel().getCodigo(), negociacao.getPessoa().getCodigo(), negociacao.getFornecedor().getCodigo(), 0, negociacao.getParceiroVO().getCodigo(), 0, usuario);
			}
		}
		if (!TipoFormaPagamento.ISENCAO.getValor().equals(obj.getFormaPagamento().getTipo()) && !TipoFormaPagamento.PERMUTA.getValor().equals(obj.getFormaPagamento().getTipo())) {
			getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(obj.getContaCorrente().getCodigo(), tipoMovimentacao, obj.getValorRecebimento(), usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void criarMovimentacaoContaCheque(FormaPagamentoNegociacaoRecebimentoVO obj, NegociacaoRecebimentoVO negociacao, String tipoMovimentacao, UsuarioVO usuario) throws Exception {
		if (obj.getCheque().getCodigo() != null && obj.getCheque().getCodigo() > 0) {
			getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixaPagamentoCheque(obj, negociacao.getContaCorrenteCaixa().getCodigo(), tipoMovimentacao, "RE", obj.getFormaPagamento().getCodigo(), negociacao.getCodigo(), negociacao.getResponsavel().getCodigo(), negociacao.getPessoa().getCodigo(), negociacao.getFornecedor().getCodigo(), 0, negociacao.getParceiroVO().getCodigo(), 0, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void criarMovimentacaoContaCartao(FormaPagamentoNegociacaoRecebimentoVO obj, NegociacaoRecebimentoVO negociacao, String tipoMovimentacao, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixaPagamentoCartao(obj.getValorRecebimento(), negociacao.getContaCorrenteCaixa().getCodigo(), tipoMovimentacao, "RE", obj.getFormaPagamento().getCodigo(), negociacao.getCodigo(), negociacao.getResponsavel().getCodigo(), negociacao.getPessoa().getCodigo(), negociacao.getFornecedor().getCodigo(), 0, negociacao.getParceiroVO().getCodigo(), 0, usuarioVO, true);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void criarMovimentacaoDepositoConta(FormaPagamentoNegociacaoRecebimentoVO obj, NegociacaoRecebimentoVO negociacao, String tipoMovimentacao, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixaPagamentoDepositoConta(obj.getValorRecebimento(), negociacao.getContaCorrenteCaixa().getCodigo(), tipoMovimentacao, "RE", obj.getFormaPagamento().getCodigo(), negociacao.getCodigo(), negociacao.getResponsavel().getCodigo(), negociacao.getPessoa().getCodigo(), negociacao.getFornecedor().getCodigo(), 0, negociacao.getParceiroVO().getCodigo(), usuarioVO);
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code> relacionados a um
	 * objeto da classe <code>financeiro.NegociacaoRecebimento</code>.
	 * 
	 * @param negociacaoRecebimento
	 *            Atributo de <code>financeiro.NegociacaoRecebimento</code> a
	 *            ser utilizado para localizar os objetos da classe
	 *            <code>FormaPagamentoNegociacaoRecebimentoVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public static List consultarFormaPagamentoNegociacaoRecebimentos(Integer negociacaoRecebimento, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FormaPagamentoNegociacaoRecebimento.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM FormaPagamentoNegociacaoRecebimento WHERE negociacaoRecebimento = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { negociacaoRecebimento });
		while (resultado.next()) {
			FormaPagamentoNegociacaoRecebimentoVO novoObj = new FormaPagamentoNegociacaoRecebimentoVO();
			novoObj = FormaPagamentoNegociacaoRecebimento.montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public FormaPagamentoNegociacaoRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM FormaPagamentoNegociacaoRecebimento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( FormaPagamentoNegociacaoRecebimento ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
    public List<FormaPagamentoNegociacaoRecebimentoVO> consultarFormaPagamentoNegociacaoRecebimentoUsadaNaNegociacaoRecebimentoPorContaReceber(Integer contaReceber, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), false, usuario);
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select formapagamentonegociacaorecebimento.*  ");
    	sb.append(" from formapagamentonegociacaorecebimento ");
    	sb.append(" inner join contareceberrecebimento on contareceberrecebimento.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo ");
    	sb.append(" where contareceberrecebimento.contareceber = ? ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), contaReceber);
    	List<FormaPagamentoNegociacaoRecebimentoVO> lista = new ArrayList<>();
    	while (tabelaResultado.next()) {
    		lista.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
			
		}
    	return lista;
    }

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return FormaPagamentoNegociacaoRecebimento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		FormaPagamentoNegociacaoRecebimento.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSaldoContaCorrenteQuandoHouverEstornoCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, NegociacaoRecebimentoVO negociacaoRecebimento, UsuarioVO usuarioLogado) throws Exception {
		List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> fpnrccVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO>(0);
		Boolean controlador = true;
		Double saldoFinal = 0.0;
		try {
			fpnrccVOs = getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().consultarPorFormaPagamentoNegociacaoRecebimento(formaPagamentoNegociacaoRecebimentoVO.getCodigo());

			for (FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO fpnrcc : fpnrccVOs) {
				if (fpnrcc.getSituacao().equals("AR")) {
					controlador = false;
				}
			}
			if (controlador) {
				saldoFinal = getFacadeFactory().getContaCorrenteFacade().consultarSaldoContaCorrente(formaPagamentoNegociacaoRecebimentoVO.getContaCorrenteOperadoraCartaoVO().getCodigo());
				saldoFinal = saldoFinal - formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento();
				getFacadeFactory().getContaCorrenteFacade().alterarSaldoContaCorrente(formaPagamentoNegociacaoRecebimentoVO.getContaCorrenteOperadoraCartaoVO().getCodigo(), saldoFinal, usuarioLogado);
				getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixaPagamentoCartao(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento(), negociacaoRecebimento.getContaCorrenteCaixa().getCodigo(), "SA", "ECR", formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getCodigo(), negociacaoRecebimento.getCodigo(), negociacaoRecebimento.getResponsavel().getCodigo(), negociacaoRecebimento.getPessoa().getCodigo(), 0, 0, negociacaoRecebimento.getParceiroVO().getCodigo(), 0, usuarioLogado, false);
			}
		} finally {
			fpnrccVOs = null;
			controlador = null;
			saldoFinal = null;
		}
	}

	@Override
	public void atualizarFormaPagamentoNegociacaoRecebimentoCartaoCreditoComChaveDeTransacao(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, CreateOrderResponse response, UsuarioVO usuario) throws Exception {
		Map<String, String> chaveTransacao = new HashMap<String, String>();
		for (int i = 0; i < response.getCreditCardTransactionResultCollection().length; i++) {
			CreditCardTransactionResult cardTransactionResult = response.getCreditCardTransactionResultCollection()[i];
			chaveTransacao.put(cardTransactionResult.getCreditCardNumber().substring(cardTransactionResult.getCreditCardNumber().length() - 4), cardTransactionResult.getTransactionKey());
		}
		for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : formaPagamentoNegociacaoRecebimentoVOs) {
			if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())) {
				if (formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroReciboTransacao(response.getOrderKey());
					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setChaveDaTransacao(chaveTransacao.get(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().substring(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().length() - 4)));
					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacaoTransacao(response.getSuccess());
					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroCartao("XXXX.XXXX.XXXX."+formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().substring(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().length() - 4));
					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoFinanceiroCartaoVO(formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO());
					getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterar(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuario);
				}
			}
		}
	}
	
	@Override
	public void removerCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception {
		negociacaoRecebimentoVO.setValorTotalRecebimento(negociacaoRecebimentoVO.getValorTotalRecebimento() - formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento());
		for (Iterator<FormaPagamentoNegociacaoRecebimentoVO> iterator = negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().iterator(); iterator.hasNext();) {
			FormaPagamentoNegociacaoRecebimentoVO objExistente = (FormaPagamentoNegociacaoRecebimentoVO) iterator.next();
			if ((formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals("CA") && objExistente.getFormaPagamento().getTipo().equals("CA")) 
					&& (formaPagamentoNegociacaoRecebimentoVO.getContaCorrente().getCodigo().intValue() == objExistente.getContaCorrente().getCodigo().intValue()) &&
					(formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getNome().equals(objExistente.getOperadoraCartaoVO().getNome())) &&
					(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().equals(objExistente.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao()))) {
				iterator.remove();
			}
//			negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().remove(formaPagamentoNegociacaoRecebimentoVO);
		}
		Integer x = 1;
		for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO2 : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
			formaPagamentoNegociacaoRecebimentoVO2.setQuantidadeCartao(x++);
		}
		if(negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().isEmpty()){
			negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().clear();
		}
	}
	
	@Override
	public void verificarExistenciaFormaPagamentoNegociacaoRecebimentoValorRecebimentoZerado(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception {
		if(negociacaoRecebimentoVO.getResiduo().equals(0.0)) {
			if(negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().size() > 1) {
				List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
				for (FormaPagamentoNegociacaoRecebimentoVO obj : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
					if(obj.getValorRecebimento().equals(0.0)) {
						formaPagamentoNegociacaoRecebimentoVOs.add(obj);
					}
				}
				negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().removeAll(formaPagamentoNegociacaoRecebimentoVOs);
				Integer x = 1;
				for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO2 : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
					formaPagamentoNegociacaoRecebimentoVO2.setQuantidadeCartao(x++);
				}
			}
		}
	}
	
	@Override
	public void atualizarFormaPagamentoNegociacaoRecebimentoCartaoCreditoCielo(List<SaleFormaPagamentoNegociacaoRecebimentoVO> vendas, ContaReceberVO contaReceberVO, UsuarioVO usuario, String erro, TipoOrigemOperadoraCodigoRetornoDCC tipoOrigemOperadoraCodigoRetornoDCC, Integer codigoOrigemOperadoraCodigoRetornoDCC, Boolean transacaoProvenienteRecorrencia, Boolean jobExecutadaManualmente, Integer cartaoCreditoDebitoRecorrenciaPessoa, String nomeCustomer) throws Exception {								
		for (SaleFormaPagamentoNegociacaoRecebimentoVO venda : vendas) {
			String cartao = venda.getNumeroCartao().substring(venda.getNumeroCartao().length() - 4);
			String codigoContaReceberRecebimento = getFacadeFactory().getContaReceberRecebimentoFacade().consultarContaReceberRecebimentoTransacaoCartao(venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getCodigo());
			TipoCartaoOperadoraCartaoEnum tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO;
			if ("CD".equals(venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamento().getTipo())) {
				tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO;
			}
			if (!Uteis.isAtributoPreenchido(erro)) {
				getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluirLogTransacaoCartaoOnline(
						new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).create().toJson(venda.getSale()), 
						new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).create().toJson(venda.getSale().getPayment()),
						venda.getSale().getPayment().getPaymentId(), 
						venda.getSale().getPayment().getPaymentId(), 
						venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getNegociacaoRecebimentoVO().getMensagemPagamentoCartaoCredito(), 
						venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), 
						codigoContaReceberRecebimento,
						SituacaoTransacaoEnum.APROVADO,
						"XXXX.XXXX.XXXX."+cartao,
						venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela(),
						venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela(),
						venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(),
						tipoCartao,
						contaReceberVO,
						tipoOrigemOperadoraCodigoRetornoDCC,
						codigoOrigemOperadoraCodigoRetornoDCC,
						transacaoProvenienteRecorrencia,
						jobExecutadaManualmente,
						cartaoCreditoDebitoRecorrenciaPessoa,
						nomeCustomer,
						usuario);
				for (FormaPagamentoNegociacaoRecebimentoVO obj : venda.getListaFormaPagamentoNegociacaoRecebimentoVOs()) {
					codigoContaReceberRecebimento = getFacadeFactory().getContaReceberRecebimentoFacade().consultarContaReceberRecebimentoTransacaoCartao(obj.getCodigo());
					obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroReciboTransacao(venda.getSale().getPayment().getTid());//TID
					obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setChaveDaTransacao(venda.getSale().getPayment().getPaymentId());
					obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacaoTransacao(true);
					obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroCartao("XXXX.XXXX.XXXX."+cartao);
					obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoFinanceiroCartaoVO(obj.getConfiguracaoFinanceiroCartaoVO());
					obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setFormaPagamentoNegociacaoRecebimentoVO(obj);
					obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setContaReceberRecebimento(codigoContaReceberRecebimento);
					getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterar(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuario);
				}
			} else {
				venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigo(0);
				getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluirLogTransacaoCartaoOnline(
						new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).create().toJson(venda.getSale()), 
						erro,
						venda.getSale().getPayment().getPaymentId(), 
						venda.getSale().getPayment().getPaymentId(), 
						venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getNegociacaoRecebimentoVO().getMensagemPagamentoCartaoCredito(), 
						venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), 
						codigoContaReceberRecebimento,
						SituacaoTransacaoEnum.REPROVADO,
						"XXXX.XXXX.XXXX."+cartao,
						venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela(),
						venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela(),
						venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(),
						tipoCartao,
						contaReceberVO,
						tipoOrigemOperadoraCodigoRetornoDCC,
						codigoOrigemOperadoraCodigoRetornoDCC,
						transacaoProvenienteRecorrencia,
						jobExecutadaManualmente,
						cartaoCreditoDebitoRecorrenciaPessoa,
						nomeCustomer,
						usuario);
			}						
							
		}
		
	}
	
	@Override
	public void atualizarFormaPagamentoNegociacaoRecebimentoCartaoRede(List<VendaRede> vendas, UsuarioVO usuario, Boolean contemErro) throws Exception {
		for (VendaRede venda : vendas) {
			if (!venda.getRequisicao().trim().isEmpty() &&
					venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()) ||
					venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor())) {
				if (venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
					TipoCartaoOperadoraCartaoEnum tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO;
					if ("CD".equals(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamento().getTipo())) {
						tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO;
					}
					String cartao = venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().substring(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().length() - 4);
					String codigoContaReceberRecebimento = getFacadeFactory().getContaReceberRecebimentoFacade().consultarContaReceberRecebimentoTransacaoCartao(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getCodigo());					
					if (!contemErro) {
						for (FormaPagamentoNegociacaoRecebimentoVO forma : venda.getFormaPagamentoNegociacaoRecebimentoVOs()) {
							forma.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setFormaPagamentoNegociacaoRecebimentoVO(forma);
							forma.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroReciboTransacao(venda.getCodigoAutorizacao());
							forma.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setChaveDaTransacao(venda.getTid());
							forma.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacaoTransacao(true);
							forma.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroCartao("XXXX.XXXX.XXXX."+cartao);
							forma.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoFinanceiroCartaoVO(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getConfiguracaoFinanceiroCartaoVO());
							forma.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setContaReceberRecebimento(codigoContaReceberRecebimento);
						}
						getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluirLogTransacaoCartaoOnline(
								venda.getRequisicao(), venda.getResposta(), venda.getCodigoAutorizacao(), venda.getTid(), 
								venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getNegociacaoRecebimentoVO().getMensagemPagamentoCartaoCredito(), 
								venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), 
								codigoContaReceberRecebimento, SituacaoTransacaoEnum.APROVADO, "XXXX.XXXX.XXXX."+cartao,
								venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela(),
								venda.getValor(),
								venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(),
								tipoCartao, null, null, null, false, false, 0, "", usuario);
							for (FormaPagamentoNegociacaoRecebimentoVO f : venda.getFormaPagamentoNegociacaoRecebimentoVOs()) {
								getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterar(f.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuario);
							}
					} else {
						for (FormaPagamentoNegociacaoRecebimentoVO forma : venda.getFormaPagamentoNegociacaoRecebimentoVOs()) {
							forma.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigo(0);
							forma.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setFormaPagamentoNegociacaoRecebimentoVO(forma);
						}
						getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluirLogTransacaoCartaoOnline(
								venda.getRequisicao(), venda.getResposta(),	venda.getCodigoAutorizacao(), venda.getTid(),
								venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getNegociacaoRecebimentoVO().getMensagemPagamentoCartaoCredito(), 
								venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), 
								codigoContaReceberRecebimento, SituacaoTransacaoEnum.REPROVADO,	"XXXX.XXXX.XXXX."+cartao,
								venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela(),
								venda.getValor(),
								venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(),
								tipoCartao, null, null, null, false, false, 0, "", usuario);
					}
				}
			}
		}
	}
	
	@Override
	public FormaPagamentoNegociacaoRecebimentoVO adicionarNovoCartaoCredito(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO, Integer quantidadeContasReceber, Integer quantidadeCartao, UsuarioVO usuarioVO) throws Exception {
		if(!negociacaoRecebimentoVO.getResiduo().equals(0.00)) {
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
			formaPagamentoNegociacaoRecebimentoVO.setQuantidadeCartao(quantidadeCartao);
			formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroParcela(quantidadeCartao.toString());
			if (!Uteis.isAtributoPreenchido(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getFormaPagamentoPadraoRecebimentoOnline().getCodigo())) {
				TipoFormaPagamento tipo = TipoFormaPagamento.CARTAO_DE_CREDITO;
				if (TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO.name().equals(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getTipo())) {
					tipo = TipoFormaPagamento.CARTAO_DE_DEBITO;
				}
				formaPagamentoNegociacaoRecebimentoVO.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorTipo(tipo.getValor(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
			} else {
				formaPagamentoNegociacaoRecebimentoVO.setFormaPagamento(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getFormaPagamentoPadraoRecebimentoOnline());
			}
			formaPagamentoNegociacaoRecebimentoVO.montarListasSelectItemParcelas(quantidadeContasReceber);
			formaPagamentoNegociacaoRecebimentoVO.setQtdeParcelasCartaoCredito(1);
			formaPagamentoNegociacaoRecebimentoVO.setConfiguracaoRecebimentoCartaoOnlineVO(negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO());
			formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoFinanceiroCartaoVO(configuracaoFinanceiroCartaoVO);
			formaPagamentoNegociacaoRecebimentoVO.setConfiguracaoFinanceiroCartaoVO(configuracaoFinanceiroCartaoVO);
			formaPagamentoNegociacaoRecebimentoVO.setOperadoraCartaoVO(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO());
			formaPagamentoNegociacaoRecebimentoVO.setContaCorrenteOperadoraCartaoVO(configuracaoFinanceiroCartaoVO.getContaCorrenteVO());
			formaPagamentoNegociacaoRecebimentoVO.setContaCorrente(configuracaoFinanceiroCartaoVO.getContaCorrenteVO());
			formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO()
			.setTipoFinanciamentoEnum(negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO()
					.getTipoFinanciamentoPermitido(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento(),
							negociacaoRecebimentoVO.realizarCalculoMaiorDataVencimento(), usuarioVO, negociacaoRecebimentoVO.getListaTipoOrigemContaReceber()));
			negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().add(formaPagamentoNegociacaoRecebimentoVO);
			if (negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().size() == 1) {
				negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).setValorRecebimento(negociacaoRecebimentoVO.getValorTotal());
			} else {
				formaPagamentoNegociacaoRecebimentoVO.setValorRecebimento(negociacaoRecebimentoVO.getResiduo());
			}
			inicializarDadosCartaoCreditoRecorrenciaCadastrada(formaPagamentoNegociacaoRecebimentoVO, negociacaoRecebimentoVO.getMatricula(), usuarioVO);
			return formaPagamentoNegociacaoRecebimentoVO;			
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_FormaPagamentoNegociacaoRecebimento_valorRestanteIgualZero"));
		}
		
	}
	
	@Override
	public void calcularTotalPago(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception {
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorParcela(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento());
		negociacaoRecebimentoVO.setValorTotalRecebimento(0.0);
		for (FormaPagamentoNegociacaoRecebimentoVO obj : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
			negociacaoRecebimentoVO.setValorTotalRecebimento(negociacaoRecebimentoVO.getValorTotalRecebimento() + obj.getValorRecebimento());					
			if(Uteis.arrendondarForcando2CadasDecimais(negociacaoRecebimentoVO.getValorTotalRecebimento()) > Uteis.arrendondarForcando2CadasDecimais(negociacaoRecebimentoVO.getValorTotal())) {
				throw new Exception(UteisJSF.internacionalizar("msg_FormaPagamentoNegociacaoRecebimento_valorRecebimentoMaiorQueORestante"));					
			}
		}
		getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().verificarExistenciaFormaPagamentoNegociacaoRecebimentoValorRecebimentoZerado(negociacaoRecebimentoVO, usuarioVO);
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirMotivoCancelamento(final FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE FormaPagamentoNegociacaoRecebimento set motivocancelamento=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setString(1, formaPagamentoNegociacaoRecebimentoVO.getMotivoCancelamento());
				sqlAlterar.setInt(2, formaPagamentoNegociacaoRecebimentoVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	
	/**
	 * Operação responsável por incluir objetos da
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code> no BD. Garantindo o
	 * relacionamento com a entidade principal
	 * <code>financeiro.NegociacaoRecebimento</code> através do atributo de
	 * vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirFormaPagamentoNegociacaoRecebimentosBaixaDCC(NegociacaoRecebimentoVO negociacaoRecebimento, UsuarioVO usuario) throws Exception {
		Iterator<FormaPagamentoNegociacaoRecebimentoVO> e = negociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoVOs().iterator();
		negociacaoRecebimento.setContaCorrenteCaixa(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(negociacaoRecebimento.getContaCorrenteCaixa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		while (e.hasNext()) {
			FormaPagamentoNegociacaoRecebimentoVO obj = e.next();
			obj.setNegociacaoRecebimento(negociacaoRecebimento.getCodigo());
			if (TipoFormaPagamento.CARTAO_DE_CREDITO.getValor().equals(obj.getFormaPagamento().getTipo())) {
				getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().persistir(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuario);
			}
			incluir(obj, usuario);
			criarMovimentacaoCaixa(obj, negociacaoRecebimento, "EN", usuario);
			getFacadeFactory().getContaReceberFacade().alterarSituacaoPagoDCCFalso(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getContaReceberNegociacaoRecebimentoVO().getContaReceber().getCodigo());
			
			MapaPendenciaCartaoCreditoVO mapaPendenciaCartaoCreditoVO = new MapaPendenciaCartaoCreditoVO();
			mapaPendenciaCartaoCreditoVO.setFormaPagamentoNegociacaoRecebimentoVO(obj);
			mapaPendenciaCartaoCreditoVO.setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO());			
			MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO = new MapaPendenciaCartaoCreditoTotalVO();
			mapaPendenciaCartaoCreditoTotalVO.setOperadoraCartaoVO(obj.getOperadoraCartaoVO());
			mapaPendenciaCartaoCreditoTotalVO.setContaCorrenteVO(obj.getContaCorrenteOperadoraCartaoVO());
			List<MapaPendenciaCartaoCreditoTotalVO> mapaPendenciaCartaoCreditoTotalVOs = new ArrayList<MapaPendenciaCartaoCreditoTotalVO>(0);
			mapaPendenciaCartaoCreditoTotalVOs.add(mapaPendenciaCartaoCreditoTotalVO);
			getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(mapaPendenciaCartaoCreditoVO, mapaPendenciaCartaoCreditoTotalVOs, true, negociacaoRecebimento.getData());
			getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().executarBaixaParcelaCartaoCredito(mapaPendenciaCartaoCreditoTotalVOs, false, 
					obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(), 
					negociacaoRecebimento.getData(),
					obj.getFormaPagamento(),  usuario);
		}
	}
	
	@Override
	public void incluirLogBaixaCartaoCreditoDCC(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, SituacaoTransacaoEnum situacaoTransacaoEnum, UsuarioVO usuarioVO) throws Exception {
		for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : formaPagamentoNegociacaoRecebimentoVOs) {
			String codigoContaReceberRecebimento = getFacadeFactory().getContaReceberRecebimentoFacade().consultarContaReceberRecebimentoTransacaoCartao(formaPagamentoNegociacaoRecebimentoVO.getCodigo());
			getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluirLogTransacaoCartaoOnline("", "", "", 
					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao(), 
					"", formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), 
					codigoContaReceberRecebimento, situacaoTransacaoEnum, formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMascaraNumeroCartao(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela(),
					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(),
					TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO, null, null, null, false, false, 0, "", usuarioVO);
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterarDataCredito(final FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception {
		if(formaPagamentoNegociacaoRecebimentoVO.getTaxaDeOperacao().equals(0.0)) {
			throw new Exception("O campo TAXA OPERADORA deve ser informado.");
		}
		
		final String sql = "UPDATE FormaPagamentoNegociacaoRecebimento set taxaOperadora = ? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setDouble(1, formaPagamentoNegociacaoRecebimentoVO.getTaxaDeOperacao());				
				sqlAlterar.setInt(2, formaPagamentoNegociacaoRecebimentoVO.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterarTaxaCartaoCredito(final FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception {
		if(formaPagamentoNegociacaoRecebimentoVO.getTaxaDeOperacao().equals(0.0)) {
			throw new Exception("O campo TAXA OPERADORA deve ser informado.");
		}
		
		final String sql = "UPDATE FormaPagamentoNegociacaoRecebimento set taxaOperadora = ? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setDouble(1, formaPagamentoNegociacaoRecebimentoVO.getTaxaDeOperacao());				
				sqlAlterar.setInt(2, formaPagamentoNegociacaoRecebimentoVO.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterarTaxaAntecipacaoCartaoCredito(final FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception {
		if(formaPagamentoNegociacaoRecebimentoVO.getTaxaDeAntecipacao().equals(0.0)) {
			throw new Exception("O campo TAXA ANTECIPAÇÃO deve ser informado.");
		}
		
		final String sql = "UPDATE FormaPagamentoNegociacaoRecebimento set taxaAntecipacao = ? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setDouble(1, formaPagamentoNegociacaoRecebimentoVO.getTaxaDeAntecipacao());				
				sqlAlterar.setInt(2, formaPagamentoNegociacaoRecebimentoVO.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	public void inicializarDadosCartaoCreditoRecorrenciaCadastrada(FormaPagamentoNegociacaoRecebimentoVO obj, String matricula, UsuarioVO usuarioVO) {
		String caminhoChavePrivada;
		try {
			caminhoChavePrivada = getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().getCaminhoChavePrivada();
			CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO = getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().consultarPorMatriculaPrimeiroCartaoCadastrado(matricula, usuarioVO);
			if (Uteis.isAtributoPreenchido(cartaoCreditoDebitoRecorrenciaPessoaVO.getCodigo())) {
				obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroCartao(Uteis.decriptografarPorAlgoritimoRSA(cartaoCreditoDebitoRecorrenciaPessoaVO.getNumeroCartao(), caminhoChavePrivada));
				obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigoVerificacao(Uteis.decriptografarPorAlgoritimoRSA(cartaoCreditoDebitoRecorrenciaPessoaVO.getCodigoSeguranca(), caminhoChavePrivada));

				obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setMesValidade(cartaoCreditoDebitoRecorrenciaPessoaVO.getMesValidade());
				obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setAnoValidade(cartaoCreditoDebitoRecorrenciaPessoaVO.getAnoValidade());
				obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNomeCartaoCredito(cartaoCreditoDebitoRecorrenciaPessoaVO.getNomeCartao());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
