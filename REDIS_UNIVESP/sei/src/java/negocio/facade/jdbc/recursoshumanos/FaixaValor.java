package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.FaixaValorVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.FaixaValorInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>FaixaValorVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>FaixaValorVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see FaixaValorGEDVO
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class FaixaValor extends ControleAcesso implements FaixaValorInterfaceFacade {

	private static final long serialVersionUID = -1084004772804472456L;

	protected static String idEntidade;

	public FaixaValor() throws Exception {
		super();
		setIdEntidade("FaixaValor");
	}

	@Override
	public void persistir(FaixaValorVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(FaixaValorVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) {
		try {
			final String sql = " INSERT INTO FaixaValor( limitesuperior, percentual, valordeduzir, valoracrescentar,valorreferenciafolhapagamento, limiteInferior) "
					+ " VALUES ( ?, ?, ?, ?, ?, ?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setBigDecimal(1, obj.getLimiteSuperior());
					sqlInserir.setBigDecimal(2, obj.getPercentual());
					sqlInserir.setBigDecimal(3, obj.getValorDeduzir());
					sqlInserir.setBigDecimal(4, obj.getValorAcrescentar());
					sqlInserir.setInt(5, obj.getValorReferenciaFolhaPagamento().getCodigo());
					sqlInserir.setBigDecimal(6, obj.getLimiteInferior());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(FaixaValorVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) {
		try {
			final String sql = "UPDATE FaixaValor set limitesuperior=?, percentual=?, valordeduzir = ?, valoracrescentar = ?, valorreferenciafolhapagamento = ?, limiteInferior = ? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBigDecimal(1, obj.getLimiteSuperior());
					sqlAlterar.setBigDecimal(2, obj.getPercentual());
					sqlAlterar.setBigDecimal(3, obj.getValorDeduzir());
					sqlAlterar.setBigDecimal(4, obj.getValorAcrescentar());
					sqlAlterar.setInt(5, obj.getValorReferenciaFolhaPagamento().getCodigo());
					sqlAlterar.setBigDecimal(6, obj.getLimiteInferior());
					sqlAlterar.setInt(7, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void excluir(FaixaValorVO faixaValor, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			String sql = "DELETE FROM FaixaValor WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { faixaValor.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List consultarPorTabelaReferenciaFolhaPagamento(int codigo, Boolean validarAcesso, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" WHERE valorreferenciafolhapagamento = ").append(codigo);
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<FaixaValorVO> lista = new ArrayList<>(0);
		while(tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado));
		}
		return lista;
	}

	public Integer consultarValorEntreLimiteInferiorELimiteSuperior(Integer valor) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT faixavalor.valordeduzir AS valor FROM faixavalor ");
		sql.append(" INNER JOIN valorreferenciafolhapagamento ON valorreferenciafolhapagamento.codigo = faixavalor.valorreferenciafolhapagamento");
		sql.append(" WHERE valorreferenciafolhapagamento.referencia = 'TABELA_FALTAS_FERIAS' AND ? BETWEEN limiteinferior and limitesuperior");
		sql.append(" LIMIT 1;");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valor);
		if(tabelaResultado.next()) {
			return tabelaResultado.getBigDecimal("valor").intValue();
		}
		return 0;
	}

	/**
	 * Monta o objeto <code>FaixaValorVO<code> consultado do banco de dados.
	 * 
	 * @param tabelaResultado
	 * @return
	 */
	public FaixaValorVO montarDados(SqlRowSet tabelaResultado) {
		FaixaValorVO obj = new FaixaValorVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setLimiteSuperior(tabelaResultado.getBigDecimal("limitesuperior"));
		obj.setPercentual(tabelaResultado.getBigDecimal("percentual"));
		obj.setValorDeduzir(tabelaResultado.getBigDecimal("valordeduzir"));
		obj.setValorAcrescentar(tabelaResultado.getBigDecimal("valoracrescentar"));
		obj.getValorReferenciaFolhaPagamento().setCodigo(tabelaResultado.getInt("valorreferenciafolhapagamento"));
		obj.setLimiteInferior(tabelaResultado.getBigDecimal("limiteInferior"));

		return obj;
	}

	/**
	 * Sql basico para consultad da <code>FaixaValorVO<code>
	 * 
	 * @return
	 */
	public String getSelectSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM faixavalor ");
		sql.append(" INNER JOIN valorreferenciafolhapagamento ON valorreferenciafolhapagamento.codigo = faixavalor.valorreferenciafolhapagamento");

		return sql.toString();
	}

	/**
	 * Valida os campos obrigatorios do <code>FaixaValorVO<code>
	 * 
	 * @param obj - FaixaValor
	 * @throws ConsistirException
	 */
	private void validarDados(FaixaValorVO obj) throws ConsistirException {

		if (obj.getLimiteSuperior() == null || obj.getLimiteSuperior().equals(BigDecimal.ZERO)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_FaixaValor_limiteSuperior"));
		}

	}

	/**
	 * Valida se o limite inferiro e maior que o limite superior
	 * 
	 * @param obj
	 */
	public void validarDadosLimiteInferiorMaiorLimiteSuperior(FaixaValorVO obj) throws ConsistirException {

		if (obj.getLimiteInferior().compareTo(obj.getLimiteSuperior()) == 1) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_FaixaValor_limiteSuperiorMenorlimiteInferior"));
		}
	}

	@Override
	public FaixaValorVO consultarPorChavePrimaria(long codigo) throws Exception {
		String sql = " SELECT * FROM faixavalor WHERE codigo = ?";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
        if (rs.next()) {
            return montarDados(rs);
        }
        throw new Exception("Dados não encontrados (Faixa Valor).");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		FaixaValor.idEntidade = idEntidade;
	}

}