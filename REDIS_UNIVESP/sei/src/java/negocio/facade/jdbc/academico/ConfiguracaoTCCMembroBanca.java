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

import negocio.comuns.academico.ConfiguracaoTCCMembroBancaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConfiguracaoTCCMembroBancaInterfaceFacade;

@Repository
@Lazy
@Scope
public class ConfiguracaoTCCMembroBanca extends ControleAcesso implements ConfiguracaoTCCMembroBancaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5040921415073755284L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(ConfiguracaoTCCMembroBancaVO configuracaoTCCMembroBancaVO) throws Exception {
		if (configuracaoTCCMembroBancaVO.isNovoObj()) {
			incluir(configuracaoTCCMembroBancaVO);
		} else {
			alterar(configuracaoTCCMembroBancaVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final ConfiguracaoTCCMembroBancaVO configuracaoTCCMembroBancaVO) throws Exception {

		configuracaoTCCMembroBancaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO ConfiguracaoTCCMembroBanca ( ");
				sql.append(" membro, nome, convidado, configuracaoTCC, ordem ");
				sql.append(" ) values (?,?,?,?,?) RETURNING codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				if (configuracaoTCCMembroBancaVO.getMembro().getCodigo().intValue() != 0) {
					ps.setInt(x++, configuracaoTCCMembroBancaVO.getMembro().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setString(x++, configuracaoTCCMembroBancaVO.getNome());
				ps.setBoolean(x++, configuracaoTCCMembroBancaVO.getConvidado());
				ps.setInt(x++, configuracaoTCCMembroBancaVO.getConfiguracaoTCC());
				ps.setInt(x++, configuracaoTCCMembroBancaVO.getOrdem());
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
		configuracaoTCCMembroBancaVO.setNovoObj(false);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final ConfiguracaoTCCMembroBancaVO configuracaoTCCMembroBancaVO) throws Exception {

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE ConfiguracaoTCCMembroBanca SET ");
				sql.append(" membro = ?, nome=?, convidado=?, configuracaoTCC=?, ordem=? ");
				sql.append(" WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				if (configuracaoTCCMembroBancaVO.getMembro().getCodigo().intValue() != 0) {
					ps.setInt(x++, configuracaoTCCMembroBancaVO.getMembro().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setString(x++, configuracaoTCCMembroBancaVO.getNome());
				ps.setBoolean(x++, configuracaoTCCMembroBancaVO.getConvidado());
				ps.setInt(x++, configuracaoTCCMembroBancaVO.getConfiguracaoTCC());
				ps.setInt(x++, configuracaoTCCMembroBancaVO.getOrdem());
				ps.setInt(x++, configuracaoTCCMembroBancaVO.getCodigo());
				return ps;
			}
		});
		configuracaoTCCMembroBancaVO.setNovoObj(false);

	}

	private String getSelectCompleto(){
		StringBuilder sql = new StringBuilder("SELECT * from ConfiguracaoTCCMembroBanca ");
		return sql.toString();
	}

	@Override
	public List<ConfiguracaoTCCMembroBancaVO> consultarPorTCC(int tcc) throws Exception {
		StringBuilder sql  = new StringBuilder(getSelectCompleto());
		sql.append(" WHERE configuracaoTCC = ").append(tcc).append(" ORDER BY nome ");		
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	public List<ConfiguracaoTCCMembroBancaVO> consultarPorTurmaDisciplina(int turma, int disciplina, int tcc) throws Exception {
		StringBuilder sql  = new StringBuilder();
		sql.append(" select distinct funcionario.codigo as professor, pessoa.nome as nomeprofessor from horarioturmadetalhado(" + turma + ", null, " + disciplina + ", null) as t inner join pessoa on pessoa.codigo = t.professor inner join funcionario on funcionario.pessoa = t.professor ");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ConfiguracaoTCCMembroBancaVO> configuracaoTCCMembroBancaVOs = new ArrayList<ConfiguracaoTCCMembroBancaVO>(0);
		while(rs.next()){
			PessoaVO professor = new PessoaVO();
			professor.setCodigo(rs.getInt("professor"));
			professor.setNome(rs.getString("nomeprofessor"));
			configuracaoTCCMembroBancaVOs.add(montarProfessorMembroBanca(professor, tcc));
		}		
		return configuracaoTCCMembroBancaVOs;
	}

	public ConfiguracaoTCCMembroBancaVO montarProfessorMembroBanca(PessoaVO professor, Integer tcc) throws Exception {		 
		ConfiguracaoTCCMembroBancaVO configuracaoTCCMembroBancaVO = new ConfiguracaoTCCMembroBancaVO();
		configuracaoTCCMembroBancaVO.setNovoObj(false);
		configuracaoTCCMembroBancaVO.setCodigo(0);
		configuracaoTCCMembroBancaVO.setNome(professor.getNome());
		configuracaoTCCMembroBancaVO.getMembro().setCodigo(professor.getCodigo());
		//configuracaoTCCMembroBancaVO.setConvidado(rs.getBoolean("convidado"));		
		configuracaoTCCMembroBancaVO.setConfiguracaoTCC(tcc);
		//configuracaoTCCMembroBancaVO.setOrdem(rs.getInt("ordem"));
		return configuracaoTCCMembroBancaVO;
	}	
	
	public List<ConfiguracaoTCCMembroBancaVO> montarCoordenadorMembroBanca(PessoaVO coordenador, Integer tcc) throws Exception {
		List<ConfiguracaoTCCMembroBancaVO> configuracaoTCCMembroBancaVOs = new ArrayList<ConfiguracaoTCCMembroBancaVO>(0);
		ConfiguracaoTCCMembroBancaVO configuracaoTCCMembroBancaVO = new ConfiguracaoTCCMembroBancaVO();
		configuracaoTCCMembroBancaVO.setNovoObj(false);
		configuracaoTCCMembroBancaVO.setCodigo(0);
		configuracaoTCCMembroBancaVO.setNome(coordenador.getNome());
		configuracaoTCCMembroBancaVO.getMembro().setCodigo(coordenador.getCodigo());
		//configuracaoTCCMembroBancaVO.setConvidado(rs.getBoolean("convidado"));		
		configuracaoTCCMembroBancaVO.setConfiguracaoTCC(tcc);
		//configuracaoTCCMembroBancaVO.setOrdem(rs.getInt("ordem"));
		configuracaoTCCMembroBancaVOs.add(configuracaoTCCMembroBancaVO);
		return configuracaoTCCMembroBancaVOs;
	}
	
	private List<ConfiguracaoTCCMembroBancaVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<ConfiguracaoTCCMembroBancaVO> configuracaoTCCMembroBancaVOs = new ArrayList<ConfiguracaoTCCMembroBancaVO>(0);
		while(rs.next()){
			configuracaoTCCMembroBancaVOs.add(montarDados(rs));
		}		
		return configuracaoTCCMembroBancaVOs;
	}
	
	// Montar dados para conf que utiliza o professor que ministrou aula.
	private List<ConfiguracaoTCCMembroBancaVO> montarDadosConsulta2(SqlRowSet rs) throws Exception{
		List<ConfiguracaoTCCMembroBancaVO> configuracaoTCCMembroBancaVOs = new ArrayList<ConfiguracaoTCCMembroBancaVO>(0);
		while(rs.next()){
			configuracaoTCCMembroBancaVOs.add(montarDados(rs));
		}		
		return configuracaoTCCMembroBancaVOs;
	}
	
	private ConfiguracaoTCCMembroBancaVO montarDados(SqlRowSet rs) throws Exception{
		ConfiguracaoTCCMembroBancaVO configuracaoTCCMembroBancaVO = new ConfiguracaoTCCMembroBancaVO();
		configuracaoTCCMembroBancaVO.setNovoObj(false);
		configuracaoTCCMembroBancaVO.setCodigo(rs.getInt("codigo"));
		configuracaoTCCMembroBancaVO.setNome(rs.getString("nome"));
		configuracaoTCCMembroBancaVO.getMembro().setCodigo(rs.getInt("membro"));
		configuracaoTCCMembroBancaVO.setConvidado(rs.getBoolean("convidado"));		
		configuracaoTCCMembroBancaVO.setConfiguracaoTCC(rs.getInt("configuracaoTCC"));
		configuracaoTCCMembroBancaVO.setOrdem(rs.getInt("ordem"));
		
		return configuracaoTCCMembroBancaVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMembrosBanca(Integer tcc, List<ConfiguracaoTCCMembroBancaVO> objetos) throws Exception {
		excluirMembrosBanca(tcc);
		incluirMembrosBanca(tcc, objetos);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void excluirMembrosBanca(Integer tcc) throws Exception {
		String sql = "DELETE FROM ConfiguracaoTCCMembroBanca WHERE (configuracaoTCC = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { tcc });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMembrosBanca(Integer tcc, List<ConfiguracaoTCCMembroBancaVO> objetos) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ConfiguracaoTCCMembroBancaVO obj = (ConfiguracaoTCCMembroBancaVO) e.next();
			obj.setConfiguracaoTCC(tcc);
			incluir(obj);
		}
	}

}