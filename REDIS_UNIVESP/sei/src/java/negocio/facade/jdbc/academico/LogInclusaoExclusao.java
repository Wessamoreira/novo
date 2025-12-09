package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.LogInclusaoExclusaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LogInclusaoExclusaoInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class LogInclusaoExclusao extends ControleAcesso implements LogInclusaoExclusaoInterfaceFacade {

	public static final long serialVersionUID = 1908603660683288474L;
	protected static String idEntidade;

	public LogInclusaoExclusao() throws Exception {
		super();
		setIdEntidade("LogInclusaoExclusao");
	}

	public LogInclusaoExclusaoVO novo() throws Exception {
		LogInclusaoExclusao.incluir(getIdEntidade());
		LogInclusaoExclusaoVO obj = new LogInclusaoExclusaoVO();
		return obj;
	}	

	public void realizarRegistroLogInclusaoExclusao(String dados, UsuarioVO usuario) throws Exception {
		LogInclusaoExclusaoVO LogInclusaoExclusaoVO = new LogInclusaoExclusaoVO();
		LogInclusaoExclusaoVO.setUsuario(usuario);
		LogInclusaoExclusaoVO.setData(new Date());
		LogInclusaoExclusaoVO.setDados(dados);
		incluir(LogInclusaoExclusaoVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LogInclusaoExclusaoVO obj) throws Exception {
		final String sql = "INSERT INTO LogInclusaoExclusao ( usuario, data, dados) VALUES ( ?, ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getUsuario().getCodigo());
				sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getData()));
				sqlInserir.setString(3, obj.getDados());
				return sqlInserir;
			}
		}, new ResultSetExtractor() {
			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
	}

	public static List<LogInclusaoExclusaoVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<LogInclusaoExclusaoVO> vetResultado = new ArrayList<LogInclusaoExclusaoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		return vetResultado;
	}

	public static LogInclusaoExclusaoVO montarDados(SqlRowSet dadosSQL) throws Exception {
		LogInclusaoExclusaoVO obj = new LogInclusaoExclusaoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDados(dadosSQL.getString("dados"));
		obj.setData(dadosSQL.getDate("data"));
		obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public LogInclusaoExclusaoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM LogInclusaoExclusao WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado));
	}

	public static String getIdEntidade() {
		return LogInclusaoExclusao.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		LogInclusaoExclusao.idEntidade = idEntidade;
	}
}