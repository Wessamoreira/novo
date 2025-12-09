package negocio.comuns.protocolo;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.TipoRequerimentoSituacaoDepartamentoInterfaceFacade;

@Service
@Scope("singleton")
@Lazy
public class TipoRequerimentoSituacaoDepartamento extends ControleAcesso implements TipoRequerimentoSituacaoDepartamentoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4824928505865931800L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO, final UsuarioVO usuarioVO) throws Exception {
		validarDados(tipoRequerimentoSituacaoDepartamentoVO);		
		tipoRequerimentoSituacaoDepartamentoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				String sql = "insert into tipoRequerimentoSituacaoDepartamento (situacaoRequerimentoDepartamento, TipoRequerimentoDepartamento) values (?, ?) returning codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				PreparedStatement ps = arg0.prepareStatement(sql);
				ps.setInt(1, tipoRequerimentoSituacaoDepartamentoVO.getSituacaoRequerimentoDepartamentoVO().getCodigo());
				ps.setInt(2, tipoRequerimentoSituacaoDepartamentoVO.getTipoRequerimentoDepartamentoVO().getCodigo());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {				
				return arg0.next()?arg0.getInt("codigo"):0;
			}
		}));
		tipoRequerimentoSituacaoDepartamentoVO.setNovoObj(false);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO, UsuarioVO usuarioVO) throws Exception {
		validarDados(tipoRequerimentoSituacaoDepartamentoVO);		
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				String sql = "update tipoRequerimentoSituacaoDepartamento set situacaoRequerimentoDepartamento = ?, TipoRequerimentoDepartamento = ? where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				PreparedStatement ps = arg0.prepareStatement(sql);
				ps.setInt(1, tipoRequerimentoSituacaoDepartamentoVO.getSituacaoRequerimentoDepartamentoVO().getCodigo());
				ps.setInt(2, tipoRequerimentoSituacaoDepartamentoVO.getTipoRequerimentoDepartamentoVO().getCodigo());
				ps.setInt(3, tipoRequerimentoSituacaoDepartamentoVO.getCodigo());
				return ps;
			}
		})==0) {
			incluir(tipoRequerimentoSituacaoDepartamentoVO, usuarioVO);
		};
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirListaTipoRequerimentoSituacaoDepartamento(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception {
		final StringBuilder sql  = new StringBuilder("delete from tipoRequerimentoSituacaoDepartamento where  TipoRequerimentoDepartamento = ? and codigo not in (0 ");
		tipoRequerimentoDepartamentoVO.getTipoRequerimentoSituacaoDepartamentoVOs().forEach(item -> sql.append(", ").append(item.getCodigo()));
		sql.append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), tipoRequerimentoDepartamentoVO.getCodigo());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirListaTipoRequerimentoSituacaoDepartamento(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception {
		for(TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO : tipoRequerimentoDepartamentoVO.getTipoRequerimentoSituacaoDepartamentoVOs()) {
			tipoRequerimentoSituacaoDepartamentoVO.setTipoRequerimentoDepartamentoVO(tipoRequerimentoDepartamentoVO);
			incluir(tipoRequerimentoSituacaoDepartamentoVO, usuarioVO);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarListaTipoRequerimentoSituacaoDepartamento(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception {
		excluirListaTipoRequerimentoSituacaoDepartamento(tipoRequerimentoDepartamentoVO, usuarioVO);
		for(TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO : tipoRequerimentoDepartamentoVO.getTipoRequerimentoSituacaoDepartamentoVOs()) {
			tipoRequerimentoSituacaoDepartamentoVO.setTipoRequerimentoDepartamentoVO(tipoRequerimentoDepartamentoVO);
			if(tipoRequerimentoSituacaoDepartamentoVO.isNovoObj()) {
				incluir(tipoRequerimentoSituacaoDepartamentoVO, usuarioVO);
			}else {
				alterar(tipoRequerimentoSituacaoDepartamentoVO, usuarioVO);
			}
		}

	}
	
	private StringBuilder getSqlConsulta() {
		StringBuilder sql = new StringBuilder("select tipoRequerimentoSituacaoDepartamento.*, SituacaoRequerimentoDepartamento.situacao ");
		sql.append(" from tipoRequerimentoSituacaoDepartamento ");
		sql.append(" inner join SituacaoRequerimentoDepartamento on SituacaoRequerimentoDepartamento.codigo = tipoRequerimentoSituacaoDepartamento.SituacaoRequerimentoDepartamento ");	
		sql.append(" inner join tipoRequerimentoDepartamento on tipoRequerimentoDepartamento.codigo = tipoRequerimentoSituacaoDepartamento.tipoRequerimentoDepartamento ");
		return sql;
	}

	@Override
	public TipoRequerimentoSituacaoDepartamentoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsulta());
		sql.append(" where tipoRequerimentoSituacaoDepartamento.codigo = ? ");
		SqlRowSet rs= getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		if(rs.next()) {
			return montarDados(rs);			
		}
		throw new Exception("Dados não encontrados(Tipo  Situação Requerimento Departamento).");
	}

	@Override
	public void validarDados(TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO) throws ConsistirException {
		if(!Uteis.isAtributoPreenchido(tipoRequerimentoSituacaoDepartamentoVO.getSituacaoRequerimentoDepartamentoVO())) {
			throw new ConsistirException("O campo SITUAÇÃO deve ser informado.");
		}
	}

	@Override
	public List<TipoRequerimentoSituacaoDepartamentoVO> consultarPorTipoRequerimentoDepartamento(Integer tipoRequerimentoDepartamento) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsulta());
		sql.append(" where tipoRequerimentoDepartamento = ? order by situacao");
		SqlRowSet rs= getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), tipoRequerimentoDepartamento);
		return montarDadosConsulta(rs);					
	}

	@Override
	public List<TipoRequerimentoSituacaoDepartamentoVO> consultarPorTipoRequerimentoDepartamento(Integer tipoRequerimento, Integer departamento, Integer ordemTramite) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsulta());		
		sql.append(" where tipoRequerimentoDepartamento.tipoRequerimento = ? ");
		sql.append(" and tipoRequerimentoDepartamento.departamento = ? ");
		sql.append(" and tipoRequerimentoDepartamento.ordemExecucao = ? ");
		sql.append(" order by situacao");
		SqlRowSet rs= getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), tipoRequerimento, departamento, ordemTramite);
		return montarDadosConsulta(rs);	
	}

	@Override
	public List<TipoRequerimentoSituacaoDepartamentoVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<TipoRequerimentoSituacaoDepartamentoVO> tipoRequerimentoSituacaoDepartamentoVOs =  new ArrayList<TipoRequerimentoSituacaoDepartamentoVO>(0);
		while(rs.next()) {
			tipoRequerimentoSituacaoDepartamentoVOs.add(montarDados(rs));
		}
		return tipoRequerimentoSituacaoDepartamentoVOs;
	}

	@Override
	public TipoRequerimentoSituacaoDepartamentoVO montarDados(SqlRowSet rs) throws Exception {
		TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO = new TipoRequerimentoSituacaoDepartamentoVO();
		tipoRequerimentoSituacaoDepartamentoVO.setNovoObj(false);
		tipoRequerimentoSituacaoDepartamentoVO.setCodigo(rs.getInt("codigo"));
		tipoRequerimentoSituacaoDepartamentoVO.getTipoRequerimentoDepartamentoVO().setCodigo(rs.getInt("tipoRequerimentoDepartamento"));
		tipoRequerimentoSituacaoDepartamentoVO.getSituacaoRequerimentoDepartamentoVO().setCodigo(rs.getInt("SituacaoRequerimentoDepartamento"));
		tipoRequerimentoSituacaoDepartamentoVO.getSituacaoRequerimentoDepartamentoVO().setSituacao(rs.getString("situacao"));
		return tipoRequerimentoSituacaoDepartamentoVO;
	}

}
