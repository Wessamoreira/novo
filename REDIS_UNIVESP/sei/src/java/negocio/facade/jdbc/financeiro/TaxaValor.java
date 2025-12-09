/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.TaxaVO;
import negocio.comuns.financeiro.TaxaValorVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.TaxaValorInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TaxaValor extends ControleAcesso implements TaxaValorInterfaceFacade {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6871068917804650164L;

	public TaxaValor() throws Exception {
		super();		
	}

	@Override
	public void validarDadosTaxaValor(TaxaValorVO taxaValorVO) throws ConsistirException {
		if (taxaValorVO.getData() == null) {
			throw new ConsistirException("O Campo Data (Taxa Valor é necessário) !");
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TaxaValorVO obj, UsuarioVO usuarioVO) throws Exception {
		final String sql = "INSERT INTO taxavalor(data, valor, taxa) VALUES (?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement sqlInserir = conn.prepareStatement(sql);
				sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
				sqlInserir.setDouble(2, obj.getValor());
				sqlInserir.setInt(3, obj.getTaxa());
				return sqlInserir;
			}
		}, new ResultSetExtractor() {

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return rs.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
	}

	public static TaxaValorVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		TaxaValorVO obj = new TaxaValorVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setValor(new Double(dadosSQL.getDouble("valor")));
		obj.getTaxaVO().setCodigo(new Integer(dadosSQL.getInt("taxa")));
		obj.setNovoObj(Boolean.FALSE);

		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTaxaValorVOs(TaxaVO taxaVO) throws Exception {
		StringBuilder sql = new StringBuilder(" delete from taxavalor where taxa = ").append(taxaVO.getCodigo());
		sql.append(" and codigo not in(0");

		for (TaxaValorVO taxaValorVO : taxaVO.getTaxaValorVOs()) {

			sql.append(",").append(taxaValorVO.getCodigo());
		}
		sql.append(" ) ");
		try {

			getConexao().getJdbcTemplate().update(sql.toString());

		} catch (Exception e) {
			throw e;
		} finally {

		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarOpcoesTaxaValor(TaxaVO taxaVO, UsuarioVO usuarioVO) throws Exception {
		this.excluirTaxaValorVOs(taxaVO);
		for (TaxaValorVO taxaValorVO : taxaVO.getTaxaValorVOs()) {
			taxaValorVO.setTaxa(taxaVO.getCodigo());
			try {
				if (taxaValorVO.isNovoObj()) {
					incluir(taxaValorVO, usuarioVO);
				}

				alterarTaxaValor(taxaValorVO, usuarioVO);

			} catch (Exception e) {
				throw e;
			} finally {

			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTaxaValor(final TaxaValorVO obj, UsuarioVO usuarioVO) throws Exception {

		try {			
			final String sql = "UPDATE taxavalor SET data=?, valor=? WHERE ((codigo = ? ))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement sqlAlterar = conn.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setDouble(2, obj.getValor());
					sqlAlterar.setInt(3, obj.getTaxa().intValue());
					return sqlAlterar;
				}
			});

			obj.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTaxaValor(TaxaValorVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			
			String sql = "DELETE FROM taxavalor WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Override
	public List consultarOpcoesTaxaValor(Integer taxa, int nivelMontarDados) throws Exception {		
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM taxavalor WHERE taxa = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { taxa });
		while (resultado.next()) {
			TaxaValorVO novoObj = new TaxaValorVO();
			novoObj = montarDados(resultado, nivelMontarDados);
			objetos.add(novoObj);
		}
		return objetos;
	}

	@Override
	public void removerTaxaValorVOs(TaxaVO taxaVO, TaxaValorVO taxaValorVO) {
		for (Iterator<TaxaValorVO> iterator = taxaVO.getTaxaValorVOs().iterator(); iterator.hasNext();) {
			TaxaValorVO taxaValor = (TaxaValorVO) iterator.next();
			if (taxaValor.getData().equals(taxaValorVO.getData())) {
				iterator.remove();
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirTaxaValor(TaxaVO taxaVO, UsuarioVO usuarioVO) throws Exception {
		Iterator<TaxaValorVO> e = taxaVO.getTaxaValorVOs().iterator();
		while (e.hasNext()) {
			TaxaValorVO obj = (TaxaValorVO) e.next();
			obj.setTaxa(taxaVO.getCodigo());
			incluir(obj, usuarioVO);
		}
	}

	
}
