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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CondicaoPagamentoVO;
import negocio.comuns.compras.CotacaoFornecedorVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.CotacaoFornecedorInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class CotacaoFornecedor extends ControleAcesso implements CotacaoFornecedorInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6865305614412733228L;
	protected static String idEntidade;

	public CotacaoFornecedor() {
		super();
		setIdEntidade("Cotacao");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCotacaoFornecedors(CotacaoVO cotacao, List<CotacaoFornecedorVO> objetos, UsuarioVO usuario) throws Exception {
		validarSeRegistroForamExcluidoDasListaSubordinadas(objetos, "CotacaoFornecedor", "cotacao", cotacao.getCodigo(), usuario);
		for (CotacaoFornecedorVO obj : (List<CotacaoFornecedorVO>) objetos) {
			obj.setCotacao(cotacao);
			if (Uteis.isAtributoPreenchido(obj)) {
				alterar(obj, usuario);
			} else {
				incluir(obj, usuario);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirCotacaoFornecedors(CotacaoVO cotacao, List<CotacaoFornecedorVO> objetos, UsuarioVO usuario) throws Exception {
		for (CotacaoFornecedorVO obj : objetos) {
			obj.setCotacao(cotacao);
			incluir(obj, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CotacaoFornecedorVO obj, UsuarioVO usuario) throws Exception {
		try {
			CotacaoFornecedorVO.validarDados(obj);
			final String sql = "INSERT INTO CotacaoFornecedor(cotacao, fornecedor, valorTotal, formaPagamento, condicaoPagamento, tipoCriacaoContaPagar, dataPrevisaoEntrega ) VALUES (?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCotacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFornecedor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorTotal(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFormaPagamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCondicaoPagamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoCriacaoContaPagarEnum(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataPrevisaoEntrega(), ++i, sqlInserir);
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
			getFacadeFactory().getItemCotacaoFacade().incluirItemCotacao(obj, obj.getItemCotacaoVOs(), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setCodigo(0);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CotacaoFornecedorVO obj, UsuarioVO usuario) throws Exception {
		CotacaoFornecedorVO.validarDados(obj);
		final String sql = "UPDATE CotacaoFornecedor set cotacao=?, fornecedor=?, valorTotal=?, formaPagamento=?, condicaoPagamento=?, tipoCriacaoContaPagar=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getCotacao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getFornecedor(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getValorTotal(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getFormaPagamento(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCondicaoPagamento(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTipoCriacaoContaPagarEnum(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
				return sqlAlterar;
			}
		});
		getFacadeFactory().getItemCotacaoFacade().alterarItemCotacao(obj, obj.getItemCotacaoVOs(), usuario);
	}
	
	@Override
	public List<CotacaoFornecedorVO> consultarCotacaoFornecedors(Integer cotacao, int nivelMontarDados, UsuarioVO usuario) throws Exception {		
		String sql = "SELECT * FROM CotacaoFornecedor WHERE cotacao = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { cotacao });
		List<CotacaoFornecedorVO> objetos = new ArrayList<>(0);
		while (resultado.next()) {
			CotacaoFornecedorVO novoObj = new CotacaoFornecedorVO();
			novoObj = CotacaoFornecedor.montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	public CotacaoFornecedorVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM CotacaoFornecedor WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( CotacaoFornecedor ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static CotacaoFornecedorVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CotacaoFornecedorVO obj = new CotacaoFornecedorVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.getCotacao().setCodigo((dadosSQL.getInt("cotacao")));
		obj.getFormaPagamento().setCodigo((dadosSQL.getInt("formaPagamento")));
		obj.getCondicaoPagamento().setCodigo((dadosSQL.getInt("condicaoPagamento")));
		obj.getFornecedor().setCodigo((dadosSQL.getInt("fornecedor")));
		obj.setValorTotal(dadosSQL.getDouble("valorTotal"));
		obj.setTipoCriacaoContaPagarEnum(TipoCriacaoContaPagarEnum.valueOf(dadosSQL.getString("tipoCriacaoContaPagar")));
		obj.setDataPrevisaoEntrega(dadosSQL.getDate("dataPrevisaoEntrega"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setItemCotacaoVOs(getFacadeFactory().getItemCotacaoFacade().consultarItemCotacaos(obj.getCodigo(), nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosFormaPagamento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosCondicaoPagamento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obj;
	}

	public static void montarDadosFornecedor(CotacaoFornecedorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFornecedor().getCodigo().intValue() == 0) {
			obj.setFornecedor(new FornecedorVO());
			return;
		}
		obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosFormaPagamento(CotacaoFornecedorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFormaPagamento().getCodigo().intValue() == 0) {
			obj.setFormaPagamento(new FormaPagamentoVO());
			return;
		}
		obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosCondicaoPagamento(CotacaoFornecedorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCondicaoPagamento().getCodigo().intValue() == 0) {
			obj.setCondicaoPagamento(new CondicaoPagamentoVO());
			return;
		}
		obj.setCondicaoPagamento(getFacadeFactory().getCondicaoPagamentoFacade().consultarPorChavePrimaria(obj.getCondicaoPagamento().getCodigo(), false, nivelMontarDados, usuario));
	}

	

	public static String getIdEntidade() {
		return CotacaoFornecedor.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		CotacaoFornecedor.idEntidade = idEntidade;
	}

}
