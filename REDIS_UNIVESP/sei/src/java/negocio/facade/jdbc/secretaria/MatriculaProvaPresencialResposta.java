/**
 * 
 */
package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import negocio.comuns.secretaria.MatriculaProvaPresencialRespostaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.MatriculaProvaPresencialRespostaInterfaceFacade;

/**
 * @author Carlos Eugênio
 *
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MatriculaProvaPresencialResposta extends ControleAcesso implements MatriculaProvaPresencialRespostaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public MatriculaProvaPresencialResposta() {
		super();
		setIdEntidade("MatriculaProvaPresencialResposta");
	}

	public void validarDados(MatriculaProvaPresencialRespostaVO obj) throws Exception {

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MatriculaProvaPresencialRespostaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO MatriculaProvaPresencialResposta( matriculaProvaPresencial, disciplina, areaConhecimento, nrQuestao, respostaAluno, respostaGabarito, totalAcerto ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getMatriculaProvaPresencialVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getMatriculaProvaPresencialVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getDisciplinaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getDisciplinaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getAreaConhecimentoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getAreaConhecimentoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setInt(4, obj.getNrQuestao());
					sqlInserir.setString(5, obj.getRespostaAluno());
					sqlInserir.setString(6, obj.getRespostaGabarito());
					sqlInserir.setBigDecimal(7, obj.getTotalAcerto());
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
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MatriculaProvaPresencialRespostaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "UPDATE MatriculaProvaPresencialResposta set matriculaProvaPresencial=? disciplina=?, areaConhecimento=?, nrQuestao=?, respostaAluno=?, respostaGabarito=?, totalAcerto=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getMatriculaProvaPresencialVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getMatriculaProvaPresencialVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getDisciplinaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getDisciplinaVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getAreaConhecimentoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getAreaConhecimentoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setInt(4, obj.getNrQuestao());
					sqlAlterar.setString(5, obj.getRespostaAluno());
					sqlAlterar.setString(6, obj.getRespostaGabarito());
					sqlAlterar.setBigDecimal(7, obj.getTotalAcerto());
					sqlAlterar.setInt(8, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MatriculaProvaPresencialRespostaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM MatriculaProvaPresencialResposta WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}
	
		public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static MatriculaProvaPresencialRespostaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		MatriculaProvaPresencialRespostaVO obj = new MatriculaProvaPresencialRespostaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getMatriculaProvaPresencialVO().setCodigo(dadosSQL.getInt("matriculaProvaPresencial.codigo"));
		obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getAreaConhecimentoVO().setCodigo(dadosSQL.getInt("areaConhecimento.codigo"));
		obj.setNrQuestao(dadosSQL.getInt("nrQuestao"));
		obj.setRespostaAluno(dadosSQL.getString("respostaAluno"));
		obj.setRespostaGabarito(dadosSQL.getString("respostaGabarito"));
		obj.setTotalAcerto(dadosSQL.getBigDecimal("totalAcerto"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public static String getIdEntidade() {
		return MatriculaProvaPresencial.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		MatriculaProvaPresencial.idEntidade = idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatriculaProvaPresencialResposta(Integer matriculaProvaPresencial, List<MatriculaProvaPresencialRespostaVO> objetos, UsuarioVO usuario) throws Exception {
		excluirMatriculaProvaPresencialRespostaPorMatriculaProvaPresencial(matriculaProvaPresencial, usuario);
		incluirMatriculaProvaPresencialResposta(matriculaProvaPresencial, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaProvaPresencialRespostaPorMatriculaProvaPresencial(Integer matriculaProvaPresencial, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM MatriculaProvaPresencialResposta WHERE (matriculaProvaPresencial = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { matriculaProvaPresencial });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorMatriculaProvaPresencial(List<MatriculaProvaPresencialVO> listaProvaPresencialVOs, UsuarioVO usuarioVO) throws Exception {
		for (MatriculaProvaPresencialVO matriculaProvaPresencialVO : listaProvaPresencialVOs) {
			excluirMatriculaProvaPresencialRespostaPorMatriculaProvaPresencial(matriculaProvaPresencialVO.getCodigo(), usuarioVO);
		}
	}



	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMatriculaProvaPresencialResposta(Integer matriculaProvaPresencial, List<MatriculaProvaPresencialRespostaVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			MatriculaProvaPresencialRespostaVO obj = (MatriculaProvaPresencialRespostaVO) e.next();
			obj.getMatriculaProvaPresencialVO().setCodigo(matriculaProvaPresencial);
			incluir(obj, usuario);
		}
	}
	
	public List<MatriculaProvaPresencialRespostaVO> consultarPorMatriculaProvaPresencial(Integer matriculaProvaPresencial, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select matriculaprovapresencialresposta.codigo, matriculaprovapresencialresposta.matriculaProvaPresencial, ");
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\",  ");
		sb.append(" areaConhecimento.codigo AS \"areaConhecimento.codigo\", areaConhecimento.nome AS \"areaConhecimento.nome\", ");
		sb.append(" matriculaprovapresencialresposta.nrquestao, matriculaprovapresencialresposta.respostaAluno, matriculaprovapresencialresposta.respostaGabarito, matriculaprovapresencialresposta.totalacerto ");
		sb.append(" from matriculaprovapresencialresposta    ");
		sb.append(" left join disciplina on disciplina.codigo = matriculaprovapresencialresposta.disciplina ");
		sb.append(" left join areaConhecimento on areaConhecimento.codigo = matriculaprovapresencialresposta.areaConhecimento ");
		sb.append(" where matriculaprovapresencialresposta.matriculaProvaPresencial = ").append(matriculaProvaPresencial);
		sb.append(" order by matriculaprovapresencialresposta.nrQuestao");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<MatriculaProvaPresencialRespostaVO> listaMatriculaProvaPresencialRespostaVOs = new ArrayList<MatriculaProvaPresencialRespostaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaProvaPresencialRespostaVO obj = new MatriculaProvaPresencialRespostaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getMatriculaProvaPresencialVO().setCodigo(tabelaResultado.getInt("matriculaProvaPresencial"));
			obj.setNrQuestao(tabelaResultado.getInt("nrQuestao"));
			obj.setRespostaAluno(tabelaResultado.getString("respostaAluno"));
			obj.setRespostaGabarito(tabelaResultado.getString("respostaGabarito"));
			obj.setTotalAcerto(tabelaResultado.getBigDecimal("totalAcerto"));
			
			obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));
			obj.getAreaConhecimentoVO().setCodigo(tabelaResultado.getInt("areaConhecimento.codigo"));
			obj.getAreaConhecimentoVO().setNome(tabelaResultado.getString("areaConhecimento.nome"));
			listaMatriculaProvaPresencialRespostaVOs.add(obj);
			
		}
		return listaMatriculaProvaPresencialRespostaVOs;
	}
	
	public List<MatriculaProvaPresencialRespostaVO> consultarPorMatriculaRespostaProvaPresencial(Integer matriculaProvaPresencial, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select matriculaprovapresencialresposta.codigo, matriculaprovapresencialresposta.matriculaProvaPresencial, ");
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\",  ");
		sb.append(" areaConhecimento.codigo AS \"areaConhecimento.codigo\", areaConhecimento.nome AS \"areaConhecimento.nome\", ");
		sb.append(" matriculaprovapresencialresposta.nrquestao, matriculaprovapresencialresposta.respostaAluno, matriculaprovapresencialresposta.respostaGabarito, matriculaprovapresencialresposta.totalacerto ");
		sb.append(" from matriculaprovapresencialresposta    ");
		sb.append(" left join disciplina on disciplina.codigo = matriculaprovapresencialresposta.disciplina ");
		sb.append(" left join areaConhecimento on areaConhecimento.codigo = matriculaprovapresencialresposta.areaConhecimento ");
		sb.append(" where matriculaprovapresencialresposta.matriculaProvaPresencial = ").append(matriculaProvaPresencial);
		sb.append(" order by matriculaprovapresencialresposta.nrQuestao");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<MatriculaProvaPresencialRespostaVO> listaMatriculaProvaPresencialRespostaVOs = new ArrayList<MatriculaProvaPresencialRespostaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaProvaPresencialRespostaVO obj = new MatriculaProvaPresencialRespostaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getMatriculaProvaPresencialVO().setCodigo(tabelaResultado.getInt("matriculaProvaPresencial"));
			obj.setNrQuestao(tabelaResultado.getInt("nrQuestao"));
			obj.setRespostaAluno(tabelaResultado.getString("respostaAluno"));
			obj.setRespostaGabarito(tabelaResultado.getString("respostaGabarito"));
			obj.setTotalAcerto(tabelaResultado.getBigDecimal("totalAcerto"));
			
			obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));
			obj.getAreaConhecimentoVO().setCodigo(tabelaResultado.getInt("areaConhecimento.codigo"));
			obj.getAreaConhecimentoVO().setNome(tabelaResultado.getString("areaConhecimento.nome"));
			listaMatriculaProvaPresencialRespostaVOs.add(obj);
			
		}
		return listaMatriculaProvaPresencialRespostaVOs;
	}

}
