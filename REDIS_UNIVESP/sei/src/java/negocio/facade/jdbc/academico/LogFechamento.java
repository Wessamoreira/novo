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

import negocio.comuns.academico.LogFechamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LogFechamentoInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class LogFechamento extends ControleAcesso implements LogFechamentoInterfaceFacade {

	protected static String idEntidade;

	public LogFechamento() throws Exception {
		super();
		setIdEntidade("LogFechamento");
	}

	public LogFechamentoVO novo() throws Exception {
		LogFechamento.incluir(getIdEntidade());
		LogFechamentoVO obj = new LogFechamentoVO();
		return obj;
	}

	public void realizarRegistroLogFechamento(String matricula) throws Exception {
		LogFechamentoVO logFechamentoVO = new LogFechamentoVO();
		logFechamentoVO.setMatricula(matricula);
		logFechamentoVO.setData(new Date());
		logFechamentoVO.setDescricao("Falha ao calcular as notas do aluno de Matrícula " + matricula + ". Razão: Faltam notas a ser lançadas.");
		incluir(logFechamentoVO);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LogFechamentoVO obj) throws Exception {
		final String sql = "INSERT INTO LogFechamento( matricula, data, descricao) VALUES ( ?, ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setString(1, obj.getMatricula());
				sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
				sqlInserir.setString(3, obj.getDescricao());
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

	public static List montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		return vetResultado;
	}

	public static LogFechamentoVO montarDados(SqlRowSet dadosSQL) throws Exception {
		LogFechamentoVO obj = new LogFechamentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public LogFechamentoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM LogFechamento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado));
	}

	public static String getIdEntidade() {
		return LogFechamento.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		LogFechamento.idEntidade = idEntidade;
	}
}