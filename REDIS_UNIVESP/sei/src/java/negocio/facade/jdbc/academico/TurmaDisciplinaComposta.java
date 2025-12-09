package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.TurmaDisciplinaCompostaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TurmaDisciplinaCompostaInterfaceFacade;

/**
 * @author Wellington - 19 de jan de 2016
 *
 */
@Repository
public class TurmaDisciplinaComposta extends ControleAcesso implements TurmaDisciplinaCompostaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	private static String idEntidade;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TurmaDisciplinaCompostaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		TurmaDisciplinaComposta.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
		final String sql = "INSERT INTO TurmaDisciplinaComposta (turmaDisciplina, gradeDisciplinaComposta, configuracaoAcademico) VALUES (?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
				final PreparedStatement sqlInserir = con.prepareStatement(sql);
				try {
					sqlInserir.setInt(1, obj.getTurmaDisciplinaVO().getCodigo());
					sqlInserir.setInt(2, obj.getGradeDisciplinaCompostaVO().getCodigo());
					if(Uteis.isAtributoPreenchido(obj.getConfiguracaoAcademicoVO())) {
						sqlInserir.setInt(3, obj.getConfiguracaoAcademicoVO().getCodigo());
					}else {
						sqlInserir.setNull(3, 0);
					}
				} catch (final Exception x) {
					return null;
				}
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {
			public Integer extractData(final ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return rs.getInt("codigo");
				}
				return null;
			}
		}));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TurmaDisciplinaCompostaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		TurmaDisciplinaComposta.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
		final String sql = "UPDATE TurmaDisciplinaComposta set turmaDisciplina = ?, gradeDisciplinaComposta = ?, configuracaoAcademico = ? where codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getTurmaDisciplinaVO().getCodigo());
				sqlAlterar.setInt(2, obj.getGradeDisciplinaCompostaVO().getCodigo());
				if(Uteis.isAtributoPreenchido(obj.getConfiguracaoAcademicoVO())) {
					sqlAlterar.setInt(3, obj.getConfiguracaoAcademicoVO().getCodigo());
				}else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setInt(4, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TurmaDisciplinaCompostaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		TurmaDisciplinaComposta.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
		String sql = "DELETE FROM TurmaDisciplinaComposta WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorTurmaDisciplina(final TurmaDisciplinaVO turmaDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		TurmaDisciplinaComposta.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM TurmaDisciplinaComposta WHERE turmaDisciplina = ? ");
		sql.append("and codigo not in (0");
		for (TurmaDisciplinaCompostaVO obj : turmaDisciplinaVO.getTurmaDisciplinaCompostaVOs()) {
			if (obj.isSelecionado() && !obj.getNovoObj()) {
				sql.append(", ").append(obj.getCodigo());
			}
		}
		sql.append(") ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), turmaDisciplinaVO.getCodigo());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirTurmaDisciplinaVOs(final TurmaDisciplinaVO turmaDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluirPorTurmaDisciplina(turmaDisciplinaVO, verificarAcesso, usuarioVO);
		for (TurmaDisciplinaCompostaVO obj : turmaDisciplinaVO.getTurmaDisciplinaCompostaVOs()) {
			if (obj.isSelecionado()) {
				if (obj.getNovoObj()) {
					incluir(obj, verificarAcesso, usuarioVO);
				} else {
					alterar(obj, verificarAcesso, usuarioVO);
				}
			}
		}
	}

	@Override
	public TurmaDisciplinaCompostaVO consultarPorChavePrimaria(Integer turmaDisciplinaComposta, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		TurmaDisciplinaComposta.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		String sql = "SELECT TurmaDisciplinaComposta.* from TurmaDisciplinaComposta WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, turmaDisciplinaComposta);
		return montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	@Override
	public List<TurmaDisciplinaCompostaVO> consultarPorTurmaDisciplina(Integer turmaDisciplina, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		TurmaDisciplinaComposta.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		String sql = "SELECT TurmaDisciplinaComposta.* from TurmaDisciplinaComposta WHERE turmaDisciplina = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, turmaDisciplina);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}	
	
	@Override
	public List<TurmaDisciplinaCompostaVO> consultarPorTurma(Integer turma, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		TurmaDisciplinaComposta.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		String sql = "SELECT TurmaDisciplinaComposta.* from TurmaDisciplinaComposta INNER JOIN TurmaDisciplina on TurmaDisciplina.codigo = TurmaDisciplinaComposta.turmaDisciplina WHERE turma = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, turma);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	private List<TurmaDisciplinaCompostaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<TurmaDisciplinaCompostaVO> turmaDisciplinaCompostaVOs = new ArrayList<TurmaDisciplinaCompostaVO>(0);
		while (tabelaResultado.next()) {
			turmaDisciplinaCompostaVOs.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		return turmaDisciplinaCompostaVOs;
	}

	private TurmaDisciplinaCompostaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		TurmaDisciplinaCompostaVO obj = new TurmaDisciplinaCompostaVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setSelecionado(true);
		obj.getGradeDisciplinaCompostaVO().setCodigo(tabelaResultado.getInt("gradeDisciplinaComposta"));
		obj.getTurmaDisciplinaVO().setCodigo(tabelaResultado.getInt("turmaDisciplina"));
		obj.getConfiguracaoAcademicoVO().setCodigo(tabelaResultado.getInt("configuracaoAcademico"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setGradeDisciplinaCompostaVO(getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorChavePrimaria(obj.getGradeDisciplinaCompostaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
//		obj.setTurmaDisciplinaVO(getFacadeFactory().getTurmaDisciplinaFacade().consultarPorChavePrimaria(obj.getTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		return obj;
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "TurmaDisciplinaComposta";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		TurmaDisciplinaComposta.idEntidade = idEntidade;
	}
	

	@Override
	public TurmaDisciplinaCompostaVO consultarPorTurmaGradeDisciplinaComposta(Integer turma, Integer gradeDisciplinaComposta, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		TurmaDisciplinaComposta.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		String sql = "SELECT TurmaDisciplinaComposta.* from TurmaDisciplinaComposta inner join turmadisciplina on turmadisciplina.codigo = TurmaDisciplinaComposta.turmadisciplina  WHERE turmaDisciplina.turma = ? and TurmaDisciplinaComposta.gradeDisciplinaComposta = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, turma, gradeDisciplinaComposta);
		if(tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
		}
		return null;
	}

}
