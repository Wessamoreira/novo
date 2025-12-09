package negocio.facade.jdbc.financeiro;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicHeader;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaMundiPaggVO;
import negocio.comuns.financeiro.DadosEnvioContaMundipagg;
import negocio.comuns.financeiro.DadosSplitMalSucedidoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.GerenciadorSplitTransacaoMundiPaggInterfaceFacade;
import webservice.mundipagg.v2.EnumTypes.HttpContentTypeEnum;
import webservice.mundipagg.v2.EnumTypes.HttpVerbEnum;
import webservice.mundipagg.v2.Utility.HttpResponse;
import webservice.mundipagg.v2.Utility.HttpResponseGenericResponse;
import webservice.mundipagg.v2.Utility.HttpUtility;
import webservice.nfse.goiania.DateConverter;

@Repository
@Scope("singleton")
@Lazy
public class GerenciadorSplitTransacaoMundiPagg extends ControleAcesso implements Serializable, GerenciadorSplitTransacaoMundiPaggInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public GerenciadorSplitTransacaoMundiPagg() throws Exception {
		super();
		setIdEntidade("GerenciadorSplitTransacaoMundiPagg");
	}
	
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return GerenciadorSplitTransacaoMundiPagg.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		GerenciadorSplitTransacaoMundiPagg.idEntidade = idEntidade;
	}
	
	public static final String FINANCIAL_MOVEMENT = "FINANCIAL_MOVEMENT";
	public static final String CHAVE = "CHAVE";
	public static final String DATA_RECEBIMENTO = "DATA_RECEBIMENTO";
	
	public static String executarCriacaoConta(DadosEnvioContaMundipagg dadosEnvioContaMundipagg) throws Exception {
		StringBuilder criarConta = new StringBuilder();
		criarConta.append("{");
		criarConta.append("    \"AccountReference\": \""+dadosEnvioContaMundipagg.getReferenciaConta()+"\",");
		criarConta.append("    \"Addresses\": [");
		criarConta.append("        {");
		criarConta.append("            \"AddressNumber\": \""+dadosEnvioContaMundipagg.getNumeroEndereco()+"\",");
		criarConta.append("            \"City\": \""+dadosEnvioContaMundipagg.getCidade()+"\",");
		criarConta.append("            \"Complement\": \""+dadosEnvioContaMundipagg.getComplemento()+"\",");
		criarConta.append("            \"Country\": \""+dadosEnvioContaMundipagg.getPais()+"\",");
		criarConta.append("            \"District\": \""+dadosEnvioContaMundipagg.getDistrito()+"\",");
		criarConta.append("            \"State\": \""+dadosEnvioContaMundipagg.getEstado()+"\",");
		criarConta.append("            \"StreetAddress\": \""+dadosEnvioContaMundipagg.getEndereco()+"\",");
		criarConta.append("            \"ZipCode\": \""+dadosEnvioContaMundipagg.getCep()+"\"");
		criarConta.append("        }");
		criarConta.append("    ],");
		criarConta.append("    \"DbaName\": \""+dadosEnvioContaMundipagg.getNomeFantasia()+"\",");
		criarConta.append("    \"DocumentNumber\": \""+dadosEnvioContaMundipagg.getCpfCnpj()+"\",");
		criarConta.append("    \"DocumentType\": \"CNPJ\",");
		criarConta.append("    \"Email\": \""+dadosEnvioContaMundipagg.getEmailEmpresa()+"\",");
		criarConta.append("    \"FinancialDetails\": [");
		criarConta.append("        {");
		criarConta.append("            \"Bank\": {");
		criarConta.append("                \"BankCode\": \""+dadosEnvioContaMundipagg.getCodigoBanco()+"\",");
		criarConta.append("                \"AgencyNumber\": \""+dadosEnvioContaMundipagg.getNumeroAgencia()+"\",");
		criarConta.append("                \"AgencyDigit\": \"\",");
		criarConta.append("                \"AccountNumber\": \""+dadosEnvioContaMundipagg.getNumeroContaBanco()+"\",");
		criarConta.append("                \"AccountDigit\": \"\"");
		criarConta.append("            },");
		criarConta.append("            \"Destination\": \""+dadosEnvioContaMundipagg.getDestino()+"\",");
		criarConta.append("            \"Email\": \""+dadosEnvioContaMundipagg.getEmailDestino()+"\"");
		criarConta.append("        }");
		criarConta.append("    ],");
		criarConta.append("    \"LegalName\": \""+dadosEnvioContaMundipagg.getRazaoSocial()+"\",");
		criarConta.append("    \"MCC\": 7372,");
		criarConta.append("    \"PhoneNumber\": \""+dadosEnvioContaMundipagg.getTelefone()+"\",");
		criarConta.append("    \"RequestKey\": \""+dadosEnvioContaMundipagg.getRequestKey()+"\",");
		criarConta.append("    \"SmartWalletKey\": \"8C199AD3-3B2D-4528-B6A2-F28819DB8AD7\",");
		criarConta.append("    \"SocialUserName\": \""+dadosEnvioContaMundipagg.getNomeSocialUsuario()+"\"");
		criarConta.append("}");
		String rawResponse = conexao(criarConta.toString(), "https://smartwallet.mundipagg.com/Account", new Object[] {"SmartWalletKey", "8C199AD3-3B2D-4528-B6A2-F28819DB8AD7"});
		if(!rawResponse.contains("errorCode")) {
			String chaveContaMundiPagg = rawResponse.substring(rawResponse.indexOf("\"accountKey\": \"") + 15, rawResponse.indexOf("\"accountKey\": \"") + 15 + 35);
			incluirContaMundiPagg(criarConta.toString(), rawResponse, chaveContaMundiPagg, "8C199AD3-3B2D-4528-B6A2-F28819DB8AD7", dadosEnvioContaMundipagg.getRequestKey(), dadosEnvioContaMundipagg.getCpfCnpj());
			return chaveContaMundiPagg;
		} 
		return "";
	}
	
	public static String conexao(String criarConta, String endereco, Object... parametros) throws Exception {
		BasicHeader[] HeaderData = new BasicHeader[1];
		HeaderData[0] = new BasicHeader(parametros[0].toString(), parametros[1].toString());
//        SerializeUtility<String> serializerRequest = new SerializeUtility();
//        String RawRequest = serializerRequest.Serialize(criarConta, HttpContentTypeEnum.Json);
        HttpResponse httpResponse = HttpUtility.SendHttpWebRequest(criarConta, HttpVerbEnum.Post, HttpContentTypeEnum.Json, endereco, HeaderData);
        HttpResponseGenericResponse<String> result = new HttpResponseGenericResponse(httpResponse.getRawResponse(), httpResponse.getHttpStatusCode(), String.class);
        return result.getRawResponse();
	}
	
	public static String executarCriacaoSplit(String chaveContaOrigemMundiPagg, String transactionKey, long authorizedAmountInCents, Date createDate, String orderReference) throws Exception {
		StringBuilder criarSplit = new StringBuilder();
		criarSplit.append("{");
		criarSplit.append("    \"CreditItemCollection\": [");
		criarSplit.append("        {");
		criarSplit.append("            \"CommissionedAccountKey\": \"9c9e1a97-dcb5-4098-ad65-df2030587620\",");
		criarSplit.append("            \"Description\": \"\",");
		criarSplit.append("            \"FeeType\": \"Percent\",");
		criarSplit.append("            \"FeeValue\": 1.44,");
		criarSplit.append("            \"HoldTransaction\": false,");
		criarSplit.append("            \"ItemReference\": \"1\",");
		criarSplit.append("            \"LiabilityShift\": null,");
		criarSplit.append("            \"OneTransactionKey\": \"" + transactionKey + "\",");
		criarSplit.append("            \"OriginAccountKey\": \"" + chaveContaOrigemMundiPagg + "\",");
		criarSplit.append("            \"SplitTransaction\": true");
		criarSplit.append("        }");
		criarSplit.append("    ],");
		criarSplit.append("    \"Order\": {");
		criarSplit.append("        \"Amount\": " + authorizedAmountInCents + ",");
		criarSplit.append("        \"OrderDate\": \"" + DateConverter.getConverted(createDate) + "\",");
		criarSplit.append("        \"OrderReference\": \"" + orderReference + "\",");
		criarSplit.append("        \"PaymentDate\": \"" + DateConverter.getConverted(createDate) + "\",");
		criarSplit.append("        \"RefundDate\": null");
		criarSplit.append("    },");
		criarSplit.append("    \"SmartWalletKey\": \"8C199AD3-3B2D-4528-B6A2-F28819DB8AD7\"");
		criarSplit.append("}");
		String response = conexao(Uteis.removerCaracteresEspeciais3(criarSplit.toString()), "https://smartwallet.mundipagg.com/Transaction", new Object[] { "SmartWalletKey", "8C199AD3-3B2D-4528-B6A2-F28819DB8AD7" });	
		String requestKey = response.substring(response.indexOf("\"requestKey\": \"") + 15, response.indexOf("\"requestKey\": \"") + 15 + 36);
		if(response.contains("\"financialMovementKey\"")) {
			String financialMovement = response.substring(response.indexOf("\"financialMovementKey\": \"") + 25, response.indexOf("\"financialMovementKey\": \"") + 25 + 36);
			incluirLogSplitTransacao(criarSplit.toString(), response, financialMovement, false, requestKey);
			return financialMovement;			
		} else {
			incluirLogSplitTransacao(criarSplit.toString(), response, "", false, requestKey);
			throw new Exception("Ocorreu um erro na realização do Split. Entre em contato com o administrador do sistema. "+requestKey);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
	public static void incluirContaMundiPagg(final String rawRequest, final String rawResponse, final String chaveContaMundiPagg, final String smartWalletKey, final String merchantKey, final String cnpj) throws Exception {
		try {
			final String sql = "INSERT INTO ContasMundiPagg(rawRequest, rawResponse, chaveContaMundiPagg, smartWalletKey, merchantkey, cnpj) VALUES ( ?, ?, ?, ?) returning codigo";
			getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, rawRequest);
					sqlInserir.setString(2, rawResponse);
					sqlInserir.setString(3, chaveContaMundiPagg);
					sqlInserir.setString(4, smartWalletKey);
					sqlInserir.setString(5, merchantKey);
					sqlInserir.setString(6, cnpj);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
	public static void incluirLogSplitTransacao(final String rawRequest, final String rawResponse, final String financialMovement, final Boolean cancelado, final String requestKey) throws Exception {
		try {
			final String sql = "INSERT INTO logSplitTransacao(rawRequest, rawResponse, financialMovement, cancelado, requestKey, dataTransacao) VALUES ( ?, ?, ?, ?, ?, ?) returning codigo";
			getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, rawRequest);
					sqlInserir.setString(2, rawResponse);
					sqlInserir.setString(3, financialMovement);
					sqlInserir.setBoolean(4, cancelado);
					sqlInserir.setString(5, requestKey);
					sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(new Date()));
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirDadosSplitMalSucedido(final DadosSplitMalSucedidoVO dadosSplitMalSucedidoVO) throws Exception {
		try {
			final String sql = "INSERT INTO dadossplitmalsucedido(finacialmovementkey, datatentativa, descricaoerro) VALUES (?, ?, ?) returning codigo";
			getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, dadosSplitMalSucedidoVO.getFinacialMovementKey());
					sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(dadosSplitMalSucedidoVO.getDataTentativa()));
					sqlInserir.setString(3, dadosSplitMalSucedidoVO.getDescricaoErro());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDadosSplitMalSucedido(final DadosSplitMalSucedidoVO dadosSplitMalSucedidoVO) throws Exception {
		final String sql = "UPDATE dadossplitmalsucedido SET finacialmovementkey=?, datatentativa=?, descricaoerro=? WHERE ((codigo = ?))";
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setString(1, dadosSplitMalSucedidoVO.getFinacialMovementKey());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(dadosSplitMalSucedidoVO.getDataTentativa()));
				sqlAlterar.setString(3, dadosSplitMalSucedidoVO.getDescricaoErro());
				sqlAlterar.setInt(4, dadosSplitMalSucedidoVO.getCodigo().intValue());
				return sqlAlterar;
			}
		}) == 0) {
			incluirDadosSplitMalSucedido(dadosSplitMalSucedidoVO);
			return;
		}
	}
	
	@Override
	public List<DadosSplitMalSucedidoVO> consultarDadosSplitMalSucedidos() throws Exception {
		String sqlStr = "SELECT * FROM dadossplitmalsucedido";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado));
	}
	
	public List<DadosSplitMalSucedidoVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<DadosSplitMalSucedidoVO> vetResultado = new ArrayList<DadosSplitMalSucedidoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		return vetResultado;
	}
	
	public DadosSplitMalSucedidoVO montarDados(SqlRowSet dadosSQL) throws Exception {
		DadosSplitMalSucedidoVO obj = new DadosSplitMalSucedidoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setFinacialMovementKey(dadosSQL.getString("finacialmovementkey"));
		obj.setDataTentativa(dadosSQL.getDate("datatentativa"));
		obj.setDescricaoErro(dadosSQL.getString("descricaoerro"));
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDadosSplitMalSucedido(DadosSplitMalSucedidoVO obj) throws Exception {
		String sql = "DELETE FROM dadossplitmalsucedido WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
	public void realizarCancelamentoSplitTransacao(List<String> financialMovementKeys) throws Exception {
		DadosSplitMalSucedidoVO dadosSplitMalSucedidoVO = null;
		String response = "";
		String requestKey = "";
		String financialMovementKeyCancel = "";
		for (String financialMovementKey : financialMovementKeys) {
			try {
				response = GerenciadorSplitTransacaoMundiPagg.conexao("", "https://smartwallet.mundipagg.com/Transaction/"+financialMovementKey, new Object[] {"SmartWalletKey", "8C199AD3-3B2D-4528-B6A2-F28819DB8AD7"});
				if (response.contains("financialMovementKey")) {
					requestKey = response.substring(response.indexOf("\"requestKey\": \"") + 15, response.indexOf("\"requestKey\": \"") + 15 + 36);
					financialMovementKeyCancel = response.substring(response.indexOf("\"financialMovementKey\": \"") + 25, response.indexOf("\"financialMovementKey\": \"") + 25 + 36);
					GerenciadorSplitTransacaoMundiPagg.incluirLogSplitTransacao(financialMovementKeyCancel, response, "", true, requestKey);
				} else {
					throw new Exception(response);
				}
			} catch (Exception e) {
				dadosSplitMalSucedidoVO = new DadosSplitMalSucedidoVO();
				dadosSplitMalSucedidoVO.setFinacialMovementKey(financialMovementKey);
				dadosSplitMalSucedidoVO.setDataTentativa(new Date());
				dadosSplitMalSucedidoVO.setDescricaoErro(e.getMessage());
				getFacadeFactory().getGerenciadorSplitTransacaoMundiPaggFacade().incluirDadosSplitMalSucedido(dadosSplitMalSucedidoVO);
				continue;
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
	public void realizarCancelamentoSplitTransacaoMalSucedido(DadosSplitMalSucedidoVO dadosSplitMalSucedidoVO) throws Exception {
		String response = "";
		String requestKey = "";
		String financialMovementKeyCancel = "";
		try {
			response = GerenciadorSplitTransacaoMundiPagg.conexao("", "https://smartwallet.mundipagg.com/Transaction/" + dadosSplitMalSucedidoVO.getFinacialMovementKey(), new Object[] { "SmartWalletKey", "8C199AD3-3B2D-4528-B6A2-F28819DB8AD7" });
			if (response.contains("financialMovementKey")) {
				requestKey = response.substring(response.indexOf("\"requestKey\": \"") + 15, response.indexOf("\"requestKey\": \"") + 15 + 36);
				financialMovementKeyCancel = response.substring(response.indexOf("\"financialMovementKey\": \"") + 25, response.indexOf("\"financialMovementKey\": \"") + 25 + 36);
				GerenciadorSplitTransacaoMundiPagg.incluirLogSplitTransacao(financialMovementKeyCancel, response, "", true, requestKey);
			} else {
				throw new Exception(response);
			}
		} catch (Exception e) {
			dadosSplitMalSucedidoVO.setDescricaoErro(e.getMessage());
			getFacadeFactory().getGerenciadorSplitTransacaoMundiPaggFacade().alterarDadosSplitMalSucedido(dadosSplitMalSucedidoVO);
		}
	}
	
	@Override
	public Integer consultarDadosSplitMalSucedidosEnvioSMS() throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select count(codigo) as qtde from dadossplitmalsucedido ");
		sqlStr.append("where current_date - CAST(dadossplitmalsucedido.datatentativa AS DATE) > 3");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(resultado.next()) {
			return resultado.getInt("qtde");
		}
		return 0;
	}
	
	/**
	 * Consulta contas mundipagg do cliente
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ContaMundiPaggVO> consultarContasMundiPagg(int nivelMontarDados, String cnpj, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM contasmundipagg";
		if(!cnpj.isEmpty()) {
			sql = sql + " WHERE cnpj = '"+cnpj+"'";
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		return montarDadosConsultaContasMundiPagg(rs, nivelMontarDados, usuarioLogado);
	}
	
	public List<ContaMundiPaggVO> montarDadosConsultaContasMundiPagg(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<ContaMundiPaggVO> vetResultado = new ArrayList<ContaMundiPaggVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String consultarMerchantKeyPorChaveContaMundiPagg(String chaveContaMundiPagg, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT merchantkey FROM contasmundipagg WHERE chavecontamundipagg  = '"+chaveContaMundiPagg+"'";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if(rs.next()) {
			return rs.getString("merchantkey");
		}
		return "";
	}
	
	public ContaMundiPaggVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ContaMundiPaggVO obj = new ContaMundiPaggVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setRawRequest(tabelaResultado.getString("rawrequest"));
		obj.setRawResponse(tabelaResultado.getString("rawresponse"));
		obj.setChaveContaMundiPagg(tabelaResultado.getString("chavecontamundipagg"));
		obj.setSmartWalletKey(tabelaResultado.getString("smartwalletkey"));
		obj.setMerchantKey(tabelaResultado.getString("merchantkey"));
		return obj;
	}
}