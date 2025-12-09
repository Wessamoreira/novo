package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.TaxaOperacaoCartaoVO;
import negocio.comuns.financeiro.enumerador.OperadoraCartaoCreditoEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.ConfiguracaoFinanceiroCartaoInterfaceFacade;
import negocio.interfaces.financeiro.ConfiguracaoFinanceiroCartaoRecebimentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ConfiguracaoFinanceiroVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ConfiguracaoFinanceiroVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ConfiguracaoFinanceiroVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoFinanceiroCartao extends ControleAcesso implements ConfiguracaoFinanceiroCartaoInterfaceFacade {

	protected static String idEntidade;

	public ConfiguracaoFinanceiroCartao() throws Exception {
		super();
		setIdEntidade("ConfiguracaoFinanceiro");
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoFinanceiroCartaoVO obj) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO ConfiguracaoFinanceiroCartao( operadoraCartao, contaCorrente, categoriaDespesa, "
					+ "configuracaoFinanceiro, permitiRecebimentoCartaoOnline, taxaDeOperacao, taxaDeAntecipacao, diaBaseCreditoConta, "
					+ "quantidadeParcelasCartaoCredito, diaBaseCreditoContaDiasUteis, centroResultadoAdministrativo) "
					+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getOperadoraCartaoVO().getCodigo());
					sqlInserir.setInt(2, obj.getContaCorrenteVO().getCodigo());
					sqlInserir.setInt(3, obj.getCategoriaDespesaVO().getCodigo());
					sqlInserir.setInt(4, obj.getConfiguracaoFinanceiroVO().getCodigo());
					sqlInserir.setBoolean(5, obj.getPermitiRecebimentoCartaoOnline());
					sqlInserir.setDouble(6, obj.getTaxaDeOperacao());
					sqlInserir.setDouble(7, obj.getTaxaDeAntecipacao());
					sqlInserir.setInt(8, obj.getDiaBaseCreditoConta());
					sqlInserir.setInt(9, obj.getQuantidadeParcelasCartaoCredito());
					sqlInserir.setBoolean(10, obj.getDiaBaseCreditoContaDiasUteis());
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), 11, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			persistirTaxaOperacaoCartaoVOs(obj);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirTaxaOperacaoCartao(final TaxaOperacaoCartaoVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO TaxaOperacaoCartao( configuracaoFinanceiroCartao, parcela, taxa, tipoFinanciamento, descricao, situacao) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getConfiguracaoFinanceiroCartao());
					sqlInserir.setInt(2, obj.getParcela());
					sqlInserir.setDouble(3, obj.getTaxa());
					sqlInserir.setString(4, obj.getTipoFinanciamentoEnum().getName());
					sqlInserir.setString(5, obj.getDescricao());
					sqlInserir.setString(6, obj.getSituacao());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirTaxaOperacaoCartaoVOs(ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO) throws Exception {
		try {
			this.excluirTaxaOperacaoPelaConfiguracaoCartaoFinanceiro(configuracaoFinanceiroCartaoVO.getCodigo(), configuracaoFinanceiroCartaoVO.getConfFinCartaoTaxaOperacao());
			for (TaxaOperacaoCartaoVO obj : configuracaoFinanceiroCartaoVO.getConfFinCartaoTaxaOperacao()) {
				if (obj.getConfiguracaoFinanceiroCartao() == null || obj.getConfiguracaoFinanceiroCartao().equals(0)) {
					obj.setConfiguracaoFinanceiroCartao(configuracaoFinanceiroCartaoVO.getCodigo());
				}
				this.persistirTaxaOperacao(obj);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoFinanceiroCartaoVO obj) throws Exception {
		try {
			validarDados(obj);
			final String sql = "UPDATE ConfiguracaoFinanceiroCartao set operadoraCartao=?, contaCorrente=?, categoriaDespesa=?, "
					+ " configuracaoFinanceiro=?, permitiRecebimentoCartaoOnline = ?, taxaDeOperacao = ?, taxaDeAntecipacao=?,"
					+ " diaBaseCreditoConta=?, quantidadeParcelasCartaoCredito=?, diaBaseCreditoContaDiasUteis=?, "
					+ " centroResultadoAdministrativo=? "
					+ " WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getOperadoraCartaoVO().getCodigo());
					sqlAlterar.setInt(2, obj.getContaCorrenteVO().getCodigo());
					sqlAlterar.setInt(3, obj.getCategoriaDespesaVO().getCodigo());
					sqlAlterar.setInt(4, obj.getConfiguracaoFinanceiroVO().getCodigo());
					sqlAlterar.setBoolean(5, obj.getPermitiRecebimentoCartaoOnline());
					sqlAlterar.setDouble(6, obj.getTaxaDeOperacao());
					sqlAlterar.setDouble(7, obj.getTaxaDeAntecipacao());
					sqlAlterar.setInt(8, obj.getDiaBaseCreditoConta());
					sqlAlterar.setInt(9, obj.getQuantidadeParcelasCartaoCredito());
					sqlAlterar.setBoolean(10, obj.getDiaBaseCreditoContaDiasUteis());
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), 11, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), 12, sqlAlterar);
					
					return sqlAlterar;
				}
			});
			persistirTaxaOperacaoCartaoVOs(obj);

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTaxaOperacaoCartao(final TaxaOperacaoCartaoVO obj) throws Exception {
		try {
			final String sql = "UPDATE TaxaOperacaoCartao set configuracaoFinanceiroCartao=?, parcela = ?, taxa = ?, tipoFinanciamento=?, descricao=?, situacao=?"
					+ " WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getConfiguracaoFinanceiroCartao());
					sqlAlterar.setInt(2, obj.getParcela());
					sqlAlterar.setDouble(3, obj.getTaxa());
					sqlAlterar.setString(4, obj.getTipoFinanciamentoEnum().getName());
					sqlAlterar.setString(5, obj.getDescricao());
					sqlAlterar.setString(6, obj.getSituacao());
					sqlAlterar.setInt(7, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoFinanceiroCartaoVO obj) throws Exception {
		try {
			String sql = "DELETE FROM ConfiguracaoFinanceiroCartao WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code>. Sempre localiza o registro a ser excluído através ConfiguracaoFinanceiro. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPelaConfiguracaoFinanceiro(Integer codigoConfiguracaoFinanceiro, List<ConfiguracaoFinanceiroCartaoVO> listaConfiguracaoFinanceiroCartao) throws Exception {
		try {
			StringBuilder sqlStr = new StringBuilder("DELETE FROM ConfiguracaoFinanceiroCartao WHERE configuracaoFinanceiro = ");
			sqlStr.append(codigoConfiguracaoFinanceiro);
			if (!listaConfiguracaoFinanceiroCartao.isEmpty()) {
				for (ConfiguracaoFinanceiroCartaoVO obj : listaConfiguracaoFinanceiroCartao) {
					sqlStr.append(" AND codigo != ").append(obj.getCodigo());
				}
			}
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTaxaOperacaoPelaConfiguracaoCartaoFinanceiro(Integer codigoConfiguracao, List<TaxaOperacaoCartaoVO> lista) throws Exception {
		try {
			StringBuilder sqlStr = new StringBuilder("DELETE FROM TaxaOperacaoCartao WHERE configuracaoFinanceiroCartao = ");
			sqlStr.append(codigoConfiguracao);
			if (!lista.isEmpty()) {
				for (TaxaOperacaoCartaoVO obj : lista) {
					sqlStr.append(" AND codigo != ").append(obj.getCodigo());
				}
			}
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param ConfiguracaoFinanceiroCartaoVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoFinanceiroCartaoVO obj) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj);
		} else {
			alterar(obj);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirTaxaOperacao(TaxaOperacaoCartaoVO obj) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluirTaxaOperacaoCartao(obj);
		} else {
			alterarTaxaOperacaoCartao(obj);
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public void validarDados(ConfiguracaoFinanceiroCartaoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		ConsistirException consistir = new ConsistirException();
		if (obj.getOperadoraCartaoVO().getCodigo() == null || obj.getOperadoraCartaoVO().getCodigo().equals(0)) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoFinanceiroCartao_operadoraCartao"));
		}
		if (obj.getContaCorrenteVO().getCodigo() == null || obj.getContaCorrenteVO().getCodigo().equals(0)) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoFinanceiroCartao_contaCorrente"));
		}
		if (obj.getCategoriaDespesaVO().getCodigo() == null || obj.getCategoriaDespesaVO().getCodigo().equals(0)) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoFinanceiroCartao_categoriaDespesa"));
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCentroResultadoAdministrativo()), "O campo Centro Resultado Administrativo do Cartão "+obj.getOperadoraCartaoVO().getNome()+" deve ser informado.");
		if (consistir.existeErroListaMensagemErro()) {
			throw consistir;
		}

	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code>.
	 */
	public void validarUnicidade(List<ConfiguracaoFinanceiroCartaoVO> lista, ConfiguracaoFinanceiroCartaoVO obj) throws ConsistirException {
		for (ConfiguracaoFinanceiroCartaoVO repetido : lista) {
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoFinanceiroCartao</code> através do valor do atributo <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ConfiguracaoFinanceiroCartaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT * FROM ConfiguracaoFinanceiroCartao WHERE upper( nome ) like(?) ORDER BY nome";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoFinanceiroCartao</code> através do valor do atributo <code>String tipo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ConfiguracaoFinanceiroCartaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM ConfiguracaoFinanceiroCartao ");
		sqlStr.append("INNER JOIN operadoraCartao ON operadoraCartao.codigo = ConfiguracaoFinanceiroCartao.operadoraCartao ");
		sqlStr.append("WHERE upper( tipo ) like(?) ORDER BY nome");
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toUpperCase()), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoFinanceiroCartao</code> através do valor do atributo <code>String tipo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ConfiguracaoFinanceiroCartaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoOperadoraCartao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE upper( operadoraCartao.tipo ) like(?) ORDER BY operadoraCartao.nome");
		return (montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toUpperCase())));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoFinanceiroCartao</code> através do valor do atributo <code>String tipo</code> e <code>Integer configuracaoFinanceiro</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ConfiguracaoFinanceiroCartaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ConfiguracaoFinanceiroCartaoVO> consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro(String valorConsulta, Integer configuracaoFinanceiro, boolean online, Integer codigoFormaPagamento, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE upper( operadoraCartao.tipo ) like(?) AND configuracaoFinanceiro.codigo = ? ");
		if (online) {
			sqlStr.append(" and configuracaoFinanceiroCartao.permitirecebimentocartaoonline ");				
		}
		if(Uteis.isAtributoPreenchido(codigoFormaPagamento)) {
			sqlStr.append(" and (operadoraCartao.formapagamentopadraorecebimentoonline is null ");
			sqlStr.append(" or operadoraCartao.formapagamentopadraorecebimentoonline = ").append(codigoFormaPagamento).append(")");
		}
		sqlStr.append(" ORDER BY operadoraCartao.nome");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta.toUpperCase(), configuracaoFinanceiro.intValue() });
		return (montarDadosConsultaCompleta(rs));
	}
	public List<ConfiguracaoFinanceiroCartaoVO> consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro(String valorConsulta, Integer configuracaoFinanceiro, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE upper( operadoraCartao.tipo ) like(?) AND configuracaoFinanceiro.codigo = ? ORDER BY operadoraCartao.nome");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta.toUpperCase(), configuracaoFinanceiro.intValue() });
		return (montarDadosConsultaCompleta(rs));
	}
	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoFinanceiroCartao</code> através do valor do atributo <code>Long codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ConfiguracaoFinanceiroCartaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ConfiguracaoFinanceiroCartao WHERE codigo >= ?  ORDER BY codigo";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT configuracaoFinanceiroCartao.codigo as \"configuracaoFinanceiroCartao.codigo\", configuracaoFinanceiroCartao.operadoraCartao as\"configuracaoFinanceiroCartao.operadoraCartao\", ");
		str.append("configuracaoFinanceiroCartao.contaCorrente as \"configuracaoFinanceiroCartao.contaCorrente\", configuracaoFinanceiroCartao.categoriaDespesa as \"configuracaoFinanceiroCartao.categoriaDespesa\", ");
		str.append("configuracaoFinanceiroCartao.configuracaoFinanceiro as \"configuracaoFinanceiroCartao.configuracaoFinanceiro\", configuracaoFinanceiroCartao.quantidadeParcelasCartaoCredito as \"configuracaoFinanceiroCartao.quantidadeParcelasCartaoCredito\", ");
		// Dados OperadoraCartao
		str.append("operadoraCartao.nome as \"operadoraCartao.nome\", operadoraCartao.tipo as \"operadoraCartao.tipo\", ");
		// Dados ContaCorrente
		str.append("contaCorrente.numero as \"contaCorrente.numero\", contaCorrente.digito as \"contaCorrente.digito\", contaCorrente.saldo as \"contaCorrente.saldo\", ");
		// Dados Agencia
		str.append("agencia.numeroAgencia as \"agencia.numeroAgencia\", agencia.digito as \"agencia.digito\", ");
		// Dados Banco
		str.append("banco.codigo as \"banco.codigo\", banco.nome as \"banco.nome\", ");
		// Dados Categoria Despesa
		str.append("categoriaDespesa.descricao as \"categoriaDespesa.descricao\", categoriaDespesa.nivelCategoriaDespesa as \"categoriaDespesa.nivelCategoriaDespesa\", ");
		
		str.append("centroResultado.descricao as \"centroResultado.descricao\", centroResultado.codigo as \"centroResultado.codigo\" ");
		
		str.append("FROM configuracaoFinanceiroCartao ");
		str.append("INNER JOIN configuracaoFinanceiro ON configuracaoFinanceiro.codigo = configuracaoFinanceiroCartao.configuracaoFinanceiro ");
		str.append("INNER JOIN operadoraCartao ON operadoraCartao.codigo = configuracaoFinanceiroCartao.operadoraCartao ");
		str.append("INNER JOIN contaCorrente ON contaCorrente.codigo = configuracaoFinanceiroCartao.contaCorrente ");
		str.append("LEFT JOIN agencia ON agencia.codigo = contaCorrente.agencia ");
		str.append("LEFT JOIN banco ON banco.codigo = agencia.banco ");
		str.append("INNER JOIN categoriaDespesa ON categoriaDespesa.codigo = configuracaoFinanceiroCartao.categoriaDespesa ");
		str.append("INNER JOIN centroResultado ON centroResultado.codigo = configuracaoFinanceiroCartao.centroResultadoAdministrativo ");
		return str;
	}

	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT configuracaoFinanceiroCartao.codigo as \"configuracaoFinanceiroCartao.codigo\", configuracaoFinanceiroCartao.operadoraCartao as\"configuracaoFinanceiroCartao.operadoraCartao\", ");
		str.append("configuracaoFinanceiroCartao.contaCorrente as \"configuracaoFinanceiroCartao.contaCorrente\", configuracaoFinanceiroCartao.categoriaDespesa as \"configuracaoFinanceiroCartao.categoriaDespesa\", ");
		str.append("configuracaoFinanceiroCartao.configuracaoFinanceiro as \"configuracaoFinanceiroCartao.configuracaoFinanceiro\", configuracaoFinanceiroCartao.permitiRecebimentoCartaoOnline,");
		str.append(" configuracaoFinanceiroCartao.taxaDeOperacao, configuracaoFinanceiroCartao.taxaDeAntecipacao, configuracaoFinanceiroCartao.diaBaseCreditoConta, configuracaoFinanceiroCartao.diaBaseCreditoContaDiasUteis, configuracaoFinanceiroCartao.quantidadeParcelasCartaoCredito, ");
		// Dados OperadoraCartao
		// TODO
		str.append("operadoraCartao.nome as \"operadoraCartao.nome\", operadoraCartao.tipo as \"operadoraCartao.tipo\", operadoraCartao.operadoraCartaoCredito as \"operadoraCartao.operadoraCartaoCredito\", operadoraCartao.tipoFinanciamento as \"operadoraCartao.tipoFinanciamento\", ");
		// TODO
		// Dados ContaCorrente
		str.append("contaCorrente.numero as \"contaCorrente.numero\", contaCorrente.digito as \"contaCorrente.digito\", contaCorrente.carteira as \"contaCorrente.carteira\", ");
		str.append("contaCorrente.convenio as \"contaCorrente.convenio\", contaCorrente.saldo as \"contaCorrente.saldo\", contaCorrente.dataAbertura as \"contaCorrente.dataAbertura\", ");
		str.append("contaCorrente.agencia as \"contaCorrente.agencia\", contaCorrente.contaCaixa as \"contaCorrente.contaCaixa\", contaCorrente.tipoContaCorrente as \"contaCorrente.tipoContaCorrente\", ");
		str.append("contaCorrente.funcionarioResponsavel as \"contaCorrente.funcionarioResponsavel\", contaCorrente.codigoCedente as \"contaCorrente.codigoCedente\", ");
		// Dados Agencia
		str.append("agencia.numeroAgencia as \"agencia.numeroAgencia\", agencia.digito as \"agencia.digito\", ");
		// Dados Banco
		str.append("banco.codigo as \"banco.codigo\", banco.nome as \"banco.nome\", ");
		// Dados Categoria Despesa
		str.append(" categoriaDespesa.descricao as \"categoriaDespesa.descricao\", categoriaDespesa.categoriaDespesaPrincipal as \"categoriaDespesa.categoriaDespesaPrincipal\", categoriaDespesa.identificadorCategoriaDespesa as \"categoriaDespesa.identificadorCategoriaDespesa\", ");
		str.append("categoriaDespesa.nivelCategoriaDespesa as \"categoriaDespesa.nivelCategoriaDespesa\", categoriaDespesa.informarTurma as \"categoriaDespesa.informarTurma\", ");
		
		str.append("centroResultado.descricao as \"centroResultado.descricao\", centroResultado.codigo as \"centroResultado.codigo\" ");
		str.append("FROM configuracaoFinanceiroCartao ");
		str.append("INNER JOIN configuracaoFinanceiro ON configuracaoFinanceiro.codigo = configuracaoFinanceiroCartao.configuracaoFinanceiro ");
		str.append("INNER JOIN operadoraCartao ON operadoraCartao.codigo = configuracaoFinanceiroCartao.operadoraCartao ");
		str.append("INNER JOIN contaCorrente ON contaCorrente.codigo = configuracaoFinanceiroCartao.contaCorrente ");
		str.append("LEFT JOIN agencia ON agencia.codigo = contaCorrente.agencia ");
		str.append("LEFT JOIN banco ON banco.codigo = agencia.banco ");
		str.append("INNER JOIN categoriaDespesa ON categoriaDespesa.codigo = configuracaoFinanceiroCartao.categoriaDespesa ");
		str.append("INNER JOIN centroResultado ON centroResultado.codigo = configuracaoFinanceiroCartao.centroResultadoAdministrativo ");
		return str;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (ConfiguracaoFinanceiroCartao.codigo= " + codigo + ")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codigo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (ConfiguracaoFinanceiroCartao.codigo= " + codigo + ")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	public void carregarDados(ConfiguracaoFinanceiroCartaoVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((ConfiguracaoFinanceiroCartaoVO) obj, NivelMontarDados.BASICO, usuario);
	}

	public void carregarDados(ConfiguracaoFinanceiroCartaoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if (obj.getIsDadosBasicosDevemSerCarregados(nivelMontarDados)) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((ConfiguracaoFinanceiroCartaoVO) obj, resultado);
		}
		if (obj.getIsDadosCompletosDevemSerCarregados(nivelMontarDados)) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((ConfiguracaoFinanceiroCartaoVO) obj, resultado);
		}
	}

	public ConfiguracaoFinanceiroCartaoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {		
		return getAplicacaoControle().getConfiguracaoFinanceiroCartaoVO(codigo, usuario);
	}
	
	@Override
	public ConfiguracaoFinanceiroCartaoVO consultarPorChavePrimariaUnica(Integer codigo, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoFinanceiroCartaoVO obj = new ConfiguracaoFinanceiroCartaoVO();
		obj.setCodigo(codigo);
		carregarDados(obj, nivelMontarDados, usuario);
		return obj;
	}
	
	

	public List<ConfiguracaoFinanceiroCartaoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<ConfiguracaoFinanceiroCartaoVO> vetResultado = new ArrayList<ConfiguracaoFinanceiroCartaoVO>(0);
		while (tabelaResultado.next()) {
			ConfiguracaoFinanceiroCartaoVO obj = new ConfiguracaoFinanceiroCartaoVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<ConfiguracaoFinanceiroCartaoVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado) throws Exception {
		List<ConfiguracaoFinanceiroCartaoVO> vetResultado = new ArrayList<ConfiguracaoFinanceiroCartaoVO>(0);
		while (tabelaResultado.next()) {
			ConfiguracaoFinanceiroCartaoVO obj = new ConfiguracaoFinanceiroCartaoVO();
			montarDadosCompleto(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(ConfiguracaoFinanceiroCartaoVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da ConfiguracaoFinanceiroCartao
		obj.setCodigo(dadosSQL.getInt("configuracaoFinanceiroCartao.codigo"));
		obj.getConfiguracaoFinanceiroVO().setCodigo(dadosSQL.getInt("configuracaoFinanceiroCartao.configuracaoFinanceiro"));
		obj.getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("configuracaoFinanceiroCartao.operadoraCartao"));
		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("configuracaoFinanceiroCartao.contaCorrente"));
		obj.getCategoriaDespesaVO().setCodigo(dadosSQL.getInt("configuracaoFinanceiroCartao.categoriaDespesa"));
		obj.setPermitiRecebimentoCartaoOnline(dadosSQL.getBoolean("permitiRecebimentoCartaoOnline"));
		obj.setTaxaDeOperacao(dadosSQL.getDouble("taxaDeOperacao"));
		obj.setTaxaDeAntecipacao(dadosSQL.getDouble("taxaDeAntecipacao"));
		obj.setDiaBaseCreditoConta(dadosSQL.getInt("diaBaseCreditoConta"));
		obj.setDiaBaseCreditoContaDiasUteis(dadosSQL.getBoolean("diaBaseCreditoContaDiasUteis"));
		obj.setQuantidadeParcelasCartaoCredito(dadosSQL.getInt("quantidadeParcelasCartaoCredito"));
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados OperadoraCartao
		obj.getOperadoraCartaoVO().setNome(dadosSQL.getString("operadoraCartao.nome"));
		obj.getOperadoraCartaoVO().setTipo(dadosSQL.getString("operadoraCartao.tipo"));
		obj.getOperadoraCartaoVO().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados ContaCorrente
		obj.getContaCorrenteVO().setNumero(dadosSQL.getString("contaCorrente.numero"));
		obj.getContaCorrenteVO().setDigito(dadosSQL.getString("contaCorrente.digito"));
		obj.getContaCorrenteVO().setSaldo(dadosSQL.getDouble("contaCorrente.saldo"));
		obj.getContaCorrenteVO().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados Agencia
		obj.getContaCorrenteVO().getAgencia().setNumeroAgencia(dadosSQL.getString("agencia.numeroAgencia"));
		obj.getContaCorrenteVO().getAgencia().setDigito(dadosSQL.getString("agencia.digito"));
		obj.getContaCorrenteVO().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados Banco
		obj.getContaCorrenteVO().getAgencia().getBanco().setCodigo(dadosSQL.getInt("banco.codigo"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setNome(dadosSQL.getString("banco.nome"));
		// Dados Categoria Despesa
		obj.getCategoriaDespesaVO().setDescricao(dadosSQL.getString("categoriaDespesa.descricao"));
		obj.getCategoriaDespesaVO().setNivelCategoriaDespesa(dadosSQL.getString("categoriaDespesa.nivelCategoriaDespesa"));
		obj.getCategoriaDespesaVO().setNivelMontarDados(NivelMontarDados.BASICO);
		
		obj.getCentroResultadoAdministrativo().setCodigo(dadosSQL.getInt("centroResultado.codigo"));
		obj.getCentroResultadoAdministrativo().setDescricao(dadosSQL.getString("centroResultado.descricao"));
	}

	private void montarDadosCompleto(ConfiguracaoFinanceiroCartaoVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da ConfiguracaoFinanceiroCartao
		obj.setCodigo(dadosSQL.getInt("configuracaoFinanceiroCartao.codigo"));
		obj.getConfiguracaoFinanceiroVO().setCodigo(dadosSQL.getInt("configuracaoFinanceiroCartao.configuracaoFinanceiro"));
		obj.getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("configuracaoFinanceiroCartao.operadoraCartao"));
		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("configuracaoFinanceiroCartao.contaCorrente"));
		obj.getCategoriaDespesaVO().setCodigo(dadosSQL.getInt("configuracaoFinanceiroCartao.categoriaDespesa"));
		obj.setPermitiRecebimentoCartaoOnline(dadosSQL.getBoolean("permitiRecebimentoCartaoOnline"));
		obj.setTaxaDeOperacao(dadosSQL.getDouble("taxaDeOperacao"));
		obj.setTaxaDeAntecipacao(dadosSQL.getDouble("taxaDeAntecipacao"));
		obj.setDiaBaseCreditoConta(dadosSQL.getInt("diaBaseCreditoConta"));
		obj.setDiaBaseCreditoContaDiasUteis(dadosSQL.getBoolean("diaBaseCreditoContaDiasUteis"));
		obj.setQuantidadeParcelasCartaoCredito(dadosSQL.getInt("quantidadeParcelasCartaoCredito"));
		obj.setNivelMontarDados(NivelMontarDados.TODOS);
		// Dados OperadoraCartao
		obj.getOperadoraCartaoVO().setNome(dadosSQL.getString("operadoraCartao.nome"));
		obj.getOperadoraCartaoVO().setTipo(dadosSQL.getString("operadoraCartao.tipo"));
		if (dadosSQL.getString("operadoraCartao.operadoraCartaoCredito") != null) {
			obj.getOperadoraCartaoVO().setOperadoraCartaoCreditoEnum(OperadoraCartaoCreditoEnum.valueOf(dadosSQL.getString("operadoraCartao.operadoraCartaoCredito")));
		}
		if (dadosSQL.getString("operadoraCartao.tipoFinanciamento") != null) {
			obj.getOperadoraCartaoVO().setTipoFinanciamentoEnum(TipoFinanciamentoEnum.valueOf(dadosSQL.getString("operadoraCartao.tipoFinanciamento")));
		}
		// obj.getOperadoraCartaoVO().setTaxaOperadora(dadosSQL.getDouble("operadoraCartao.taxaOperadora"));
		// obj.getOperadoraCartaoVO().setTaxaAntecipacao(dadosSQL.getDouble("operadoraCartao.taxaAntecipacao"));
		// obj.getOperadoraCartaoVO().setDiasCreditoConta(dadosSQL.getInt("operadoraCartao.diasCreditoConta"));
		// obj.getOperadoraCartaoVO().getArquivoImagem().setCodigo(dadosSQL.getInt("operadoraCartao.arquivoImagem"));
		obj.getOperadoraCartaoVO().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados ContaCorrente
		obj.getContaCorrenteVO().setNumero(dadosSQL.getString("contaCorrente.numero"));
		obj.getContaCorrenteVO().setDigito(dadosSQL.getString("contaCorrente.digito"));
		obj.getContaCorrenteVO().setCarteira(dadosSQL.getString("contaCorrente.carteira"));
		obj.getContaCorrenteVO().setConvenio(dadosSQL.getString("contaCorrente.convenio"));
		obj.getContaCorrenteVO().setSaldo(dadosSQL.getDouble("contaCorrente.saldo"));
		obj.getContaCorrenteVO().setDataAbertura(dadosSQL.getDate("contaCorrente.dataAbertura"));
		obj.getContaCorrenteVO().getAgencia().setCodigo(dadosSQL.getInt("contaCorrente.agencia"));
		// obj.getContaCorrenteVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("contaCorrente.unidadeEnsino"));
		obj.getContaCorrenteVO().setContaCaixa(dadosSQL.getBoolean("contaCorrente.contaCaixa"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("contaCorrente.tipoContaCorrente"))) {
			obj.getContaCorrenteVO().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("contaCorrente.tipoContaCorrente")));
		}
		obj.getContaCorrenteVO().getFuncionarioResponsavel().setCodigo(dadosSQL.getInt("contaCorrente.funcionarioResponsavel"));
		obj.getContaCorrenteVO().setCodigoCedente(dadosSQL.getString("contaCorrente.codigoCedente"));
		obj.getContaCorrenteVO().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados Agencia
		obj.getContaCorrenteVO().getAgencia().setNumeroAgencia(dadosSQL.getString("agencia.numeroAgencia"));
		obj.getContaCorrenteVO().getAgencia().setDigito(dadosSQL.getString("agencia.digito"));
		obj.getContaCorrenteVO().getAgencia().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados Banco
		obj.getContaCorrenteVO().getAgencia().getBanco().setCodigo(dadosSQL.getInt("banco.codigo"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setNome(dadosSQL.getString("banco.nome"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados Categoria Despesa
		obj.getCategoriaDespesaVO().setDescricao(dadosSQL.getString("categoriaDespesa.descricao"));
		obj.getCategoriaDespesaVO().setCategoriaDespesaPrincipal(dadosSQL.getInt("categoriaDespesa.categoriaDespesaPrincipal"));
		obj.getCategoriaDespesaVO().setIdentificadorCategoriaDespesa(dadosSQL.getString("categoriaDespesa.identificadorCategoriaDespesa"));
		obj.getCategoriaDespesaVO().setNivelCategoriaDespesa(dadosSQL.getString("categoriaDespesa.nivelCategoriaDespesa"));
		obj.getCategoriaDespesaVO().setInformarTurma(dadosSQL.getString("categoriaDespesa.informarTurma"));
		obj.getCategoriaDespesaVO().setNivelMontarDados(NivelMontarDados.BASICO);
		
		obj.getCentroResultadoAdministrativo().setCodigo(dadosSQL.getInt("centroResultado.codigo"));
		obj.getCentroResultadoAdministrativo().setDescricao(dadosSQL.getString("centroResultado.descricao"));
		
		//Dados configuração financeira
		obj.setConfiguracaoFinanceiroVO(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorChavePrimariaUnica(obj.getConfiguracaoFinanceiroVO().getCodigo(), null));
		
		obj.setConfFinCartaoTaxaOperacao(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarTaxaOperacaoCartaoConfiguracaoFinanceiroCartao(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ConfiguracaoFinanceiroCartaoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code>.
	 * 
	 * @return O objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code> com os dados devidamente montados.
	 */
	public static ConfiguracaoFinanceiroCartaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoFinanceiroCartaoVO obj = new ConfiguracaoFinanceiroCartaoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("operadoraCartao"));
		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contaCorrente"));
		obj.getCategoriaDespesaVO().setCodigo(dadosSQL.getInt("categoriaDespesa"));
		obj.getCentroResultadoAdministrativo().setCodigo(dadosSQL.getInt("centroResultadoAdministrativo"));
		obj.setPermitiRecebimentoCartaoOnline(dadosSQL.getBoolean("permitiRecebimentoCartaoOnline"));
		obj.setTaxaDeOperacao(dadosSQL.getDouble("taxaDeOperacao"));
		obj.setTaxaDeAntecipacao(dadosSQL.getDouble("taxaDeAntecipacao"));
		obj.setDiaBaseCreditoConta(dadosSQL.getInt("diaBaseCreditoConta"));
		obj.setDiaBaseCreditoContaDiasUteis(dadosSQL.getBoolean("diaBaseCreditoContaDiasUteis"));
		obj.getConfiguracaoFinanceiroVO().setCodigo(dadosSQL.getInt("configuracaofinanceiro"));
		obj.setQuantidadeParcelasCartaoCredito(dadosSQL.getInt("quantidadeParcelasCartaoCredito"));
		montarDadosOperadoraCartao(obj, nivelMontarDados, usuario);
		montarDadosContaCorrente(obj, nivelMontarDados, usuario);
		montarDadosCategoriaDespesa(obj, nivelMontarDados, usuario);
		obj.setCentroResultadoAdministrativo(Uteis.montarDadosVO(dadosSQL.getInt("centroResultadoAdministrativo"), CentroResultadoVO.class, p -> getFacadeFactory().getCentroResultadoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
		obj.setNovoObj(Boolean.FALSE);
		obj.setConfFinCartaoTaxaOperacao(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarTaxaOperacaoCartaoConfiguracaoFinanceiroCartao(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setConfiguracaoFinanceiroVO(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorChavePrimaria(obj.getConfiguracaoFinanceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		return obj;
	}

	public static TaxaOperacaoCartaoVO montarDadosTaxaOperacao(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TaxaOperacaoCartaoVO obj = new TaxaOperacaoCartaoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setConfiguracaoFinanceiroCartao(dadosSQL.getInt("configuracaoFinanceiroCartao"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setParcela(dadosSQL.getInt("parcela"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setTaxa(dadosSQL.getDouble("taxa"));
		obj.setTipoFinanciamentoEnum(TipoFinanciamentoEnum.valueOf(dadosSQL.getString("tipoFinanciamento")));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public static void montarDadosOperadoraCartao(ConfiguracaoFinanceiroCartaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getOperadoraCartaoVO().getCodigo().intValue() == 0) {
			obj.setOperadoraCartaoVO(new OperadoraCartaoVO());
			return;
		}
		obj.setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(obj.getOperadoraCartaoVO().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosContaCorrente(ConfiguracaoFinanceiroCartaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCorrenteVO().getCodigo().intValue() == 0) {
			obj.setContaCorrenteVO(new ContaCorrenteVO());
			return;
		}
		obj.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosCategoriaDespesa(ConfiguracaoFinanceiroCartaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCategoriaDespesaVO().getCodigo().intValue() == 0) {
			obj.setCategoriaDespesaVO(new CategoriaDespesaVO());
			return;
		}
		obj.setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCategoriaDespesaVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por consultar todos os <code>FuncionarioCargoVO</code> relacionados a um objeto da classe <code>administrativo.funcionario</code>.
	 * 
	 * @param funcionario
	 *            Atributo de <code>administrativo.funcionario</code> a ser utilizado para localizar os objetos da classe <code>FuncionarioCargoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>FuncionarioCargoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public List consultarConfiguracaoFinanceiroCartao(Integer configuracaoFinanceiro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {		
		ConfiguracaoFinanceiroCartao.consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM ConfiguracaoFinanceiroCartao WHERE configuracaoFinanceiro = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { configuracaoFinanceiro.intValue() });
		while (resultado.next()) {
			ConfiguracaoFinanceiroCartaoVO novoObj = new ConfiguracaoFinanceiroCartaoVO();
			novoObj = ConfiguracaoFinanceiroCartao.montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	public List<TaxaOperacaoCartaoVO> consultarTaxaOperacaoCartaoConfiguracaoFinanceiroCartao(Integer configuracaoFinanceiroCartao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM TaxaOperacaoCartao WHERE configuracaoFinanceiroCartao = ? order by parcela::integer, tipoFinanciamento ";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { configuracaoFinanceiroCartao.intValue() });
		while (resultado.next()) {
			TaxaOperacaoCartaoVO novoObj = new TaxaOperacaoCartaoVO();
			novoObj = montarDadosTaxaOperacao(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ConfiguracaoFinanceiroCartaoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ConfiguracaoFinanceiroCartaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return getAplicacaoControle().getConfiguracaoFinanceiroCartaoVO(codigoPrm, usuario);
	}

	public List<ConfiguracaoFinanceiroCartaoVO> consultarUnicidade(ConfiguracaoFinanceiroCartaoVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
		super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
		return new ArrayList(0);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConfiguracaoFinanceiroCartao.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConfiguracaoFinanceiroCartao.idEntidade = idEntidade;
	}

	@Override
	public Boolean verificarExistenciaConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(Integer codigoConfiguracaoFinanceiro, Double valorAReceber, String campo) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select configuracaofinanceirocartao.codigo from configuracaofinanceirocartao");
		sqlStr.append(" inner join operadoracartao on  operadoracartao.codigo = configuracaofinanceirocartao.operadoracartao");
		sqlStr.append(" where configuracaofinanceiro = ").append(codigoConfiguracaoFinanceiro);
		sqlStr.append(" and  permitirecebimentocartaoonline = 't'");
		sqlStr.append(" and operadoracartao.tipo = ").append("'CARTAO_CREDITO'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean consultarConfiguracaoFinanceiroCartaoVOsPorCodigoConfiguracaoFinanceiroEPermiteRecimentoOnline(Integer codigoConfiguracaoFinanceiro, String campo, Double valorAReceber, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select configuracaofinanceirocartao.codigo from configuracaofinanceirocartao");
		sqlStr.append(" inner join operadoracartao on  operadoracartao.codigo = configuracaofinanceirocartao.operadoracartao");
		sqlStr.append(" where configuracaofinanceirocartao.codigo = ").append(codigoConfiguracaoFinanceiro);
		sqlStr.append(" and operadoracartao.tipo = ").append("'CARTAO_CREDITO'");
		sqlStr.append(" and  permitirecebimentocartaoonline = 't'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean verificarExistenciaConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(Integer codigoConfiguracaoFinanceiro) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select configuracaofinanceirocartao.codigo from configuracaofinanceirocartao");
		sqlStr.append(" inner join operadoracartao on  operadoracartao.codigo = configuracaofinanceirocartao.operadoracartao");
		sqlStr.append(" where configuracaofinanceiro = ").append(codigoConfiguracaoFinanceiro);
		sqlStr.append(" and  permitirecebimentocartaoonline");
		sqlStr.append(" and operadoracartao.tipo = ").append("'CARTAO_CREDITO'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean consultarConfiguracaoFinanceiroCartaoVOsPorCodigoConfiguracaoFinanceiroEPermiteRecimentoOnlineUsarMinhasContasVisaoAluno(Integer codigoConfiguracaoFinanceiro, String campo, Double valorAReceber, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select configuracaofinanceirocartao.codigo from configuracaofinanceirocartao");
		sqlStr.append(" inner join operadoracartao on  operadoracartao.codigo = configuracaofinanceirocartao.operadoracartao");
		sqlStr.append(" where configuracaofinanceirocartao.codigo = ").append(codigoConfiguracaoFinanceiro);
		sqlStr.append(" and operadoracartao.tipo = ").append("'CARTAO_CREDITO'");
		sqlStr.append(" and  permitirecebimentocartaoonline = 't'");
		sqlStr.append(" and  usarminhascontasvisaoaluno = 't'");
		sqlStr.append(" and valorminimopararecebimentocartao <= ").append(valorAReceber);
		sqlStr.append(" and ").append(campo).append(" = 'f'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return true;
		}
		return false;
	}

	@Override
	public List<ConfiguracaoFinanceiroCartaoVO> consultarConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(Integer codigoConfiguracaoFinanceiro, Double valorAReceber, String campo, String tipoCartao, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select * from configuracaofinanceirocartao");
		sqlStr.append(" inner join operadoracartao on  operadoracartao.codigo = configuracaofinanceirocartao.operadoracartao");
		sqlStr.append(" where configuracaofinanceiro = ").append(codigoConfiguracaoFinanceiro);
		sqlStr.append(" and  permitirecebimentocartaoonline = 't'");
		sqlStr.append(" and operadoracartao.tipo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), tipoCartao);
		ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO = null;
		List<ConfiguracaoFinanceiroCartaoVO> configuracaoFinanceiroCartaoVOs = new ArrayList<ConfiguracaoFinanceiroCartaoVO>(0);
		while (rs.next()) {
			configuracaoFinanceiroCartaoVO = new ConfiguracaoFinanceiroCartaoVO();
			configuracaoFinanceiroCartaoVO = montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS, usuario);
			configuracaoFinanceiroCartaoVOs.add(configuracaoFinanceiroCartaoVO);
		}
		return configuracaoFinanceiroCartaoVOs;
	}

	public void adicionarObjTaxaOperacaoCartaoVOs(ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO, TaxaOperacaoCartaoVO obj, UsuarioVO usuario) throws Exception {
		// getFacadeFactory().getTaxaOperacaoCartaoFacade().validarDados(obj);
		int index = 0;
		Iterator<TaxaOperacaoCartaoVO> i = configuracaoFinanceiroCartaoVO.getConfFinCartaoTaxaOperacao().iterator();
		while (i.hasNext()) {
			TaxaOperacaoCartaoVO objExistente = i.next();
			if (objExistente.getParcela().equals(obj.getParcela()) && objExistente.getTipoFinanciamentoEnum().equals(obj.getTipoFinanciamentoEnum())) {
				configuracaoFinanceiroCartaoVO.getConfFinCartaoTaxaOperacao().set(index, obj);
				return;
			}
			index++;
		}
		configuracaoFinanceiroCartaoVO.getConfFinCartaoTaxaOperacao().add(obj);
	}

	public void excluirObjTaxaOperacaoCartaoVOs(ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO, TaxaOperacaoCartaoVO obj, UsuarioVO usuario) throws Exception {
		Iterator<TaxaOperacaoCartaoVO> i = configuracaoFinanceiroCartaoVO.getConfFinCartaoTaxaOperacao().iterator();
		while (i.hasNext()) {
			TaxaOperacaoCartaoVO objExistente =  i.next();
			if (objExistente.getParcela().equals(obj.getParcela()) && objExistente.getTipoFinanciamentoEnum().equals(obj.getTipoFinanciamentoEnum())) {
				i.remove();
				return;
			}
		}
	}

	public ConfiguracaoFinanceiroCartaoVO consultarObjConfiguracaoFinanceiroCartaoVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoFinanceiroCartaoVO obj, UsuarioVO usuario) throws Exception {
		Iterator i = configuracaoFinanceiroVO.getListaConfiguracaoFinanceiroCartaoVO().iterator();
		while (i.hasNext()) {
			ConfiguracaoFinanceiroCartaoVO objExistente = (ConfiguracaoFinanceiroCartaoVO) i.next();
			if (objExistente.getOperadoraCartaoVO().getCodigo().equals(obj.getOperadoraCartaoVO().getCodigo()) && objExistente.getContaCorrenteVO().getCodigo().equals(obj.getContaCorrenteVO().getCodigo())) {
				return objExistente;
			}
		}
		return null;
	}

	@Override
	public ConfiguracaoFinanceiroCartaoVO consultarPorOperadoraCartaoConfiguracaoFinanceiro(OperadoraCartaoVO operadoraCartaoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from configuracaofinanceirocartao");
		sb.append(" inner join operadoracartao on  operadoracartao.codigo = configuracaofinanceirocartao.operadoracartao");
		sb.append(" where operadoraCartao.codigo = ").append(operadoraCartaoVO.getCodigo());
		sb.append(" and configuracaoFinanceiro = ").append(configuracaoFinanceiroVO.getCodigo());
		sb.append(" and permitirecebimentocartaoonline ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		ConfiguracaoFinanceiroCartaoVO obj = new ConfiguracaoFinanceiroCartaoVO();
		while (dadosSQL.next())		 {
			obj = montarDados(dadosSQL, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		return obj;
	}
}
