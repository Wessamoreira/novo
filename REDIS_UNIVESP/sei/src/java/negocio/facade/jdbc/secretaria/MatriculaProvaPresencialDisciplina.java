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
import negocio.comuns.secretaria.MatriculaProvaPresencialDisciplinaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMatriculaProvaPresencialDisciplinaEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.MatriculaProvaPresencialDisciplinaInterfaceFacade;

/**
 * @author Carlos Eugênio
 *
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MatriculaProvaPresencialDisciplina extends ControleAcesso implements MatriculaProvaPresencialDisciplinaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public MatriculaProvaPresencialDisciplina() {
		super();
		setIdEntidade("MatriculaProvaPresencialDisciplina");
	}

	public void validarDados(MatriculaProvaPresencialDisciplinaVO obj) throws Exception {
		if (obj.getDisciplinaVO().getCodigo().equals(0)) {
			throw new Exception("O campo DISCIPLINA deve ser informado.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MatriculaProvaPresencialDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO MatriculaProvaPresencialDisciplina( matriculaProvaPresencial, disciplina, areaConhecimento, nota, situacaoMatriculaProvaPresencialDisciplina ) VALUES ( ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
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
					sqlInserir.setBigDecimal(4, obj.getNota());
					sqlInserir.setString(5, obj.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name());
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
	public void alterar(final MatriculaProvaPresencialDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "UPDATE MatriculaProvaPresencialDisciplina set matriculaProvaPresencial=?, disciplina=?, areaConhecimento=?, nota=?, SituacaoMatriculaProvaPresencialDisciplina=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

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
					sqlAlterar.setBigDecimal(4, obj.getNota());
					sqlAlterar.setString(5, obj.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name());
					sqlAlterar.setInt(6, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MatriculaProvaPresencialDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM MatriculaProvaPresencialDisciplina WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static MatriculaProvaPresencialDisciplinaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		MatriculaProvaPresencialDisciplinaVO obj = new MatriculaProvaPresencialDisciplinaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getMatriculaProvaPresencialVO().setCodigo(dadosSQL.getInt("matriculaProvaPresencial.codigo"));
		obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getAreaConhecimentoVO().setCodigo(dadosSQL.getInt("areaConhecimento.codigo"));
		obj.setNota(dadosSQL.getBigDecimal("nota"));
		if (dadosSQL.getString("situacaoMatriculaProvaPresencialDisciplina") != null) {
			obj.setSituacaoMatriculaProvaPresencialDisciplinaEnum(SituacaoMatriculaProvaPresencialDisciplinaEnum.valueOf(dadosSQL.getString("situacaoMatriculaProvaPresencialDisciplina")));
		}
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
	public void alterarMatriculaProvaPresencial(Integer matriculaProvaPresencial, List<MatriculaProvaPresencialDisciplinaVO> objetos, UsuarioVO usuario) throws Exception {
		excluirMatriculaProvaPresencialDisciplinaPorMatriculaProvaPresencial(matriculaProvaPresencial, usuario);
		incluirMatriculaProvaPresencialDisciplina(matriculaProvaPresencial, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaProvaPresencialDisciplinaPorMatriculaProvaPresencial(Integer matriculaProvaPresencial, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM MatriculaProvaPresencialDisciplina WHERE (matriculaProvaPresencial = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { matriculaProvaPresencial });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorMatriculaProvaPresencial(List<MatriculaProvaPresencialVO> listaProvaPresencialVOs, UsuarioVO usuarioVO) throws Exception {
		for (MatriculaProvaPresencialVO matriculaProvaPresencialVO : listaProvaPresencialVOs) {
			excluirMatriculaProvaPresencialDisciplinaPorMatriculaProvaPresencial(matriculaProvaPresencialVO.getCodigo(), usuarioVO);
		}
	}



	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMatriculaProvaPresencialDisciplina(Integer matriculaProvaPresencial, List<MatriculaProvaPresencialDisciplinaVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			MatriculaProvaPresencialDisciplinaVO obj = (MatriculaProvaPresencialDisciplinaVO) e.next();
			obj.getMatriculaProvaPresencialVO().setCodigo(matriculaProvaPresencial);
			incluir(obj, usuario);
		}
	}
	
	public List<MatriculaProvaPresencialDisciplinaVO> consultarPorMatriculaProvaPresencial(Integer resultadoProcessamentoArquivo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select matriculaprovapresencialdisciplina.codigo, matriculaprovapresencialdisciplina.matriculaProvaPresencial, ");
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		sb.append(" areaConhecimento.codigo AS \"areaConhecimento.codigo\", areaConhecimento.nome AS \"areaConhecimento.nome\", ");
		sb.append(" matriculaprovapresencialdisciplina.nota, matriculaprovapresencialdisciplina.situacaoMatriculaProvaPresencialDisciplina ");
		sb.append(" from matriculaprovapresencialdisciplina  ");
		sb.append(" inner join disciplina on disciplina.codigo = matriculaprovapresencialdisciplina.disciplina ");
		sb.append(" left join areaConhecimento on areaConhecimento.codigo = matriculaprovapresencialdisciplina.areaConhecimento ");
		sb.append(" where matriculaprovapresencialdisciplina.matriculaProvaPresencial = ").append(resultadoProcessamentoArquivo);
		sb.append(" order by disciplina.nome");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<MatriculaProvaPresencialDisciplinaVO> listaMatriculaProvaPresencialDisciplinaVOs = new ArrayList<MatriculaProvaPresencialDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaProvaPresencialDisciplinaVO obj = new MatriculaProvaPresencialDisciplinaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getMatriculaProvaPresencialVO().setCodigo(tabelaResultado.getInt("matriculaProvaPresencial"));
			obj.setNota(tabelaResultado.getBigDecimal("nota"));
			
			obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));
			obj.getAreaConhecimentoVO().setCodigo(tabelaResultado.getInt("areaConhecimento.codigo"));
			obj.getAreaConhecimentoVO().setNome(tabelaResultado.getString("areaConhecimento.nome"));
			
			if (tabelaResultado.getString("situacaoMatriculaProvaPresencialDisciplina") != null) {
				obj.setSituacaoMatriculaProvaPresencialDisciplinaEnum(SituacaoMatriculaProvaPresencialDisciplinaEnum.valueOf(tabelaResultado.getString("situacaoMatriculaProvaPresencialDisciplina")));
			}

			listaMatriculaProvaPresencialDisciplinaVOs.add(obj);
			
		}
		return listaMatriculaProvaPresencialDisciplinaVOs;
	}

}
