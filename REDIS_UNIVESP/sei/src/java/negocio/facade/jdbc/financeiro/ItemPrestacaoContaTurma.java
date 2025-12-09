package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ItemPrestacaoContaTurmaVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.ItemPrestacaoContaTurmaInterfaceFacade;

@Service
@Lazy
public class ItemPrestacaoContaTurma extends ControleAcesso implements ItemPrestacaoContaTurmaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4399677640957550635L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ItemPrestacaoContaTurmaVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO ItemPrestacaoContaTurma( prestacaoContaTurma, prestacaoConta) VALUES ( ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getPrestacaoContaTurma().getCodigo());
					sqlInserir.setInt(2, obj.getPrestacaoConta().getCodigo());
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
			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemPrestacaoContaTurmaVO obj) throws Exception {
		try {
			final String sql = "UPDATE ItemPrestacaoContaTurma set prestacaoContaTurma=?, prestacaoConta = ? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getPrestacaoContaTurma().getCodigo());
					sqlAlterar.setInt(2, obj.getPrestacaoConta().getCodigo());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void incluirItemPrestacaoContaTurmaVOs(List<ItemPrestacaoContaTurmaVO> itemPrestacaoContaTurmaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception {
		for (ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO : itemPrestacaoContaTurmaVOs) {
			itemPrestacaoContaTurmaVO.setPrestacaoConta(prestacaoContaVO);
			incluir(itemPrestacaoContaTurmaVO);
		}

	}

	@Override
	public void alterarItemPrestacaoContaTurmaVOs(List<ItemPrestacaoContaTurmaVO> itemPrestacaoContaTurmaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception {
		excluirItemPrestacaoContaTurmaVOs(itemPrestacaoContaTurmaVOs, prestacaoContaVO);
		for (ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO : itemPrestacaoContaTurmaVOs) {
			itemPrestacaoContaTurmaVO.setPrestacaoConta(prestacaoContaVO);
			if (itemPrestacaoContaTurmaVO.getCodigo() == null || itemPrestacaoContaTurmaVO.getCodigo() == 0) {
				incluir(itemPrestacaoContaTurmaVO);
			} else {
				alterar(itemPrestacaoContaTurmaVO);
			}
		}

	}

	@Override
	public void excluirItemPrestacaoContaTurmaVOs(List<ItemPrestacaoContaTurmaVO> itemPrestacaoContaTurmaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		try {
			sql.append("DELETE FROM ItemPrestacaoContaTurma where prestacaoConta = ").append(prestacaoContaVO.getCodigo());
			int x = 0;
			for (ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO : itemPrestacaoContaTurmaVOs) {
				if (x == 0) {
					sql.append(" and codigo not in (").append(itemPrestacaoContaTurmaVO.getCodigo());
					x++;
				} else {
					sql.append(", ").append(itemPrestacaoContaTurmaVO.getCodigo());
				}
			}
			if (x > 0) {
				sql.append(") ");
			}
			getConexao().getJdbcTemplate().execute(sql.toString());
		} catch (Exception e) {
			throw e;
		} finally {

			sql = null;
		}

	}

	@Override
	public List<ItemPrestacaoContaTurmaVO> consultarItemPrestacaoContaTurmaPorPrestacaoConta(Integer prestacaoConta, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM ItemPrestacaoContaTurma WHERE  prestacaoConta = ").append(prestacaoConta);
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
	}

	public List<ItemPrestacaoContaTurmaVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<ItemPrestacaoContaTurmaVO> objs = new ArrayList<ItemPrestacaoContaTurmaVO>(0);
		while (rs.next()) {
			objs.add(montarDados(rs, nivelMontarDados, usuarioVO));
		}
		return objs;
	}

	public ItemPrestacaoContaTurmaVO montarDados(SqlRowSet rs, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ItemPrestacaoContaTurmaVO obj = new ItemPrestacaoContaTurmaVO();
		obj.setCodigo(rs.getInt("codigo"));
		obj.getPrestacaoConta().setCodigo(rs.getInt("prestacaoConta"));
		obj.getPrestacaoContaTurma().setCodigo(rs.getInt("prestacaoContaTurma"));
		obj.setNovoObj(false);
		obj.setPrestacaoContaTurma(getFacadeFactory().getPrestacaoContaFacade().consultarPorChavePrimaria(obj.getPrestacaoContaTurma().getCodigo(), NivelMontarDados.BASICO, usuarioVO));
		return obj;
	}

}
