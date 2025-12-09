package negocio.facade.jdbc.financeiro;

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
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ItemCondicaoDescontoRenegociacaoUnidadeEnsinoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ItemCondicaoDescontoRenegociacaoUnidadeEnsino extends ControleAcesso implements ItemCondicaoDescontoRenegociacaoUnidadeEnsinoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8454705425252391174L;
	protected static String idEntidade;

	public ItemCondicaoDescontoRenegociacaoUnidadeEnsino() {
		super();
		setIdEntidade("CondicaoDescontoRenegociacao");
	}

	public void validarDados(ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO obj)  {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()), "O Campo Unidade Ensino ( ItemCondicaoDescontoRenegociacaoUnidadeEnsino ) deve ser informado.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ItemCondicaoDescontoRenegociacaoVO obj, List<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarSeRegistroForamExcluidoDasListaSubordinadas(lista, "itemCondicaoDescontoRenegociacaoUnidadeEnsino", "itemCondicaoDescontoRenegociacao", obj.getCodigo(), usuarioVO);
		for (ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO icdrue : lista) {
			validarDados(icdrue);
			if (icdrue.getCodigo() == 0) {
				incluir(icdrue);
			} else {
				alterar(icdrue);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO obj) throws Exception {
		try {			
			final String sql = "INSERT INTO ItemCondicaoDescontoRenegociacaoUnidadeEnsino( itemCondicaoDescontoRenegociacao, unidadeEnsino) VALUES ( ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);

					if (obj.getItemCondicaoDescontoRenegociacaoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getItemCondicaoDescontoRenegociacaoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getUnidadeEnsinoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}

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
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO obj) throws Exception {
		try {
			validarDados(obj);
			final String sql = "UPDATE ItemCondicaoDescontoRenegociacaoUnidadeEnsino set itemCondicaoDescontoRenegociacao=?, unidadeEnsino=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getItemCondicaoDescontoRenegociacaoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getItemCondicaoDescontoRenegociacaoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getUnidadeEnsinoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO obj = new ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO();
		obj.setNovoObj(false);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.getItemCondicaoDescontoRenegociacaoVO().setCodigo(dadosSQL.getInt("itemCondicaoDescontoRenegociacao"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(true);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setUnidadeEnsinoVO(Uteis.montarDadosVO(dadosSQL.getInt("unidadeEnsino"), UnidadeEnsinoVO.class, p -> getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO)));
		obj.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(true);
		return obj;
	}
	

	@Override
	public List<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> consultarItemCondicaoDescontoRenegociacaoUnidadeEnsinoPoritemCondicaoDescontoRenegociacao(Integer itemCondicaoDescontoRenegociacao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ItemCondicaoDescontoRenegociacaoUnidadeEnsino.consultar(getIdEntidade());
		List<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> objetos = new ArrayList<>();
		String sql = "SELECT * FROM ItemCondicaoDescontoRenegociacaoUnidadeEnsino WHERE itemCondicaoDescontoRenegociacao = " + itemCondicaoDescontoRenegociacao;
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		while (resultado.next()) {
			ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO novoObj = new ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO();
			novoObj = montarDados(resultado, nivelMontarDados, usuarioVO);
			objetos.add(novoObj);
		}
		return objetos;
	}

	public ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ItemCondicaoDescontoRenegociacaoUnidadeEnsino.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ItemCondicaoDescontoRenegociacaoUnidadeEnsino WHERE codigo = " + codigoPrm;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ItemCondicaoDescontoRenegociacaoUnidadeEnsino ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return ItemCondicaoDescontoRenegociacaoUnidadeEnsino.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ItemCondicaoDescontoRenegociacaoUnidadeEnsino.idEntidade = idEntidade;
	}
}