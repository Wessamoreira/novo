package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.TrabalhoConclusaoCursoMembroBancaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TrabalhoConclusaoCursoMembroBancaInterfaceFacade;

@Repository
@Lazy
@Scope
public class TrabalhoConclusaoCursoMembroBanca extends ControleAcesso implements TrabalhoConclusaoCursoMembroBancaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5040921415073755284L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(TrabalhoConclusaoCursoMembroBancaVO trabalhoConclusaoCursoMembroBancaVO) throws Exception {
		if (trabalhoConclusaoCursoMembroBancaVO.isNovoObj()) {
			incluir(trabalhoConclusaoCursoMembroBancaVO);
		} else {
			alterar(trabalhoConclusaoCursoMembroBancaVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final TrabalhoConclusaoCursoMembroBancaVO trabalhoConclusaoCursoMembroBancaVO) throws Exception {

		trabalhoConclusaoCursoMembroBancaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO TrabalhoConclusaoCursoMembroBanca ( ");
				sql.append(" membro, nome, convidado, trabalhoConclusaoCurso ");
				sql.append(" ) values (?,?,?,?) RETURNING codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				if (trabalhoConclusaoCursoMembroBancaVO.getMembro().getCodigo().intValue() != 0) {
					ps.setInt(x++, trabalhoConclusaoCursoMembroBancaVO.getMembro().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setString(x++, trabalhoConclusaoCursoMembroBancaVO.getNome());
				ps.setBoolean(x++, trabalhoConclusaoCursoMembroBancaVO.getConvidado());
				ps.setInt(x++, trabalhoConclusaoCursoMembroBancaVO.getTrabalhoConclusaoCurso().getCodigo());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		trabalhoConclusaoCursoMembroBancaVO.setNovoObj(false);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final TrabalhoConclusaoCursoMembroBancaVO trabalhoConclusaoCursoMembroBancaVO) throws Exception {

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE TrabalhoConclusaoCursoMembroBanca SET ");
				sql.append(" membro = ?, nome=?, convidado=?, trabalhoConclusaoCurso=? ");
				sql.append(" WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				if (trabalhoConclusaoCursoMembroBancaVO.getMembro().getCodigo().intValue() != 0) {
					ps.setInt(x++, trabalhoConclusaoCursoMembroBancaVO.getMembro().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setString(x++, trabalhoConclusaoCursoMembroBancaVO.getNome());
				ps.setBoolean(x++, trabalhoConclusaoCursoMembroBancaVO.getConvidado());
				ps.setInt(x++, trabalhoConclusaoCursoMembroBancaVO.getTrabalhoConclusaoCurso().getCodigo());
				ps.setInt(x++, trabalhoConclusaoCursoMembroBancaVO.getCodigo());
				return ps;
			}
		});
		trabalhoConclusaoCursoMembroBancaVO.setNovoObj(false);

	}

	private String getSelectCompleto(){
		StringBuilder sql = new StringBuilder("SELECT * from TrabalhoConclusaoCursoMembroBanca ");
		return sql.toString();
	}

	@Override
	public List<TrabalhoConclusaoCursoMembroBancaVO> consultarPorTCC(int tcc) throws Exception {
		StringBuilder sql  = new StringBuilder(getSelectCompleto());
		sql.append(" WHERE trabalhoConclusaoCurso = ").append(tcc).append(" ORDER BY nome ");		
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	private List<TrabalhoConclusaoCursoMembroBancaVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<TrabalhoConclusaoCursoMembroBancaVO> trabalhoConclusaoCursoMembroBancaVOs = new ArrayList<TrabalhoConclusaoCursoMembroBancaVO>(0);
		while(rs.next()){
			trabalhoConclusaoCursoMembroBancaVOs.add(montarDados(rs));
		}		
		return trabalhoConclusaoCursoMembroBancaVOs;
	}
	private TrabalhoConclusaoCursoMembroBancaVO montarDados(SqlRowSet rs) throws Exception{
		TrabalhoConclusaoCursoMembroBancaVO trabalhoConclusaoCursoMembroBancaVO = new TrabalhoConclusaoCursoMembroBancaVO();
		trabalhoConclusaoCursoMembroBancaVO.setNovoObj(false);
		trabalhoConclusaoCursoMembroBancaVO.setCodigo(rs.getInt("codigo"));
		trabalhoConclusaoCursoMembroBancaVO.setNome(rs.getString("nome"));
		trabalhoConclusaoCursoMembroBancaVO.getMembro().setCodigo(rs.getInt("membro"));
		trabalhoConclusaoCursoMembroBancaVO.setConvidado(rs.getBoolean("convidado"));		
		trabalhoConclusaoCursoMembroBancaVO.getTrabalhoConclusaoCurso().setCodigo(rs.getInt("trabalhoConclusaoCurso"));
		
		return trabalhoConclusaoCursoMembroBancaVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMembrosBanca(Integer tcc, List<TrabalhoConclusaoCursoMembroBancaVO> objetos, UsuarioVO usuario) throws Exception {
		excluirMembrosBanca(tcc, usuario);
		incluirMembrosBanca(tcc, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void excluirMembrosBanca(Integer tcc, UsuarioVO usuario) throws Exception {
//		TrabalhoConclusaoCursoMembroBancaVO.excluir(getIdEntidade());
		String sql = "DELETE FROM TrabalhoConclusaoCursoMembroBanca WHERE (trabalhoConclusaoCurso = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { tcc });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluirMembrosBanca(Integer tcc, List<TrabalhoConclusaoCursoMembroBancaVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			TrabalhoConclusaoCursoMembroBancaVO obj = (TrabalhoConclusaoCursoMembroBancaVO) e.next();
			obj.getTrabalhoConclusaoCurso().setCodigo(tcc);
			incluir(obj);
		}
	}

}
