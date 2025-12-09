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

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.LogLancamentoNotaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LogLancamentoNotaInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class LogLancamentoNota extends ControleAcesso implements LogLancamentoNotaInterfaceFacade {

	public static final long serialVersionUID = 1908603660683288474L;
	protected static String idEntidade;

	public LogLancamentoNota() throws Exception {
		super();
		setIdEntidade("LogLancamentoNota");
	}

	public LogLancamentoNotaVO novo() throws Exception {
		LogLancamentoNota.incluir(getIdEntidade());
		LogLancamentoNotaVO obj = new LogLancamentoNotaVO();
		return obj;
	}

	public void registrarLogLancamentoNota(List<HistoricoVO> historicoVOs, String operacao, UsuarioVO usuario) throws Exception {
		StringBuilder dados = new StringBuilder();
		dados.append("Operação -> ").append(operacao).append(" - ");
		dados.append("Disciplina -> ").append(historicoVOs.get(0).getDisciplina().getNome()).append(" - ");
		dados.append("Com os seguintes dados : ");
		for (HistoricoVO historicoVO : historicoVOs) {
			dados.append("Matrícula: ").append(historicoVO.getMatricula().getMatricula()).append(" - Frequência: ").append(historicoVO.getFrequencia_Apresentar()).append(" - Média: ");
			dados.append(historicoVO.getMediaFinal_Apresentar()).append(" - Nota1: ");
			dados.append(historicoVO.getNota1()).append(" - Nota2: ");
			dados.append(historicoVO.getNota2()).append(" - Nota3: ");
			dados.append(historicoVO.getNota3()).append(" - Nota4: ");
			dados.append(historicoVO.getNota4()).append(" - Nota5: ");
			dados.append(historicoVO.getNota5()).append(" - Nota6: ");
			dados.append(historicoVO.getNota6()).append(" - Nota7: ");
			dados.append(historicoVO.getNota7()).append(" - Nota8: ");
			dados.append(historicoVO.getNota8()).append(" - Nota9: ");
			dados.append(historicoVO.getNota9()).append(" - Nota10: ");
			dados.append(historicoVO.getNota10()).append(" - Nota11: ");
			dados.append(historicoVO.getNota11()).append(" - Nota12: ");
			dados.append(historicoVO.getNota12()).append(" - Nota13: ");
			dados.append(historicoVO.getNota13()).append(" - Nota14: ");
			dados.append(historicoVO.getNota14()).append(" - Nota15: ");
			dados.append(historicoVO.getNota15()).append(" - Nota16: ");
			dados.append(historicoVO.getNota16()).append(" - Nota17: ");
			dados.append(historicoVO.getNota17()).append(" - Nota18: ");
			dados.append(historicoVO.getNota18()).append(" - Nota19: ");
			dados.append(historicoVO.getNota19()).append(" - Nota20: ");
			dados.append(historicoVO.getNota20()).append(" - Nota21: ");
			dados.append(historicoVO.getNota21()).append(" - Nota22: ");
			dados.append(historicoVO.getNota22()).append(" - Nota23: ");
			dados.append(historicoVO.getNota23()).append(" - Nota24: ");
			dados.append(historicoVO.getNota24()).append(" - Nota25: ");
			dados.append(historicoVO.getNota25()).append(" - Nota26: ");
			dados.append(historicoVO.getNota26()).append(" - Nota27: ");
			dados.append(historicoVO.getNota27()).append(" - Nota28: ");
			dados.append(historicoVO.getNota28()).append(" - Nota29: ");
			dados.append(historicoVO.getNota29()).append(" - Nota30: ");
			dados.append(historicoVO.getNota30()).append(" -  ");
			

		}
		realizarRegistroLogLancamentoNota(dados.toString(), usuario);
	}
	
	public void realizarRegistroLogLancamentoNota(String dados, UsuarioVO usuario) throws Exception {
		LogLancamentoNotaVO logLancamentoNotaVO = new LogLancamentoNotaVO();
		logLancamentoNotaVO.setUsuario(usuario);
		logLancamentoNotaVO.setData(new Date());
		logLancamentoNotaVO.setDados(dados);
		incluir(logLancamentoNotaVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LogLancamentoNotaVO obj) throws Exception {
		final String sql = "INSERT INTO LogLancamentoNota ( usuario, data, dados) VALUES ( ?, ?, ? ) returning codigo";
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

	public static List<LogLancamentoNotaVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<LogLancamentoNotaVO> vetResultado = new ArrayList<LogLancamentoNotaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		return vetResultado;
	}

	public static LogLancamentoNotaVO montarDados(SqlRowSet dadosSQL) throws Exception {
		LogLancamentoNotaVO obj = new LogLancamentoNotaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDados(dadosSQL.getString("dados"));
		obj.setData(dadosSQL.getDate("data"));
		obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public LogLancamentoNotaVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM LogLancamentoNota WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado));
	}

	public static String getIdEntidade() {
		return LogLancamentoNota.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		LogLancamentoNota.idEntidade = idEntidade;
	}
}