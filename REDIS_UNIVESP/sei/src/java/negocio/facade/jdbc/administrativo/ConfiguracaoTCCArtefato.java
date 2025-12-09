package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.ConfiguracaoTCCArtefatoVO;
import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ConfiguracaoTCCArtefatoInterfaceFacade;

@Repository
@Lazy
@Scope
public class ConfiguracaoTCCArtefato extends ControleAcesso implements ConfiguracaoTCCArtefatoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3817855087147048821L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirConfiguracaoTCCArtefatoVOs(ConfiguracaoTCCVO configuracaoTCCVO) throws Exception {
		for(ConfiguracaoTCCArtefatoVO obj:configuracaoTCCVO.getConfiguracaoTCCArtefatoVOs()){
			obj.setConfiguracaoTCC(configuracaoTCCVO);
			incluir(obj);
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO) throws Exception {		
		try {
			configuracaoTCCArtefatoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO configuracaoTCCArtefato (");
					sql.append(" artefato, configuracaoTCC ");
					sql.append(") VALUES (?,?) RETURNING codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setString(x++, configuracaoTCCArtefatoVO.getArtefato());
					ps.setInt(x++, configuracaoTCCArtefatoVO.getConfiguracaoTCC().getCodigo());					
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
			
			configuracaoTCCArtefatoVO.setNovoObj(false);
		} catch (Exception e) {
			configuracaoTCCArtefatoVO.setNovoObj(true);
			configuracaoTCCArtefatoVO.setCodigo(0);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO) throws Exception {
		
		try {
			if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE configuracaoTCCArtefato SET ");
					sql.append(" artefato = ?, configuracaoTCC = ? ");
					sql.append(" WHERE  codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setString(x++, configuracaoTCCArtefatoVO.getArtefato());
					ps.setInt(x++, configuracaoTCCArtefatoVO.getConfiguracaoTCC().getCodigo());					
					ps.setInt(x++, configuracaoTCCArtefatoVO.getCodigo());
					return ps;
				}
			}) == 0){
				incluir(configuracaoTCCArtefatoVO);
				return;
			}
			configuracaoTCCArtefatoVO.setNovoObj(false);			
		} catch (Exception e) {

			throw e;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarConfiguracaoTCCArtefatoVOs(ConfiguracaoTCCVO configuracaoTCCVO) throws Exception {
		excluirConfiguracaoTCCArtefatoVOs(configuracaoTCCVO);
		for(ConfiguracaoTCCArtefatoVO obj:configuracaoTCCVO.getConfiguracaoTCCArtefatoVOs()){
			obj.setConfiguracaoTCC(configuracaoTCCVO);
			if(obj.getNovoObj()){
				incluir(obj);
			}else{
				alterar(obj);
			}
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirConfiguracaoTCCArtefatoVOs(ConfiguracaoTCCVO configuracaoTCCVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM ConfiguracaoTCCArtefato WHERE ConfiguracaoTCC = ").append(configuracaoTCCVO.getCodigo()).append(" and  codigo not in (0 ");
		for(ConfiguracaoTCCArtefatoVO obj:configuracaoTCCVO.getConfiguracaoTCCArtefatoVOs()){
			sql.append(", ").append(obj.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	public List<ConfiguracaoTCCArtefatoVO> consultarPorConfiguracaoTCC(Integer configuracaoTCC) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM ConfiguracaoTCCArtefato WHERE ConfiguracaoTCC = ").append(configuracaoTCC).append(" order by artefato");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	private List<ConfiguracaoTCCArtefatoVO> montarDadosConsulta(SqlRowSet rs){
		List<ConfiguracaoTCCArtefatoVO> configuracaoTCCArtefatoVOs = new ArrayList<ConfiguracaoTCCArtefatoVO>(0);
		while(rs.next()){
			configuracaoTCCArtefatoVOs.add(montarDados(rs));
		}
		return configuracaoTCCArtefatoVOs;
	}
	
	private ConfiguracaoTCCArtefatoVO montarDados(SqlRowSet rs){
		ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO = new ConfiguracaoTCCArtefatoVO();
		configuracaoTCCArtefatoVO.setArtefato(rs.getString("artefato"));
		configuracaoTCCArtefatoVO.setCodigo(rs.getInt("codigo"));
		configuracaoTCCArtefatoVO.getConfiguracaoTCC().setCodigo(rs.getInt("configuracaoTCC"));
		configuracaoTCCArtefatoVO.setNovoObj(false);
		return configuracaoTCCArtefatoVO;
	}

	@Override
	public void validarDados(ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO) throws ConsistirException {
		if(configuracaoTCCArtefatoVO.getArtefato().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoTCCArtefato_artefato"));
		}
	}

}
