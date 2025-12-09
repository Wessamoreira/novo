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

import negocio.comuns.academico.TrabalhoConclusaoCursoArtefatoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TrabalhoConclusaoCursoArtefatoInterfaceFacade;

@Repository
@Lazy
@Scope
public class TrabalhoConclusaoCursoArtefato extends ControleAcesso implements TrabalhoConclusaoCursoArtefatoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5040921415073755284L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(TrabalhoConclusaoCursoArtefatoVO trabalhoConclusaoCursoArtefatoVO) throws Exception {
		if (trabalhoConclusaoCursoArtefatoVO.isNovoObj()) {
			incluir(trabalhoConclusaoCursoArtefatoVO);
		} else {
			alterar(trabalhoConclusaoCursoArtefatoVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final TrabalhoConclusaoCursoArtefatoVO trabalhoConclusaoCursoArtefatoVO) throws Exception {

		trabalhoConclusaoCursoArtefatoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO TrabalhoConclusaoCursoArtefato ( ");
				sql.append(" dataEntrega, configuracaoTCCArtefato, entregue, trabalhoConclusaoCurso, responsavelRegistro ");
				sql.append(" ) values (?,?,?,?,?) RETURNING codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				if (trabalhoConclusaoCursoArtefatoVO.getDataEntrega() != null) {
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(trabalhoConclusaoCursoArtefatoVO.getDataEntrega()));
				} else {
					ps.setNull(x++, 0);
				}
				ps.setInt(x++, trabalhoConclusaoCursoArtefatoVO.getConfiguracaoTCCArtefato().getCodigo());
				ps.setBoolean(x++, trabalhoConclusaoCursoArtefatoVO.getEntregue());
				ps.setInt(x++, trabalhoConclusaoCursoArtefatoVO.getTrabalhoConclusaoCurso().getCodigo());
				if (trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getCodigo() != null && trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getCodigo() > 0) {
					ps.setInt(x++, trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
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
		trabalhoConclusaoCursoArtefatoVO.setNovoObj(false);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final TrabalhoConclusaoCursoArtefatoVO trabalhoConclusaoCursoArtefatoVO) throws Exception {

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE TrabalhoConclusaoCursoArtefato SET ");
				sql.append(" dataEntrega = ?, configuracaoTCCArtefato=?, entregue=?, trabalhoConclusaoCurso=?, responsavelRegistro = ? ");
				sql.append(" WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				if (trabalhoConclusaoCursoArtefatoVO.getDataEntrega() != null) {
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(trabalhoConclusaoCursoArtefatoVO.getDataEntrega()));
				} else {
					ps.setNull(x++, 0);
				}
				ps.setInt(x++, trabalhoConclusaoCursoArtefatoVO.getConfiguracaoTCCArtefato().getCodigo());
				ps.setBoolean(x++, trabalhoConclusaoCursoArtefatoVO.getEntregue());
				ps.setInt(x++, trabalhoConclusaoCursoArtefatoVO.getTrabalhoConclusaoCurso().getCodigo());
				if (trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getCodigo() != null && trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getCodigo() > 0) {
					ps.setInt(x++, trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setInt(x++, trabalhoConclusaoCursoArtefatoVO.getCodigo());
				return ps;
			}
		});
		trabalhoConclusaoCursoArtefatoVO.setNovoObj(false);

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarRegistroEntregaArtefato(TrabalhoConclusaoCursoArtefatoVO trabalhoConclusaoCursoArtefatoVO, UsuarioVO usuarioVO) throws Exception {
		try {
			trabalhoConclusaoCursoArtefatoVO.setEntregue(true);
			trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().setCodigo(usuarioVO.getCodigo());
			trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getPessoa().setNome(usuarioVO.getPessoa().getNome());
			if (trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getPessoa().getNome().trim().isEmpty()) {
				trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getPessoa().setNome(usuarioVO.getNome());
			}
			trabalhoConclusaoCursoArtefatoVO.setDataEntrega(new Date());
			persistir(trabalhoConclusaoCursoArtefatoVO);
		} catch (Exception e) {
			trabalhoConclusaoCursoArtefatoVO.setEntregue(false);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarDevolucaoEntregaArtefato(TrabalhoConclusaoCursoArtefatoVO trabalhoConclusaoCursoArtefatoVO, UsuarioVO usuarioVO) throws Exception {
		try {
			trabalhoConclusaoCursoArtefatoVO.setEntregue(false);
			trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().setCodigo(usuarioVO.getCodigo());
			trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getPessoa().setNome(usuarioVO.getPessoa().getNome());
			if (trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getPessoa().getNome().trim().isEmpty()) {
				trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getPessoa().setNome(usuarioVO.getNome());
			}
			trabalhoConclusaoCursoArtefatoVO.setDataEntrega(new Date());
			persistir(trabalhoConclusaoCursoArtefatoVO);
		} catch (Exception e) {
			trabalhoConclusaoCursoArtefatoVO.setEntregue(true);
			throw e;
		}

	}
	
	private String getSelectCompleto(){
		StringBuilder sql = new StringBuilder("SELECT TrabalhoConclusaoCursoArtefato.*, configuracaoTCCArtefato.artefato as \"configuracaoTCCArtefato.artefato\", responsavelRegistro.nome as \"responsavelRegistro.nome\", pessoa.nome as \"pessoa.nome\"  ");
		sql.append(" from TrabalhoConclusaoCursoArtefato ");
		sql.append(" inner join configuracaoTCCArtefato on configuracaoTCCArtefato.codigo =  TrabalhoConclusaoCursoArtefato.configuracaoTCCArtefato ");
		sql.append(" left join usuario as responsavelRegistro on responsavelRegistro.codigo =  TrabalhoConclusaoCursoArtefato.responsavelRegistro ");
		sql.append(" left join pessoa as pessoa on responsavelRegistro.pessoa =  pessoa.codigo ");
		
		return sql.toString();
	}

	@Override
	public List<TrabalhoConclusaoCursoArtefatoVO> consultarPorTCC(int tcc) throws Exception {
		StringBuilder sql  = new StringBuilder(getSelectCompleto());
		sql.append(" WHERE trabalhoConclusaoCurso = ").append(tcc).append(" ORDER BY configuracaoTCCArtefato.artefato ");		
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	private List<TrabalhoConclusaoCursoArtefatoVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<TrabalhoConclusaoCursoArtefatoVO> trabalhoConclusaoCursoArtefatoVOs = new ArrayList<TrabalhoConclusaoCursoArtefatoVO>(0);
		while(rs.next()){
			trabalhoConclusaoCursoArtefatoVOs.add(montarDados(rs));
		}		
		return trabalhoConclusaoCursoArtefatoVOs;
	}
	private TrabalhoConclusaoCursoArtefatoVO montarDados(SqlRowSet rs) throws Exception{
		TrabalhoConclusaoCursoArtefatoVO trabalhoConclusaoCursoArtefatoVO = new TrabalhoConclusaoCursoArtefatoVO();
		trabalhoConclusaoCursoArtefatoVO.setNovoObj(false);
		trabalhoConclusaoCursoArtefatoVO.setCodigo(rs.getInt("codigo"));		
		trabalhoConclusaoCursoArtefatoVO.setEntregue(rs.getBoolean("entregue"));		
		trabalhoConclusaoCursoArtefatoVO.getTrabalhoConclusaoCurso().setCodigo(rs.getInt("trabalhoConclusaoCurso"));
		trabalhoConclusaoCursoArtefatoVO.getConfiguracaoTCCArtefato().setCodigo(rs.getInt("configuracaoTCCArtefato"));
		trabalhoConclusaoCursoArtefatoVO.getConfiguracaoTCCArtefato().setArtefato(rs.getString("configuracaoTCCArtefato.artefato"));
		trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().setCodigo(rs.getInt("responsavelRegistro"));
		trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getPessoa().setNome(rs.getString("pessoa.nome"));
		if (trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getPessoa().getNome().trim().isEmpty()) {
			trabalhoConclusaoCursoArtefatoVO.getResponsavelRegistro().getPessoa().setNome(rs.getString("responsavelRegistro.nome"));
		}
		trabalhoConclusaoCursoArtefatoVO.setDataEntrega(rs.getTimestamp("dataEntrega"));
		
		return trabalhoConclusaoCursoArtefatoVO;
	}

}
