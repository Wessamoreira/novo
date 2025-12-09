package negocio.facade.jdbc.bancocurriculum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.bancocurriculum.VagaContatoVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.VagaContatoInterfaceFacade;

@Repository
@Lazy
public class VagaContato extends ControleAcesso implements VagaContatoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3422808486460352538L;

	@Override
	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void incluirVagaContato(VagasVO vagasVO) throws Exception {
		for(VagaContatoVO vagaContatoVO:vagasVO.getVagaContatoVOs()){
			vagaContatoVO.setVaga(vagasVO);
			incluir(vagaContatoVO);
		}

	}
	
	private void incluir(final VagaContatoVO vagaContatoVO) throws Exception{
		final StringBuilder sql = new StringBuilder("INSERT INTO VagaContato (nome, email, vaga) VALUES (?,?,?) returning codigo ");
		vagaContatoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setString(1, vagaContatoVO.getNome());
				ps.setString(2, vagaContatoVO.getEmail());
				ps.setInt(3, vagaContatoVO.getVaga().getCodigo());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if(arg0.next()){
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		vagaContatoVO.setNovoObj(false);		
	}
	
	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	private void alterar(final VagaContatoVO vagaContatoVO) throws Exception{
		final StringBuilder sql = new StringBuilder("UPDATE VagaContato set nome=?, email=?, vaga=? where codigo = ? ");
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setString(1, vagaContatoVO.getNome());
				ps.setString(2, vagaContatoVO.getEmail());
				ps.setInt(3, vagaContatoVO.getVaga().getCodigo());
				ps.setInt(4, vagaContatoVO.getCodigo());
				return ps;
			}
		})==0){
			incluir(vagaContatoVO);
			return;
		}
		vagaContatoVO.setNovoObj(false);		
	}
	
	public void excluirVagaContato(VagasVO vagasVO){
		StringBuilder sql  = new StringBuilder(" DELETE FROM VagaContato WHERE vaga = ").append(vagasVO.getCodigo());
		sql.append(" and codigo not in (0 ");
		for(VagaContatoVO vagaContatoVO:vagasVO.getVagaContatoVOs()){
			sql.append(", ").append(vagaContatoVO.getCodigo());			
		}
		sql.append(" ) ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	public void alterarVagaContato(VagasVO vagasVO) throws Exception {
		excluirVagaContato(vagasVO);
		for(VagaContatoVO vagaContatoVO:vagasVO.getVagaContatoVOs()){
			vagaContatoVO.setVaga(vagasVO);
			if(vagaContatoVO.isNovoObj()){
				incluir(vagaContatoVO);
			}else{
				alterar(vagaContatoVO);
			}
		}

	}

	@Override
	public List<VagaContatoVO> consultarPorVaga(Integer vaga) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT * FROM VagaContato WHERE vaga = ").append(vaga).append(" order by codigo ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	private List<VagaContatoVO> montarDadosConsulta(SqlRowSet rs){
		List<VagaContatoVO> vagaContatoVOs = new ArrayList<VagaContatoVO>(0);
		VagaContatoVO vagaContatoVO = null;
		while(rs.next()){
			vagaContatoVO = new VagaContatoVO();
			vagaContatoVO.setNovoObj(false);
			vagaContatoVO.setNome(rs.getString("nome"));
			vagaContatoVO.setEmail(rs.getString("email"));
			vagaContatoVO.setCodigo(rs.getInt("codigo"));
			vagaContatoVO.getVaga().setCodigo(rs.getInt("vaga"));
			vagaContatoVOs.add(vagaContatoVO);
		}
		
		return vagaContatoVOs;		
	}
	
	

	@Override
	public void validarDados(VagaContatoVO vagaContatoVO) throws ConsistirException {
		if(vagaContatoVO.getNome().trim().isEmpty()){
			throw new ConsistirException("O campo NOME(Contato) deve ser informado");
		}
		if(vagaContatoVO.getEmail().trim().isEmpty()){
			throw new ConsistirException("O campo EMAIL(Contato) deve ser informado");
		}
	}

}
