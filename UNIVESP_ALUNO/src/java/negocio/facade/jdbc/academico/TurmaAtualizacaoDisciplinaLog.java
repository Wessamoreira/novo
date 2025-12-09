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
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.TurmaAtualizacaoDisciplinaLogVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TurmaAtualizacaoDisciplinaLogInterfaceFacade;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class TurmaAtualizacaoDisciplinaLog extends ControleAcesso implements TurmaAtualizacaoDisciplinaLogInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6228148268228259596L;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TurmaAtualizacaoDisciplinaLogVO obj) throws Exception {
		final String sql = "INSERT INTO TurmaAtualizacaoDisciplinaLog( codigoTurma, identificadorTurma, responsavel, data, disciplinasAdicionadas ) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getCodigoTurma());
				sqlInserir.setString(2, obj.getIdentificadorTurma());
				if (obj.getResponsavel().getCodigo() != 0) {
					sqlInserir.setInt(3, obj.getResponsavel().getCodigo());
				} else {
					sqlInserir.setNull(3, 0);
				}
				sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getData()));
				sqlInserir.setString(5, obj.getDisciplinasAdicionadas());

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
	}
	
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT turmaatualizacaodisciplinalog.codigo, codigoturma, identificadorturma, data, disciplinasadicionadas,");
		sql.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\"");
		sql.append(" FROM turmaatualizacaodisciplinalog ");
		sql.append(" inner join usuario on turmaatualizacaodisciplinalog.responsavel = usuario.codigo ");
		return sql;
	}	

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<TurmaAtualizacaoDisciplinaLogVO> consultaRapidaPorTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE codigoTurma = ").append(turma).append(" ");
			sqlStr.append(" ORDER BY data desc ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}	

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<TurmaAtualizacaoDisciplinaLogVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<TurmaAtualizacaoDisciplinaLogVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			TurmaAtualizacaoDisciplinaLogVO obj = new TurmaAtualizacaoDisciplinaLogVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(TurmaAtualizacaoDisciplinaLogVO obj, SqlRowSet dadosSQL, int nivelMontarDados) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setCodigoTurma(dadosSQL.getInt("codigoTurma"));
		obj.setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
		obj.setDisciplinasAdicionadas(dadosSQL.getString("disciplinasAdicionadas"));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getResponsavel().setNome(dadosSQL.getString("usuario.nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarLogAtualizacaoDisciplinaTurma(TurmaVO obj, List<TurmaDisciplinaVO> listaTurmaDisciplinaVOs, UsuarioVO usuarioLogado) throws Exception {
		if (!listaTurmaDisciplinaVOs.isEmpty()) {
			TurmaAtualizacaoDisciplinaLogVO log = new TurmaAtualizacaoDisciplinaLogVO();
			log.setCodigoTurma(obj.getCodigo());
			log.setIdentificadorTurma(obj.getIdentificadorTurma());
			log.setData(new Date());
			log.setResponsavel(usuarioLogado);
			log.setDisciplinasAdicionadas(registrarLogDisciplinasAdicionadas(listaTurmaDisciplinaVOs));

			incluir(log);
		}
	}

	public String registrarLogDisciplinasAdicionadas(List<TurmaDisciplinaVO> listaTurmaDisciplinaVOs) {
		StringBuilder dados = new StringBuilder();
		dados.append("---------------------------------------------------------------------");
		dados.append("Log Alteração Turma Disciplina");
		dados.append("---------------------------------------------------------------------");
		int index = 1;
		for (TurmaDisciplinaVO turmaDisciplinaVO : listaTurmaDisciplinaVOs) {
			dados.append(index).append("- Código(Disciplina): ").append(turmaDisciplinaVO.getDisciplina().getCodigo()).append(". --- Nome(Disciplina): ").append(turmaDisciplinaVO.getDisciplina().getNome()).append(". ");
			dados.append(System.lineSeparator());
			index++;
		}
		return dados.toString();

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarInclusaoLog(TurmaAtualizacaoDisciplinaLogVO log, TurmaVO turma, UsuarioVO usuarioLogado) throws Exception {
		log.setCodigoTurma(turma.getCodigo());
		log.setIdentificadorTurma(turma.getIdentificadorTurma());
		log.setData(new Date());
		log.setResponsavel(usuarioLogado);
		incluir(log);	
	}	

	@Override
	public void registrarLinhaTracejada(TurmaAtualizacaoDisciplinaLogVO tadLog, boolean quebraLinha) {
		registrarInstrucaoLog(tadLog, "---------------------------------------------------------------------", quebraLinha);
	}

	@Override
	public void registrarInstrucaoLog(TurmaAtualizacaoDisciplinaLogVO tadLog, String instrucao, boolean quebraLinha) {
		tadLog.setDisciplinasAdicionadas(tadLog.getDisciplinasAdicionadas() + instrucao);
		if (quebraLinha) {
			tadLog.setDisciplinasAdicionadas(tadLog.getDisciplinasAdicionadas() + System.lineSeparator());
		}
	}
}
