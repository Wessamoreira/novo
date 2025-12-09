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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.ItemCotacaoUnidadeEnsinoVO;
import negocio.comuns.compras.ItemCotacaoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.ItemCotacaoUnidadeEnsinoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ItemCotacaoUnidadeEnsino extends ControleAcesso implements ItemCotacaoUnidadeEnsinoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6229559267034371375L;
	protected static String idEntidade;
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarItemCotacaoUnidadeEnsino(ItemCotacaoVO itemCotacao, List<ItemCotacaoUnidadeEnsinoVO> objetos, UsuarioVO usuario) throws Exception {
		validarSeRegistroForamExcluidoDasListaSubordinadas(objetos, "ItemCotacaoUnidadeEnsino", "ItemCotacao", itemCotacao.getCodigo(), usuario);
		for (ItemCotacaoUnidadeEnsinoVO obj : (List<ItemCotacaoUnidadeEnsinoVO>) objetos) {
			obj.setItemCotacao(itemCotacao);
			obj.setCotacao(itemCotacao.getCotacao());
			if (Uteis.isAtributoPreenchido(obj)) {
				alterar(obj, usuario);
			} else {
				incluir(obj, usuario);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirItemCotacaoUnidadeEnsino(ItemCotacaoVO itemCotacao, List<ItemCotacaoUnidadeEnsinoVO> objetos, UsuarioVO usuario) throws Exception {
		for (ItemCotacaoUnidadeEnsinoVO obj : objetos) {
			obj.setItemCotacao(itemCotacao);
			obj.setCotacao(itemCotacao.getCotacao());
			incluir(obj, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ItemCotacaoUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception {
		try {
			ItemCotacaoUnidadeEnsinoVO.validarDados(obj);
			final String sql = "INSERT INTO ItemCotacaoUnidadeEnsino( unidadeEnsino, produto, itemCotacao, cotacao, qtdRequisicao, qtdAdicional, totalQtd ) VALUES ( ?, ?, ?, ?, ?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i=0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getProdutoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getItemCotacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCotacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQtdRequisicao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQtdAdicional(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalQtd(), ++i, sqlInserir);
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
			obj.setCodigo(0);
			throw e;
		}
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemCotacaoUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception {
		ItemCotacaoUnidadeEnsinoVO.validarDados(obj);
		final String sql = "UPDATE ItemCotacaoUnidadeEnsino set unidadeEnsino=?, produto=?, itemCotacao=?, cotacao=?, qtdRequisicao=?, qtdAdicional=?, totalQtd=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				int i=0;
				Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getProdutoVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getItemCotacao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCotacao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQtdRequisicao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQtdAdicional(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTotalQtd(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
				return sqlAlterar;
			}
		});
	}


	public List consultarItemCotacaoUnidadeEnsinos(Integer itemCotacao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ItemCotacao.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM ItemCotacaoUnidadeEnsino WHERE itemCotacao = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql,  itemCotacao );
		while (resultado.next()) {
			ItemCotacaoUnidadeEnsinoVO novoObj = new ItemCotacaoUnidadeEnsinoVO();
			novoObj = montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	
	
	public  List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public ItemCotacaoUnidadeEnsinoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ItemCotacaoUnidadeEnsinoVO obj = new ItemCotacaoUnidadeEnsinoVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setQtdAdicional((dadosSQL.getDouble("qtdadicional")));
		obj.getUnidadeEnsinoVO().setCodigo((dadosSQL.getInt("unidadeensino")));
		obj.getProdutoVO().setCodigo((dadosSQL.getInt("produto")));
		obj.getItemCotacao().setCodigo((dadosSQL.getInt("itemCotacao")));
		obj.getCotacao().setCodigo((dadosSQL.getInt("cotacao")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setListaRequisicaoItemVOs(getFacadeFactory().getRequisicaoItemFacade().consultarRequisicaoItemsPorItemCotacaoUnidadeEnsinoVO(null, obj, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosProduto(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		return obj;
	}

	public static void montarDadosProduto(ItemCotacaoUnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProdutoVO().getCodigo().intValue() == 0) {
			obj.setProdutoVO(new ProdutoServicoVO());
			return;
		}
		obj.setProdutoVO(getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(obj.getProdutoVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(ItemCotacaoUnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	public ItemCotacaoUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ItemCotacaoUnidadeEnsino WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ItemCotacaoUnidadeEnsino ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return ItemCotacaoUnidadeEnsino.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ItemCotacaoUnidadeEnsino.idEntidade = idEntidade;
	}
}