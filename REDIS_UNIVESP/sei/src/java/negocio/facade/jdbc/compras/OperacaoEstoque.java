package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.compras.OperacaoEstoqueVO;
import negocio.comuns.compras.enumeradores.OperacaoEstoqueEnum;
import negocio.comuns.compras.enumeradores.TipoOperacaoEstoqueOrigemEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.OperacaoEstoqueInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class OperacaoEstoque extends ControleAcesso implements OperacaoEstoqueInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8619452690673897183L;
	private static String idEntidade = "OperacaoEstoque";

	public OperacaoEstoque() {
		super();
	}

	private void validarDados(OperacaoEstoqueVO obj) {
		/*Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getIdentificadorOperacaoEstoque()), "O campo Identificador Centro Resultado (Centro Resultado) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDescricao()), "O campo Descrição (Centro Resultado) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getSituacaoEnum()), "O campo Situação (Centro Resultado) não foi informado.");
		Uteis.checkState(validarUnicidade(obj), "Já existe um Centro de Resultado com esse Identificador: " + obj.getIdentificadorOperacaoEstoque() + ".");*/
	}

	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<OperacaoEstoqueVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			for (OperacaoEstoqueVO operacaoEstoqueVO : lista) {
				persistir(operacaoEstoqueVO, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(OperacaoEstoqueVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			validarDados(obj);			
			if (obj.getCodigo() == 0) {
				obj.setData(new Date());
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final OperacaoEstoqueVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			OperacaoEstoque.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO OperacaoEstoque (estoque, data, codOrigem,tipoOperacaoEstoqueOrigemEnum, operacaoEstoqueEnum, quantidade, usuario) ");
			sql.append("  VALUES ( ?, ?, ?, ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getEstoqueVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getData(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCodOrigem(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoOperacaoEstoqueOrigemEnum(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getOperacaoEstoqueEnum(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQuantidade(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getUsuario(), ++i, sqlInserir);
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
	public void alterar(final OperacaoEstoqueVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			OperacaoEstoque.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE OperacaoEstoque ");
			sql.append("   SET estoqueVO=?, data=?, codOrigem=?,  ");
			sql.append("   tipoOperacaoEstoqueOrigemEnum=?, operacaoEstoqueEnum=?, ");
			sql.append("   quantidade=?, usuario=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getEstoqueVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getData(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodOrigem(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoOperacaoEstoqueOrigemEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getOperacaoEstoqueEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getQuantidade(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getUsuario(), ++i, sqlAlterar);
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
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(OperacaoEstoqueVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			OperacaoEstoque.excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM OperacaoEstoque WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT operacaoestoque.codigo as \"operacaoestoque.codigo\",  ");
		sql.append(" operacaoestoque.data as \"operacaoestoque.data\", operacaoestoque.codOrigem as \"operacaoestoque.codOrigem\", ");
		sql.append(" operacaoestoque.tipooperacaoestoqueOrigemEnum as \"operacaoestoque.tipooperacaoestoqueOrigemEnum\", ");
		sql.append(" operacaoestoque.operacaoestoqueEnum as \"operacaoestoque.operacaoestoqueEnum\",  ");
		sql.append(" operacaoestoque.quantidade as \"operacaoestoque.quantidade\",  ");
		sql.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\",  ");
		sql.append(" estoque.codigo as \"estoque.codigo\", estoque.quantidade as \"estoque.quantidade\", ");
		sql.append(" estoque.precoUnitario as \"estoque.precoUnitario\" ");
		sql.append(" FROM operacaoestoque ");
		sql.append(" INNER JOIN estoque ON estoque.codigo = operacaoestoque.estoque ");
		sql.append(" INNER JOIN usuario ON usuario.codigo = operacaoestoque.usuario ");
		return sql;
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<OperacaoEstoqueVO> consultaRapidaPorCodOrigemPorTipoOperacaoEstoqueOrigemEnum(String codOrigem, TipoOperacaoEstoqueOrigemEnum tipoOperacaoEstoqueOrigemEnum, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE operacaoestoque.codOrigem = '").append(codOrigem).append("' ");
			sqlStr.append(" and operacaoestoque.tipooperacaoestoqueOrigemEnum = '").append(tipoOperacaoEstoqueOrigemEnum.name()).append("' ");
			sqlStr.append(" ORDER BY operacaoestoque.codigo ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	

	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public OperacaoEstoqueVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE operacaoestoque.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( OperacaoEstoqueVO ).");
			}
			OperacaoEstoqueVO obj = new OperacaoEstoqueVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}	

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<OperacaoEstoqueVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<OperacaoEstoqueVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			OperacaoEstoqueVO obj = new OperacaoEstoqueVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(OperacaoEstoqueVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		try {
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(dadosSQL.getInt("operacaoestoque.codigo"));
			obj.setData(dadosSQL.getDate("operacaoestoque.data"));
			obj.setQuantidade((dadosSQL.getDouble("operacaoestoque.quantidade")));
			obj.setCodOrigem(dadosSQL.getString("operacaoestoque.codorigem"));
			obj.setTipoOperacaoEstoqueOrigemEnum(TipoOperacaoEstoqueOrigemEnum.valueOf(dadosSQL.getString("operacaoestoque.tipooperacaoestoqueorigemenum")));
			obj.setOperacaoEstoqueEnum(OperacaoEstoqueEnum.valueOf(dadosSQL.getString("operacaoestoque.operacaoestoqueenum")));
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
				return;
			}
			obj.getEstoqueVO().setCodigo((dadosSQL.getInt("estoque.codigo")));
			obj.getEstoqueVO().setQuantidade((dadosSQL.getDouble("estoque.quantidade")));			
			obj.getEstoqueVO().setPrecoUnitario((dadosSQL.getDouble("estoque.precoUnitario")));			
			obj.getUsuario().setCodigo((dadosSQL.getInt("usuario.codigo")));
			obj.getUsuario().setNome(dadosSQL.getString("usuario.nome"));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return OperacaoEstoque.idEntidade;
	}

}
