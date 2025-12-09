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
import negocio.comuns.financeiro.ItemPrestacaoContaOrigemContaReceberVO;
import negocio.comuns.financeiro.ItemPrestacaoContaReceberVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.ItemPrestacaoContaOrigemContaReceberInterfaceFacade;

@Service
@Lazy
public class ItemPrestacaoContaOrigemContaReceber extends ControleAcesso implements ItemPrestacaoContaOrigemContaReceberInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3123915092461115392L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ItemPrestacaoContaOrigemContaReceberVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO ItemPrestacaoContaOrigemContaReceber( tipoOrigemContaReceber, prestacaoConta, valor, valorinformadomanual, valormanual) VALUES ( ?, ?, ?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getTipoOrigemContaReceber().toString());
					sqlInserir.setInt(2, obj.getPrestacaoConta().getCodigo());
					sqlInserir.setDouble(3, obj.getValor());
					sqlInserir.setBoolean(4, obj.getValorInformadoManual());
					sqlInserir.setDouble(5, obj.getValorManual());
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
			getFacadeFactory().getItemPrestacaoContaReceberFacade().incluirItemPrestacaoContaReceberVOs(obj.getItemPrestacaoContaReceberVOs(), obj);
		} catch (Exception e) {
			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemPrestacaoContaOrigemContaReceberVO obj) throws Exception {
		try {
			final String sql = "UPDATE ItemPrestacaoContaOrigemContaReceber set tipoOrigemContaReceber=?, prestacaoConta = ?, valor = ?, valorinformadomanual = ?, valormanual = ? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getTipoOrigemContaReceber().toString());
					sqlAlterar.setInt(2, obj.getPrestacaoConta().getCodigo());
					sqlAlterar.setDouble(3, obj.getValor());
					sqlAlterar.setBoolean(4, obj.getValorInformadoManual());
					sqlAlterar.setDouble(5, obj.getValorManual());
					sqlAlterar.setInt(6, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getItemPrestacaoContaReceberFacade().alterarItemPrestacaoContaReceberVOs(obj.getItemPrestacaoContaReceberVOs(), obj);

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void incluirItemPrestacaoContaOrigemContaReceberVOs(List<ItemPrestacaoContaOrigemContaReceberVO> itemPrestacaoContaOrigemContaReceberVOs, PrestacaoContaVO prestacaoContaVO) throws Exception {
		for (ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO : itemPrestacaoContaOrigemContaReceberVOs) {
			itemPrestacaoContaOrigemContaReceberVO.setPrestacaoConta(prestacaoContaVO);
			incluir(itemPrestacaoContaOrigemContaReceberVO);
		}
	}

	@Override
	public void alterarItemPrestacaoContaOrigemContaReceberVOs(List<ItemPrestacaoContaOrigemContaReceberVO> itemPrestacaoContaOrigemContaReceberVOs, PrestacaoContaVO prestacaoContaVO) throws Exception {
		excluirItemPrestacaoContaOrigemContaReceberVOs(itemPrestacaoContaOrigemContaReceberVOs, prestacaoContaVO);
		for (ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO : itemPrestacaoContaOrigemContaReceberVOs) {
			itemPrestacaoContaOrigemContaReceberVO.setPrestacaoConta(prestacaoContaVO);
			if (itemPrestacaoContaOrigemContaReceberVO.getCodigo() == null || itemPrestacaoContaOrigemContaReceberVO.getCodigo() == 0) {
				incluir(itemPrestacaoContaOrigemContaReceberVO);
			} else {
				alterar(itemPrestacaoContaOrigemContaReceberVO);
			}
		}
	}

	@Override
	public void excluirItemPrestacaoContaOrigemContaReceberVOs(List<ItemPrestacaoContaOrigemContaReceberVO> itemPrestacaoContaOrigemContaReceberVOs, PrestacaoContaVO prestacaoContaVO) throws Exception {
		StringBuilder notIn = new StringBuilder("");
		StringBuilder sql = new StringBuilder("");
		try {

			int x = 0;
			for (ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO : itemPrestacaoContaOrigemContaReceberVOs) {
				if (x == 0) {
					notIn.append(itemPrestacaoContaOrigemContaReceberVO.getCodigo());
					x++;
				} else {
					notIn.append(", ").append(itemPrestacaoContaOrigemContaReceberVO.getCodigo());
				}
			}

			sql.append(" DELETE FROM ItemPrestacaoContaReceber where  itemPrestacaoContaOrigemContaReceber in (");
			sql.append(" select codigo from itemPrestacaoContaOrigemContaReceber  where prestacaoConta = ").append(prestacaoContaVO.getCodigo());
			if (!notIn.toString().isEmpty()) {
				sql.append(" and codigo not in (").append(notIn).append(") ");
			}
			sql.append(" ) ");

			getConexao().getJdbcTemplate().execute(sql.toString());
			sql = new StringBuilder();
			sql.append("DELETE FROM itemPrestacaoContaOrigemContaReceber where prestacaoConta = ").append(prestacaoContaVO.getCodigo());
			if (!notIn.toString().isEmpty()) {
				sql.append(" and codigo not in (").append(notIn).append(") ");
			}
			getConexao().getJdbcTemplate().execute(sql.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			notIn = null;
			sql = null;
		}

	}

	@Override
	public List<ItemPrestacaoContaOrigemContaReceberVO> consultarItemPrestacaoContaOrigemContaReceberPorPrestacaoConta(Integer prestacaoConta, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM itemPrestacaoContaOrigemContaReceber WHERE  prestacaoConta = ").append(prestacaoConta);
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
	}

	public List<ItemPrestacaoContaOrigemContaReceberVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<ItemPrestacaoContaOrigemContaReceberVO> objs = new ArrayList<ItemPrestacaoContaOrigemContaReceberVO>(0);
		while (rs.next()) {
			objs.add(montarDados(rs, nivelMontarDados, usuarioVO));
		}
		return objs;
	}

	public ItemPrestacaoContaOrigemContaReceberVO montarDados(SqlRowSet rs, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ItemPrestacaoContaOrigemContaReceberVO obj = new ItemPrestacaoContaOrigemContaReceberVO();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setValor(rs.getDouble("valor"));
		obj.setValorInformadoManual(rs.getBoolean("valorinformadomanual"));
		obj.setValorManual(rs.getDouble("valormanual"));
		obj.getPrestacaoConta().setCodigo(rs.getInt("prestacaoConta"));
		obj.setTipoOrigemContaReceber(TipoOrigemContaReceber.valueOf(rs.getString("tipoOrigemContaReceber")));
		obj.setNovoObj(false);
		if (nivelMontarDados.equals(NivelMontarDados.FORCAR_RECARGATODOSOSDADOS)) {
			obj.getItemPrestacaoContaReceberVOs().addAll(getFacadeFactory().getItemPrestacaoContaReceberFacade().consultarItemPrestacaoContaReceberPorItemPrestacaoContaOrigemContaReceber(obj, nivelMontarDados, usuarioVO));
		}
		return obj;
	}

	@Override
	public void adicionarItemPrestacaoContaReceberVO(ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO, ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO) {
		for (ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO2 : itemPrestacaoContaOrigemContaReceberVO.getItemPrestacaoContaReceberVOs()) {
			if (itemPrestacaoContaReceberVO2.getContaReceber().getCodigo().intValue() == itemPrestacaoContaReceberVO.getContaReceber().getCodigo().intValue()) {
				return;
			}
		}
		itemPrestacaoContaOrigemContaReceberVO.getItemPrestacaoContaReceberVOs().add(itemPrestacaoContaReceberVO);
		itemPrestacaoContaOrigemContaReceberVO.setValor(itemPrestacaoContaOrigemContaReceberVO.getValor() + itemPrestacaoContaReceberVO.getContaReceber().getValorRecebido());

	}

	@Override
	public void removerItemPrestacaoContaReceberVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO, ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO) {
		itemPrestacaoContaOrigemContaReceberVO.getItemPrestacaoContaReceberVOs().remove(itemPrestacaoContaReceberVO);
		itemPrestacaoContaOrigemContaReceberVO.setValor(itemPrestacaoContaOrigemContaReceberVO.getValor() - itemPrestacaoContaReceberVO.getContaReceber().getValorRecebido());
		prestacaoContaVO.setValorTotalRecebimento(prestacaoContaVO.getValorTotalRecebimento() - itemPrestacaoContaReceberVO.getContaReceber().getValorRecebido());
		if (itemPrestacaoContaOrigemContaReceberVO.getItemPrestacaoContaReceberVOs().isEmpty() && itemPrestacaoContaOrigemContaReceberVO.getValor() <= 0) {
			getFacadeFactory().getPrestacaoContaFacade().removerItemPrestacaoContaOrigemContaReceberVO(prestacaoContaVO, itemPrestacaoContaOrigemContaReceberVO);
		}
	}

	@Override
	public void adicionarVariasItensPrestacaoContaReceberVOs(List<ItemPrestacaoContaReceberVO> itemPrestacaoContaReceberVOs, PrestacaoContaVO prestacaoContaVO) throws Exception {
		ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberNovoVO = null;
		q: for (ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO : itemPrestacaoContaReceberVOs) {
			for (ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO : prestacaoContaVO.getItemPrestacaoContaOrigemContaReceberVOs()) {
				if (itemPrestacaoContaOrigemContaReceberVO.getTipoOrigemContaReceber().equals(TipoOrigemContaReceber.getEnum(itemPrestacaoContaReceberVO.getContaReceber().getTipoOrigem()))) {
					prestacaoContaVO.setValorTotalRecebimento(prestacaoContaVO.getValorTotalRecebimento() - itemPrestacaoContaOrigemContaReceberVO.getValor());
					adicionarItemPrestacaoContaReceberVO(itemPrestacaoContaOrigemContaReceberVO, itemPrestacaoContaReceberVO);
					prestacaoContaVO.setValorTotalRecebimento(prestacaoContaVO.getValorTotalRecebimento() + itemPrestacaoContaOrigemContaReceberVO.getValor());
					continue q;
				}
			}
			itemPrestacaoContaOrigemContaReceberNovoVO = new ItemPrestacaoContaOrigemContaReceberVO();
			itemPrestacaoContaOrigemContaReceberNovoVO.setTipoOrigemContaReceber(TipoOrigemContaReceber.getEnum(itemPrestacaoContaReceberVO.getContaReceber().getTipoOrigem()));
			adicionarItemPrestacaoContaReceberVO(itemPrestacaoContaOrigemContaReceberNovoVO, itemPrestacaoContaReceberVO);
			getFacadeFactory().getPrestacaoContaFacade().adicionarItemPrestacaoContaOrigemContaReceberVO(prestacaoContaVO, itemPrestacaoContaOrigemContaReceberNovoVO);
		}
	}

}
