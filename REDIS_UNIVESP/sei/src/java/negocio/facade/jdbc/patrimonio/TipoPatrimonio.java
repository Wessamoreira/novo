package negocio.facade.jdbc.patrimonio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.patrimonio.TipoPatrimonioInterface;

@Repository
@Scope("singleton")
@Lazy
public class TipoPatrimonio extends ControleAcesso implements TipoPatrimonioInterface {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public TipoPatrimonio() throws Exception {
		super();
		setIdEntidade("TipoRecursoControleReserva");
	}

	public TipoPatrimonio novo() throws Exception {
		TipoPatrimonio.incluir(getIdEntidade());
		TipoPatrimonio obj = new TipoPatrimonio();
		return obj;
	}

	public static void validarDados(TipoPatrimonioVO obj) throws Exception {
		if (obj.getDescricao().equals("")) {
			throw new Exception("O campo DESCRIÇÃO (Tipo Patrimônio) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getQuantidadeReservasSimultaneas()) || obj.getQuantidadeReservasSimultaneas() < 1) {
			throw new Exception("O campo QUANTIDADE RESERVAS SIMULTÂNEAS deve ser informado e ser MAIOR QUE ZERO.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(TipoPatrimonioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (obj.getNovoObj()) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoPatrimonioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			TipoPatrimonio.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO tipopatrimonio (descricao, quantidadereservassimultaneas, quantidadediaslimitereserva) VALUES (?,?,?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					try {
						sqlInserir.setString(1, obj.getDescricao());
						sqlInserir.setInt(2, obj.getQuantidadeReservasSimultaneas().intValue());
						sqlInserir.setInt(3, obj.getQuantidadeDiasLimiteReserva().intValue());
					} catch (final Exception x) {
						return null;
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TipoPatrimonioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			TipoPatrimonio.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE tipopatrimonio set descricao = ?, quantidadereservassimultaneas = ?, quantidadediaslimitereserva= ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setInt(2, obj.getQuantidadeReservasSimultaneas().intValue());
					sqlAlterar.setInt(3, obj.getQuantidadeDiasLimiteReserva());
					sqlAlterar.setInt(4, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TipoPatrimonioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			TipoPatrimonio.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM tipopatrimonio WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	public List<TipoPatrimonioVO> montarDadosConsulta(SqlRowSet tabelaResultado) {
		List<TipoPatrimonioVO> TipoPatrimonioVOs = new ArrayList<TipoPatrimonioVO>(0);
		while (tabelaResultado.next()) {
			TipoPatrimonioVOs.add(montarDados(tabelaResultado));
		}
		return TipoPatrimonioVOs;
	}

	public TipoPatrimonioVO montarDados(SqlRowSet tabelaResultado) {
		TipoPatrimonioVO obj = new TipoPatrimonioVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setQuantidadeReservasSimultaneas(tabelaResultado.getInt("quantidadereservassimultaneas"));
		obj.setQuantidadeDiasLimiteReserva(tabelaResultado.getInt("quantidadediaslimitereserva")); 
		return obj;
	}

	@Override
	public List<TipoPatrimonioVO> consultar(String valorConsulta, String campoConsulta, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		List<TipoPatrimonioVO>  objs = new ArrayList<TipoPatrimonioVO>(0);
		if (valorConsulta.equals("")) {
			throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
		}
		if (campoConsulta.equals("codigo")) {
			TipoPatrimonioVO obj = consultarPorChavePrimaria(new Integer(valorConsulta), verificarAcesso, usuarioVO);
			if (!obj.getCodigo().equals(0)) {
				objs.add(obj);
			}
			return objs;
		} else if (campoConsulta.equals("descricao")) {
			return objs = consultarPorDescricao(valorConsulta, verificarAcesso, usuarioVO);
		}
		return objs;
	}

	@Override
	public TipoPatrimonioVO consultarPorChavePrimaria(Integer codigo, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		TipoPatrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM tipopatrimonio where codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado);
		}
		return new TipoPatrimonioVO();
	}

	@Override
	public List<TipoPatrimonioVO> consultarPorDescricao(String descricao, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		TipoPatrimonio.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM tipopatrimonio where descricao ilike('").append(descricao).append("%') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado);
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		TipoPatrimonio.idEntidade = idEntidade;
	}

}
