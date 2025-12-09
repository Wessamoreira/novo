package negocio.facade.jdbc.faturamento.nfe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaImpostoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.NotaFiscalEntradaImpostoInterfaceFacade;

/**
 *
 * @author Pedro Otimize
 */
@Repository
@Scope("singleton")
@Lazy
public class NotaFiscalEntradaImposto extends ControleAcesso implements NotaFiscalEntradaImpostoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 979354290401777531L;
	protected static String idEntidade = "NotaFiscalEntradaImposto";

	public NotaFiscalEntradaImposto() {
		super();
	}

	public void validarDados(NotaFiscalEntradaImpostoVO obj) {
		if (!Uteis.isAtributoPreenchido(obj.getImpostoVO())) {
			throw new StreamSeiException("O campo Imposto (NotaFiscalEntradaImposto) não foi informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getPorcentagem())) {
			throw new StreamSeiException("O campo Porcentagem (NotaFiscalEntradaImposto) não foi informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getValor())) {
			throw new StreamSeiException("O campo Valor (NotaFiscalEntradaImposto) não foi informado.");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaImpostoInterfaceFacade#persistir(java.util.List, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<NotaFiscalEntradaImpostoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		for (NotaFiscalEntradaImpostoVO obj : lista) {
			validarDados(obj);
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final NotaFiscalEntradaImpostoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntradaImposto.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO NotaFiscalEntradaImposto (imposto, notaFiscalEntrada, retido, porcentagem, valor) ");
			sql.append("    VALUES (?,?,?,?,?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getImpostoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNotaFiscalEntradaVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.isRetido(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPorcentagem(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final NotaFiscalEntradaImpostoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntradaImposto.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE NotaFiscalEntradaImposto ");
			sql.append("   SET imposto=?, notaFiscalEntrada=?, retido=?, porcentagem=?, valor=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getImpostoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNotaFiscalEntradaVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.isRetido(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPorcentagem(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaImpostoInterfaceFacade#excluir(negocio.comuns.faturamento.nfe.NotaFiscalEntradaImpostoVO, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(NotaFiscalEntradaImpostoVO obj, boolean verificarAcesso, UsuarioVO usuario)  {
		try {
			NotaFiscalEntradaImposto.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM NotaFiscalEntradaImposto WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT NotaFiscalEntradaImposto.codigo as \"NotaFiscalEntradaImposto.codigo\",  ");
		sql.append(" NotaFiscalEntradaImposto.retido as \"NotaFiscalEntradaImposto.retido\", ");
		sql.append(" NotaFiscalEntradaImposto.valor as \"NotaFiscalEntradaImposto.valor\", ");
		sql.append(" NotaFiscalEntradaImposto.porcentagem as \"NotaFiscalEntradaImposto.porcentagem\", ");

		sql.append(" notaFiscalEntrada.codigo as \"notaFiscalEntrada.codigo\",  ");

		sql.append(" imposto.codigo as \"imposto.codigo\", imposto.nome as \"imposto.nome\"  ");

		sql.append(" FROM NotaFiscalEntradaImposto ");
		sql.append(" inner join notaFiscalEntrada on notaFiscalEntrada.codigo = NotaFiscalEntradaImposto.notaFiscalEntrada");
		sql.append(" inner join imposto on imposto.codigo = NotaFiscalEntradaImposto.imposto");
		return sql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaImpostoInterfaceFacade#consultaRapidaPorImposto(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaImpostoVO> consultaRapidaPorImposto(Integer compra, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE NotaFiscalEntradaImposto.imposto = ").append(compra).append(" ");
			sqlStr.append(" ORDER BY NotaFiscalEntradaImposto.codigo desc ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaImpostoInterfaceFacade#consultaRapidaPorNotaFiscalEntrada(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaImpostoVO> consultaRapidaPorNotaFiscalEntrada(Integer notaFiscalEntrada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE NotaFiscalEntradaImposto.notaFiscalEntrada = ").append(notaFiscalEntrada).append(" ");
			sqlStr.append(" ORDER BY NotaFiscalEntradaImposto.codigo ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaImpostoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NotaFiscalEntradaImpostoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE NotaFiscalEntradaImposto.codigo = ").append(codigoPrm).append(" ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( NotaFiscalEntradaImpostoVO ).");
			}
			NotaFiscalEntradaImpostoVO obj = new NotaFiscalEntradaImpostoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaImpostoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<NotaFiscalEntradaImpostoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			NotaFiscalEntradaImpostoVO obj = new NotaFiscalEntradaImpostoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(NotaFiscalEntradaImpostoVO obj, SqlRowSet dadosSQL, int nivelMontarDados) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("NotaFiscalEntradaImposto.codigo"));
		obj.setRetido(dadosSQL.getBoolean("NotaFiscalEntradaImposto.retido"));
		obj.setValor(dadosSQL.getDouble("NotaFiscalEntradaImposto.valor"));
		obj.setPorcentagem(dadosSQL.getDouble("NotaFiscalEntradaImposto.porcentagem"));
		obj.getImpostoVO().setCodigo(dadosSQL.getInt("imposto.codigo"));
		obj.getImpostoVO().setNome(dadosSQL.getString("imposto.nome"));
		obj.getNotaFiscalEntradaVO().setCodigo(dadosSQL.getInt("notaFiscalEntrada.codigo"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return NotaFiscalEntradaImposto.idEntidade;
	}

}
