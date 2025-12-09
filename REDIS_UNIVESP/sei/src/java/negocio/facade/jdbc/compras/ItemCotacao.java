package negocio.facade.jdbc.compras;

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

import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CotacaoFornecedorVO;
import negocio.comuns.compras.ItemCotacaoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.ItemCotacaoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ItemCotacao extends ControleAcesso implements ItemCotacaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8253086667941258748L;
	protected static String idEntidade;

	public ItemCotacao() {
		super();
		setIdEntidade("Cotacao");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarItemCotacao(CotacaoFornecedorVO cotacaoFornecedor, List<ItemCotacaoVO> objetos, UsuarioVO usuario) throws Exception {
		validarSeRegistroForamExcluidoDasListaSubordinadas(objetos, "ItemCotacao", "cotacaoFornecedor", cotacaoFornecedor.getCodigo(), usuario);
		for (ItemCotacaoVO obj : (List<ItemCotacaoVO>) objetos) {
			obj.setCotacao(cotacaoFornecedor.getCotacao());
			obj.setCotacaoFornecedor(cotacaoFornecedor);
			if (Uteis.isAtributoPreenchido(obj)) {
				alterar(obj, usuario);
			} else {
				incluir(obj, usuario);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirItemCotacao(CotacaoFornecedorVO cotacaoFornecedor, List<ItemCotacaoVO> objetos, UsuarioVO usuario) throws Exception {
		for (ItemCotacaoVO obj : objetos) {
			obj.setCotacao(cotacaoFornecedor.getCotacao());
			obj.setCotacaoFornecedor(cotacaoFornecedor);
			incluir(obj, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ItemCotacaoVO obj, UsuarioVO usuario) throws Exception {
		try {
			ItemCotacaoVO.validarDados(obj);
			final String sql = "INSERT INTO ItemCotacao( cotacao, cotacaoFornecedor, produto,  quantidade, compraAutorizadaFornecedor, precoAnterior, precoUnitario, precoTotal ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCotacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCotacaoFornecedor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getProduto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQuantidade(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCompraAutorizadaFornecedor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPrecoAnterior(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPrecoUnitario(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPrecoTotal(), ++i, sqlInserir);
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
			getFacadeFactory().getItemCotacaoUnidadeEnsinoFacade().incluirItemCotacaoUnidadeEnsino(obj, obj.getListaItemCotacaoUnidadeEnsinoVOs(), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setCodigo(0);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemCotacaoVO obj, UsuarioVO usuario) throws Exception {
		ItemCotacaoVO.validarDados(obj);
		final String sql = "UPDATE ItemCotacao set cotacao=?, cotacaoFornecedor=?, produto=?,  quantidade=?, compraAutorizadaFornecedor=?, precoAnterior=?, precoUnitario=?, precoTotal=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getCotacao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCotacaoFornecedor(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getProduto(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQuantidade(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCompraAutorizadaFornecedor(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getPrecoAnterior(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getPrecoUnitario(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getPrecoTotal(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
				return sqlAlterar;
			}
		});
		getFacadeFactory().getItemCotacaoUnidadeEnsinoFacade().alterarItemCotacaoUnidadeEnsino(obj, obj.getListaItemCotacaoUnidadeEnsinoVOs(), usuario);
	}

	public Double consultarUltimoPrecoProdutoFornecedor(Integer fornecedor, Integer produto) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("Select ItemCotacao.precoUnitario as precoAnterior from ItemCotacao");
		sb.append(" inner join CotacaoFornecedor on CotacaoFornecedor.codigo  = ItemCotacao.CotacaoFornecedor ");
		sb.append(" inner join Cotacao on Cotacao.codigo  = CotacaoFornecedor.Cotacao ");
		sb.append(" WHERE ItemCotacao.produto = ").append(produto);
		sb.append(" and CotacaoFornecedor.fornecedor = ").append(fornecedor);
		sb.append(" order by Cotacao.dataCotacao desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (Double) Uteis.getSqlRowSetTotalizador(tabelaResultado, "precoAnterior", TipoCampoEnum.DOUBLE);
	}

	public List<ItemCotacaoVO> consultarItemCotacaos(Integer cotacaoFornecedor, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ItemCotacao.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT ItemCotacao.* FROM ItemCotacao inner join produtoservico on produtoservico.codigo = ItemCotacao.produto WHERE cotacaoFornecedor = ? order by produtoservico.nome ";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { cotacaoFornecedor });
		while (resultado.next()) {
			ItemCotacaoVO novoObj = new ItemCotacaoVO();
			novoObj = ItemCotacao.montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	public ItemCotacaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ItemCotacao WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ItemCotacao ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static ItemCotacaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ItemCotacaoVO obj = new ItemCotacaoVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.getCotacao().setCodigo((dadosSQL.getInt("cotacao")));
		obj.getCotacaoFornecedor().setCodigo((dadosSQL.getInt("cotacaoFornecedor")));
		obj.getProduto().setCodigo((dadosSQL.getInt("produto")));
		obj.setPrecoUnitario((dadosSQL.getDouble("precoUnitario")));
		obj.setPrecoAnterior((dadosSQL.getDouble("precoAnterior")));
		obj.setQuantidade((dadosSQL.getDouble("quantidade")));
		obj.setCompraAutorizadaFornecedor(dadosSQL.getBoolean("compraAutorizadaFornecedor"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setListaItemCotacaoUnidadeEnsinoVOs(getFacadeFactory().getItemCotacaoUnidadeEnsinoFacade().consultarItemCotacaoUnidadeEnsinos(obj.getCodigo(), nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosProduto(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);

		return obj;
	}

	public static void montarDadosProduto(ItemCotacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProduto().getCodigo().intValue() == 0) {
			obj.setProduto(new ProdutoServicoVO());
			return;
		}
		obj.setProduto(getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(obj.getProduto().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return ItemCotacao.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ItemCotacao.idEntidade = idEntidade;
	}

}
