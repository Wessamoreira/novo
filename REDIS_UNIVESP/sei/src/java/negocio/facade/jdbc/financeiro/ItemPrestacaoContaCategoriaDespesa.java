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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ItemPrestacaoContaCategoriaDespesaVO;
import negocio.comuns.financeiro.ItemPrestacaoContaPagarVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.ItemPrestacaoContaCategoriaDespesaInterfaceFacade;

@Service
@Scope
@Lazy
public class ItemPrestacaoContaCategoriaDespesa extends ControleAcesso implements ItemPrestacaoContaCategoriaDespesaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8530016629066380382L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ItemPrestacaoContaCategoriaDespesaVO obj) throws Exception {
		try {
		final String sql = "INSERT INTO ItemPrestacaoContaCategoriaDespesa( categoriaDespesa, prestacaoConta, valor, valorTotalItemPrestacaoContaPagar, valorInformadoManual) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
				final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getCategoriaDespesa().getCodigo());
				sqlInserir.setInt(2, obj.getPrestacaoConta().getCodigo());
				sqlInserir.setDouble(3, obj.getValor());
				sqlInserir.setDouble(4, obj.getValorTotalItemPrestacaoContaPagar());
				sqlInserir.setBoolean(5, obj.getValorInformadoManual());
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
		getFacadeFactory().getItemPrestacaoContaPagarFacade().incluirItemPrestacaoContaPagarVOs(obj.getItemPrestacaoContaPagarVOs(), obj);
		} catch (Exception e) {
			obj.setCodigo(0);
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemPrestacaoContaCategoriaDespesaVO obj) throws Exception {
		try {
			final String sql = "UPDATE ItemPrestacaoContaCategoriaDespesa set categoriaDespesa=?, prestacaoConta = ?, valor = ?,  valorTotalItemPrestacaoContaPagar = ?, valorInformadoManual = ? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getCategoriaDespesa().getCodigo());
					sqlAlterar.setInt(2, obj.getPrestacaoConta().getCodigo());
					sqlAlterar.setDouble(3, obj.getValor());
					sqlAlterar.setDouble(4, obj.getValorTotalItemPrestacaoContaPagar());
					sqlAlterar.setBoolean(5, obj.getValorInformadoManual());
					sqlAlterar.setInt(6, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getItemPrestacaoContaPagarFacade().alterarItemPrestacaoContaPagarVOs(obj.getItemPrestacaoContaPagarVOs(), obj);

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void incluirItemPrestacaoContaCategoriaDespesaVOs(List<ItemPrestacaoContaCategoriaDespesaVO> itemPrestacaoContaCategoriaDespesaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception {
		for (ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO : itemPrestacaoContaCategoriaDespesaVOs) {
			itemPrestacaoContaCategoriaDespesaVO.setPrestacaoConta(prestacaoContaVO);
			incluir(itemPrestacaoContaCategoriaDespesaVO);
		}
	}

	@Override
	public void alterarItemPrestacaoContaCategoriaDespesaVOs(List<ItemPrestacaoContaCategoriaDespesaVO> itemPrestacaoContaCategoriaDespesaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception {
		excluirItemPrestacaoContaCategoriaDespesaVOs(itemPrestacaoContaCategoriaDespesaVOs, prestacaoContaVO);
		for (ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO : itemPrestacaoContaCategoriaDespesaVOs) {
			itemPrestacaoContaCategoriaDespesaVO.setPrestacaoConta(prestacaoContaVO);
			if (itemPrestacaoContaCategoriaDespesaVO.getCodigo() == null || itemPrestacaoContaCategoriaDespesaVO.getCodigo() == 0) {
				incluir(itemPrestacaoContaCategoriaDespesaVO);
			} else {
				alterar(itemPrestacaoContaCategoriaDespesaVO);
			}
		}
	}

	@Override
	public void excluirItemPrestacaoContaCategoriaDespesaVOs(List<ItemPrestacaoContaCategoriaDespesaVO> itemPrestacaoContaCategoriaDespesaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception {
		StringBuilder notIn = new StringBuilder("");
		StringBuilder sql = new StringBuilder("");
		try {
			int x = 0;
			for (ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO : itemPrestacaoContaCategoriaDespesaVOs) {
				if (x == 0) {
					notIn.append(itemPrestacaoContaCategoriaDespesaVO.getCodigo());
					x++;
				} else {
					notIn.append(", ").append(itemPrestacaoContaCategoriaDespesaVO.getCodigo());
				}
			}

			sql.append(" DELETE FROM ItemPrestacaoContaPagar where  itemPrestacaoContaCategoriaDespesa in (");
			sql.append(" select codigo from itemPrestacaoContaCategoriaDespesa  where prestacaoConta = ").append(prestacaoContaVO.getCodigo());
			if (!notIn.toString().isEmpty()) {
				sql.append(" and codigo not in (").append(notIn).append(") ");
			}
			sql.append(" ) ");

			getConexao().getJdbcTemplate().execute(sql.toString());
			sql = new StringBuilder();
			sql.append("DELETE FROM ItemPrestacaoContaCategoriaDespesa where prestacaoConta = ").append(prestacaoContaVO.getCodigo());
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
	public List<ItemPrestacaoContaCategoriaDespesaVO> consultarItemPrestacaoContaCategoriaDespesaPorPrestacaoConta(Integer prestacaoConta, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT ItemPrestacaoContaCategoriaDespesa.*, CategoriaDespesa.descricao as descricao FROM ItemPrestacaoContaCategoriaDespesa inner join CategoriaDespesa on CategoriaDespesa.codigo = ItemPrestacaoContaCategoriaDespesa.CategoriaDespesa WHERE  prestacaoConta = ").append(prestacaoConta);
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
	}

	public List<ItemPrestacaoContaCategoriaDespesaVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<ItemPrestacaoContaCategoriaDespesaVO> objs = new ArrayList<>(0);
		while (rs.next()) {
			objs.add(montarDados(rs, nivelMontarDados, usuarioVO));
		}
		return objs;
	}

	public ItemPrestacaoContaCategoriaDespesaVO montarDados(SqlRowSet rs, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ItemPrestacaoContaCategoriaDespesaVO obj = new ItemPrestacaoContaCategoriaDespesaVO();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setValor(rs.getDouble("valor"));
		obj.setValorTotalItemPrestacaoContaPagar(rs.getDouble("valorTotalItemPrestacaoContaPagar"));
		obj.getPrestacaoConta().setCodigo(rs.getInt("prestacaoConta"));
		obj.getCategoriaDespesa().setCodigo(rs.getInt("categoriaDespesa"));
		obj.getCategoriaDespesa().setDescricao(rs.getString("descricao"));
		obj.setValorInformadoManual(rs.getBoolean("valorinformadomanual"));
		
		obj.setNovoObj(false);
		if (nivelMontarDados.equals(NivelMontarDados.FORCAR_RECARGATODOSOSDADOS)) {
			obj.getItemPrestacaoContaPagarVOs().addAll(getFacadeFactory().getItemPrestacaoContaPagarFacade().consultarItemPrestacaoContaPagarPorItemPrestacaoContaCategoriaDespesa(obj, nivelMontarDados, usuarioVO));
			if(!Uteis.isAtributoPreenchido(obj.getValorTotalItemPrestacaoContaPagar())){
				obj.calcularValorTotalItemPrestacaoContaPagar();
			}
		}
		return obj;

	}

	@Override
	public void adicionarItemPrestacaoContaPagarVO(ItemPrestacaoContaCategoriaDespesaVO ipccd, ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO) {
		int index = 0;
		for (ItemPrestacaoContaPagarVO objExistente : ipccd.getItemPrestacaoContaPagarVOs()) {
			if (objExistente.equalsCampoSelecaoLista(itemPrestacaoContaPagarVO)) {
				ipccd.getItemPrestacaoContaPagarVOs().set(index, itemPrestacaoContaPagarVO);
				ipccd.calcularValorTotalItemPrestacaoContaPagar();
				return;
			}
			index++;
		}
		itemPrestacaoContaPagarVO.setItemPrestacaoContaCategoriaDespesa(ipccd);		
		ipccd.getItemPrestacaoContaPagarVOs().add(itemPrestacaoContaPagarVO);
		ipccd.calcularValorTotalItemPrestacaoContaPagar();
	}
	
	@Override
	public ItemPrestacaoContaPagarVO consultarItemPrestacaoContaCategoriaDespesaVO(ItemPrestacaoContaCategoriaDespesaVO ipccd, ContaPagarVO contaPagar) {
		for (ItemPrestacaoContaPagarVO objExistente : ipccd.getItemPrestacaoContaPagarVOs()){
			if (objExistente.getContaPagar().getCodigo().equals(contaPagar.getCodigo())) {
				return objExistente;
			}
		}
		return new ItemPrestacaoContaPagarVO();
	}
	
	

	@Override
	public void removerItemPrestacaoContaPagarVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO, ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO) {
		Iterator<ItemPrestacaoContaPagarVO> i = itemPrestacaoContaCategoriaDespesaVO.getItemPrestacaoContaPagarVOs().iterator();
		while (i.hasNext()) {
			ItemPrestacaoContaPagarVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoLista(itemPrestacaoContaPagarVO)) {
				i.remove();
				itemPrestacaoContaCategoriaDespesaVO.calcularValorTotalItemPrestacaoContaPagar();
				break;
			}
		}
		if (itemPrestacaoContaCategoriaDespesaVO.getItemPrestacaoContaPagarVOs().isEmpty()) {
			getFacadeFactory().getPrestacaoContaFacade().removerItemPrestacaoContaCategoriaDespesaVO(prestacaoContaVO, itemPrestacaoContaCategoriaDespesaVO);
		}

	}

}